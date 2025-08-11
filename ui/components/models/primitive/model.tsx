"use client";
import { modeling } from "@/proto-bundle";
import { ReactNode } from "react";
import { Quadrilateral } from "./quadrilateral";
import { Triangle } from "./triangle";
import { PivotControls } from "@react-three/drei";
import { THREE_LDU_SCALAR_VECTOR } from "@/utils/units-utilities";

export function Model( { gpb }: { gpb: modeling.IModel | undefined } ): ReactNode 
{
	if( gpb === undefined ) return [];
	return (
		<PivotControls rotation={[ Math.PI, Math.PI / 2, 2 * Math.PI ] } anchor={[ -1.4, 1, -1.4 ]} scale={THREE_LDU_SCALAR_VECTOR.x} lineWidth={3.5}>
			<object3D>
				{extractMeshes( gpb )}
			</object3D>
		</PivotControls>
	);
}

function extractMeshes( model: modeling.IModel | null | undefined ): ReactNode[] {
	if ( !model ) return [];
  
	const meshes: ReactNode[] = [];
  
	const quadMeshes = model.quadrilateral?.map( ( quad ) => (
	  <Quadrilateral key={`quad-${crypto.randomUUID()}`} gpb={quad} />
	) ) ?? [];
  
	const triangleMeshes = model.triangle?.map( ( triangle ) => (
	  <Triangle key={`tri-${crypto.randomUUID()}`} gpb={triangle} />
	) ) ?? [];
  
	meshes.push( ...quadMeshes, ...triangleMeshes );
  
	const childMeshes = model.piece?.flatMap( ( child ) =>
	  extractMeshes( child.subModel ) ) ?? [];
  
	meshes.push( ...childMeshes );
  
	return meshes;
}
  