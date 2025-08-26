"use client";
import { CameraControls, GizmoHelper, GizmoViewport } from "@react-three/drei";
import { Canvas } from "@react-three/fiber";
import { ReactNode } from "react";
import { Model } from "./primitive/model";
import { fetchModelById } from "@/api/model-api";
import { useQuery } from "@tanstack/react-query";
import { Object3D, Vector3 } from "three";
import { GRID_SIZE, GridHelper } from "./grid-helper";
import { ModelRefId } from "@/api/schema";
import { THREE_LDU_SCALAR_VECTOR } from "@/utils/units-utilities";

// LDraw uses a right-handed co-ordinate system where -Y is "up".
const DEFAULT_UP = new Vector3( 0, -1, 0 );

/**
 * @returns a ThreeJS Canvas that will render the provided modeel based on {@link ModelRefId}
 */
export function ModelCanvas( { modelId }: {modelId: ModelRefId | undefined} ): ReactNode
{
	// Sets default UP direction for the ThreeJS Canvas
	Object3D.DEFAULT_UP = DEFAULT_UP;
	
	const { data: model } = useQuery( { queryKey: [ "model", modelId ],
		enabled: modelId !== undefined,
		queryFn: () => fetchModelById( modelId! ) } );
	
	return (
		<Canvas camera={{ position: [ 0, -GRID_SIZE / 2, -GRID_SIZE / 2 ] }}>
			<group scale={THREE_LDU_SCALAR_VECTOR}>
				<Model gpb={model}/>
			</group>
			<GridHelper/>
			<ambientLight intensity={1} />
			<directionalLight position={[ 0, -GRID_SIZE, 0 ]} color="white" intensity={1}/>
			<GizmoHelper alignment="top-right" margin={[ 50, 50 ]}>
				<GizmoViewport labelColor="black" axisHeadScale={1} />
			</GizmoHelper>
			<CameraControls makeDefault/>
		</Canvas>
	);
}
