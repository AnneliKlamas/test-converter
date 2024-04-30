import React, { useEffect } from "react";

const Modal = ({ show, onClose, children }) => {
  useEffect(() => {
    const handleEscapeKey = (event) => {
      if (event.key === "Escape") {
        onClose();
      }
    };

    if (show) {
      document.addEventListener("keydown", handleEscapeKey);
    }

    return () => {
      document.removeEventListener("keydown", handleEscapeKey);
    };
  }, [show, onClose]);

  if (!show) {
    return null;
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      <div className="bg-black bg-opacity-50 fixed inset-0" onClick={onClose} />
      <div className="bg-white rounded-lg shadow-md p-6 max-h-[550px] overflow-y-auto relative z-10">
        {children}
      </div>
    </div>
  );
};

export default Modal;
