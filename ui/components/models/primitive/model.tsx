"use client";
import { ReactNode } from "react";
import * as BufferGeometryUtils from 'three/addons/utils/BufferGeometryUtils.js';
import { BufferAttribute, BufferGeometry, DoubleSide } from "three";
import { verticesToFloat32Array, colorToFloat32Array } from "@/utils/common-utilities";
import { LineEntity, ModelEntity, QuadrilateralEntity, TriangleEntity, Vector3dEmbeddable } from "@/api/schema";

/**
 * @returns a group of two meshes for all quads and triangles provided in the protobuf model object
 */
export function Model( { entity }: { entity: ModelEntity | undefined } ): ReactNode
{	
	if( entity == undefined ) return [];

	const quadGeometries: BufferGeometry[] = getQuadrilaterals( entity ).map( quad => quadrilateralToBufferGeometry( quad ) ).filter( quadGeometry => quadGeometry != undefined );
	
	const triangleGeometries: BufferGeometry[] = getTriangles( entity ).map( triangle => triangleToBufferGeometry( triangle ) ).filter( triangleGeometry => triangleGeometry != undefined );

	const lineGeometries: BufferGeometry[] = getLines( entity ).map( line => lineToBufferGeometry( line ) ).filter( lineGeometry => lineGeometry != undefined );

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
			{
				lineGeometries.map( ( line, index ) =>
				{
					return (
						<lineSegments key={index} geometry={line}>
							<lineBasicMaterial color="black" linewidth={1} />
						</lineSegments >
					);
				} )
			}
		</group>	
	);	  
}


function getTriangles( model: ModelEntity ): TriangleEntity[] {

	const triangles: TriangleEntity[] = model.triangles;
    
	for ( const subFileRef of model.pieces ) triangles.push( ...getTriangles( subFileRef.subModel ) ); 

	return triangles;
}

function getQuadrilaterals( model: ModelEntity ): QuadrilateralEntity[] {
	const quads: QuadrilateralEntity[] = model.quadrilaterals;
    
	for ( const subFileRef of model.pieces ) quads.push( ...getQuadrilaterals( subFileRef.subModel ) ); 

	return quads;
} 

function getLines( model: ModelEntity ): LineEntity[] {
	const lines: LineEntity[] = model.lines;
    
	for ( const subFileRef of model.pieces ) lines.push( ...getLines( subFileRef.subModel ) ); 

	return lines;
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
	const gpbVertices: Array<Vector3dEmbeddable> = [ p1, p2, p3 ];

	const vertices = verticesToFloat32Array( gpbVertices );

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
	const gpbVertices: Array<Vector3dEmbeddable> = [ p1, p2, p3, p4 ];
	const vertices = verticesToFloat32Array( gpbVertices );
	const indices = [ 0, 1, 2, 2, 3, 0 ];
        
	geometry.setIndex( indices );
	geometry.setAttribute( "position", new BufferAttribute( vertices, 3, false ) );

	geometry.computeVertexNormals();

	geometry.setAttribute( "color", new BufferAttribute( colorToFloat32Array( color, 4 ), 3 ) );

	return geometry;
}

function lineToBufferGeometry( lineEntity: LineEntity ): BufferGeometry | undefined
{
	const { p1, p2 } = lineEntity;

	if( !p1 || !p2 )
	{
		console.warn( "Vertex is undefined" );
		return undefined;
	}

	const geometry = new BufferGeometry();
	const gpbVertices: Array<Vector3dEmbeddable> = [ p1, p2  ];
	const vertices = verticesToFloat32Array( gpbVertices );
        
	geometry.setAttribute( "position", new BufferAttribute( vertices, 3, false ) );

	return geometry;
}

