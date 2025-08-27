import { modeling } from "@/proto-bundle";
import { API_VERSION, ModelEntity, ModelRefId, entityToProtobuf } from "@/api/schema";

const MODEL_API = "models";

export async function fetchAllModelIds(): Promise<ModelRefId[]> {
	const response = await fetch( `http://localhost:8080/${API_VERSION}/${MODEL_API}/ids` );
	if ( !response.ok ) {
		throw new Error( 'Network response was not ok' );
	}
	return response.json();
}

export async function fetchModelById( modelId: string ): Promise<modeling.Model> {
	const response = await fetch( `http://localhost:8080/${API_VERSION}/${MODEL_API}/${modelId}` );

	if ( !response.ok ) {
		throw new Error( `Failed to fetch model: ${response.status}` );
	}

	// FIXME, This is what zod is for
	const modelEntity = await response.json() as unknown as ModelEntity;

	const model = entityToProtobuf<modeling.Model>( modelEntity.modelData, modeling.Model.decode );
	
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

	return response.json();
}