"use client";
import { modeling } from "@/proto-bundle";
import { LDU_TO_THREE_SCALAR_VECTOR } from "@/utils/units-utilities";
import { verticesToFloat32Array } from "@/utils/vertex-utilities";
import { ReactElement } from "react";
import { BufferAttribute, BufferGeometry, MeshBasicMaterial } from "three";

export function Quadrilateral( { gpb }: { gpb: modeling.IQuadrilateral } ): ReactElement | undefined
{
	if( !gpb.p1 ||
        !gpb.p2 ||
        !gpb.p3 ||
        !gpb.p4 )
	{
		console.warn( "Vertex is undefined" );
		return undefined;
	}

	const { p1,p2,p3,p4, colorId } = gpb;
        
	const geometry = new BufferGeometry();

        
	const gpbVertices: Array<modeling.IVertex> = [ p1, p2, p3, p4 ];

	const vertices = verticesToFloat32Array( gpbVertices );

	const indices = [ 0, 1, 2,
		2, 3, 0, ];
        
	geometry.setIndex( indices );
	geometry.setAttribute( 'position', new BufferAttribute( vertices, 3, false ) );
        
	const material = new MeshBasicMaterial( { color: 0xFF_F0_00 } );
	return (
		<mesh geometry={geometry} material={material} scale={LDU_TO_THREE_SCALAR_VECTOR}/>
	);
}