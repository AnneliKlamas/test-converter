"use client";

import React, { useState } from "react";
import UploadButton from "@/app/components/utils/UploadButton";
import FileUpload from "@/app/components/utils/FileUpload";
import { sendFileToBackend } from "../utils/SendFileToBackend";
import { toast } from "react-toastify";
import data from "../data/info.json";

export default function Landingpage() {
  const [file, setFiles] = useState(null);
  const [details, setDetails] = useState([]);

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

      setDetails(details);
    } catch (error) {
      throw error;
    }
  };

  return (
    <main>
      <div className="flex flex-col gap-5 justify-center items-center pt-10">
        <FileUpload onFileUpload={handleFileUpload} />
        <div className="mt-8 bg-white rounded-lg shadow-md p-8">
          <h2 className="text-2xl font-bold text-gray-800 mb-6">
            Document Details
          </h2>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
            <div>
              <p className="mb-4">
                <span className="font-bold text-gray-700">Question Count:</span>{" "}
                <span className="text-gray-600">
                  {details?.questionCount || "N/A"}
                </span>
              </p>
              <p className="mb-4">
                <span className="font-bold text-gray-700">
                  Answer Picture Count:
                </span>{" "}
                <span className="text-gray-600">
                  {details?.answerPictureCount || "N/A"}
                </span>
              </p>
              <p className="mb-4">
                <span className="font-bold text-gray-700">Answers Count:</span>{" "}
                <span className="text-gray-600">
                  {details?.answersCount || "N/A"}
                </span>
              </p>
              <p className="mb-4">
                <span className="font-bold text-gray-700">
                  Question Pictures Count:
                </span>{" "}
                <span className="text-gray-600">
                  {details?.questionPicturesCount || "N/A"}
                </span>
              </p>
            </div>
            <div>
              <div className="mb-4">
                <span className="font-bold text-gray-700">
                  Question Config Details:
                </span>
                <pre className="bg-gray-100 p-2 rounded text-gray-600 overflow-auto">
                  {JSON.stringify(details?.questionConfigDetails, null, 2) ||
                    "N/A"}
                </pre>
              </div>
              <div className="mb-4">
                <span className="font-bold text-gray-700">
                  Question Errors:
                </span>
                {details?.questionErrors &&
                details?.questionErrors.length > 0 ? (
                  <ul className="list-disc list-inside text-gray-600">
                    {details?.questionErrors.map((error, index) => (
                      <li key={index}>{error}</li>
                    ))}
                  </ul>
                ) : (
                  <span className="text-green-600">None</span>
                )}
              </div>
              <div className="mb-4">
                <span className="font-bold text-gray-700">
                  Question Warnings:
                </span>
                {details?.questionWarnings &&
                details?.questionWarnings.length > 0 ? (
                  <ul className="list-disc list-inside text-gray-600">
                    {details?.questionWarnings.map((warning, index) => (
                      <li key={index}>{warning}</li>
                    ))}
                  </ul>
                ) : (
                  <span className="text-green-600">None</span>
                )}
              </div>
              <div className="mb-4">
                <span className="font-bold text-gray-700">
                  Skipped Questions:
                </span>
                {details?.skippedQuestions &&
                details?.skippedQuestions.length > 0 ? (
                  <ul className="list-disc list-inside text-gray-600">
                    {details?.skippedQuestions.map((question, index) => (
                      <li key={index}>{question}</li>
                    ))}
                  </ul>
                ) : (
                  <span className="text-green-600">None</span>
                )}
              </div>
            </div>
          </div>
        </div>
        <div className="text-3xl text-center font-bold text-blue pt-10">
          <p>{data.landing.download}</p>
        </div>
        <div className="flex sm:flex-row flex-col sm:gap-32">
          <UploadButton
            name={data.landing.moodle}
            handleSubmit={() => handleSubmit("moodleXML")}
          />
          <UploadButton
            name={data.landing.Coursera}
            handleSubmit={() => handleSubmit("courseraDocx")}
          />
        </div>
      </div>
    </main>
  );
}
