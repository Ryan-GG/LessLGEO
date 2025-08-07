import React, { useState, ChangeEvent, ReactElement, Suspense } from "react";

export function FileUploader(): ReactElement {
	const [selectedFile, setSelectedFile] = useState<File | null>(null);

	const onFileChange = (event: ChangeEvent<HTMLInputElement>) => {
		if (event.target.files && event.target.files.length > 0) {
			setSelectedFile(event.target.files[0]);
		}
	};

	const onFileUpload = async () => {
		if (!selectedFile) return;

		const formData = new FormData();
		formData.append("myFile", selectedFile, selectedFile.name);

		console.log(selectedFile);

        console.log( await selectedFile.text() );

		try {
			// await axios.post("api/uploadfile", formData);
			console.log("File uploaded successfully");
		} catch (error) {
			console.error("Error uploading file", error);
		}
	};

	const fileData = () => {
		if (selectedFile) {
			return (
				<div>
					<h2>File Details:</h2>
					<p>File Name: {selectedFile.name}</p>
					<p>File Type: {selectedFile.type}</p>
					<p>
						Last Modified:{" "}
						{selectedFile.lastModified
							? new Date(selectedFile.lastModified).toDateString()
							: "Unknown"}
					</p>
				</div>
			);
		} else {
			return (
				<div>
					<br />
					<h4>Choose before pressing the Upload button</h4>
				</div>
			);
		}
	};

	return (
		<div>
			<h1>GeeksforGeeks</h1>
			<h3>File Upload using React and TypeScript!</h3>
			<div>
				<input type="file" onChange={onFileChange} />
				<button onClick={onFileUpload}>Upload!</button>
			</div>
			{fileData()}
		</div>
	);
};
