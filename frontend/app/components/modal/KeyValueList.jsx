const KeyValueList = ({ data, renderValue }) => (
  <div className="list-disc list-inside text-xs">
    {Object.entries(data).map(([key, value], index) => (
      <div key={index}>
        {renderValue ? renderValue(key, value) : `${value}`}
      </div>
    ))}
  </div>
);

export default KeyValueList;
