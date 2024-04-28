import React from "react";
import Link from "next/link";

export default function Button({ name, href, handleSubmit, className }) {
  const safeHref = href || "#";

  return (
    <Link href={safeHref} passHref legacyBehavior>
      <a
        className={`px-7 p-2 text-blue rounded border-2 border-blue hover:bg-blue hover:text-white transition duration-300 ease-in-out ${className}`}
        onClick={handleSubmit}
      >
        {name}
      </a>
    </Link>
  );
}
