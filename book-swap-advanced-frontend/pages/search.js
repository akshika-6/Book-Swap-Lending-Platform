import Navbar from "../components/Navbar";

export default function Search() {
  return (
    <div>
      <Navbar />

      <div className="p-6">
        <h1 className="text-2xl font-bold mb-4">Search Books</h1>

        <input
          type="text"
          placeholder="Search by title, author..."
          className="w-full p-3 border rounded-lg mb-6"
        />

        <div className="text-gray-500">Results will appear here...</div>
      </div>
    </div>
  );
}