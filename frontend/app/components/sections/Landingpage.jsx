"use client";

import React from "react";
import UploadButton from "@/app/components/utils/UploadButton";
import FileUpload from "@/app/components/utils/FileUpload";

export default function Landingpage() {
  return (
    <main>
      <div className="flex flex-col gap-5 justify-center items-center pt-10">
        <FileUpload />
        <div className="text-3xl text-center font-bold text-blue pt-10">
          <p>Download as</p>
        </div>
        <div className="flex sm:flex-row flex-col sm:gap-32">
          <UploadButton name="Moodle XML" />
          <UploadButton name="Coursera .docx" />
        </div>
      </div>
    </main>
  );
}
