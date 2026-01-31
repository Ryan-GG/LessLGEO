"use client";
import { ReactNode } from "react";
import * as BufferGeometryUtils from 'three/addons/utils/BufferGeometryUtils.js';
import { BufferAttribute, BufferGeometry, DoubleSide } from "three";
import { verticesToFloat32Array, colorToFloat32Array } from "@/utils/common-utilities";
import { ModelEntity, QuadrilateralEntity, TriangleEntity, Vector3dEmbeddable } from "@/api/schema";

/**
 * @returns a group of two meshes for all quads and triangles provided in the protobuf model object
 */
export function Model( { entity }: { entity: ModelEntity | undefined } ): ReactNode
{	
	if( entity == undefined ) return [];

	const quadGeometries: BufferGeometry[] = getQuadrilaterals( entity ).map( quad => quadrilateralToBufferGeometry( quad ) ).filter( quadGeometry => quadGeometry != undefined );
	
	const triangleGeometries: BufferGeometry[] = getTriangles( entity ).map( triangle => triangleToBufferGeometry( triangle ) ).filter( triangleGeometry => triangleGeometry != undefined );

	let quad: BufferGeometry | undefined = undefined;
	let triangle: BufferGeometry | undefined = undefined;

	if( quadGeometries.length > 0 )
	{
		quad = BufferGeometryUtils.mergeGeometries( quadGeometries, false );
	}
	if( triangleGeometries.length > 0 )
	{
		triangle = BufferGeometryUtils.mergeGeometries( triangleGeometries, false );
	}

	return (
		<group>
			{quad && (
				<group>
					<mesh geometry={quad}>
						<meshPhongMaterial vertexColors transparent side={DoubleSide} />
					</mesh>

					<lineSegments>
						<edgesGeometry args={[ quad ]}/>
						<lineBasicMaterial color="black" linewidth={1} />
					</lineSegments>
				</group>
			)}
			{triangle && (
				<group>
					<mesh geometry={triangle}>
						<meshPhongMaterial vertexColors transparent side={DoubleSide} />
					</mesh>

					<lineSegments>
						<wireframeGeometry args={[ triangle ]}/>
						<lineBasicMaterial color="black" linewidth={1} />
					</lineSegments>
				</group>
			)}
		</group>	
	);	  
}


function getTriangles( model: ModelEntity ): TriangleEntity[] {

	const triangles: TriangleEntity[] = model.triangles;
    
	for ( const subFileReference of model.pieces ) triangles.push( ...getTriangles( subFileReference.subModel ) ); 

	return triangles;
}

function getQuadrilaterals( model: ModelEntity ): QuadrilateralEntity[] {
	const quads: QuadrilateralEntity[] = model.quadrilaterals;
    
	for ( const subFileReference of model.pieces ) quads.push( ...getQuadrilaterals( subFileReference.subModel ) ); 

	return quads;
} 

function triangleToBufferGeometry( triangleEntity: TriangleEntity ): BufferGeometry | undefined
{
	const { p1, p2, p3, color } = triangleEntity;

	if( !p1 ||
        !p2 ||
        !p3 )
	{
		console.warn( "Vertex is undefined" );
		return undefined;
	}

	const geometry = new BufferGeometry();
	const vertices = verticesToFloat32Array( [ p1, p2, p3 ] );

	const indices = [ 0, 1, 2  ];
        
	geometry.setIndex( indices );
	geometry.setAttribute( 'position', new BufferAttribute( vertices, 3, false ) );
	
	geometry.computeVertexNormals();

	geometry.setAttribute( "color", new BufferAttribute( colorToFloat32Array( color, 3 ), 3 ) );
        
	return geometry;
}

function quadrilateralToBufferGeometry( quadrilateralEntity: QuadrilateralEntity ): BufferGeometry | undefined
{
	const { p1, p2, p3, p4, color } = quadrilateralEntity;

	if( !p1 ||
        !p2 ||
        !p3 ||
        !p4 )
	{
		console.warn( "Vertex is undefined" );
		return undefined;
	}

	const geometry = new BufferGeometry();
	const vertices = verticesToFloat32Array( [ p1, p2, p3, p4 ] );
	const indices = [ 0, 1, 2, 2, 3, 0 ];
        
	geometry.setIndex( indices );
	geometry.setAttribute( "position", new BufferAttribute( vertices, 3, false ) );

	geometry.computeVertexNormals();

	geometry.setAttribute( "color", new BufferAttribute( colorToFloat32Array( color, 4 ), 3 ) );

	return geometry;
}
