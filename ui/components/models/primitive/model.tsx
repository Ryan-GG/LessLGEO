"use client";
import { modeling } from "@/proto-bundle";
import { ReactNode } from "react";
import * as BufferGeometryUtils from 'three/addons/utils/BufferGeometryUtils.js';
import { BufferAttribute, BufferGeometry, Color, DoubleSide } from "three";
import { ColorEntity, fetchAllColors } from "@/api/color-api";
import { useQuery } from "@tanstack/react-query";
import { verticesToFloat32Array, colorToFloat32Array } from "@/utils/common-utilities";

export type ColorMap = Record<string, ColorEntity>;

export function Model( { gpb }: { gpb: modeling.IModel | undefined } ): ReactNode
{
	const { data: colors } = useQuery( { queryKey: [ "colors" ], queryFn: fetchAllColors } );
	
	if( gpb == undefined ) return [];

	const colorMap: ColorMap = colors?.reduce( ( map, colorEntity ) => {
		map[colorEntity.id] = colorEntity;
		return map;
	}, {} as ColorMap ) ?? {};

	const quadGeometries: BufferGeometry[] = getQuadrilaterals( gpb ).map( quad => quadrilateralToBufferGeometery( quad, colorMap ) ).filter( quadGeometry => quadGeometry != undefined );
	const triangleGeometries: BufferGeometry[] = getTriangles( gpb ).map( triangle => triangleToBufferGeometry( triangle, colorMap ) ).filter( triangleGeometry => triangleGeometry != undefined );


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


function getTriangles( model: modeling.IModel | null | undefined ): modeling.ITriangle[] {

	const triangles: modeling.ITriangle[] = model?.triangle ?? [];
    
	model?.piece?.forEach( subFileReference => triangles.push( ...getTriangles( subFileReference.subModel ) ) )

	return triangles;
}

function getQuadrilaterals( model: modeling.IModel | null | undefined ): modeling.IQuadrilateral [] {
	const quads: modeling.IQuadrilateral[] = model?.quadrilateral ?? [];
    
	model?.piece?.forEach( subFileReference => quads.push( ...getQuadrilaterals( subFileReference.subModel ) ) )

	return quads;
} 

function triangleToBufferGeometry( gpb: modeling.ITriangle, colorMap: ColorMap ): BufferGeometry | undefined
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
	const colorEntity: ColorEntity = colorMap[colorId ?? 0];
	geometry.setAttribute( "color", new BufferAttribute( colorToFloat32Array( colorEntity, 3 ), 4 ) );
        
	return geometry;
}

function quadrilateralToBufferGeometery( gpb: modeling.IQuadrilateral, colorMap: ColorMap ): BufferGeometry | undefined
{
	const { p1,p2,p3,p4, colorId } = gpb;

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

	// Defaults to black
	const colorEntity: ColorEntity = colorMap[colorId ?? 0];
	geometry.setAttribute( "color", new BufferAttribute( colorToFloat32Array( colorEntity, 4 ), 4 ) );

	return geometry;
}
