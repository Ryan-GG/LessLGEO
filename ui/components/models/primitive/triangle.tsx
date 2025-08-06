"use client";
import { fetchColorById } from "@/api/model/colorApi";
import { modeling } from "@/proto-bundle";
import { verticesToFloat32Array } from "@/utils/vertex-utilities";
import { useQuery } from "@tanstack/react-query";
import { ReactElement } from "react";
import { BufferAttribute, BufferGeometry, Color, MeshBasicMaterial } from "three";

export function Triangle( { gpb }: { gpb: modeling.ITriangle } ): ReactElement | undefined
{
	if( !gpb.p1 ||
        !gpb.p2 ||
        !gpb.p3 )
	{
		console.warn( "Vertex is undefined" );
		return undefined;
	}

	const { p1,p2,p3, colorId } = gpb;

	const { data: color } = useQuery( { queryKey: [ "color" ], 
	enabled: colorId !== undefined,
	queryFn: () => fetchColorById( colorId! ) } );
        
	const geometry = new BufferGeometry();

        
	const gpbVertices: Array<modeling.IVertex> = [ p1, p2, p3 ];

	const vertices = verticesToFloat32Array( gpbVertices );

	const indices = [ 0, 1, 2, 2 ];
        
	geometry.setIndex( indices );
	geometry.setAttribute( 'position', new BufferAttribute( vertices, 3, false ) );
        
	const material = new MeshBasicMaterial( { color: new Color(`#${color?.rgb}`) });
	return (
		<mesh geometry={geometry} material={material}/>
	);
}