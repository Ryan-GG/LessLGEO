import { API_VERSION, ColorEntity } from "@/api/schema";

const COLOR_API = "colors";

export async function fetchColorById( colorId: number ): Promise<ColorEntity> {
	const response = await fetch( `http://localhost:8080/${API_VERSION}/${COLOR_API}/${colorId}` );
	if ( !response.ok ) {
		throw new Error( 'Network response was not ok' );
	}

	// FIXME, This is what zod is for
	return response.json() as unknown as ColorEntity;
}

export async function fetchAllColors(): Promise<ColorEntity[]> {
	const response = await fetch( `http://localhost:8080/${API_VERSION}/${COLOR_API}/` );
	if ( !response.ok ) {
		throw new Error( 'Network response was not ok' );
	}

	// FIXME, This is what zod is for
	return response.json() as unknown as ColorEntity[];
}