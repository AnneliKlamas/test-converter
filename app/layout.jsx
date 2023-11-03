import { Inter } from "next/font/google";
import "./globals.css";
import Footer from "./components/sections/Footer";
import Navbar from "./components/sections/Navbar";

const inter = Inter({ subsets: ["latin"] });

export const metadata = {
  title: "Create Next App",
  description: "Generated by create next app",
};

export default function RootLayout({ children }) {
  return (
    <html lang="en" className="h-full overflow-visible">
      <body className={`${inter.className} relative flex min-w-[360px] flex-col bg-white`}>
        <Navbar />
        {children}
        <Footer />
      </body>
    </html>
  );
}
