import { useState, useEffect, useCallback } from "react";

const BASE_URL = "http://localhost:8080";

const api = {
  getAllBooks: () => fetch(`${BASE_URL}/books`).then(r => r.json()),
  searchBooks: (q) => fetch(`${BASE_URL}/books/search?q=${encodeURIComponent(q)}`).then(r => r.json()),
  addBook: (book) => fetch(`${BASE_URL}/books`, {
    method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(book),
  }).then(r => r.json()),
  createRequest: (req) => fetch(`${BASE_URL}/requests`, {
    method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(req),
  }).then(r => r.json()),
  approveRequest: (id) => fetch(`${BASE_URL}/requests/approve/${id}`, { method: "PUT" }).then(r => r.json()),
  rejectRequest: (id) => fetch(`${BASE_URL}/requests/reject/${id}`, { method: "PUT" }).then(r => r.json()),
};

function Badge({ status }) {
  const map = {
    AVAILABLE: { bg: "#e8f5e9", color: "#2e7d32" },
    BORROWED:  { bg: "#fff3e0", color: "#e65100" },
    SWAPPED:   { bg: "#f3e5f5", color: "#6a1b9a" },
    PENDING:   { bg: "#fff8e1", color: "#f57f17" },
    APPROVED:  { bg: "#e8f5e9", color: "#2e7d32" },
    REJECTED:  { bg: "#ffebee", color: "#b71c1c" },
    RETURNED:  { bg: "#e3f2fd", color: "#0d47a1" },
  };
  const s = map[status] || { bg: "#f5f5f5", color: "#555" };
  return (
    <span style={{
      background: s.bg, color: s.color, padding: "3px 10px",
      borderRadius: 20, fontSize: 11, fontWeight: 700, letterSpacing: 0.5,
      textTransform: "uppercase"
    }}>{status}</span>
  );
}

function BookCard({ book, onRequest, onApprove, onReject }) {
  const [hover, setHover] = useState(false);
  const colors = ["#f4a261","#e76f51","#2a9d8f","#264653","#e9c46a","#8338ec","#3a86ff"];
  const hue = colors[book.title.charCodeAt(0) % colors.length];

  return (
    <div
      onMouseEnter={() => setHover(true)}
      onMouseLeave={() => setHover(false)}
      style={{
        borderRadius: 16, overflow: "hidden", background: "#fff",
        boxShadow: hover ? "0 12px 40px rgba(0,0,0,0.15)" : "0 4px 16px rgba(0,0,0,0.07)",
        transform: hover ? "translateY(-4px)" : "translateY(0)",
        transition: "all 0.25s ease", display: "flex", flexDirection: "column",
      }}
    >
      <div style={{ height: 5, background: hue }} />
      <div style={{
        background: `linear-gradient(135deg, ${hue}22 0%, ${hue}08 100%)`,
        padding: "24px 20px 16px", borderBottom: "1px solid #f0f0f0",
        display: "flex", alignItems: "center", gap: 14
      }}>
        <div style={{
          width: 50, height: 64, background: hue, borderRadius: 6, flexShrink: 0,
          display: "flex", alignItems: "center", justifyContent: "center",
          fontSize: 22, boxShadow: "3px 3px 10px rgba(0,0,0,0.12)"
        }}>📖</div>
        <div style={{ minWidth: 0 }}>
          <div style={{
            fontWeight: 800, fontSize: 15, color: "#1a1a2e", lineHeight: 1.3,
            overflow: "hidden", textOverflow: "ellipsis", whiteSpace: "nowrap"
          }}>{book.title}</div>
          <div style={{ color: "#777", fontSize: 13, marginTop: 3 }}>by {book.author}</div>
          <div style={{ marginTop: 8 }}><Badge status={book.status} /></div>
        </div>
      </div>

      <div style={{
        padding: "12px 16px", display: "flex", gap: 8,
        alignItems: "center", background: "#fafafa", flexWrap: "wrap"
      }}>
        <span style={{
          fontSize: 11, color: "#999", background: "#f0f0f0",
          padding: "3px 10px", borderRadius: 20, fontWeight: 600, marginRight: "auto"
        }}>{book.genre || "General"}</span>

        {book.status === "AVAILABLE" && (
          <button onClick={() => onRequest(book)} style={{
            background: "#1a1a2e", color: "#fff", border: "none",
            padding: "6px 14px", borderRadius: 20, fontSize: 12,
            fontWeight: 700, cursor: "pointer", fontFamily: "inherit"
          }}>Request →</button>
        )}
        {book.status === "PENDING" && (
          <>
            <button onClick={() => onApprove(book)} style={{
              background: "#2e7d32", color: "#fff", border: "none",
              padding: "6px 12px", borderRadius: 20, fontSize: 12,
              fontWeight: 700, cursor: "pointer", fontFamily: "inherit"
            }}>✓ Approve</button>
            <button onClick={() => onReject(book)} style={{
              background: "#b71c1c", color: "#fff", border: "none",
              padding: "6px 12px", borderRadius: 20, fontSize: 12,
              fontWeight: 700, cursor: "pointer", fontFamily: "inherit"
            }}>✕ Reject</button>
          </>
        )}
      </div>
    </div>
  );
}

function RequestModal({ book, onClose, onSubmit }) {
  const [type, setType] = useState("BORROW");
  const [requesterId, setRequesterId] = useState("");
  const [ownerId, setOwnerId] = useState(book.ownerId || "");
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);

  const handleSubmit = async () => {
    if (!requesterId) return alert("Requester ID daalo");
    setLoading(true);
    try {
      await onSubmit({ bookId: book.id, requesterId, ownerId, type });
      setSuccess(true);
      setTimeout(onClose, 1500);
    } catch { alert("Request fail ho gayi"); }
    finally { setLoading(false); }
  };

  return (
    <div style={{
      position: "fixed", inset: 0, background: "rgba(0,0,0,0.5)",
      display: "flex", alignItems: "center", justifyContent: "center",
      zIndex: 1000, backdropFilter: "blur(4px)"
    }} onClick={onClose}>
      <div onClick={e => e.stopPropagation()} style={{
        background: "#fff", borderRadius: 20, width: 420, maxWidth: "92vw",
        overflow: "hidden", boxShadow: "0 30px 80px rgba(0,0,0,0.3)"
      }}>
        <div style={{ background: "#1a1a2e", padding: "22px 28px", color: "#fff" }}>
          <div style={{ fontSize: 10, color: "#aaa", letterSpacing: 2, textTransform: "uppercase" }}>Book Request</div>
          <div style={{ fontWeight: 800, fontSize: 19, marginTop: 5 }}>{book.title}</div>
          <div style={{ color: "#aaa", fontSize: 13 }}>by {book.author}</div>
        </div>
        <div style={{ padding: 24 }}>
          {success ? (
            <div style={{ textAlign: "center", padding: "24px 0" }}>
              <div style={{ fontSize: 48 }}>✅</div>
              <div style={{ fontWeight: 700, marginTop: 10, fontSize: 16 }}>Request Bhej Di!</div>
            </div>
          ) : (
            <>
              <div style={{ marginBottom: 16 }}>
                <label style={{ fontSize: 13, fontWeight: 700, color: "#333", display: "block", marginBottom: 8 }}>Type</label>
                <div style={{ display: "flex", gap: 8 }}>
                  {["BORROW", "SWAP"].map(t => (
                    <button key={t} onClick={() => setType(t)} style={{
                      flex: 1, padding: 10,
                      border: `2px solid ${type === t ? "#1a1a2e" : "#e0e0e0"}`,
                      background: type === t ? "#1a1a2e" : "#fff",
                      color: type === t ? "#fff" : "#333",
                      borderRadius: 10, fontWeight: 700, cursor: "pointer",
                      fontFamily: "inherit", fontSize: 14, transition: "all 0.15s"
                    }}>{t}</button>
                  ))}
                </div>
              </div>
              {[
                { label: "Requester ID (aapka)", val: requesterId, set: setRequesterId },
                { label: "Owner ID", val: ownerId, set: setOwnerId },
              ].map((f, i) => (
                <div key={i} style={{ marginBottom: 14 }}>
                  <label style={{ fontSize: 13, fontWeight: 700, color: "#333", display: "block", marginBottom: 6 }}>{f.label}</label>
                  <input value={f.val} onChange={e => f.set(e.target.value)} style={{
                    width: "100%", padding: "10px 14px", border: "2px solid #e0e0e0",
                    borderRadius: 10, fontSize: 14, fontFamily: "inherit",
                    outline: "none", boxSizing: "border-box"
                  }} />
                </div>
              ))}
              <div style={{ display: "flex", gap: 10, marginTop: 20 }}>
                <button onClick={onClose} style={{
                  flex: 1, padding: 12, background: "#f5f5f5", border: "none",
                  borderRadius: 10, fontWeight: 700, cursor: "pointer", fontFamily: "inherit"
                }}>Cancel</button>
                <button onClick={handleSubmit} disabled={loading} style={{
                  flex: 2, padding: 12, background: "#1a1a2e", color: "#fff",
                  border: "none", borderRadius: 10, fontWeight: 700, cursor: "pointer",
                  fontFamily: "inherit", opacity: loading ? 0.7 : 1
                }}>{loading ? "Bhej raha hai..." : "Request Bhejo"}</button>
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  );
}

function AddBookModal({ onClose, onAdded }) {
  const [form, setForm] = useState({ title: "", author: "", genre: "", ownerId: "" });
  const [loading, setLoading] = useState(false);
  const set = k => e => setForm(f => ({ ...f, [k]: e.target.value }));

  const handleAdd = async () => {
    if (!form.title || !form.author) return alert("Title aur Author zaroori hai");
    setLoading(true);
    try {
      const book = await api.addBook(form);
      onAdded(book);
      onClose();
    } catch { alert("Book add nahi hui"); }
    finally { setLoading(false); }
  };

  return (
    <div style={{
      position: "fixed", inset: 0, background: "rgba(0,0,0,0.5)",
      display: "flex", alignItems: "center", justifyContent: "center",
      zIndex: 1000, backdropFilter: "blur(4px)"
    }} onClick={onClose}>
      <div onClick={e => e.stopPropagation()} style={{
        background: "#fff", borderRadius: 20, width: 420, maxWidth: "92vw",
        overflow: "hidden", boxShadow: "0 30px 80px rgba(0,0,0,0.3)"
      }}>
        <div style={{ background: "#2a9d8f", padding: "22px 28px", color: "#fff" }}>
          <div style={{ fontSize: 10, color: "#b2dfdb", letterSpacing: 2, textTransform: "uppercase" }}>Library</div>
          <div style={{ fontWeight: 800, fontSize: 20, marginTop: 4 }}>Nayi Book Add Karo 📚</div>
        </div>
        <div style={{ padding: 24 }}>
          {[
            { k: "title",   label: "Title *",   ph: "e.g. The Alchemist"  },
            { k: "author",  label: "Author *",  ph: "e.g. Paulo Coelho"   },
            { k: "genre",   label: "Genre",     ph: "e.g. Fiction"        },
            { k: "ownerId", label: "Owner ID",  ph: "Aapka user ID"       },
          ].map(f => (
            <div key={f.k} style={{ marginBottom: 14 }}>
              <label style={{ fontSize: 13, fontWeight: 700, color: "#333", display: "block", marginBottom: 6 }}>{f.label}</label>
              <input value={form[f.k]} onChange={set(f.k)} placeholder={f.ph} style={{
                width: "100%", padding: "10px 14px", border: "2px solid #e0e0e0",
                borderRadius: 10, fontSize: 14, fontFamily: "inherit",
                outline: "none", boxSizing: "border-box"
              }} />
            </div>
          ))}
          <div style={{ display: "flex", gap: 10, marginTop: 20 }}>
            <button onClick={onClose} style={{
              flex: 1, padding: 12, background: "#f5f5f5", border: "none",
              borderRadius: 10, fontWeight: 700, cursor: "pointer", fontFamily: "inherit"
            }}>Cancel</button>
            <button onClick={handleAdd} disabled={loading} style={{
              flex: 2, padding: 12, background: "#2a9d8f", color: "#fff",
              border: "none", borderRadius: 10, fontWeight: 700, cursor: "pointer",
              fontFamily: "inherit", opacity: loading ? 0.7 : 1
            }}>{loading ? "Add ho rahi hai..." : "Book Add Karo ✓"}</button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default function App() {
  const [books, setBooks] = useState([]);
  const [query, setQuery] = useState("");
  const [loading, setLoading] = useState(false);
  const [requestBook, setRequestBook] = useState(null);
  const [showAdd, setShowAdd] = useState(false);
  const [toast, setToast] = useState("");

  const notify = (msg) => { setToast(msg); setTimeout(() => setToast(""), 2500); };

  const loadBooks = useCallback(async () => {
    setLoading(true);
    try {
      const data = query ? await api.searchBooks(query) : await api.getAllBooks();
      setBooks(Array.isArray(data) ? data : []);
    } catch { setBooks([]); }
    finally { setLoading(false); }
  }, [query]);

  useEffect(() => {
    const t = setTimeout(loadBooks, 300);
    return () => clearTimeout(t);
  }, [loadBooks]);

  const handleApprove = async (book) => {
    try {
      await api.approveRequest(book.id);
      notify("✅ Approve ho gaya!");
      loadBooks();
    } catch { notify("❌ Kuch gadbad ho gayi"); }
  };

  const handleReject = async (book) => {
    try {
      await api.rejectRequest(book.id);
      notify("🚫 Reject ho gaya.");
      loadBooks();
    } catch { notify("❌ Kuch gadbad ho gayi"); }
  };

  return (
    <div style={{
      minHeight: "100vh", background: "#f7f8fc",
      fontFamily: "'Segoe UI', -apple-system, BlinkMacSystemFont, sans-serif",
    }}>
      {/* Header */}
      <div style={{
        background: "#fff", borderBottom: "1px solid #eee",
        position: "sticky", top: 0, zIndex: 100,
        boxShadow: "0 2px 12px rgba(0,0,0,0.05)"
      }}>
        <div style={{
          maxWidth: 1100, margin: "0 auto", padding: "0 24px",
          display: "flex", alignItems: "center", gap: 16, height: 64
        }}>
          <div style={{ display: "flex", alignItems: "center", gap: 10, flexShrink: 0 }}>
            <div style={{
              width: 36, height: 36, background: "#1a1a2e", borderRadius: 10,
              display: "flex", alignItems: "center", justifyContent: "center", fontSize: 18
            }}>📖</div>
            <div>
              <div style={{ fontWeight: 900, fontSize: 16, color: "#1a1a2e", lineHeight: 1 }}>BookSwap</div>
              <div style={{ fontSize: 9, color: "#aaa", letterSpacing: 1.5, textTransform: "uppercase" }}>Community Library</div>
            </div>
          </div>

          <div style={{ flex: 1, position: "relative", maxWidth: 420, margin: "0 auto" }}>
            <span style={{
              position: "absolute", left: 12, top: "50%", transform: "translateY(-50%)",
              fontSize: 16, pointerEvents: "none"
            }}>🔍</span>
            <input
              value={query}
              onChange={e => setQuery(e.target.value)}
              placeholder="Title se search karo..."
              style={{
                width: "100%", padding: "9px 14px 9px 38px",
                border: "2px solid #e0e0e0", borderRadius: 12, fontSize: 14,
                fontFamily: "inherit", outline: "none", boxSizing: "border-box",
                transition: "border 0.2s"
              }}
              onFocus={e => e.target.style.border = "2px solid #1a1a2e"}
              onBlur={e => e.target.style.border = "2px solid #e0e0e0"}
            />
          </div>

          <button onClick={() => setShowAdd(true)} style={{
            padding: "9px 20px", background: "#1a1a2e", color: "#fff",
            border: "none", borderRadius: 12, fontWeight: 700, cursor: "pointer",
            fontFamily: "inherit", fontSize: 14, whiteSpace: "nowrap", flexShrink: 0
          }}>+ Add Book</button>
        </div>
      </div>

      {/* Toast */}
      {toast && (
        <div style={{
          position: "fixed", bottom: 24, left: "50%", transform: "translateX(-50%)",
          background: "#1a1a2e", color: "#fff", padding: "12px 28px",
          borderRadius: 30, fontWeight: 600, fontSize: 14, zIndex: 9999,
          boxShadow: "0 8px 30px rgba(0,0,0,0.25)", whiteSpace: "nowrap"
        }}>{toast}</div>
      )}

      {/* Content */}
      <div style={{ maxWidth: 1100, margin: "0 auto", padding: "28px 24px" }}>
        <div style={{ marginBottom: 20, color: "#888", fontSize: 14 }}>
          {loading ? "Load ho raha hai..." : `${books.length} book${books.length !== 1 ? "s" : ""} mili`}
          {query && !loading && <span> — "<strong>{query}</strong>"</span>}
        </div>

        {books.length === 0 && !loading ? (
          <div style={{ textAlign: "center", padding: "80px 20px", color: "#bbb" }}>
            <div style={{ fontSize: 60, marginBottom: 16 }}>📭</div>
            <div style={{ fontWeight: 700, fontSize: 18, color: "#999" }}>Koi book nahi mili</div>
            <div style={{ marginTop: 8, fontSize: 14 }}>Search badlo ya nayi book add karo</div>
          </div>
        ) : (
          <div style={{
            display: "grid",
            gridTemplateColumns: "repeat(auto-fill, minmax(260px, 1fr))",
            gap: 20
          }}>
            {books.map(book => (
              <BookCard
                key={book.id}
                book={book}
                onRequest={setRequestBook}
                onApprove={handleApprove}
                onReject={handleReject}
              />
            ))}
          </div>
        )}
      </div>

      {requestBook && (
        <RequestModal
          book={requestBook}
          onClose={() => setRequestBook(null)}
          onSubmit={api.createRequest}
        />
      )}

      {showAdd && (
        <AddBookModal
          onClose={() => setShowAdd(false)}
          onAdded={b => setBooks(prev => [b, ...prev])}
        />
      )}
    </div>
  );
}