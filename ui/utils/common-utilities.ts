import { ColorEntity } from "@/api/schema";
import { modeling } from "@/proto-bundle";
import { Color, Vector3 } from "three";

export const MISSING_VALUE_EXCEPTION = "Missing Value Exception" as const;
export function gpbToVector3( vertex: modeling.IVertex ): Vector3
{
	const { x, y, z } = vertex;
	if ( x == undefined || y == undefined || z == undefined ) {
		throw new Error( MISSING_VALUE_EXCEPTION);
	}
	return new Vector3( x, y, z );
}

export function gpbToFloat32Array( vertex: modeling.IVertex ): Float32Array
{
	const { x, y, z } = vertex;
	if ( x == undefined || y == undefined || z == undefined ) {
		throw new Error( MISSING_VALUE_EXCEPTION );
	}
	return new Float32Array( [ x, y, z ] );
}

export function verticesToFloat32Array( vertices: ReadonlyArray<modeling.IVertex> ): Float32Array
{
	return new Float32Array(
		vertices.flatMap( vertex => [ ...gpbToFloat32Array( vertex ) ] )
	);
}

export function colorToFloat32Array(
	colorEntity: ColorEntity,
	numberOfVertices: number
  ): Float32Array {
	const color = new Color( `#${colorEntity.rgb}` );  
	if( numberOfVertices < 0 ) numberOfVertices = 0;
	const colorArray = new Float32Array(numberOfVertices * 3);
  
	for (let i = 0; i < numberOfVertices; i++) {
	  const offset = i * 3;
	  colorArray[offset] = color.r;
	  colorArray[offset + 1] = color.g;
	  colorArray[offset + 2] = color.b;
	}
	
	return colorArray;
  }