package za.co.s2c.cb.bittrex_client;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Component;
import za.co.s2c.cb.bittrex_model.Balance;

import java.util.Date;
import java.util.Set;

@Component
@EnableFeignClients
public class BalancesService {
    @Autowired
    private BalancesClient balanceClient;

    public Set<Balance> getBalances() {

        String apiTimestamp = new Date().getTime() + "";
        String contentHash = DigestUtils.sha512Hex("");
        String preSign = apiTimestamp + "https://api.bittrex.com/v3" + "/balances" + "GET" + contentHash;
        String sign = new HmacUtils(HmacAlgorithms.HMAC_SHA_512, BittrexConstants.API_SECRET).hmacHex(preSign);

        return balanceClient.getBalances(BittrexConstants.API_KEY,
                apiTimestamp,
                contentHash,
                sign);
    }
}
