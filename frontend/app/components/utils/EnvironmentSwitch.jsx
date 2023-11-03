import React from "react";
import Switch from "@mui/material/Switch";

export default function EnvironmentSwitch({
  isMoodle,
  isCoursera,
  onSwitchChange,
}) {
  return (
    <div className="flex gap-20">
      <div>
        <div>Moodle</div>
        <Switch
          checked={isMoodle}
          onChange={onSwitchChange}
          name="moodle"
          size="large"
        />
      </div>
      <div>
        <div>Coursera</div>
        <Switch
          checked={isCoursera}
          onChange={onSwitchChange}
          name="coursera"
          size="large"
        />
      </div>
    </div>
  );
}
