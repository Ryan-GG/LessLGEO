"use client";
import { CameraControls, PivotControls } from "@react-three/drei";
import { Canvas } from "@react-three/fiber";
import { ReactNode } from "react";

export function ModelCanvas(): ReactNode
{
	return (
		<Canvas>
			<PivotControls anchor={[ -1.1, -1.1, -1.1 ]} scale={0.75} lineWidth={3.5}>
				<ambientLight intensity={0.1} />
				<directionalLight position={[ 0, 0, 5 ]} color="red" />
				<CameraControls makeDefault/>
			</PivotControls>
		</Canvas>
	);
}
