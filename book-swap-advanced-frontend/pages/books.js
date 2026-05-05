import BookCard from "../components/BookCard";
import Navbar from "../components/Navbar";

export default function Books() {
  const books = [
    { title: "Atomic Habits", author: "James Clear", status: "Available" }
  ];

  return (
    <div>
      <Navbar />
      <h2>My Books</h2>
      {books.map((b, i) => (
        <BookCard key={i} book={b} />
      ))}
    </div>
  );
}