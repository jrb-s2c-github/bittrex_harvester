package za.co.s2c.cb.model.linqua_franca;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssetTest {

    @Test
    public void testEquals() {
        String code1 = "code1";
        Asset a1 = Asset.builder().canOwn(true).neighbours(null).code(code1).build();
        Asset a2 = Asset.builder().canOwn(false).neighbours(null).code(code1).build();

        assertEquals(a1, a2);
    }
}
