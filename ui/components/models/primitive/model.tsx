"use client";
import { modeling } from "@/proto-bundle";
import { ReactNode } from "react";
import { getQuadrilateral } from "./quadrilateral";
import { getTriangle } from "./triangle";
import * as BufferGeometryUtils from 'three/addons/utils/BufferGeometryUtils.js';
import { BufferGeometry, DoubleSide } from "three";
import { ColorEntity, fetchAllColors } from "@/api/color-api";
import { useQuery } from "@tanstack/react-query";

export type ColorMap = Record<string, ColorEntity>;

export function Model( { gpb }: { gpb: modeling.IModel | undefined } ): ReactNode
{
	const { data: colors } = useQuery( { queryKey: [ "colors" ], queryFn: fetchAllColors } );
	
	if( gpb === undefined ) return [];

	const colorMap: ColorMap = colors?.reduce( ( map, colorEntity ) => {
		map[colorEntity.id] = colorEntity;
		return map;
	}, {} as ColorMap ) ?? {};

	const quadGeometries: BufferGeometry[] = extractQuadGeometries( gpb, colorMap );
	const triangleGeometries: BufferGeometry[] = extractTriangleGeometries( gpb, colorMap );


	const quad: BufferGeometry = BufferGeometryUtils.mergeGeometries( quadGeometries, false );
	const triangle: BufferGeometry = BufferGeometryUtils.mergeGeometries( triangleGeometries, false );

	return (
		<group>
			<mesh geometry={quad}>
				<meshBasicMaterial vertexColors side={DoubleSide}/>
				<lineSegments>
					<edgesGeometry args={[ quad ]} />
					<lineBasicMaterial color={"black"} linewidth={1}/>
				</lineSegments>
			</mesh>	
			<mesh geometry={triangle}>
				<meshBasicMaterial vertexColors side={DoubleSide}/>
				<lineSegments>
					<edgesGeometry args={[ triangle ]} />
					<lineBasicMaterial color={"black"} linewidth={1}/>
				</lineSegments>
			</mesh>	
		</group>
		
	);
}


function extractTriangleGeometries( model: modeling.IModel | null | undefined, colorMap: ColorMap ): BufferGeometry[] {
	if ( !model ) return [];
  
	const meshes: BufferGeometry[] = [];
  
	
	const triangles = model.triangle?.map( triangle => getTriangle( triangle, colorMap ) ).filter( geometry => geometry != undefined ) ?? [];
    
	meshes.push( ...triangles );
  
	const childMeshes = model.piece?.flatMap( ( child ) =>
		extractTriangleGeometries( child.subModel, colorMap ) ) ?? [];
  
	meshes.push( ...childMeshes );
		
	return meshes;
}

function extractQuadGeometries( model: modeling.IModel | null | undefined, colorMap: ColorMap ): BufferGeometry[] {
	if ( !model ) return [];
  
	const meshes: BufferGeometry[] = [];
  
	const quads = model.quadrilateral?.map( quad => getQuadrilateral( quad, colorMap ) ).filter( geometry => geometry != undefined ) ?? [];
    
	meshes.push( ...quads );
		
	const childMeshes = model.piece?.flatMap( ( child ) =>
		extractQuadGeometries( child.subModel, colorMap ) ) ?? [];
  
	meshes.push( ...childMeshes );
		
	return meshes;
}  