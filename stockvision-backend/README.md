# StockVision Backend

FastAPI + MongoDB backend for StockVision FYP with Email/Password and Google OAuth authentication.

---

## 📁 Project Structure

```
stockvision-backend/
├── app/
│   ├── core/
│   │   ├── config.py      # Environment variables
│   │   ├── database.py    # MongoDB connection (Motor)
│   │   └── security.py    # JWT token creation & verification
│   ├── models/
│   │   └── user.py        # Pydantic models (request/response schemas)
│   ├── routes/
│   │   └── auth.py        # /signup, /login, /google endpoints
│   └── main.py            # FastAPI app entry point
├── .env.example           # Copy this to .env and fill in values
├── requirements.txt
└── README.md
```

---

## 🚀 Setup & Run

### 1. Install Python dependencies

```bash
cd stockvision-backend
pip install -r requirements.txt
```

### 2. Set up environment variables

```bash
cp .env.example .env
```

Open `.env` and fill in:
- `MONGODB_URL` — your MongoDB connection string
- `SECRET_KEY` — any long random string (keep secret!)
- `GOOGLE_CLIENT_ID` — from Google Cloud Console (see below)

### 3. Make sure MongoDB is running

```bash
# If using local MongoDB:
mongod

# Or use MongoDB Atlas (cloud) — just paste the connection string in .env
```

### 4. Start the backend

```bash
uvicorn app.main:app --reload
```

API runs at: **http://localhost:8000**  
Interactive docs: **http://localhost:8000/docs**

---

## 🔑 Get Your Google Client ID

1. Go to [console.cloud.google.com](https://console.cloud.google.com)
2. Create a project (or select existing)
3. **APIs & Services** → **Credentials** → **Create Credentials** → **OAuth 2.0 Client ID**
4. Application type: **Web application**
5. Add to **Authorized JavaScript origins**:
   - `http://localhost:3000`
6. Click **Create** → copy the **Client ID**
7. Paste it in:
   - `.env` → `GOOGLE_CLIENT_ID=...`
   - `src/pages/Login.jsx` → `const GOOGLE_CLIENT_ID = '...'`

---

## 📡 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/signup` | Register with email & password |
| POST | `/api/auth/login` | Login with email & password |
| POST | `/api/auth/google` | Login/Register with Google |
| GET | `/api/auth/me` | Get current user (requires token) |
| GET | `/health` | Health check |

---

## 🔌 Frontend Integration

### 1. Replace your Login page

Copy `Login.jsx` (provided) to `src/pages/Login.jsx`

### 2. Set your Google Client ID in Login.jsx

```js
const GOOGLE_CLIENT_ID = 'YOUR_GOOGLE_CLIENT_ID_HERE';
```

### 3. The token is stored in localStorage

```js
localStorage.getItem('sv_token') // use this for authenticated API calls
```

### 4. Sending authenticated requests

```js
const token = localStorage.getItem('sv_token');
const res = await fetch('http://localhost:8000/api/auth/me', {
  headers: { Authorization: `Bearer ${token}` }
});
```

---

## 🧪 Test with Swagger UI

Visit **http://localhost:8000/docs** to test all endpoints interactively.

---

## ✨ Added small features

These additions map directly to StockVision's report/database design and use the existing authentication system.

### 1. Watchlist
Authenticated users can save/remove tracked assets. Watchlist responses are enriched with the latest bundled processed market snapshot.

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/watchlist` | List the signed-in user's watchlist |
| POST | `/api/watchlist/{ticker}` | Add a tracked ticker |
| DELETE | `/api/watchlist/{ticker}` | Remove a ticker |

### 2. Price alerts
Users can create simple above/below price alerts, enable/disable them, and remove them. The API also reports whether the alert is currently triggered against the project's bundled processed price data.

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/alerts` | List alerts |
| POST | `/api/alerts` | Create an alert |
| PATCH | `/api/alerts/{alert_id}/toggle` | Enable/disable an alert |
| DELETE | `/api/alerts/{alert_id}` | Delete an alert |

Create example:

```json
{
  "ticker": "AAPL",
  "condition": "above",
  "target_price": 200
}
```

### 3. Market overview API
A dependency-free endpoint exposes the latest observation from each CSV in `processed/`, including close price, change, volume, RSI, MACD and SMA-20.

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/market/overview` | Snapshot for all bundled tickers |
| GET | `/api/market/{ticker}` | Snapshot for one ticker |

> The market endpoints intentionally return `data_type: "processed_historical"`; they do **not** claim the bundled CSV data is a live quote.
