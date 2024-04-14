import Link from "next/link";
import data from "../components/data/info.json";

export default function Documentation() {
  return (
    <main className="flex flex-col items-center justify-center pt-10">
      <h1 className="text-4xl font-bold text-blue">
        {data.documentation.title}
      </h1>
      <div className="flex flex-row gap-20 pt-16">
        <div className="flex gap-3 flex-col items-center justify-center">
          <h2 className="text-lg font-semibold text-blue">
            {data.documentation.howTo}
          </h2>
          <button className="mt-4 px-7 text-base p-2 text-blue rounded border-2 border-blue hover:bg-blue hover:text-white transition duration-300 ease-in-out">
            {data.documentation.doc}
          </button>
        </div>
        <div className="flex gap-3 flex-col items-center justify-center">
          <h2 className="text-lg font-semibold text-blue">
            {data.documentation.template}
          </h2>
          <button className="mt-4 px-7 text-base p-2 text-blue rounded border-2 border-blue hover:bg-blue hover:text-white transition duration-300 ease-in-out">
            <Link
              legacyBehavior
              passHref
              href="https://docs.google.com/document/d/106xTDS6IG7fofL6Ju4JnqntjxLeBfBM_LIyjKtskFb8/edit"
            >
              <a target="_blank"> {data.documentation.templateName}</a>
            </Link>
          </button>
        </div>
      </div>
    </main>
  );
}
