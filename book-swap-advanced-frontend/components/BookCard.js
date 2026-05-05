export default function BookCard({ book }) {
  return (
    <div style={{ border: "1px solid #ddd", padding: "10px", margin: "10px" }}>
      <h3>{book.title}</h3>
      <p>Author: {book.author}</p>
      <p>Status: {book.status}</p>
      <button>Request</button>
    </div>
  );
}