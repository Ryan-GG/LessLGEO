import { modeling } from "@/proto-bundle";
import { API_VERSION, ModelEntity, ModelEntitySchema, ModelRefId, UUIDArraySchema, UUIDSchema, entityToProtobuf } from "@/api/schema";

const MODEL_API = "models";

export async function fetchAllModelIds(): Promise<ModelRefId[]> {
	const response = await fetch( `http://localhost:8080/${API_VERSION}/${MODEL_API}/ids` );
	if ( !response.ok ) {
		throw new Error( 'Network response was not ok' );
	}

	const jsonResponse = await response.json();
	const { success, error, data } = UUIDArraySchema.safeParse(jsonResponse);
	
	if( data == undefined || !success )
	{
		console.log( error );
	}
	return data!;
}

export async function fetchModelById( modelId: string ): Promise<modeling.Model> {
	const response = await fetch( `http://localhost:8080/${API_VERSION}/${MODEL_API}/${modelId}` );

	if ( !response.ok ) {
		throw new Error( `Failed to fetch model: ${response.status}` );
	}

	const jsonResponse = await response.json();
	const { success, error, data: modelEntity } = ModelEntitySchema.safeParse(jsonResponse);
	
	if( modelEntity == undefined || !success )
	{
		console.log( error );
	}
	
	const model = entityToProtobuf<modeling.Model>( modelEntity?.modelData!, modeling.Model.decode );
	
	return model == undefined ? modeling.Model.create() : model;
}

export async function insertModel( lDrawText: string ): Promise<ModelRefId> {
	const response = await fetch( `http://localhost:8080/${API_VERSION}/${MODEL_API}/insert`,
		{ 
			method: "POST",
			body: lDrawText
		} );

	if ( !response.ok ) {
		throw new Error( 'Network response was not ok' );
	}

	const jsonResponse = await response.json();
	const { success, error, data } = UUIDSchema.safeParse(jsonResponse);
	
	if( data == undefined || !success )
	{
		console.log( error );
	}
	return data!;
}