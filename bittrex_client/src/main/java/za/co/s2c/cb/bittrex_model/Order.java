package za.co.s2c.cb.bittrex_model;

import lombok.Data;

// TODO: 2022/03/18 move from string to other primitives as required 
@Data
public class Order {
    private String id;
    private String marketSymbol;
    private String direction;
    private String type;
    private String quantity;
    private String limit;
    private String ceiling;
    private String timeInForce;
    private String clientOrderId;
    private String fillQuantity;
    private String commission;
    private String proceeds;
    private String status;
    private String createdAt;
    private String updatedAt;
    private String closedAt;
}
