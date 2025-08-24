export const API_VERSION = "v1";

export interface ColorEntity {
    readonly id: string,
    readonly name: string,
    readonly rgb: string,
    readonly isTrans: boolean,
    readonly numParts: number,
    readonly numSets: number,
    readonly startYear?: number,
    readonly endYear?: number
}