package za.co.s2c.cb.tickers_listener;

import lombok.Data;

@Deprecated
@Data
public class MinTradeRequirement {
    private double btc;
    private double eth;
    private double usd;
    private double eur;
    private double usdt;
}
