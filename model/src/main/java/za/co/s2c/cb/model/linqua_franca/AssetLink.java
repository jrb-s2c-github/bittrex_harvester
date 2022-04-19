package za.co.s2c.cb.model.linqua_franca;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@EqualsAndHashCode(exclude = {"askRate", "bidRate", "lastTradeRate", "from", "to", "logWeight"})
public class AssetLink implements Serializable {
    @Getter
    // NOTE: id is something like USD-BTC to indicate the formal quite from bittrex, even though the AssetLink instance
    // might quote in terms of the reverse quote
    private String asset1Asset2;
    @Getter
    private Asset from;
    @Getter
    private Asset to;

    @Setter
    @Getter
    private double lastTradeRate;
    @Setter
    @Getter
    private double bidRate;

    @Setter
    @Getter
    private double askRate;

    @Getter
    private Double logWeight;

    private boolean isBuy;

    @Override
    public String toString() {
        return new StringBuffer().append("asset1Asset2: ").append(asset1Asset2).append(" from: ").
                append(from.getCode()).append(" to: ").append(to.getCode()).append(" askRate: ").append(askRate).
                append(" bidRate: ").append(bidRate).append(" lastTradeRate: ").append(lastTradeRate).append(" logWeight: ").append(logWeight).toString();
    }

    @Deprecated
    public double getTradeRate() {
        if (isBuy)
            return 1 / askRate;
        else
            return bidRate;
//        return Math.pow(2.0, logWeight);
    }

    public AssetLink afterBuild(boolean isBuy) {
        this.isBuy = isBuy;
        double rate = 1 / askRate;
        if (! isBuy) {
            rate = bidRate;
        }
//        double rate = 1 / lastTradeRate;
//        if (! isBuy) {
//            rate = lastTradeRate;
//        }

        logWeight = Math.log10(rate) / Math.log10(2);

        return this;
    }
}
