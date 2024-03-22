export default function Footer() {
  const year = new Date().getFullYear();

  return (
    <footer className="flex pt-20 justify-center">
      <p className="text-sm">{year}</p>
    </footer>
  );
}
