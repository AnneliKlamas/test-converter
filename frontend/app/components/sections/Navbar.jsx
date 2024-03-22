import Image from "next/image";
import tu from "@/app/components/assets/tu.png";
import data from "../data/info.json";

export default function Navbar() {
  return (
    <nav className="h-20 bg-blue flex justify-between">
      <Image
        src={tu}
        alt="logo"
        priority
        className="sm:h-14 h-10 w-auto rounded-sm self-center sm:pl-10"
      />
      <button className="self-center p-1 sm:px-5 border-2 sm:mr-10 rounded-md text-white">
        {data.navbar.documentation}
      </button>
    </nav>
  );
}
