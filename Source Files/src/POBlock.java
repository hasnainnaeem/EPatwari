import java.io.Serializable;
import java.util.ArrayList;

public class POBlock extends Block implements Serializable {
    static final long serialVersionUID = 1L;

    private ArrayList<Person> sellers;
    private ArrayList<Organization> buyers;
    private ArrayList<Person> heirs;

    public POBlock(String previousHash, Land land, int marketValue, boolean mortgageFlag, String CNICOfVerifier,
                   ArrayList<Person> sellers, ArrayList<Organization> buyers, ArrayList<Person> heirs) throws Exception{
        super(previousHash, land, marketValue, mortgageFlag, CNICOfVerifier, "POBlock");
        this.sellers = sellers;
        this.buyers = buyers;
        this.heirs = heirs;

        this.hash = calculateHash();
    }

    /* Game begins */
    public String calculateHash() {
        String calculatedhash = HashUtil.getHash(toString());
        return calculatedhash;
    }

    public String getBuyersDataStr(){
        String overallStr = "";

        try {
            for (Person seller : sellers)
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
        for(Person seller: sellers)
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

    public ArrayList<Person> getSellers() {
        return sellers;
    }

    public void setSellers(ArrayList<Person> sellers) {
        this.sellers = sellers;
    }

    public ArrayList<Organization> getBuyers() {
        return buyers;
    }

    public void setBuyers(ArrayList<Organization> buyers) {
        this.buyers = buyers;
    }

    public ArrayList<Person> getHeirs() {
        return heirs;
    }

    public void setHeirs(ArrayList<Person> heirs) {
        this.heirs = heirs;
    }
}
