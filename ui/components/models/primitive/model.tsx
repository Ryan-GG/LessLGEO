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

	console.log( gpb );

	const quadGeometries: BufferGeometry[] = getQuadrilaterals( gpb ).map( quad => getQuadrilateral( quad, colorMap ) ).filter( quadGeometry => quadGeometry != undefined );
	const triangleGeometries: BufferGeometry[] = getTriangles( gpb ).map( triangle => getTriangle( triangle, colorMap ) ).filter( triangleGeometry => triangleGeometry != undefined );


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


function getTriangles( model: modeling.IModel): modeling.ITriangle[] {

	const triangles: modeling.ITriangle[] = model.triangle ?? [];
    
	model.piece?.filter( subFileRef => subFileRef.subModel != undefined ).forEach( subFileReference => triangles.push( ...getTriangles( subFileReference.subModel ) ) )

	return triangles;
}

function getQuadrilaterals( model: modeling.IModel ): modeling.IQuadrilateral [] {
	const quads: modeling.IQuadrilateral[] = model.quadrilateral ?? [];
    
	model.piece?.filter( subFileRef => subFileRef.subModel != undefined ).forEach( subFileReference => quads.push( ...getQuadrilaterals( subFileReference.subModel ) ) )

	return quads;
}  