from datetime import datetime
from enum import Enum
from typing import Optional
from pydantic import BaseModel, Field

class MarketSnapshot(BaseModel):
    ticker: str
    as_of: Optional[str] = None
    price: Optional[float] = None
    change: Optional[float] = None
    percent_change: Optional[float] = None
    volume: Optional[int] = None
    rsi: Optional[float] = None
    macd: Optional[float] = None
    sma_20: Optional[float] = None
    data_type: str = "processed_historical"

class WatchlistItem(BaseModel):
    id: str
    ticker: str
    added_at: datetime
    market: Optional[MarketSnapshot] = None

class AlertCondition(str, Enum):
    above = "above"
    below = "below"

class PriceAlertCreate(BaseModel):
    ticker: str = Field(..., min_length=1, max_length=20)
    condition: AlertCondition
    target_price: float = Field(..., gt=0)

class PriceAlertResponse(BaseModel):
    id: str
    ticker: str
    condition: AlertCondition
    target_price: float
    is_active: bool
    created_at: datetime
    current_price: Optional[float] = None
    is_triggered: Optional[bool] = None
