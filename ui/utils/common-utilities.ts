import { modeling } from "@/proto-bundle";
import { Color, Vector3 } from "three";

export function gpbToVector3( vertex: modeling.IVertex ): Vector3
{
	const { x, y, z } = vertex;
	if ( x == undefined || y == undefined || z == undefined ) {
		throw new Error( "Missing position information" );
	}
	return new Vector3( x, y, z );
}

export function gpbToFloat32Array( vertex: modeling.IVertex ): Float32Array
{
	const { x, y, z } = vertex;
	if ( x == undefined || y == undefined || z == undefined ) {
		throw new Error( "Missing position information" );
	}
	return new Float32Array( [ x, y, z ] );
}

export function verticesToFloat32Array( vertices: ReadonlyArray<modeling.IVertex> ): Float32Array
{
	return new Float32Array(
		vertices.flatMap( vertex => [ ...gpbToFloat32Array( vertex ) ] )
	);
}

export function colorToFloat32Array( color: Color, numberOfVertices: number ): Float32Array
{
	const colorArray = new Float32Array( numberOfVertices * 3 );

	for ( let index = 0; index < 4; index++ ) {
		color.toArray( colorArray, index * 3 );
	}

	return colorArray;
}