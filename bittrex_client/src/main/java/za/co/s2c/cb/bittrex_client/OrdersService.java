package za.co.s2c.cb.bittrex_client;

import com.google.gson.Gson;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Component;
import za.co.s2c.cb.bittrex_model.NewOrder;
import za.co.s2c.cb.bittrex_model.Order;

import java.util.Date;
import java.util.List;

@Component
@EnableFeignClients
public class OrdersService {
    @Autowired
    private OrdersClient ordersClient;

    public List<Order> getOpenOrders() {
        String apiTimestamp = new Date().getTime() + "";
        String contentHash = DigestUtils.sha512Hex("");
        String preSign = apiTimestamp + "https://api.bittrex.com/v3" + "/orders/open" + "GET" + contentHash;
        String sign = new HmacUtils(HmacAlgorithms.HMAC_SHA_512, BittrexConstants.API_SECRET).hmacHex(preSign);
        return ordersClient.getOpenOrders(BittrexConstants.API_KEY,
                apiTimestamp,
                contentHash,
                sign,
                null);
    }

    public Order newOrders(NewOrder newOrder) {
        String apiTimestamp = new Date().getTime() + "";
        String requestBody = new Gson().toJson(newOrder);
        String contentHash = DigestUtils.sha512Hex(requestBody);
        String preSign = apiTimestamp + "https://api.bittrex.com/v3" + "/orders" + "POST" + contentHash;
        String sign = new HmacUtils(HmacAlgorithms.HMAC_SHA_512, BittrexConstants.API_SECRET1).hmacHex(preSign);
        return ordersClient.newOrder(BittrexConstants.API_KEY1,
                apiTimestamp,
                contentHash,
                sign,
                requestBody);
    }

    public List<Order> getClosedOrders() {
        String apiTimestamp = new Date().getTime() + "";
        String contentHash = DigestUtils.sha512Hex("");
        String preSign = apiTimestamp + "https://api.bittrex.com/v3" + "/orders/closed" + "GET" + contentHash;
        String sign = new HmacUtils(HmacAlgorithms.HMAC_SHA_512, BittrexConstants.API_SECRET).hmacHex(preSign);
        return ordersClient.getClosedOrders(BittrexConstants.API_KEY,
                apiTimestamp,
                contentHash,
                sign,
                null);
    }
}
