"use client";
import { CameraControls } from "@react-three/drei";
import { Canvas } from "@react-three/fiber";
import { ReactNode } from "react";
import { Model } from "./primitive/model";
import { fetchModelById } from "@/api/model/modelApi";
import { useQuery } from "@tanstack/react-query";
import { Object3D, Vector3 } from "three";

export const GRID_SIZE: number = 50;

export function ModelCanvas( { modelId }: {modelId: string | undefined} ): ReactNode
{
	const { data: model } = useQuery( { queryKey: [ "model" ], 
		enabled: modelId !== undefined,
		queryFn: () => fetchModelById( modelId! ) } );

	Object3D.DEFAULT_UP = new Vector3( 0, -1, 0 );

	return (
		<Canvas>
			<Model gpb={model}/>
			<gridHelper/>
			<ambientLight intensity={0.1} />
			<directionalLight position={[ 0, 0, 5 ]} color="red" />
			<CameraControls makeDefault/>
		</Canvas>
	);
}
