"""Helpers for reading the bundled StockVision processed market datasets."""

from __future__ import annotations

import csv
from functools import lru_cache
from pathlib import Path
from typing import Any

PROJECT_ROOT = Path(__file__).resolve().parents[2]
PROCESSED_DIR = PROJECT_ROOT / "processed"


def available_tickers() -> list[str]:
    if not PROCESSED_DIR.exists():
        return []
    return sorted(path.stem.upper() for path in PROCESSED_DIR.glob("*.csv"))


def _as_float(value: str | None) -> float | None:
    if value in (None, ""):
        return None
    try:
        return float(value)
    except (TypeError, ValueError):
        return None


@lru_cache(maxsize=32)
def _last_two_rows(ticker: str) -> tuple[dict[str, str], dict[str, str] | None]:
    ticker = ticker.upper()
    path = PROCESSED_DIR / f"{ticker}.csv"
    if not path.exists():
        raise KeyError(ticker)

    previous: dict[str, str] | None = None
    current: dict[str, str] | None = None
    with path.open("r", newline="", encoding="utf-8") as handle:
        for row in csv.DictReader(handle):
            previous, current = current, row

    if current is None:
        raise ValueError(f"No processed market data found for {ticker}")
    return current, previous


def get_market_snapshot(ticker: str) -> dict[str, Any]:
    ticker = ticker.upper()
    current, previous = _last_two_rows(ticker)

    price = _as_float(current.get("close"))
    previous_close = _as_float(previous.get("close")) if previous else None
    change = None
    percent_change = None
    if price is not None and previous_close not in (None, 0):
        change = price - previous_close
        percent_change = (change / previous_close) * 100

    return {
        "ticker": ticker,
        "as_of": current.get("date"),
        "price": round(price, 4) if price is not None else None,
        "change": round(change, 4) if change is not None else None,
        "percent_change": round(percent_change, 4) if percent_change is not None else None,
        "volume": int(float(current["volume"])) if current.get("volume") else None,
        "rsi": round(_as_float(current.get("rsi")), 2) if _as_float(current.get("rsi")) is not None else None,
        "macd": round(_as_float(current.get("macd")), 4) if _as_float(current.get("macd")) is not None else None,
        "sma_20": round(_as_float(current.get("sma_20")), 4) if _as_float(current.get("sma_20")) is not None else None,
        "data_type": "processed_historical",
    }
