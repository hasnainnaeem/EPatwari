import java.io.Serializable;
import java.util.ArrayList;

public class OOBlock extends Block implements Serializable {
    static final long serialVersionUID = 1L;

    private ArrayList<Organization> sellers;
    private ArrayList<Organization> buyers;
    private ArrayList<Person> heirs;

    // Constructor
    public OOBlock(String previousHash, Land land, int marketValue, boolean mortgageFlag, String CNICOfVerifier,
                   ArrayList<Organization> buyers, ArrayList<Organization> sellers, ArrayList<Person> heirs) throws Exception {
        super(previousHash, land, marketValue, mortgageFlag, CNICOfVerifier, "OOBlock");
        this.buyers = buyers;
        this.sellers = sellers;
        this.heirs = heirs;

        // Generating unique hash for each node
        hash = calculateHash();
    }


    /* Game begins */
    public String calculateHash() {
        String calculatedhash = HashUtil.getHash(toString());
        return calculatedhash;
    }

    public String getBuyersDataStr(){
        String overallStr = "";
        try {
            for (Organization seller : sellers)
                overallStr += seller.toString();
        }
        catch(NullPointerException e)
        {
            overallStr = "";
        }
        return overallStr;
    }

    public String getSellersDataStr(){
        String overallStr = "";
        for(Organization seller: sellers)
            overallStr += seller.toString();
        return overallStr;
    }

    public String getHeirsDataStr(){
        String overallStr = "";
        for(Person heir: heirs)
            overallStr += heir.toString();
        return overallStr;
    }

    @Override
    public String toString() {
        return "OOBlock{" +
                "sellers=" + getSellersDataStr() +
                ", buyers=" + getBuyersDataStr() +
                ", heirs=" + getHeirsDataStr() +
                ", land=" + land +
                ", timestamp=" + timestamp +
                ", hash='" + hash + '\'' +
                ", previousHash='" + previousHash + '\'' +
                ", marketValue=" + marketValue +
                ", mortgageFlag=" + mortgageFlag +
                ", CNICOfVerifier='" + CNICOfVerifier + '\'' +
                '}';
    }

    /* Getters and Setters */
    public ArrayList<Organization> getBuyers() {
        return buyers;
    }

    public void setBuyers(ArrayList<Organization> buyers) {
        this.buyers = buyers;
    }

    public ArrayList<Organization> getSellers() {
        return sellers;
    }

    public void setSellers(ArrayList<Organization> sellers) {
        this.sellers = sellers;
    }

    public ArrayList<Person> getHeirs() {
        return heirs;
    }

    public void setHeirs(ArrayList<Person> heirs) {
        this.heirs = heirs;
    }
}
