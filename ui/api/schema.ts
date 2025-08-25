import { modeling } from "@/proto-bundle";
import { Reader } from "protobufjs";

export const API_VERSION = "v1";

// ---------- Entities ----------

export interface ModelEntity {
    readonly uuid: string,
    readonly modelData: Base64String,
}

export interface ColorEntity {
    readonly id: number,
    readonly name: string,
    readonly rgb: string,
    readonly isTrans: boolean,
    readonly numParts: number,
    readonly numSets: number,
    readonly startYear?: number,
    readonly endYear?: number
}

// ---------- Reference Ids ----------

export type ModelRefId = modeling.Model["UUID"];
export type ColorRefId = modeling.Color["id"];


// ---------- Entity to Protobuf ----------

type Base64String = string & { __brand: "base64" };

export function entityToProtobuf<T>( base64EncodedPayload: Base64String, decode: ( reader: Reader | Uint8Array, length?: number | undefined) => T ): T | undefined
{
	try {
		const protoObject = decode( Uint8Array.from(Buffer.from(base64EncodedPayload, "base64")) );
		return protoObject;
	}
	catch( error: unknown )
	{
		console.error( "Received error: ", error );
        return undefined;
	}
}