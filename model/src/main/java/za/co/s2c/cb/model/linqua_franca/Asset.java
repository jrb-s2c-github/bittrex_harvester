package za.co.s2c.cb.model.linqua_franca;

import lombok.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//@NoArgsConstructor

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
    // key is code of Asset instance being linked referred instance of AssetLink
    private Map<Asset, AssetLink> neighbours;

    @Setter
    @Getter
    private Asset parent;

    public void incrementTransactions() {
        transactionAmounts++;
    }

//    public void incrementSales() {
//        saleAmounts++;
//    }

//    private int buyAmounts;
    @Getter
//    @Setter
    private int transactionAmounts;

//    private Map<String, AssetLink> initNeighbours() {
//        return new HashMap<>(100);
//    }

    @Override
    public String toString() {
        return new StringBuffer().append("Asset: ").append(" code: ").append(code).append(" canOwn: ").
                append(canOwn).append("    transactionAmounts: ").append(transactionAmounts).
                append(" parent: ").append(parent != null ? parent.getCode() : null).toString();
    }
}
