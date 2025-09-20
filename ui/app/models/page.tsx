"use client";
import { fetchAllParentModelIds } from "@/api/model-api";
import { FileUploader } from "@/components/file-uploader";
import { AppNavBar } from "@/components/home/app-nav-bar";
import { ModelCanvas } from "@/components/models/model-canvas";
import { Button } from "@/components/ui/button";
import { Skeleton } from "@/components/ui/skeleton";
import { DropdownMenu, DropdownMenuTrigger, DropdownMenuContent, DropdownMenuItem } from "@/components/ui/dropdown-menu";
import { useQuery } from "@tanstack/react-query";
import { ReactElement, useState } from "react";

/**
 * Models Landing Page, where to view and add new Models
 */
export default function Models() {

	const [ modelId, setModelId ] = useState<number | undefined>(  );
	const { data: modelIds } = useQuery( { queryKey: [ "modelIds" ], queryFn: fetchAllParentModelIds } );
	
	return (
		<div className="flex flex-col min-h-screen bg-black">
			<header className="px-8">
				<AppNavBar/>
			</header>
			<main className="flex-1 pb-8 px-8 pt-2">
				<div className="h-full w-full p-8 flex flex-col gap-4 bg-gray-200">
					<div className="flex gap-4">
						<div className="h-[10vh] w-1/3">
							<ModelIdDropDown modelIds={modelIds ?? []} setModelId={setModelId}/>
							<p>{`Current id: ${modelId}`}</p>
						</div>
					</div>
					<div className="flex gap-4">
						<div className="h-[70vh] w-2/3 border-2 border-gray-500 rounded-lg p-6" id="canvas-container">
							<ModelCanvas modelId={modelId}/>
						</div>
						<div className="h-[70vh] w-1/3 border-2 border-gray-500 rounded-lg p-6" id="file-uploader">
							<FileUploader/>
						</div>
					</div>
				</div>
			</main>
			<footer className="px-8">
				<Skeleton className="h-8 w-full"/>
			</footer>
		</div>
	);
}

function ModelIdDropDown( { modelIds, setModelId }: { modelIds: number[], setModelId: ( id: number ) => void } ): ReactElement
{
	return (
		  <DropdownMenu>
			<DropdownMenuTrigger asChild>
			  <Button variant="outline">Model Ids</Button>
			</DropdownMenuTrigger>
			<DropdownMenuContent className="w-56 z-[1]" align="start">
				{modelIds?.map( id => {
					return ( 
						<DropdownMenuItem key={`id-${id}`} onClick={() => setModelId( id )}>
							{`ID: ${id}`}
						</DropdownMenuItem>			
					);
				} )}
			</DropdownMenuContent>
		  </DropdownMenu>
	);
}
