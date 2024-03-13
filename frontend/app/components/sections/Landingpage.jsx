"use client";

import React, { useState } from "react";
import UploadButton from "@/app/components/utils/UploadButton";
import FileUpload from "@/app/components/utils/FileUpload";
import { sendFilesToBackend } from "../utils/SendFileToBackend";

export default function Landingpage() {
  const [files, setFiles] = useState(null);

  const handleFileUpload = (uploadedFiles) => {
    setFiles(uploadedFiles);
  };

  const handleSubmit = async (outputFormat) => {
    if (!files) {
      console.error("No files uploaded");
      return;
    }

    try {
      const result = await sendFilesToBackend(files, outputFormat);
      console.log(result);
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <main>
      <div className="flex flex-col gap-5 justify-center items-center pt-10">
        <FileUpload onFileUpload={handleFileUpload} />
        <div className="text-3xl text-center font-bold text-blue pt-10">
          <p>Download as</p>
        </div>
        <div className="flex sm:flex-row flex-col sm:gap-32">
          <UploadButton
            name="Moodle XML"
            handleSubmit={() => handleSubmit("moodle")}
          />
          <UploadButton
            name="Coursera .docx"
            handleSubmit={() => handleSubmit("coursera")}
          />
        </div>
      </div>
    </main>
  );
}
