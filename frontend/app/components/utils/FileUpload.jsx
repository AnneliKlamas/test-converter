import React, { useCallback, useState } from "react";
import { useDropzone } from "react-dropzone";

export default function FileUpload() {
  const [files, setFiles] = useState([]);

  const onDrop = useCallback((acceptedFiles) => {
    setFiles((prevFiles) => [...prevFiles, ...acceptedFiles]);
  }, []);

  const {
    getRootProps: rootProps,
    getInputProps: inputProps,
    acceptedFiles,
  } = useDropzone({ onDrop, multiple: true });

  const allFilesAreDocx = acceptedFiles.every((file) =>
    file.path.endsWith(".docx"),
  );

  const borderColorClass = allFilesAreDocx ? "border-blue" : "border-red-500";

  const fileList = acceptedFiles.map((file, index) => (
    <li key={`${file.path}-${index}`}>{file.path}</li>
  ));

  return (
    <div
      {...rootProps()}
      className={`sm:w-[600px] w-80 h-64 border border-dotted ${borderColorClass} border-1 rounded-lg cursor-pointer flex flex-col justify-center items-center`}
    >
      <input multiple {...inputProps()} />
      {fileList.length > 0 && allFilesAreDocx ? (
        <ul className="text-center">
          <b>{fileList}</b>
        </ul>
      ) : (
        <div className="px-2">
          {allFilesAreDocx ? (
            <p>Click to upload or drag and drop .docx file</p>
          ) : (
            <p className="text-red-500">Please upload only .docx files.</p>
          )}
        </div>
      )}
    </div>
  );
}
