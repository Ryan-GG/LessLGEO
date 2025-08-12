"use client";
import { modeling } from "@/proto-bundle";
import { ReactNode, useRef, useState } from "react";
import { Quadrilateral } from "./quadrilateral";
import { Triangle } from "./triangle";
import { PivotControls } from "@react-three/drei";
import { THREE_LDU_SCALAR_VECTOR } from "@/utils/units-utilities";

export function Model( { gpb }: { gpb: modeling.IModel | undefined } ): ReactNode[]
{
	if( gpb === undefined ) return [];

	return ( gpb.piece?.map( piece => extractMeshes( piece.subModel ) ) ?? [] )
			.map( ( mesh, index ) => <ModelWithControls subMeshes={mesh} key={`model-with-control-${index}`}/> );
}

function ModelWithControls( { subMeshes }: { subMeshes: ReactNode } ): ReactNode {

	const [enabled, setEnabled] = useState<boolean>(false);
	return (
		<group>
			<PivotControls
			rotation={[ Math.PI, Math.PI / 2, 2 * Math.PI ] } 
			anchor={[ -1.4, 1, -1.4 ]} 
			scale={THREE_LDU_SCALAR_VECTOR.x} 
			lineWidth={3.5}
			enabled={enabled}
			onDragEnd={() => setEnabled(false)}
			>
				<mesh onClick={(event) => {
					event.stopPropagation();
					setEnabled( true );
				}}>
					{subMeshes}
				</mesh>	
			</PivotControls>
		</group>
		
	);
}

function extractMeshes( model: modeling.IModel | null | undefined ): ReactNode[] {
	if ( !model ) return [];
  
	const meshes: ReactNode[] = [];
  
	// TODO, add line / optional line
	
	const quads = extractQuadrilaterals( model );
	const triangles = extractTriangles( model );
    
	meshes.push( ...quads, ...triangles );
  
	const childMeshes = model.piece?.flatMap( ( child ) =>
	  extractMeshes( child.subModel ) ) ?? [];
  
	meshes.push( ...childMeshes );
  
	return meshes;
}

function extractQuadrilaterals( model: modeling.IModel | null | undefined ): ReactNode[]
{
	if( !model ) return [];

	return model.quadrilateral?.map( ( quad ) => (
		<Quadrilateral key={`quad-${crypto.randomUUID()}`} gpb={quad} />
	  ) ) ?? [];
}

function extractTriangles( model: modeling.IModel | null | undefined ): ReactNode[]
{
	if( !model ) return [];

	return model.triangle?.map( ( triangle ) => (
		<Triangle key={`triangle-${crypto.randomUUID()}`} gpb={triangle} />
	  ) ) ?? [];
}
  