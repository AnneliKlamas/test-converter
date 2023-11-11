import tu from "@/app/components/assets/tu.png";
import Image from "next/image";

export default function Footer() {
  return (
    <footer className="flex pt-20 justify-center">
      <Image priority src={tu} className="w-52" alt="logo" />
    </footer>
  );
}
