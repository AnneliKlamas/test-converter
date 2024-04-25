import Image from "next/image";
import tu from "@/app/components/assets/tu.png";
import data from "../../data/en.json";
import Link from "next/link";

export default function Navbar() {
  return (
    <nav className="h-20 bg-blue flex justify-between">
      <Link
        href="/"
        passHref
        className="sm:h-14 h-10 w-auto rounded-sm self-center sm:pl-10"
      >
        <Image
          src={tu}
          alt="logo"
          priority
          className="sm:h-14 h-10 w-auto rounded-sm self-center sm:pl-10"
          href="/"
        />
      </Link>
      <button className="self-center p-1 sm:px-5 border-2 sm:mr-10 rounded-md text-white">
        <Link href="/documentation" passHref>
          {data.navbar.documentation}
        </Link>
      </button>
    </nav>
  );
}
