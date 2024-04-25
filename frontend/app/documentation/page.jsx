import data from "../data/en.json";
import Button from "@/app/components/utils/Button";

export default function Documentation() {
  return (
    <main className="flex flex-col items-center justify-center pt-10">
      <h1 className="text-4xl font-bold text-blue">
        {data?.documentation?.title}
      </h1>
      <div className="flex flex-row gap-20 pt-16">
        <div className="flex gap-3 flex-col items-center justify-center">
          <h2 className="text-lg font-semibold text-blue">
            {data?.documentation?.howTo}
          </h2>
          <Button
            name={data?.documentation?.doc}
            href={data?.documentation?.docLink}
          />
        </div>
        <div className="flex gap-3 flex-col items-center justify-center">
          <h2 className="text-lg font-semibold text-blue">
            {data?.documentation?.template}
          </h2>
          <Button
            name={data?.documentation?.templateName}
            href={data?.documentation?.templateLink}
          />
        </div>
      </div>
    </main>
  );
}
