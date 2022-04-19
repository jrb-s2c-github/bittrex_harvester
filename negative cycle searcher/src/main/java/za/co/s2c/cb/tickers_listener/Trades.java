package za.co.s2c.cb.tickers_listener;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

// TODO: 2022/03/18 use tradeRequess directly should MinTradeRequirement not be used  
@Deprecated
@Builder
public class Trades {
    @Getter
    private List<TradeRequest> tradeRequests;
    @Getter
    private MinTradeRequirement minTradeRequirement;
}
