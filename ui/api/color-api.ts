import { API_VERSION, ColorEntity, ColorEntityArraySchema, ColorEntitySchema } from "@/api/schema";

const COLOR_API = "colors";

export async function fetchColorById( colorId: number ): Promise<ColorEntity> {
	const URI = `http://localhost:8080/${API_VERSION}/${COLOR_API}/${colorId}`;
	const response = await fetch( URI );
	if ( !response.ok ) {
		throw new Error( `${URI}, Status: ${response.statusText}` );
	}

	const jsonResponse = await response.json();
	const { success, error, data } = ColorEntitySchema.safeParse(jsonResponse);
	
	if( data == undefined || !success )
	{
		console.error( error );
		throw new Error( error.message );
	}
	return data!;
}

export async function fetchAllColors(): Promise<ColorEntity[]> {
	const URI = `http://localhost:8080/${API_VERSION}/${COLOR_API}/`;
	const response = await fetch( URI );

	if ( !response.ok ) {
		throw new Error( `${URI}, Status: ${response.statusText}` );
	}

	const jsonResponse = await response.json();
	const { success, error, data } = ColorEntityArraySchema.safeParse(jsonResponse);
	
	if( data == undefined || !success )
	{
		console.error( error );
		throw new Error( error.message );
	}
	return data ?? [];

}