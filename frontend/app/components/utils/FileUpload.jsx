import React from "react";

export default function FileUpload({
  getRootProps,
  getInputProps,
  acceptedFiles,
}) {
  const fileList = acceptedFiles.map((file) => (
    <li key={file.path}>{file.path}</li>
  ));

  return (
    <div
      {...getRootProps()}
      className="bg-gray-300 w-1/2 h-64 rounded-lg cursor-pointer flex flex-col justify-center items-center"
    >
      <input {...getInputProps()} />
      {fileList.length > 0 ? (
        <ul className="text-center">
          <b>{fileList}</b>
        </ul>
      ) : (
        <>
          <p>Drag and drop or click to upload files</p>
          <p>Only .docx files will be accepted</p>
        </>
      )}
    </div>
  );
}
