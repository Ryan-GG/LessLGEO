"use client";
import { modeling } from "@/proto-bundle";
import { colorToFloat32Array, verticesToFloat32Array } from "@/utils/common-utilities";
import { BufferAttribute, BufferGeometry, Color } from "three";
import { ColorMap } from "./model";

export function getTriangle( gpb: modeling.ITriangle, colorMap: ColorMap ): BufferGeometry | undefined
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
	
	geometry.computeVertexNormals();

	// Defaults to black
	const color: Color = new Color( `#${colorMap[colorId ?? 0]?.rgb}` );

	geometry.setAttribute( "color", new BufferAttribute( colorToFloat32Array( color, 3 ), 3 ) );
        
	return geometry;
}