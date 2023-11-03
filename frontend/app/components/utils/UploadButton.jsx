import React from "react";

export default function UploadButton({ onSubmit }) {
  return (
    <button
      onClick={onSubmit}
      className="mt-4 p-2 bg-blue-500 rounded text-white"
    >
      Upload
    </button>
  );
}
