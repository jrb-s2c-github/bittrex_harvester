package za.co.s2c.cb.bittrex_model;

import lombok.Data;

// TODO: 2022/03/18 move from string to other primitives as required
@Data
public class NewOrder {
    private String marketSymbol;
    private String direction;
    private String type;
    private String quantity;
    private String ceiling;
    private String limit;
    private String timeInForce;
    private String clientOrderId;
    private String useAwards;
}
