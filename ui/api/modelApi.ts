import { modeling } from "@/proto-bundle";

const MODEL_API = "models";

export async function fetchAllModelIds(): Promise<string[]> {
    const response = await fetch(`http://localhost:8080/${API_VERSION}/${MODEL_API}/ids`);
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    return response.json();
}

export async function fetchModelById(modelId: string ): Promise<modeling.Model> {
    const response = await fetch(`http://localhost:8080/${API_VERSION}/${MODEL_API}/${modelId}`);

    if (!response.ok) {
        throw new Error(`Failed to fetch model: ${response.status}`);
    }

    const buffer = await response.arrayBuffer();
    const uint8Array = new Uint8Array(buffer);
    try {
    const decodedModel = modeling.Model.decode(uint8Array);
    return decodedModel;
    }
    catch( err: unknown)
    {
        console.log( err );
    }
    return modeling.Model.create();
}