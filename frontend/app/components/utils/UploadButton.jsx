import React from "react";
import handleSubmit from "@/app/components/utils/SubmissionHandler";

export default function UploadButton({ name }) {
  return (
    <button
      onClick={handleSubmit}
      className="mt-4 px-7 p-2 text-blue rounded border-2 border-blue hover:bg-blue hover:text-white transition duration-300 ease-in-out"
    >
      {name}
    </button>
  );
}
