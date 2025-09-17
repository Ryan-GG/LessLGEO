import { z } from "zod";
import { modeling } from "@/proto-bundle";
import { Reader } from "protobufjs";

export const API_VERSION = "v1";

// ---------- Entities ----------

export interface LineEntity {
    readonly id: number;
    readonly color: ColorEntity;
    readonly p1: modeling.Vertex;
    readonly p2: modeling.Vertex;
}

export interface TriangleEntity {
    readonly id: number;
    readonly color: ColorEntity;
    readonly p1: modeling.Vertex;
    readonly p2: modeling.Vertex;
    readonly p3: modeling.Vertex;
}

export interface QuadrilateralEntity {
    readonly id: number;
    readonly color: ColorEntity;
    readonly p1: modeling.Vertex;
    readonly p2: modeling.Vertex;
    readonly p3: modeling.Vertex;
    readonly p4: modeling.Vertex;
}

export interface OptionalLineEntity {
    readonly id: number;
    readonly color: ColorEntity;
    readonly p1: modeling.Vertex;
    readonly p2: modeling.Vertex;
    readonly p3: modeling.Vertex;
    readonly p4: modeling.Vertex;
}

export interface ModelEntity {
    readonly id: number;
    readonly lines: LineEntity[];
    readonly triangles: TriangleEntity[];
    readonly quadrilaterals: QuadrilateralEntity[];
    readonly optionalLines: OptionalLineEntity[];
    readonly children: ModelEntity[];
}

export interface ColorEntity {
    readonly id: number;
    readonly name: string;
    readonly rgb: string;
    readonly isTrans: boolean;
    readonly numParts: number;
    readonly numSets: number;
    readonly startYear?: number | null;
    readonly endYear?: number | null;
}

// ---------- Entity Schemas ----------

export const IdSchema = z.number();
export const IdSchemaArray = z.array( IdSchema );

const VertexSchema = z.object({
    x: z.number(),
    y: z.number(),
    z: z.number(),
})

const ColorEntitySchema = z.object({
    id: z.number(),
    name: z.string(),
    rgb: z.string(),
    isTrans: z.coerce.boolean(),
    numParts: z.number(),
    numSets: z.number(),
    startYear: z.optional( z.number() ).nullable(),
    endYear: z.optional( z.number() ).nullable(),
});

const LineEntitySchema = z.object({
    id: z.number(),
    color: ColorEntitySchema,
    p1: VertexSchema,
    p2: VertexSchema
});

const LineEntityArraySchema = z.array( LineEntitySchema );

const TriangleEntitySchema = z.object({
    id: z.number(),
    color: ColorEntitySchema,
    p1: VertexSchema,
    p2: VertexSchema,
    p3: VertexSchema
});

const TriangleEntityArraySchema = z.array( TriangleEntitySchema );

const QuadrilateralEntitySchema = z.object({
    id: z.number(),
    color: ColorEntitySchema,
    p1: VertexSchema,
    p2: VertexSchema,
    p3: VertexSchema,
    p4: VertexSchema
});

const QuadrilateralEntityArraySchema = z.array( QuadrilateralEntitySchema );

const OptionalLineEntitySchema = z.object({
    id: z.number(),
    color: ColorEntitySchema,
    p1: VertexSchema,
    p2: VertexSchema,
    p3: VertexSchema,
    p4: VertexSchema
});

const OptionalLineEntityArraySchema = z.array( OptionalLineEntitySchema );

/**
 * This type is needs so that zod can define the type that the schema object will return.
 * This is a problem due to the recursive structure of LDraw Models.
 * This can be safely casted to a {@link ModelEntity}
 */
export type ModelEntitySchemaType = {
    id: z.infer<typeof IdSchema>,
    lines: z.infer<typeof LineEntityArraySchema>;
    triangles: z.infer<typeof TriangleEntityArraySchema>;
    quadrilaterals: z.infer<typeof QuadrilateralEntityArraySchema>;
    optionalLines: z.infer<typeof OptionalLineEntityArraySchema>;
    children: ModelEntitySchemaType[];
  };
  
export const ModelEntitySchema: z.ZodType<ModelEntitySchemaType> = z.lazy(() =>
    z.object({
      id: IdSchema,
      lines: LineEntityArraySchema,
      triangles: TriangleEntityArraySchema,
      quadrilaterals: QuadrilateralEntityArraySchema,
      optionalLines: OptionalLineEntityArraySchema,
      children: z.array(ModelEntitySchema),
    })
  );

// ---------- Reference Ids ----------

export type ColorRefId = modeling.Color["id"];
