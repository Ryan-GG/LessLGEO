"use client";
import { CameraControls, PivotControls } from "@react-three/drei";
import { Canvas, useFrame } from "@react-three/fiber";
import { ReactNode, useRef } from "react";
import { Mesh } from "three";

export function ModelCanvas(): ReactNode
{
	return (
		<Canvas>
			<PivotControls anchor={[ -1.1, -1.1, -1.1 ]} scale={0.75} lineWidth={3.5}>
				<Box/>
				<ambientLight intensity={0.1} />
				<directionalLight position={[ 0, 0, 5 ]} color="red" />
				<CameraControls makeDefault/>
			</PivotControls>
		</Canvas>
	);
}

function Box(): ReactNode
{
	const meshReference = useRef<Mesh>( undefined );

	useFrame( ( { clock } ) => {
		if( meshReference.current !== undefined )
		{
			meshReference.current.rotation.x = clock.elapsedTime;
			meshReference.current.rotation.y = clock.elapsedTime;
		}
	} );

    
	return (
		<mesh ref={meshReference}>
			<boxGeometry args={[ 2, 2, 2 ]} />
			<meshPhongMaterial />
		</mesh>
	);
}