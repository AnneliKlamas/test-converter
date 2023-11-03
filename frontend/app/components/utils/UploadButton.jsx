import React from "react";

export default function UploadButton({ onSubmit }) {
  return (
    <button
      onClick={onSubmit}
      className="mt-4 w-24 p-2 bg-blue rounded text-white"
    >
      Upload
    </button>
  );
}
