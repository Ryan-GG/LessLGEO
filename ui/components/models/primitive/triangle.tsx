"use client";
import { modeling } from "@/proto-bundle";
import { verticesToFloat32Array } from "@/utils/vertex-utilities";
import { ReactElement } from "react";
import { BufferAttribute, BufferGeometry, MeshBasicMaterial } from "three";

export function Triangle( { gpb }: { gpb: modeling.ITriangle } ): ReactElement | null
{
    if( !gpb.p1 ||
        !gpb.p2 ||
        !gpb.p3 )
        {
            console.warn("Vertex is undefined");
            return null;
        }

        const {p1,p2,p3, colorId} = gpb;
        
        const geometry = new BufferGeometry();

        
        const gpbVertices: Array<modeling.IVertex> = [ p1, p2, p3 ];

        const vertices = verticesToFloat32Array( gpbVertices );

        const indices = [ 0, 1, 2, 2 ];
        
        geometry.setIndex( indices );
        geometry.setAttribute( 'position', new BufferAttribute( vertices, 3, false ) );
        
        const material = new MeshBasicMaterial( { color: 0xff0000 } );
    return (
        <mesh geometry={geometry} material={material}/>
    );
}