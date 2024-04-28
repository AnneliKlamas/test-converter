import Image from "next/image";
import tu from "@/app/components/assets/tu.png";
import data from "../../data/en.json";
import Link from "next/link";
import Button from "../utils/Button";

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
      <Button
        name={data?.documentation?.doc}
        href="/documentation"
        className="self-center border-2 border-white sm:mr-10 rounded-md text-white"
      />
    </nav>
  );
}
