import { THREE_LDU_SCALAR_VECTOR } from "@/utils/units-utilities";
import { Text3D } from "@react-three/drei";
import { ReactElement, Suspense } from "react";
import { Color, MeshStandardMaterial }  from "three";

export const GRID_SIZE: number = 50 as const;
export const GRID_COLOR_CENTER: Color = new Color(1,0,0);
export const GRID_COLOR_LINES: Color = new Color(0.2,0.2, 0.2);

export const X_COLOR: Color = new Color(1,0,0);
export const Z_COLOR: Color = new Color(0,0,1);

export function GridHelper(): ReactElement
{
	return ( 
		<group>
			<Suspense fallback={undefined}>
				<Text3D
					font="/fonts/Mozilla Text ExtraLight_Regular.json"
					size={THREE_LDU_SCALAR_VECTOR.x}
					height={1}
					curveSegments={12}
					rotation={[ 0, Math.PI, Math.PI ]}
					position={[ ( ( GRID_SIZE * THREE_LDU_SCALAR_VECTOR.x ) / 2 ), 0, 0 ]}
				>
					{`X: ${( GRID_SIZE ) / 2}`}
					<meshStandardMaterial color={X_COLOR} />
				</Text3D>
			</Suspense>
			<Suspense fallback={undefined}>
				<Text3D
					font="/fonts/Mozilla Text ExtraLight_Regular.json"
					size={THREE_LDU_SCALAR_VECTOR.z}
					height={1}
					curveSegments={12}
					rotation={[ 0, Math.PI, Math.PI ]}
					position={[ 0, 0, ( ( GRID_SIZE * THREE_LDU_SCALAR_VECTOR.z ) / 2 ) ]}
				>
					{`Z: ${( GRID_SIZE ) / 2}`}
					<meshStandardMaterial color={Z_COLOR} />
				</Text3D>
			</Suspense>
			<gridHelper args={[ GRID_SIZE, GRID_SIZE, GRID_COLOR_CENTER, GRID_COLOR_LINES ]} scale={THREE_LDU_SCALAR_VECTOR}/>
		</group>
	);
}
