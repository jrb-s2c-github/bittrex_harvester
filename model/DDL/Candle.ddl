CREATE TABLE IF NOT EXISTS  sample.candles (
    id UUID,
    sequence int,
    marketSymbol varchar,
    interval varchar,
    decision int,
    startsAt timestamp,
    open double,
    close double,
    high double,
    low double,
    volume double,
    quoteVolume double,
    tradeFulfilledTS timestamp,
    fulfillingCandleID UUID,
    primary key((id))
);