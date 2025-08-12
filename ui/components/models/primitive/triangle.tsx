"use client";
import { fetchColorById } from "@/api/colorApi";
import { modeling } from "@/proto-bundle";
import { verticesToFloat32Array } from "@/utils/vertex-utilities";
import { useQuery } from "@tanstack/react-query";
import { ReactElement } from "react";
import { BufferAttribute, BufferGeometry, Color, DoubleSide } from "three";

export function Triangle( { gpb }: { gpb: modeling.ITriangle } ): ReactElement | undefined
{
	const { p1,p2,p3, colorId } = gpb;

	const { data: colorEntity } = useQuery( { 
		queryKey: [ "color", colorId ], 
		enabled: colorId !== undefined,
		queryFn: () => fetchColorById( colorId! ) } );

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

	const indices = [ 0, 1, 2, 2 ];
        
	geometry.setIndex( indices );
	geometry.setAttribute( 'position', new BufferAttribute( vertices, 3, false ) );
        
	const color: Color = new Color(`#${colorEntity?.rgb}`);

	return (
		<mesh geometry={geometry}>
			{ /* TODO, [Task] Implement BFC(Back Face Culling) Meta command #29 */}
			<meshBasicMaterial color={color} side={DoubleSide}/>
			<lineSegments>
				<edgesGeometry args={[geometry]} />
				<lineBasicMaterial color={"black"} linewidth={1} />
			</lineSegments>
		</mesh>	
	);
}