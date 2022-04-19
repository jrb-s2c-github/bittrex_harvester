package za.co.s2c.cb.tickers_listener;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

// TODO: 2022/03/18 batten down field restrictions to only allow getters
@Builder
public class TradeRequest {
    @Getter
    private String symbol;
    @Getter
    @Setter
    // TODO: 2022/03/18 can this be onle a getter ? 
    private boolean isBuy;
    @Getter
    @Setter
    private double price;
    @Getter
    @Setter
    private double quantity;

}
