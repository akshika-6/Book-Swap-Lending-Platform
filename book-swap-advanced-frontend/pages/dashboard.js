import Navbar from "../components/Navbar";

export default function Dashboard() {
  return (
    <div>
      <Navbar />

      <div className="p-6">
        <h1 className="text-2xl font-bold mb-6">Dashboard</h1>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          
          <div className="bg-blue-100 p-6 rounded-xl">
            <h2 className="text-lg font-semibold">Total Books</h2>
            <p className="text-2xl font-bold">12</p>
          </div>

          <div className="bg-green-100 p-6 rounded-xl">
            <h2 className="text-lg font-semibold">Borrowed</h2>
            <p className="text-2xl font-bold">3</p>
          </div>

          <div className="bg-yellow-100 p-6 rounded-xl">
            <h2 className="text-lg font-semibold">Pending Requests</h2>
            <p className="text-2xl font-bold">5</p>
          </div>

        </div>
      </div>
    </div>
  );
}