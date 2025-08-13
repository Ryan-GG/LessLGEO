"use client";
import { modeling } from "@/proto-bundle";
import { ReactNode } from "react";
import { getQuadrilateral } from "./quadrilateral";
import { getTriangle } from "./triangle";
import * as BufferGeometryUtils from 'three/addons/utils/BufferGeometryUtils.js';
import { BufferGeometry, DoubleSide } from "three";

/**
 * i can create a single geometry and material for each piece / Color
 * then create a new mesh referecing those geometries/materials
 * ( this still have the problem of how to merge geometries / applying the rotation / translation to it )
 */

export function Model( { gpb }: { gpb: modeling.IModel | undefined } ): ReactNode
{
	if( gpb === undefined ) return [];

	const quadGeometries: BufferGeometry[] = extractQuadGeometries( gpb );
	const triangleGeometries: BufferGeometry[] = extractTriangleGeometries( gpb );


	const quad: BufferGeometry = BufferGeometryUtils.mergeGeometries( quadGeometries, false );
	const triangle: BufferGeometry = BufferGeometryUtils.mergeGeometries( triangleGeometries, false );

	return (
		<group>
			<mesh geometry={quad}>
				<meshBasicMaterial color={"black"} side={DoubleSide}/>
				<lineSegments>
					<edgesGeometry args={[quad]} />
					<lineBasicMaterial color={"white"} linewidth={1}/>
				</lineSegments>
			</mesh>	
			<mesh geometry={triangle}>
				<meshBasicMaterial color={"black"} side={DoubleSide}/>
				<lineSegments>
					<edgesGeometry args={[triangle]} />
					<lineBasicMaterial color={"white"} linewidth={1}/>
				</lineSegments>
			</mesh>	
		</group>
		
	);
}


function extractTriangleGeometries( model: modeling.IModel | null | undefined ): BufferGeometry[] {
	if ( !model ) return [];
  
	const meshes: BufferGeometry[] = [];
  
	// TODO, add line / optional line
	
	const triangles = extractTriangles( model );
    
	meshes.push( ...triangles );
  
	const childMeshes = model.piece?.flatMap( ( child ) =>
		extractTriangleGeometries( child.subModel ) ) ?? [];
  
	meshes.push( ...childMeshes );
		
	return meshes;
}

function extractQuadGeometries( model: modeling.IModel | null | undefined ): BufferGeometry[] {
	if ( !model ) return [];
  
	const meshes: BufferGeometry[] = [];
  
	// TODO, add line / optional line
	
	const quads = extractQuadrilaterals( model );
    
	meshes.push( ...quads );
		
	const childMeshes = model.piece?.flatMap( ( child ) =>
		extractQuadGeometries( child.subModel ) ) ?? [];
  
	meshes.push( ...childMeshes );
		
	return meshes;
}

function extractQuadrilaterals( model: modeling.IModel | null | undefined ): BufferGeometry[]
{
	if( !model ) return [];

	return model.quadrilateral?.map( quad => getQuadrilateral( quad )).filter( geometry => geometry != null ) ?? [];
}

function extractTriangles( model: modeling.IModel | null | undefined ): BufferGeometry[]
{
	if( !model ) return [];

	return model.triangle?.map( triangle => getTriangle( triangle )).filter( geometry => geometry != null ) ?? [];
}
  