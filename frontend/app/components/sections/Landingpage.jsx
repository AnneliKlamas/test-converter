"use client";

import React, { useState } from "react";
import UploadButton from "@/app/components/utils/UploadButton";
import FileUpload from "@/app/components/utils/FileUpload";
import { sendFileToBackend } from "../utils/SendFileToBackend";
import { toast } from "react-toastify";
import data from "../data/info.json";

export default function Landingpage() {
  const [file, setFiles] = useState(null);

  const handleFileUpload = (uploadedFile) => {
    setFiles(uploadedFile);
  };

  const handleSubmit = async (outputFormat) => {
    if (!file) {
      toast.error(data.landing.noFile, {
        position: "top-right",
        autoClose: 2000,
        hideProgressBar: true,
      });
      return;
    }

    toast.promise(sendFileToBackend(file, outputFormat), {
      pending: {
        render: data.landing.uploading,
        position: "top-right",
        autoClose: false,
        hideProgressBar: false,
      },
      success: {
        render: data.landing.uploaded,
        position: "top-right",
        autoClose: 2000,
        hideProgressBar: true,
      },
      error: {
        render: data.landing.uploadError,
        position: "top-right",
        autoClose: 2000,
        hideProgressBar: true,
      },
    });
  };

  return (
    <main>
      <div className="flex flex-col gap-5 justify-center items-center pt-10">
        <FileUpload onFileUpload={handleFileUpload} />
        <div className="text-3xl text-center font-bold text-blue pt-10">
          <p>{data.landing.download}</p>
        </div>
        <div className="flex sm:flex-row flex-col sm:gap-32">
          <UploadButton
            name={data.landing.moodle}
            handleSubmit={() => handleSubmit("moodle")}
          />
          <UploadButton
            name={data.landing.Coursera}
            handleSubmit={() => handleSubmit("coursera")}
          />
        </div>
      </div>
    </main>
  );
}
