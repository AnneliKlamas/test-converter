import React from "react";

const Button = ({
  children,
  onClick,
  variant = "primary",
  className = "",
  ...rest
}) => {
  const baseClasses = "rounded-md p-2 text-sm";
  const variantClasses = {
    primary: "bg-blue text-white",
    secondary:
      "mt-4 px-7 text-base p-2 text-blue rounded border-2 border-blue hover:bg-blue hover:text-white transition duration-300 ease-in-out",
  };

  return (
    <button
      onClick={onClick}
      className={`${baseClasses} ${variantClasses[variant]} ${className}`}
      {...rest}
    >
      {children}
    </button>
  );
};

export default Button;
