"use client";
import { CameraControls, PivotControls } from "@react-three/drei";
import { Canvas } from "@react-three/fiber";
import { ReactNode } from "react";
import { Vector3 } from "three";

export function ModelCanvas(): ReactNode
{
	return (
		<Canvas>
			<PivotControls anchor={[ -1.1, -1.1, -1.1 ]} scale={0.75} lineWidth={3.5}>
				{/* <Box/> */}
				<Boxes/>
				<ambientLight intensity={0.1} />
				<directionalLight position={[ 0, 0, 5 ]} color="red" />
				<CameraControls makeDefault/>
			</PivotControls>
		</Canvas>
	);
}

function Boxes(): ReadonlyArray<ReactNode>
{
	const boxPositions: Array<Vector3> = [];

	for( let index = 0; index < 500; index++ )
	{
		const min = Math.ceil( 0 );
		const max = Math.floor( 100 );
		const value1 =  Math.floor( Math.random() * ( max - min + 1 ) ) + min;
		const value2 =  Math.floor( Math.random() * ( max - min + 1 ) ) + min;
		const value3 =  Math.floor( Math.random() * ( max - min + 1 ) ) + min;
		boxPositions.push( new Vector3( value1, value2, value3 ) );
	}
	return boxPositions.map( ( positions, index ) => {
		return (
			<mesh position={positions} key={index}>
				<boxGeometry args={[ 2, 2, 2 ]}/>
				<meshPhongMaterial color={getRandomHexColor()}/>
			</mesh>
		);
	} );
}

function getRandomHexColor() {
	return "#" + Math.floor( Math.random() * 0xFF_FF_FF ).toString( 16 ).padStart( 6, "0" );
}