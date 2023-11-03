import tü from "@/app/components/assets/tü.png";
import Image from "next/image";

export default function Footer() {
  return (
    <footer className="flex pt-20 justify-center">
      <Image priority src={tü} className="w-52" alt="logo" />
    </footer>
  );
}
