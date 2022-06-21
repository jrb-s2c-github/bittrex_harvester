package za.co.s2c.cb.model.linqua_franca;

import java.io.*;
import java.util.Arrays;
import java.util.Map;

public class AssetNoLombok implements Serializable {

    private String exchange = "Bittrex";

    private String code;

    private boolean canOwn;

    @Deprecated
    private byte[] neighbours; // TODO remove


    private AssetNoLombok parent;

    public void incrementTransactions() {
        transactionAmounts++;
    }


    private int transactionAmounts;

    AssetNoLombok(final String exchange, final String code, final boolean canOwn, final byte[] neighbours, final AssetNoLombok parent, final int transactionAmounts) {
        this.exchange = exchange;
        this.code = code;
        this.canOwn = canOwn;
        this.neighbours = neighbours;
        this.parent = parent;
        this.transactionAmounts = transactionAmounts;
    }

    @Override
    public String toString() {
        return new StringBuffer().append("Asset: ").append(" code: ").append(getCode()).append(" canOwn: ").
                append(isCanOwn()).append("    transactionAmounts: ").append(transactionAmounts).
                append(" parent: ").append(getParent() != null ? getParent().getCode() : null).toString();
    }
    
    @Override
    public boolean equals(Object object) {
        if (object instanceof AssetNoLombok) {
            AssetNoLombok another = (AssetNoLombok)object;

            return this.code == another.code;
        }

        return false;
    }

    @Override
    public int hashCode( )
    {
        int hash = 7;
        hash = 31 * hash + code.hashCode();
        return hash;
    }

    public Map<AssetNoLombok, AssetLink> getNeighbours() throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(neighbours);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (Map<AssetNoLombok, AssetLink>) ois.readObject();
    }

    public void setNeighbours(Map<AssetNoLombok, AssetLink> neighbours) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(neighbours);

        this.neighbours = baos.toByteArray();
    }

    public AssetNoLombok getParent() {
        return parent;
    }

    public void setParent(AssetNoLombok parent) {
        this.parent = parent;
    }

    public String getCode() {
        return code;
    }

    public boolean isCanOwn() {
        return canOwn;
    }

    public void setCanOwn(boolean canOwn) {
        this.canOwn = canOwn;
    }


    public static AssetNoLombok.AssetBuilder builder() {
        return new AssetNoLombok.AssetBuilder();
    }

    public static class AssetBuilder {
        private String exchange;
        private String code;
        private boolean canOwn;
        private byte[] neighbours;
        private AssetNoLombok parent;
        private int transactionAmounts;

        AssetBuilder() {
        }

        public AssetNoLombok.AssetBuilder exchange(final String exchange) {
            this.exchange = exchange;
            return this;
        }

        public AssetNoLombok.AssetBuilder code(final String code) {
            this.code = code;
            return this;
        }

        public AssetNoLombok.AssetBuilder canOwn(final boolean canOwn) {
            this.canOwn = canOwn;
            return this;
        }

        /** @deprecated */
        @Deprecated
        public AssetNoLombok.AssetBuilder neighbours(final byte[] neighbours) {
            this.neighbours = neighbours;
            return this;
        }

        public AssetNoLombok.AssetBuilder parent(final AssetNoLombok parent) {
            this.parent = parent;
            return this;
        }

        public AssetNoLombok.AssetBuilder transactionAmounts(final int transactionAmounts) {
            this.transactionAmounts = transactionAmounts;
            return this;
        }

        public AssetNoLombok build() {
            return new AssetNoLombok(this.exchange, this.code, this.canOwn, this.neighbours, this.parent, this.transactionAmounts);
        }

        public String toString() {
            String var10000 = this.exchange;
            return "Asset.AssetBuilder(exchange=" + var10000 + ", code=" + this.code + ", canOwn=" + this.canOwn + ", neighbours=" + Arrays.toString(this.neighbours) + ", parent=" + this.parent + ", transactionAmounts=" + this.transactionAmounts + ")";
        }
    }


}
