from datetime import datetime, timezone
import uuid

from fastapi import APIRouter, Depends, HTTPException, status

from app.core.database import get_db
from app.core.security import get_current_user
from app.models.features import WatchlistItem
from app.services.market_data import available_tickers, get_market_snapshot

router = APIRouter(prefix="/watchlist", tags=["Watchlist"])

def _validate_ticker(ticker: str) -> str:
    normalized = ticker.upper()
    if normalized not in available_tickers():
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"Ticker '{normalized}' is not tracked by StockVision",
        )
    return normalized

def _serialize(doc: dict) -> WatchlistItem:
    market = None
    try:
        market = get_market_snapshot(doc["ticker"])
    except (KeyError, ValueError):
        pass
    return WatchlistItem(
        id=doc["_id"],
        ticker=doc["ticker"],
        added_at=doc["added_at"],
        market=market,
    )

@router.get("", response_model=list[WatchlistItem])
async def list_watchlist(current_user: dict = Depends(get_current_user)):
    db = get_db()
    cursor = db.watchlist.find({"user_id": current_user["_id"]}).sort("added_at", -1)
    docs = await cursor.to_list(length=100)
    return [_serialize(doc) for doc in docs]

@router.post("/{ticker}", response_model=WatchlistItem, status_code=status.HTTP_201_CREATED)
async def add_to_watchlist(ticker: str, current_user: dict = Depends(get_current_user)):
    ticker = _validate_ticker(ticker)
    db = get_db()
    existing = await db.watchlist.find_one({"user_id": current_user["_id"], "ticker": ticker})
    if existing:
        raise HTTPException(
            status_code=status.HTTP_409_CONFLICT,
            detail=f"{ticker} is already in your watchlist",
        )
    doc = {
        "_id": str(uuid.uuid4()),
        "user_id": current_user["_id"],
        "ticker": ticker,
        "added_at": datetime.now(timezone.utc),
    }
    await db.watchlist.insert_one(doc)
    return _serialize(doc)

@router.delete("/{ticker}", status_code=status.HTTP_204_NO_CONTENT)
async def remove_from_watchlist(ticker: str, current_user: dict = Depends(get_current_user)):
    ticker = ticker.upper()
    db = get_db()
    result = await db.watchlist.delete_one({"user_id": current_user["_id"], "ticker": ticker})
    if result.deleted_count == 0:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"{ticker} is not in your watchlist",
        )
