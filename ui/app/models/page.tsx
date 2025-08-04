"use client";
import { AppNavBar } from "@/components/home/app-nav-bar";
import { ModelCanvas } from "@/components/models/model-canvas";
import { Skeleton } from "@/components/ui/skeleton";
import { useEffect, useState } from "react";

/**
 * Models Landing Page, where to view and add new Models
 */
export default function Models() {

	const [ data, setData ] = useState<Array<string>>( [ "foop" ] );  

	useEffect( () => {
	  fetch( 'http://localhost:8080/api/model/v1/ids' )
			.then( ( response ) => response.json() )
			.then( ( data ) => {
		  setData( data );
			} );
	}, [] );

	return (
		<div className="flex flex-col min-h-screen bg-black">
			<header className="px-8">
				<AppNavBar/>
			</header>
			<main className="flex-1 pb-8 px-8 pt-2">
				<div className="h-full w-full p-8 flex flex-col gap-4 bg-gray-200">
					<div className="flex gap-4">
						<div className="h-[10vh] w-1/3">
							{data.map( id => {
								return ( 
									<p key={`id-${id}`}>
										{`ID: ${id}`}
									</p> 
								);
							} )}
						</div>
					</div>
					<div className="flex gap-4">
						<div className="h-[70vh] w-2/3 border-2 border-gray-500 rounded-lg p-6" id="canvas-container">
							<ModelCanvas/>
						</div>
						<Skeleton className="h-[70vh] w-1/3 bg-black"/>
					</div>
				</div>
			</main>
			<footer className="px-8">
				<Skeleton className="h-8 w-full"/>
			</footer>
		</div>
	);
}
