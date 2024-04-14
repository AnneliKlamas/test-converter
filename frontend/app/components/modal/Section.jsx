import React from "react";

const Section = ({ title, children }) => (
  <div className="pb-4">
    <span className="font-bold">{title}</span>
    {children}
  </div>
);

export default Section;
