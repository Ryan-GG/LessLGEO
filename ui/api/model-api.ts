import { API_VERSION, ModelEntity, ModelEntitySchema, idSchema, idSchemaArray } from "@/api/schema";

const MODEL_API = "models";

export async function fetchAllParentModelIds(): Promise<number[]> {
	const URI = `http://localhost:8080/${API_VERSION}/${MODEL_API}/ids`;
	const response = await fetch( URI );
	if ( !response.ok ) {
		throw new Error( `${URI}, Status: ${response.statusText}` );
	}

	const jsonResponse = await response.json();
	const { success, error, data } = idSchemaArray.safeParse(jsonResponse);
	
	if( data == undefined || !success )
	{
		console.error( error );
		throw new Error( error.message );
	}
	return data!;
}

export async function fetchModelById( modelId: number ): Promise<ModelEntity> {
	const URI = `http://localhost:8080/${API_VERSION}/${MODEL_API}/${modelId}`;
	const response = await fetch( URI  );

	if ( !response.ok ) {
		throw new Error( `${URI}, Status: ${response.statusText}` );
	}

	const jsonResponse = await response.json();

	const { success, error, data: modelEntity } = ModelEntitySchema.safeParse(jsonResponse);
	
	if( modelEntity == undefined || !success )
	{
		console.error( error );
		throw new Error( error.message );
	}
	
	return modelEntity as ModelEntity ?? [];
}

export async function insertModel( lDrawText: string ): Promise<number> {
	const URI = `http://localhost:8080/${API_VERSION}/${MODEL_API}/insert`;
	const response = await fetch( URI,
		{ 
			method: "POST",
			body: lDrawText
		} );

	if ( !response.ok ) {
		throw new Error( `${URI}, Status: ${response.statusText}` );
	}

	const jsonResponse = await response.json();
	const { success, error, data } = idSchema.safeParse(jsonResponse);
	
	if( data == undefined || !success )
	{
		console.error( error );
		throw new Error( error.message );
	}
	return data!;
}