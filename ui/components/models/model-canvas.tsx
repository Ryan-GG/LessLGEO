"use client";
import { CameraControls } from "@react-three/drei";
import { Canvas } from "@react-three/fiber";
import { ReactNode } from "react";
import { Model } from "./primitive/model";
import { fetchModelById } from "@/api/model/modelApi";
import { useQuery } from "@tanstack/react-query";
import { Object3D, Vector3 } from "three";
import { GridHelper } from "./grid-helper";

// LDraw uses a right-handed co-ordinate system where -Y is "up".
export const DEFAULT_UP = new Vector3( 0, -1, 0 );

export function ModelCanvas( { modelId }: {modelId: string | undefined} ): ReactNode
{
	const { data: model } = useQuery( { queryKey: [ "model" ], 
		enabled: modelId !== undefined,
		queryFn: () => fetchModelById( modelId! ) } );

	Object3D.DEFAULT_UP = DEFAULT_UP;

	return (
		<Canvas camera={{ position: [ 0, -500, -500 ], far: 5000 }}>
			<Model gpb={model}/>
			<GridHelper/>
			<ambientLight intensity={0.1} />
			<directionalLight position={[ 0, -500, 0 ]} color="red" />
			<CameraControls makeDefault/>
		</Canvas>
	);
}
