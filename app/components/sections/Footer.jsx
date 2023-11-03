import tü from "../assets/tü.png";
import Image from "next/image";

export default function Footer() {
  return (
    <footer className="flex pt-20 justify-center">
      <Image src={tü} className="w-52" alt="logo" />
    </footer>
  );
}
