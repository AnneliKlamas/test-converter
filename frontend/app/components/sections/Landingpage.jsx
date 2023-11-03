"use client";

import React, { useState, useCallback } from "react";
import EnvironmentSwitch from "@/app/components/utils/EnvironmentSwitch";
import UploadButton from "@/app/components/utils/UploadButton";
import FileUpload from "@/app/components/utils/FileUpload";
import sendFilesToBackend from "@/app/components/utils/SendFileToBackend";
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

  const handleSubmit = async () => {
    try {
      const result = await sendFilesToBackend(files, isMoodle, isCoursera);
      console.log("Files uploaded successfully::", result);
    } catch (error) {
      console.error("Failed to upload files:", error);
    }
  };

  const { getRootProps, getInputProps, acceptedFiles } = useDropzone({
    onDrop,
  });

  return (
    <main>
      <div className="flex flex-col gap-10 justify-center items-center pt-10">
        <div className="text-3xl font-bold text-blue">
          Select the download type/environment
        </div>
        <div className="flex gap-20">
          <EnvironmentSwitch
            isMoodle={isMoodle}
            isCoursera={isCoursera}
            onSwitchChange={handleSwitchChange}
          />
        </div>
        <FileUpload
          getRootProps={getRootProps}
          getInputProps={getInputProps}
          acceptedFiles={acceptedFiles}
        />
        <UploadButton onSubmit={handleSubmit} />
      </div>
    </main>
  );
}
