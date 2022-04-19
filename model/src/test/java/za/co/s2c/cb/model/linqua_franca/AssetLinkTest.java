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
        // TODO: 2022/03/20 uncomment
//        AssetLink al12 = AssetLink.builder().asset1Asset2(asset1Asset2).askRate(askRate).bidRate(bidRate).lastTradeRate(1.1).from(Asset.builder().code(a1).build()).to(Asset.builder().code(a2).build()).build().afterBuild();
//        assertEquals(askRate, al12.getTradeRate());

        // TODO: 2022/03/20 uncomment
// TODO: 2022/03/13  
//        AssetLink al21 = AssetLink.builder().asset1Asset2(asset1Asset2).askRate(askRate).bidRate(bidRate).lastTradeRate(1.1).from(Asset.builder().code(a2).build()).to(Asset.builder().code(a1).build()).build().afterBuild();
//        assertEquals(bidRate, al21.getTradeRate());
    }
}
