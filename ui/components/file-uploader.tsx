import React, { useState, ChangeEvent, ReactElement } from "react";
import { Input } from "./ui/input";
import { Button } from "./ui/button";
import { insertModel } from "@/api/model-api";

export function FileUploader(): ReactElement {
	const [ selectedFile, setSelectedFile ] = useState<File | undefined>( );

	const onFileChange = ( event: ChangeEvent<HTMLInputElement> ) => {
		if ( event.target.files && event.target.files.length > 0 ) {
			setSelectedFile( event.target.files[0] );
		}
	};

	const onFileUpload = async () => {
		if ( !selectedFile ) return;

		const formData = new FormData();
		formData.append( "myFile", selectedFile, selectedFile.name );

		const fileText = await selectedFile.text();
		insertModel( fileText );
	};

	return (
		<div className="self-center">
			<Input type="file" onChange={onFileChange}/>
			<Button onClick={onFileUpload}>
				Upload!
			</Button>
			<FileData selectedFile={selectedFile}/>
		</div>
	);
};

function FileData( { selectedFile } : { selectedFile: File | undefined } ): ReactElement
{
	return selectedFile ? (
		<div>
			<h2>File Details:</h2>
			<p>File Name: {selectedFile.name}</p>
			<p>File Type: {selectedFile.type}</p>
			<p>
					Last Modified:{" "}
				{selectedFile.lastModified
					? new Date( selectedFile.lastModified ).toDateString()
					: "Unknown"}
			</p>
		</div>
	) : (
		<div>
			<br />
			<h4>Choose before pressing the Upload button</h4>
		</div>
	);
};
