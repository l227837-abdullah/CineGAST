from fastapi import APIRouter, HTTPException, status

from app.models.features import MarketSnapshot
from app.services.market_data import available_tickers, get_market_snapshot

router = APIRouter(prefix="/market", tags=["Market"])

@router.get("/overview", response_model=list[MarketSnapshot])
async def market_overview():
    return [get_market_snapshot(ticker) for ticker in available_tickers()]

@router.get("/{ticker}", response_model=MarketSnapshot)
async def market_snapshot(ticker: str):
    try:
        return get_market_snapshot(ticker)
    except KeyError:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"Ticker '{ticker.upper()}' is not tracked by StockVision",
        )
