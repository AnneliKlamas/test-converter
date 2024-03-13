import React, { useCallback } from "react";
import { useDropzone } from "react-dropzone";

const FileUploadMessage = ({ isFileAccepted }) => {
  if (isFileAccepted === null) {
    return <p>Click to upload or drag and drop .docx file</p>;
  }

  if (isFileAccepted) {
    return <p className="text-green-500">File uploaded successfully</p>;
  }

  return <p className="text-red-500">Please upload only .docx files.</p>;
};

export default function FileUpload({ onFileUpload }) {
  const onDrop = useCallback(
    (acceptedFiles) => {
      onFileUpload(acceptedFiles);
    },
    [onFileUpload],
  );

  const { getRootProps, getInputProps, isDragActive, acceptedFiles } =
    useDropzone({
      onDrop,
      multiple: false,
      accept: {
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
          [".docx"],
      },
    });

  const isFileAccepted =
    acceptedFiles.length > 0 && acceptedFiles[0].path.endsWith(".docx")
      ? true
      : acceptedFiles.length > 0
      ? false
      : null;

  const borderColorClass =
    isFileAccepted === null
      ? "border-blue"
      : isFileAccepted
      ? "border-blue"
      : "border-red-500";

  return (
    <div
      {...getRootProps()}
      className={`sm:w-[600px] w-80 h-64 border border-dotted ${borderColorClass} border-1 rounded-lg cursor-pointer flex flex-col justify-center items-center`}
    >
      <input {...getInputProps()} />
      {isDragActive ? (
        <p>Drop the file here ...</p>
      ) : (
        <>
          {acceptedFiles.length > 0 && (
            <ul className="text-center font-bold">{acceptedFiles[0].path}</ul>
          )}
          <FileUploadMessage isFileAccepted={isFileAccepted} />
        </>
      )}
    </div>
  );
}
