import { API_VERSION } from "./versionApi";

export interface Color {
    readonly id: string,
    readonly name: string,
    readonly rgb: string,
    readonly isTrans: boolean,
    readonly numParts: number,
    readonly numSets: number,
    readonly startYear?: number,
    readonly endYear?: number
}


const COLOR_API = "colors";

export async function fetchColorById(colorId: number): Promise<Color> {
    const response = await fetch(`http://localhost:8080/${API_VERSION}/${COLOR_API}/${colorId}`);
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }

    // FIXME, This is what zod is for
    return response.json() as unknown as Color;
}