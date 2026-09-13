from datetime import datetime, timezone
import uuid

from fastapi import APIRouter, Depends, HTTPException, status

from app.core.database import get_db
from app.core.security import get_current_user
from app.models.features import PriceAlertCreate, PriceAlertResponse
from app.services.market_data import available_tickers, get_market_snapshot

router = APIRouter(prefix="/alerts", tags=["Price Alerts"])

def _validate_ticker(ticker: str) -> str:
    normalized = ticker.upper()
    if normalized not in available_tickers():
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"Ticker '{normalized}' is not tracked by StockVision",
        )
    return normalized

def _serialize(doc: dict) -> PriceAlertResponse:
    current_price = None
    is_triggered = None
    try:
        current_price = get_market_snapshot(doc["ticker"])["price"]
        if current_price is not None:
            if doc["condition"] == "above":
                is_triggered = current_price >= doc["target_price"]
            else:
                is_triggered = current_price <= doc["target_price"]
    except (KeyError, ValueError):
        pass
    return PriceAlertResponse(
        id=doc["_id"],
        ticker=doc["ticker"],
        condition=doc["condition"],
        target_price=doc["target_price"],
        is_active=doc.get("is_active", True),
        created_at=doc["created_at"],
        current_price=current_price,
        is_triggered=is_triggered if doc.get("is_active", True) else False,
    )

@router.get("", response_model=list[PriceAlertResponse])
async def list_alerts(current_user: dict = Depends(get_current_user)):
    db = get_db()
    cursor = db.price_alerts.find({"user_id": current_user["_id"]}).sort("created_at", -1)
    docs = await cursor.to_list(length=100)
    return [_serialize(doc) for doc in docs]

@router.post("", response_model=PriceAlertResponse, status_code=status.HTTP_201_CREATED)
async def create_alert(body: PriceAlertCreate, current_user: dict = Depends(get_current_user)):
    ticker = _validate_ticker(body.ticker)
    db = get_db()
    doc = {
        "_id": str(uuid.uuid4()),
        "user_id": current_user["_id"],
        "ticker": ticker,
        "condition": body.condition.value,
        "target_price": body.target_price,
        "is_active": True,
        "created_at": datetime.now(timezone.utc),
    }
    await db.price_alerts.insert_one(doc)
    return _serialize(doc)

@router.patch("/{alert_id}/toggle", response_model=PriceAlertResponse)
async def toggle_alert(alert_id: str, current_user: dict = Depends(get_current_user)):
    db = get_db()
    doc = await db.price_alerts.find_one({"_id": alert_id, "user_id": current_user["_id"]})
    if not doc:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Price alert not found")
    new_state = not doc.get("is_active", True)
    await db.price_alerts.update_one({"_id": alert_id}, {"$set": {"is_active": new_state}})
    doc["is_active"] = new_state
    return _serialize(doc)

@router.delete("/{alert_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_alert(alert_id: str, current_user: dict = Depends(get_current_user)):
    db = get_db()
    result = await db.price_alerts.delete_one({"_id": alert_id, "user_id": current_user["_id"]})
    if result.deleted_count == 0:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Price alert not found")
