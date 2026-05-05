import { useState } from "react";
import BookList from "./components/BookList";
import AddBook from "./components/AddBook";

function App() {
  const [books, setBooks] = useState([]);

  const addBook = (book) => {
    setBooks([...books, book]);
  };

  return (
    <div style={{ padding: "20px" }}>
      <h1>📚 Book Swap Platform</h1>
      <AddBook addBook={addBook} />
      <BookList books={books} />
    </div>
  );
}

export default App;