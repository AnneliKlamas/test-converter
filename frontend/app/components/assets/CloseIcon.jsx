import React from "react";

const CloseIcon = ({ className = "", style = {} }) => {
  return (
    <svg
      className={`h-6 w-6 ${className}`}
      style={{ fill: "none", stroke: "currentColor", ...style }}
      viewBox="0 0 24 24"
      xmlns="http://www.w3.org/2000/svg"
    >
      <path
        strokeLinecap="round"
        strokeLinejoin="round"
        strokeWidth="2"
        d="M6 18L18 6M6 6l12 12"
      />
    </svg>
  );
};

export default CloseIcon;
