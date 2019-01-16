import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;

public abstract class Block implements Serializable {
    static final long serialVersionUID = 1L;

    protected Land land;
    protected Timestamp timestamp;
    protected String hash;
    protected String previousHash;
    protected long marketValue;
    protected boolean mortgageFlag;
    private String type;
    protected String CNICOfVerifier; // CNIC of government official who will verify transaction

    public Block(String previousHash, Land land,
                 long marketValue, boolean mortgageFlag,
                 String CNICOfVerifier, String type) throws Exception {
        Date date = new Date();
        this.timestamp = new Timestamp(date.getTime());
        this.previousHash = previousHash;
        this.marketValue = marketValue;
        this.mortgageFlag = mortgageFlag;
        this.CNICOfVerifier = CNICOfVerifier;
        this.land = land;
        if(type.equals("OOBlock") || type.equals("OPBlock") || type.equals("POBlock") || type.equals("PPBlock"))
            this.type = type;
        else
            throw new Exception("Wrong block type!");
    }

    public Land getLand() {
        return land;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public long getMarketValue() {
        return marketValue;
    }

    public boolean isMortgageFlag() {
        return mortgageFlag;
    }

    public String getCNICOfVerifier() {
        return CNICOfVerifier;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Block{" +
                "land=" + land +
                ", timestamp=" + timestamp +
                ", hash='" + hash + '\'' +
                ", previousHash='" + previousHash + '\'' +
                ", marketValue=" + marketValue +
                ", mortgageFlag=" + mortgageFlag +
                ", CNICOfVerifier='" + CNICOfVerifier + '\'' +
                '}';
    }
}

