import java.io.Serializable;
import java.util.ArrayList;

public class OPBlock extends Block implements Serializable {
    static final long serialVersionUID = 1L;

    private ArrayList<Organization> sellers;
    private ArrayList<Person> buyers;
    private ArrayList<Person> heirs;

    public OPBlock(String previousHash, Land land, int marketValue, boolean mortgageFlag, String CNICOfVerifier,
                   ArrayList<Organization> sellers, ArrayList<Person> buyers, ArrayList<Person> heirs) throws  Exception{
        super(previousHash, land, marketValue, mortgageFlag, CNICOfVerifier, "OPBlock");
        this.sellers = sellers;
        this.buyers = buyers;
        this.heirs = heirs;

        hash = calculateHash();
    }

    /* Game begins */
    public String calculateHash() {
        String calculatedhash = HashUtil.getHash(toString());
        return calculatedhash;
    }

    public String getBuyersDataStr(){
        String overallStr = "";
        for(Person buyer: buyers)
            overallStr += buyer.toString();
        return overallStr;
    }

    public String getSellersDataStr(){
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


    public ArrayList<Organization> getSellers() {
        return sellers;
    }

    public void setSellers(ArrayList<Organization> sellers) {
        this.sellers = sellers;
    }

    public ArrayList<Person> getBuyers() {
        return buyers;
    }

    public void setBuyers(ArrayList<Person> buyers) {
        this.buyers = buyers;
    }

    public ArrayList<Person> getHeirs() {
        return heirs;
    }

    public void setHeirs(ArrayList<Person> heirs) {
        this.heirs = heirs;
    }
}
