import Link from "next/link";

export default function Navbar() {
  return (
    <nav style={{ padding: "10px", background: "#eee" }}>
      <Link href="/">Home</Link> |{" "}
      <Link href="/dashboard">Dashboard</Link> |{" "}
      <Link href="/search">Search</Link> |{" "}
      <Link href="/books">My Books</Link>
    </nav>
  );
}