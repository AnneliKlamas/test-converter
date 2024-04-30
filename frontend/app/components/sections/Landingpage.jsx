"use client";

import React, { useState } from "react";
import Button from "@/app/components/utils/Button";
import FileUpload from "@/app/components/utils/FileUpload";
import InfoModal from "@/app/components/modal/InfoModal";
import { sendFileToBackend } from "../utils/SendFileToBackend";
import { toast } from "react-toastify";
import data from "../../data/en.json";

export default function Landingpage() {
  const [file, setFiles] = useState(null);
  const [details, setDetails] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [showDetailsButtons, setShowDetailsButtons] = useState(false);

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

    try {
      const { details } = await toast.promise(
        sendFileToBackend(file, outputFormat),
        {
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
        },
      );
      setShowDetailsButtons(true);
      setDetails(details);
      setShowModal(true);
    } catch (error) {
      throw error;
    }
  };

  const closeModal = () => {
    setShowModal(false);
  };

  const openModal = () => {
    setShowModal(true);
  };

  return (
    <main>
      <div className="flex flex-col gap-3 justify-center items-center pt-10">
        <FileUpload onFileUpload={handleFileUpload} />
        {showDetailsButtons && (
          <Button
              onClick={openModal}
              variant="primary"
              name={data.landing.details}/>
        )}
        <div className="text-3xl text-center font-bold text-blue pt-5">
          {data.landing.download}
        </div>
        <div className="flex sm:flex-row flex-col sm:gap-32">
          <Button
            name={data.landing.moodle}
            onClick={() => handleSubmit("moodleXML")}
          />
          <Button
            name={data.landing.Coursera}
            onClick={() => handleSubmit("courseraDocx")}
          />
        </div>
      </div>
      <InfoModal show={showModal} onClose={closeModal} details={details} />
    </main>
  );
}
