"use client";
import { modeling } from "@/proto-bundle";
import { ReactNode } from "react";
import * as BufferGeometryUtils from 'three/addons/utils/BufferGeometryUtils.js';
import { BufferAttribute, BufferGeometry, DoubleSide } from "three";
import { verticesToFloat32Array, colorToFloat32Array } from "@/utils/common-utilities";
import { ColorEntity, ColorRefId, ModelEntity, QuadrilateralEntity, TriangleEntity } from "@/api/schema";

type ColorMap = Record<ColorRefId, ColorEntity>;

/**
 * @returns a group of two meshes for all quads and triangles provided in the protobuf model object
 */
export function Model( { entity }: { entity: ModelEntity | undefined } ): ReactNode
{	
	if( entity == undefined ) return [];

	const quadGeometries: BufferGeometry[] = getQuadrilaterals( entity ).map( quad => quadrilateralToBufferGeometry( quad ) ).filter( quadGeometry => quadGeometry != undefined );
	const triangleGeometries: BufferGeometry[] = getTriangles( entity ).map( triangle => triangleToBufferGeometry( triangle ) ).filter( triangleGeometry => triangleGeometry != undefined );


	const quad: BufferGeometry = BufferGeometryUtils.mergeGeometries( quadGeometries, false );
	const triangle: BufferGeometry = BufferGeometryUtils.mergeGeometries( triangleGeometries, false );

	return (
		<group>
			<mesh geometry={quad}>
				<meshPhongMaterial vertexColors transparent side={DoubleSide}/>
				<lineSegments>
					<edgesGeometry args={[ quad ]} />
					<lineBasicMaterial color={"black"} linewidth={1}/>
				</lineSegments>
			</mesh>	
			<mesh geometry={triangle}>
				<meshPhongMaterial vertexColors transparent side={DoubleSide}/>
				<lineSegments>
					<edgesGeometry args={[ triangle ]} />
					<lineBasicMaterial color={"black"} linewidth={1}/>
				</lineSegments>
			</mesh>	
		</group>	
	);	  
}


function getTriangles( model: ModelEntity ): TriangleEntity[] {

	const triangles: TriangleEntity[] = model.triangles ?? [];
    
	for ( const subModel of model?.children ) triangles.push( ...getTriangles( subModel ) ); 

	return triangles;
}

function getQuadrilaterals( model: ModelEntity ): QuadrilateralEntity[] {
	const quads: QuadrilateralEntity[] = model.quadrilaterals ?? [];
    
	for ( const subModel of model?.children ) quads.push( ...getQuadrilaterals( subModel ) ); 

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
	const gpbVertices: Array<modeling.IVertex> = [ p1, p2, p3 ];

	const vertices = verticesToFloat32Array( gpbVertices );

	const indices = [ 0, 1, 2 ];
        
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
	const gpbVertices: Array<modeling.IVertex> = [ p1, p2, p3, p4 ];
	const vertices = verticesToFloat32Array( gpbVertices );
	const indices = [ 0, 1, 2, 2, 3, 0, ];
        
	geometry.setIndex( indices );
	geometry.setAttribute( "position", new BufferAttribute( vertices, 3, false ) );

	geometry.computeVertexNormals();

	geometry.setAttribute( "color", new BufferAttribute( colorToFloat32Array( color, 4 ), 3 ) );

	return geometry;
}
