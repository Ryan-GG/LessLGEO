import { ColorEntity, Vector3dEmbeddable } from "@/api/schema";
import { Color, Vector3 } from "three";

export const MISSING_VALUE_EXCEPTION = "Missing Value Exception" as const;

/**
 * @param vertex vertex protobuf
 * @returns Vector3 encoding of vertex protobuf
 */
export function toVector3( vertex: Vector3dEmbeddable ): Vector3
{
	const { x, y, z } = vertex;
	if ( x == undefined || y == undefined || z == undefined ) {
		throw new Error( MISSING_VALUE_EXCEPTION);
	}
	return new Vector3( x, y, z );
}

/**
 * 
 * @param vertex vertex protobuf
 * @returns Float32Array encoding of vertex protobuf [ x, y, z ]
 */
export function toFloat32Array( vertex: Vector3dEmbeddable): Float32Array
{
	const { x, y, z } = vertex;
	if ( x == undefined || y == undefined || z == undefined ) {
		throw new Error( MISSING_VALUE_EXCEPTION );
	}
	return new Float32Array( [ x, y, z ] );
}

/**
 * 
 * @param vertices Array of vertex protobufs
 * @returns Float32Array encoding of each vertex [ x1, y1, z1, x2, y2, z2, ... xN-1, yN-1, zN-1 ]
 */
export function verticesToFloat32Array( vertices: ReadonlyArray<Vector3dEmbeddable> ): Float32Array
{
	return new Float32Array(
		vertices.flatMap( vertex => [ ...toFloat32Array( vertex ) ] )
	);
}

/**
 * 
 * @param colorEntity Color ORM Entity
 * @param numberOfVertices Number of vertices for the BufferGeometry
 * @returns Float32Array encoding of color ( r, g, b ) for N-vertices, [ v1-r, v1-g, v1-b, v1-r, v1-g, v1-b, ... ]
 */
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