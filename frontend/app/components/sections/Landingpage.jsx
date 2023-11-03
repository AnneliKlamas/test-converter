"use client";
import React, { useState, useCallback } from "react";
import EnvironmentSwitch from "../utils/EnvironmentSwitch";
import UploadButton from "../utils/UploadButton";
import FileUpload from "../utils/FileUpload";
import { useDropzone } from "react-dropzone";

export default function Landingpage() {
  const [isMoodle, setIsMoodle] = useState(false);
  const [isCoursera, setIsCoursera] = useState(false);
  const [files, setFiles] = useState([]);

  const onDrop = useCallback((acceptedFiles) => {
    console.log("Files:", acceptedFiles);
    setFiles(acceptedFiles);
  }, []);

  const handleSwitchChange = (event) => {
    if (event.target.name === "moodle") {
      setIsMoodle(event.target.checked);
    } else {
      setIsCoursera(event.target.checked);
    }
  };

  //send files to backend
  const handleSubmit = async () => {
    console.log("Uploading files:", files);
  };

  const { getRootProps, getInputProps } = useDropzone({ onDrop });

  return (
    <main>
      <div className="flex flex-col gap-2 justify-center items-center pt-10">
        <div className="text-3xl font-bold text-blue-500">
          Select the download type/environment
        </div>
        <div className="flex gap-20">
          <EnvironmentSwitch
            isMoodle={isMoodle}
            isCoursera={isCoursera}
            onSwitchChange={handleSwitchChange}
          />
        </div>
        <FileUpload getRootProps={getRootProps} getInputProps={getInputProps} />
        <UploadButton onSubmit={handleSubmit} />
      </div>
    </main>
  );
}
