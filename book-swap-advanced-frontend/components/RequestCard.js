export default function RequestCard({ request }) {
  return (
    <div style={{ border: "1px solid #aaa", padding: "10px", margin: "10px" }}>
      <p>{request.user} requested {request.book}</p>
      <button>Approve</button>
      <button>Reject</button>
    </div>
  );
}