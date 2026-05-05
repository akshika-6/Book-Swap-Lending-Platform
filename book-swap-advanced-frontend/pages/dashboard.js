import Link from 'next/link';

export default function Dashboard() {
  return (
    <div className="container">
      <h2>Dashboard</h2>
      <p>Manage your books and requests</p>
      <Link href="/books">My Books</Link> <br/>
      <Link href="/requests">Requests</Link>
    </div>
  );
}
