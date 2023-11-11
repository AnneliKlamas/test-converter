import React from "react";

export default function FileUpload({
  rootProps,
  inputProps,
  acceptedFiles,
}) {
  const fileList = acceptedFiles.map((file) => (
    <li key={file.path}>{file.path}</li>
  ));

  return (
    <div
      {...rootProps()}
      className="bg-gray-300 w-1/2 h-64 rounded-lg cursor-pointer flex flex-col justify-center items-center"
    >
      <input {...inputProps()} />
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
