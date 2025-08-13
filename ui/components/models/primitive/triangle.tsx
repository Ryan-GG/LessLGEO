"use client";
import { fetchColorById } from "@/api/colorApi";
import { modeling } from "@/proto-bundle";
import { verticesToFloat32Array } from "@/utils/vertex-utilities";
import { BufferAttribute, BufferGeometry, Color } from "three";

export function getTriangle( gpb: modeling.ITriangle, colorEntity?: any ): BufferGeometry | undefined
{
	const { p1,p2,p3, colorId } = gpb;

	if( !p1 ||
        !p2 ||
        !p3 )
	{
		console.warn( "Vertex is undefined" );
		return undefined;
	}

	const geometry = new BufferGeometry();
	const gpbVertices: Array<modeling.IVertex> = [ p1, p2, p3 ];

	const vertices = verticesToFloat32Array( gpbVertices );

	const indices = [ 0, 1, 2 ];
        
	geometry.setIndex( indices );
	geometry.setAttribute( 'position', new BufferAttribute( vertices, 3, false ) );
	
	// Calculate normals for proper lighting
	geometry.computeVertexNormals();
        
	// Use the passed color entity if available
	if (colorEntity?.rgb) {
		const color = new Color(`#${colorEntity.rgb}`);
		// You could store this color in the geometry userData for later use
		geometry.userData = { color };
	}

	return geometry;
}