"use client";
import { CameraControls, PivotControls } from "@react-three/drei";
import { Canvas } from "@react-three/fiber";
import { ReactNode } from "react";
import { Model } from "./primitive/model";
import { fetchModelById } from "@/api/model/modelApi";
import { useQuery } from "@tanstack/react-query";

export function ModelCanvas( {modelId}: {modelId: string | undefined}): ReactNode
{
	const { data: model } = useQuery({ 
		queryKey: ["model"], 
		enabled: modelId !== undefined,
		queryFn: () => fetchModelById(modelId!) } );

	return (
		<Canvas>
			<PivotControls anchor={[ -1.1, -1.1, -1.1 ]} scale={0.75} lineWidth={3.5}>
				<Model gpb={model}/>
				<ambientLight intensity={0.1} />
				<directionalLight position={[ 0, 0, 5 ]} color="red" />
				<CameraControls makeDefault/>
			</PivotControls>
		</Canvas>
	);
}
