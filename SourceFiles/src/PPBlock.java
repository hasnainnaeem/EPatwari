import java.io.Serializable;
import java.util.ArrayList;

public class PPBlock extends Block implements Serializable {
    static final long serialVersionUID = 1L;

    private ArrayList<Person> sellers;
    private ArrayList<Person> buyers;
    private ArrayList<Person> heirs;

    public PPBlock(String previousHash, Land land, int marketValue, boolean mortgageFlag, String CNICOfVerifier,
                   ArrayList<Person> sellers, ArrayList<Person> buyers, ArrayList<Person> heirs) throws Exception{
        super(previousHash, land, marketValue, mortgageFlag, CNICOfVerifier, "PPBlock");
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
        for(Person buyer: buyers)
            overallStr += buyer.toString();
        return overallStr;
    }

    public String getSellersDataStr(){
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

    public String getHeirsDataStr(){
        String overallStr = "";
        for(Person heir: heirs)
            overallStr += heir.toString();
        return overallStr;
    }

    @Override
    public String toString() {
        return "OOBlock{" +
                "Sellers=" + getSellersDataStr() +
                ", Buyers=" + getBuyersDataStr() +
                ", Heirs=" + getHeirsDataStr() +
                ", Land Details=" + land +
                ", timestamp=" + timestamp +
                ", hash='" + hash + '\'' +
                ", previousHash='" + previousHash + '\'' +
                ", marketValue=" + marketValue +
                ", mortgageFlag=" + mortgageFlag +
                ", CNICOfVerifier='" + CNICOfVerifier + '\'' + "Other block details: " + super.toString()+
                '}';
    }

    public ArrayList<Person> getSellers() {
        return sellers;
    }

    public void setSellers(ArrayList<Person> sellers) {
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
