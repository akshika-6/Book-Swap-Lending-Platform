import Link from 'next/link';

export default function Home() {
  return (
    <div className="container">
      <h1>📚 Book Swap Platform</h1>
      <nav>
        <Link href="/login">Login</Link> |{" "}
        <Link href="/signup">Signup</Link> |{" "}
        <Link href="/dashboard">Dashboard</Link>
      </nav>
    </div>
  );
}
