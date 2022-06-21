package za.co.s2c.cb.model.linqua_franca;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Builder(toBuilder = false)
@EqualsAndHashCode(exclude = {"canOwn", "neighbours", "parent", "transactionAmounts", "exchange"})
public class Asset implements Serializable {

    private String exchange = "Bittrex";

    @Getter
    private String code;
    @Setter
    @Getter
    private boolean canOwn;
    @Getter
    private Map<Asset, AssetLink> neighbours;

    @Setter
    @Getter
    private Asset parent;

    public void incrementTransactions() {
        transactionAmounts++;
    }

    @Getter
    private int transactionAmounts;


    @Override
    public String toString() {
        return new StringBuffer().append("Asset: ").append(" code: ").append(code).append(" canOwn: ").
                append(canOwn).append("    transactionAmounts: ").append(transactionAmounts).
                append(" parent: ").append(parent != null ? parent.getCode() : null).toString();
    }
}
