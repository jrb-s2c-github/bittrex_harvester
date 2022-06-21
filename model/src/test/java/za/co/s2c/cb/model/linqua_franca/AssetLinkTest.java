package za.co.s2c.cb.model.linqua_franca;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssetLinkTest {

    @Test
    public void testEquals() {
        String code1 = "code1";
        String asset1Asset2 = "a1-a2";
        AssetLink a1 = AssetLink.builder().asset1Asset2(asset1Asset2).askRate(1.1).bidRate(1.1).lastTradeRate(1.1).from(Asset.builder().build()).to(Asset.builder().build()).build();
        AssetLink a2 = AssetLink.builder().asset1Asset2(asset1Asset2).askRate(2.1).bidRate(2.1).lastTradeRate(2.1).from(Asset.builder().build()).to(Asset.builder().build()).build();
        assertEquals(a1, a2);
    }

    @Test
    public void testAfterBuildGetTradeRate() {
        String a1 = "a1";
        String a2 = "a2";
        String asset1Asset2 = a1 + "-" + a2;
        String asset2Asset1 = a2 + "-" + a1;
        double askRate = 1.1;
        double bidRate = 2.2;

    }
}
