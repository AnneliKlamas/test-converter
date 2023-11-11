"use client";

import React, { useState, useCallback } from "react";
import EnvironmentSwitch from "@/app/components/utils/EnvironmentSwitch";
import UploadButton from "@/app/components/utils/UploadButton";
import FileUpload from "@/app/components/utils/FileUpload";
import handleSubmit from "@/app/components/utils/SubmissionHandler";
import { useDropzone } from "react-dropzone";

export default function Landingpage() {
  const [isMoodle, setIsMoodle] = useState(false);
  const [isCoursera, setIsCoursera] = useState(false);
  const [files, setFiles] = useState([]);

  const onDrop = useCallback((acceptedFiles) => {
    setFiles(acceptedFiles);
  }, []);

  const handleSwitchChange = (event) => {
    if (event.target.name === "moodle") {
      setIsMoodle(event.target.checked);
    } else {
      setIsCoursera(event.target.checked);
    }
  };

  const handleFormSubmit = async () => {
    try {
      const result = await handleSubmit(files, isMoodle, isCoursera);
      //...
    } catch (error) {
      //...
    }
  };

  const {
    getRootProps: rootProps,
    getInputProps: inputProps,
    acceptedFiles,
  } = useDropzone({ onDrop });

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
          rootProps={rootProps}
          inputProps={inputProps}
          acceptedFiles={acceptedFiles}
        />
        <UploadButton onSubmit={handleFormSubmit} />
      </div>
    </main>
  );
}
