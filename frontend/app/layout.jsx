import React from "react";
import { Inter } from "next/font/google";
import "./globals.css";
import Footer from "./components/sections/Footer";
import Navbar from "./components/sections/Navbar";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const inter = Inter({ subsets: ["latin"] });

export const metadata = {
  title: "Quiz converter",
  description: "Quiz converter",
};

export default function RootLayout({ children }) {
  return (
    <html lang="en" className="h-full overflow-visible">
      <body
        className={`${inter.className} relative flex min-w-[360px] min-h-screen flex-col bg-white`}
      >
        <Navbar />
        {children}
        <Footer />
        <ToastContainer />
      </body>
    </html>
  );
}
