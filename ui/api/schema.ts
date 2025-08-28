import { z } from "zod";
import { modeling } from "@/proto-bundle";
import { Reader } from "protobufjs";

export const API_VERSION = "v1";

// ---------- Entities ----------

export interface ModelEntity {
    readonly uuid: string,
    readonly modelData: string,
}

export interface ColorEntity {
    readonly id: number,
    readonly name: string,
    readonly rgb: string,
    readonly isTrans: boolean,
    readonly numParts: number,
    readonly numSets: number,
    readonly startYear?: number | null,
    readonly endYear?: number | null,
}

// ---------- Entity Schemas ----------
export const UUIDSchema = z.string().uuid();
export const UUIDArraySchema = z.array( UUIDSchema );

export const ModelEntitySchema = z.object({
    uuid: UUIDSchema,
    modelData: z.string().base64(),
})

export const ColorEntitySchema = z.object({
    id: z.number(),
    name: z.string(),
    rgb: z.string(),
    isTrans: z.coerce.boolean(),
    numParts: z.number(),
    numSets: z.number(),
    startYear: z.optional( z.number() ).nullable(),
    endYear: z.optional( z.number() ).nullable(),
});

export const ColorEntityArraySchema = z.array( ColorEntitySchema );

// ---------- Reference Ids ----------

export type ModelRefId = modeling.Model["UUID"];
export type ColorRefId = modeling.Color["id"];


// ---------- Entity to Protobuf ----------

export function entityToProtobuf<T>( base64EncodedPayload: string, decode: ( reader: Reader | Uint8Array, length?: number | undefined) => T ): T | undefined
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