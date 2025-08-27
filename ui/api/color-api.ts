import { API_VERSION, ColorEntity, ColorEntityArraySchema, ColorEntitySchema } from "@/api/schema";

const COLOR_API = "colors";

export async function fetchColorById( colorId: number ): Promise<ColorEntity> {
	const response = await fetch( `http://localhost:8080/${API_VERSION}/${COLOR_API}/${colorId}` );
	if ( !response.ok ) {
		throw new Error( 'Network response was not ok' );
	}

	const jsonResponse = await response.json();
	const { success, error, data } = ColorEntitySchema.safeParse(jsonResponse);
	
	if( data == undefined || !success )
	{
		console.log( error );
	}
	return data!;
}

export async function fetchAllColors(): Promise<ColorEntity[]> {
	const response = await fetch( `http://localhost:8080/${API_VERSION}/${COLOR_API}/` );
	if ( !response.ok ) {
		throw new Error( 'Network response was not ok' );
	}

	const jsonResponse = await response.json();
	const { success, error, data } = ColorEntityArraySchema.safeParse(jsonResponse);
	
	if( data == undefined || !success )
	{
		console.log( error );
	}
	return data ?? [];

}