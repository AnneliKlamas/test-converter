"use client";

import React, { useState } from "react";
import Button from "@/app/components/utils/Button";
import FileUpload from "@/app/components/utils/FileUpload";
import { sendFileToBackend } from "../utils/SendFileToBackend";
import { toast } from "react-toastify";
import data from "../../data/en.json";

export default function Landingpage() {
  const [file, setFiles] = useState(null);

  const handleFileUpload = (uploadedFile) => {
    setFiles(uploadedFile);
  };

  const handleSubmit = async (outputFormat) => {
    if (!file) {
      toast.error(data.landing.noFile, {
        position: "bottom-right",
        autoClose: 2000,
        hideProgressBar: true,
      });
      return;
    }

    toast.promise(sendFileToBackend(file, outputFormat), {
      pending: {
        render: data.landing.uploading,
        position: "bottom-right",
        autoClose: false,
        hideProgressBar: false,
      },
      success: {
        render: data.landing.uploaded,
        position: "bottom-right",
        autoClose: 2000,
        hideProgressBar: true,
      },
      error: {
        render: data.landing.uploadError,
        position: "bottom-right",
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
          <Button
            name={data.landing.moodle}
            handleSubmit={() => handleSubmit("moodle")}
          />
          <Button
            name={data.landing.Coursera}
            handleSubmit={() => handleSubmit("coursera")}
          />
        </div>
      </div>
    </main>
  );
}
