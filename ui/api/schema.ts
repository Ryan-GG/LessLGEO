import { z } from "zod";

export const API_VERSION = "v1";

// ---------- Embeddable ----------

export interface Vector3dEmbeddable {
    readonly x: number;
    readonly y: number;
    readonly z: number;
}

export interface MatrixEmbeddable {
    readonly a: number;
    readonly b: number;
    readonly c: number;
    readonly x: number;
    readonly d: number;
    readonly e: number;
    readonly f: number;
    readonly y: number;
    readonly g: number;
    readonly h: number;
    readonly i: number;
    readonly z: number;
    readonly scale: number;
}


// ---------- Entities ----------

export interface LineEntity {
    readonly id: number;
    readonly color: ColorEntity;
    readonly p1: Vector3dEmbeddable;
    readonly p2: Vector3dEmbeddable;
}

export interface TriangleEntity {
    readonly id: number;
    readonly color: ColorEntity;
    readonly p1: Vector3dEmbeddable;
    readonly p2: Vector3dEmbeddable;
    readonly p3: Vector3dEmbeddable;
}

export interface QuadrilateralEntity {
    readonly id: number;
    readonly color: ColorEntity;
    readonly p1: Vector3dEmbeddable;
    readonly p2: Vector3dEmbeddable;
    readonly p3: Vector3dEmbeddable;
    readonly p4: Vector3dEmbeddable;
}

export interface OptionalLineEntity {
    readonly id: number;
    readonly color: ColorEntity;
    readonly p1: Vector3dEmbeddable;
    readonly p2: Vector3dEmbeddable;
    readonly p3: Vector3dEmbeddable;
    readonly p4: Vector3dEmbeddable;
}

export interface ModelEntity {
    readonly id: number;
    readonly lines: LineEntity[];
    readonly triangles: TriangleEntity[];
    readonly quadrilaterals: QuadrilateralEntity[];
    readonly optionalLines: OptionalLineEntity[];
    readonly pieces: SubFileReferenceEntity[];
}

export interface SubFileReferenceEntity {
    readonly id: number;
    readonly color: ColorEntity;
    readonly subModel: ModelEntity;
    readonly fileName: string;
    readonly connectionId: number;
    readonly matrix: MatrixEmbeddable;
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

const MatrixSchema = z.object({
    a: z.number(),
    b: z.number(),
    c: z.number(),
    x: z.number(),
    d: z.number(),
    e: z.number(),
    f: z.number(),
    y: z.number(),
    g: z.number(),
    h: z.number(),
    i: z.number(),
    z: z.number(),
    scale: z.number(),
})

const ColorEntitySchema = z.object({
    id: IdSchema,
    name: z.string(),
    rgb: z.string(),
    isTrans: z.coerce.boolean(),
    numParts: z.number(),
    numSets: z.number(),
    startYear: z.optional( z.number() ).nullable(),
    endYear: z.optional( z.number() ).nullable(),
});

const LineEntitySchema = z.object({
    id: IdSchema,
    color: ColorEntitySchema,
    p1: VertexSchema,
    p2: VertexSchema
});

const LineEntityArraySchema = z.array( LineEntitySchema );

const TriangleEntitySchema = z.object({
    id: IdSchema,
    color: ColorEntitySchema,
    p1: VertexSchema,
    p2: VertexSchema,
    p3: VertexSchema
});

const TriangleEntityArraySchema = z.array( TriangleEntitySchema );

const QuadrilateralEntitySchema = z.object({
    id: IdSchema,
    color: ColorEntitySchema,
    p1: VertexSchema,
    p2: VertexSchema,
    p3: VertexSchema,
    p4: VertexSchema
});

const QuadrilateralEntityArraySchema = z.array( QuadrilateralEntitySchema );

const OptionalLineEntitySchema = z.object({
    id: IdSchema,
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
export let ModelEntitySchema: z.ZodType<any>;
export let SubFileReferenceEntitySchema: z.ZodType<any>;

ModelEntitySchema = z.lazy(() =>
  z.object({
    id: IdSchema,
    lines: LineEntityArraySchema,
    triangles: TriangleEntityArraySchema,
    quadrilaterals: QuadrilateralEntityArraySchema,
    optionalLines: OptionalLineEntityArraySchema,
    pieces: z.array(SubFileReferenceEntitySchema),
  })
);

SubFileReferenceEntitySchema = z.lazy(() =>
  z.object({
    id: IdSchema,
    color: ColorEntitySchema,
    subModel: ModelEntitySchema,
    fileName: z.string(),
    connectionId: z.number(),
    matrix: MatrixSchema,
  })
);

export type ModelEntitySchemaType = z.infer<typeof ModelEntitySchema>;
export type SubFileReferenceEntitySchemaType = z.infer<typeof SubFileReferenceEntitySchema>;

