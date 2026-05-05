import { useState } from 'react';

export default function Search() {
  const [q, setQ] = useState("");

  return (
    <div className="container">
      <h2>Search Books</h2>
      <input value={q} onChange={e=>setQ(e.target.value)} placeholder="Search..." />
      <p>Searching for: {q}</p>
    </div>
  );
}
