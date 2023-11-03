import React from "react";

export default function FileUpload({ getRootProps, getInputProps }) {
  return (
    <div
      {...getRootProps()}
      className="bg-gray-300 w-96 h-52 rounded-lg cursor-pointer"
    >
      <input {...getInputProps()} />
      <p className="rounded-lg pt-20 text-center">
        Drag and drop or click to upload
      </p>
      <p className="rounded-lg pt-20 text-center">.docx files</p>
    </div>
  );
}
