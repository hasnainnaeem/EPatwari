
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;

public class Blockchain implements Serializable{

    private LinkedList<Block> blockchain;
    OwnershipChain ownershipChain;
    String previousHash = "0"; // root hash
    Scanner scanner;

    ObjectOutputStream blockchainOutputFile;
    ObjectInputStream blockchainInputFile;

    // Constructor
    public Blockchain() {
        this.blockchain = new LinkedList<Block>();
        ownershipChain = new OwnershipChain();
        scanner = new Scanner(System.in);
        readBlockchain(); // reads serialized blockchain stored on device
    }

    public void getStarted(){
        importData();
        saveBlockchain();
    }

    /*
     * This method imports registry records from files and initializes blockchain
     */
    public void importData(){
        importPersonOwners();
        importOrganizationOwners();
    }

    public void saveBlockchain()
    {
        try {
            blockchainOutputFile = new ObjectOutputStream(new BufferedOutputStream(
                    new FileOutputStream("Data\\epatwariData.bat")));

            // Serializing blockchain object
            blockchainOutputFile.writeObject(blockchain);

            blockchainOutputFile.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void readBlockchain()
    {
        try {
            blockchainInputFile = new ObjectInputStream(new BufferedInputStream(
                    new FileInputStream("Data\\epatwariData.bat")));
            // Serializing blockchain object
            blockchain = (LinkedList<Block>) blockchainInputFile.readObject();

            blockchainInputFile.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void printBlockchain()
    {
        OPBlock opBlock = null;
        POBlock poBlock = null;
        PPBlock ppBlock = null;
        OOBlock ooBlock = null;

        int blockNumber = 0;
        for(Block block: blockchain)
        {
            System.out.println("Printing Block # " + blockNumber + " , block hash = " + block.getHash()+" :");

            // Casting each block according to its type
            if(block.getType().equals("OPBlock")) {
                opBlock = (OPBlock) block;

                System.out.println("Type of block: "+ block.getType());

                // Printing land details
                System.out.println("Land Details: ");
                System.out.println(opBlock.getLand().toString());

                // Printing seller details
                System.out.println("Sellers: ");
                for(Organization owner: opBlock.getSellers())
                    System.out.println(owner.toString());

                // Printing buyer details
                System.out.println("Buyers: ");
                for(Person owner: opBlock.getBuyers())
                    System.out.println(owner.toString());

                // Printing heir details
                System.out.println("Heirs: ");
                for(Person heir: opBlock.getHeirs())
                    System.out.println(heir.toString());
            }

            else if(block.getType().equals("POBlock")) {
                poBlock = (POBlock) block;

                System.out.println("Type of block: "+ block.getType());

                // Printing land details
                System.out.println("Land Details: ");
                System.out.println(poBlock.getLand().toString());

                // Printing seller details
                System.out.println("Sellers: ");
                for(Person owner: poBlock.getSellers())
                    System.out.println(owner.toString());

                // Printing buyer details
                System.out.println("Buyers: ");
                for(Organization owner: poBlock.getBuyers())
                    System.out.println(owner.toString());

                // Printing heir details
                System.out.println("Heirs: ");
                for(Person heir: poBlock.getHeirs())
                    System.out.println(heir.toString());

            }

            else if(block.getType().equals("OOBlock")) {
                ooBlock = (OOBlock) block;

                System.out.println("Type of block: "+ block.getType());

                // Printing land details
                System.out.println("Land Details: ");
                System.out.println(ooBlock.getLand().toString());

                // Printing seller details
                System.out.println("Sellers: ");
                for(Organization owner: ooBlock.getSellers())
                    System.out.println(owner.toString());

                // Printing buyer details
                System.out.println("Buyers: ");
                for(Organization owner: ooBlock.getBuyers())
                    System.out.println(owner.toString());

                // Printing heir details
                System.out.println("Heirs: ");
                for(Person heir: ooBlock.getHeirs())
                    System.out.println(heir.toString());

            }
            else if(block.getType().equals("PPBlock")) {
                ppBlock = (PPBlock) block;

                System.out.println("Type of block: "+ block.getType());

                // Printing land details
                System.out.println("Land Details: ");
                System.out.println(ppBlock.getLand().toString());

                // Printing seller details
                System.out.println("Sellers: ");
                try {
                    for (Person owner : ppBlock.getSellers())
                        System.out.println(owner.toString());
                }
                catch(NullPointerException e)
                {}

                // Printing buyer details

                System.out.println("Buyers: ");
                for(Person owner: ppBlock.getBuyers())
                    System.out.println(owner.toString());

                // Printing heir details
                System.out.println("Heirs: ");
                for(Person heir: ppBlock.getHeirs())
                    System.out.println(heir.toString());
            }
            System.out.println("\n\t\t\t_____________________________________\n");
            blockNumber++;
        }
    }

    public LinkedList<Land> getLandsOfOwners(LinkedList<String> identifiers, String flag) throws Exception{

        // Identifier is CNIC or Organization. It will be differentiated on the basis flag variable value
        LinkedList<Land> lands = new LinkedList<Land>();
        if( !(flag.equals("person") || flag.equals("organization")))
            throw new Exception("Wrong owner type used for finding properties of owner.");
        OOBlock currBlockOO;
        PPBlock currBlockPP;
        OPBlock currBlockOP;
        POBlock currBlockPO;

        ArrayList<Person> ownersP = new ArrayList<Person>(3);
        ArrayList<Organization> ownersO = new ArrayList<Organization>(3);
        boolean ownersFlag;
        LinkedList<String> tempIdentifiers = new LinkedList<String>();
        for (Block block : blockchain) {
            // Casting to a specific block
            tempIdentifiers.clear();
            if (block.getType().equals("OPBlock") || block.getType().equals("PPBlock")) {

                if (block.getType().equals("OPBlock")) {
                    currBlockOP = (OPBlock) block;
                    ownersP = currBlockOP.getBuyers();
                    // Suppose given persons/organizations are owners unless their identifiers differ from identifiers of buyers in block
                    ownersFlag = true;
                    // Checking if buyers in current block match

                    for (int i = 0; i < ownersP.size(); i++) {
                        tempIdentifiers.add(ownersP.get(i).getCNIC());
                    }

                    if (!(tempIdentifiers.containsAll(identifiers)))
                        ownersFlag = false;

                    if (ownersFlag) { // Given identifiers matched identifiers of buyers of land in current block
                        lands.add(currBlockOP.getLand()); // Suppose this land is owned by given people
                    } else // Now if some land is in found lands but it was sold to someone and its current owners differ, then we have to remove that land from lands
                    {
                        for (Land ownedLand : lands) {
                            if (ownedLand.equals(currBlockOP.getLand())) {
                                lands.remove(currBlockOP.getLand());
                            }
                        }
                    }
                } else if (block.getType().equals("PPBlock")) {
                    currBlockPP = (PPBlock) block;
                    ownersP = currBlockPP.getBuyers();

                    ownersFlag = true;
                    // Checking if buyers in current block match

                    for (int i = 0; i < ownersP.size(); i++) {
                        tempIdentifiers.add(ownersP.get(i).getCNIC());
                    }

                    if (!(tempIdentifiers.containsAll(identifiers)))
                        ownersFlag = false;

                    if (ownersFlag) { // Given identifiers matched identifiers of buyers of land in current block
                        lands.add(currBlockPP.getLand()); // Suppose this land is owned by given people
                    } else // Now if some land is in found lands but it was sold to someone and its current owners differ, then we have to remove that land from lands
                    {
                        for (Land ownedLand : lands) {
                            if (ownedLand.equals(currBlockPP.getLand())) {
                                lands.remove(currBlockPP.getLand());
                            }
                        }
                    }

                }
            }


            else if (block.getType().equals("POBlock") || block.getType().equals("OOBlock")) {
                currBlockPO = (POBlock) block;
                ownersO = currBlockPO.getBuyers();

                ownersFlag = true;
                if (block.getType().equals("POBlock")) {
                    // Checking if buyers in current block match
                    if (!(tempIdentifiers.containsAll(identifiers)))
                        ownersFlag = false;

                    if (ownersFlag == true) // Given identifiers matched identifiers of buyers of land in current block
                        lands.add(currBlockPO.getLand()); // Suppose this land is owned by given people

                    else // Now if some land is in found lands but it was sold to someone and its current owners differ, then we have to remove that land from lands
                    {
                        for (Land ownedLand : lands) {
                            if (ownedLand.equals(currBlockPO.getLand())) {
                                lands.remove(currBlockPO.getLand());
                            }
                        }
                    }
                }
                else // block.getType() == "OOBlock"
                {
                    currBlockOO = (OOBlock) block;
                    ownersO = currBlockOO.getBuyers();

                    ownersFlag = true;
                    // Checking if buyers in current block match
                    if (!(tempIdentifiers.containsAll(identifiers)))
                        ownersFlag = false;

                    if (ownersFlag == true) // Given identifiers matched identifiers of buyers of land in current block
                        lands.add(currBlockOO.getLand()); // Suppose this land is owned by given people
                    else // Now if some land is in found lands but it was sold to someone and its current owners differ, then we have to remove that land from lands
                    {
                        for (Land ownedLand : lands) {
                            if (ownedLand.equals(currBlockOO.getLand())) {
                                lands.remove(currBlockOO.getLand());
                            }
                        }
                    }
                }
            }
        }
        return lands;
    }

    private boolean checkIfOwnersP(ArrayList<Person> sellers, Land land)
    {
        OPBlock opBlock = null;
        POBlock poBlock = null;
        PPBlock ppBlock = null;
        OOBlock ooBlock = null;
        Block block;
        ArrayList<Person> owners = new ArrayList<Person>(); // To match owners of land with sellers

        // Iterating through last to new node. Because latest node buyers are current owners
        for(int i = blockchain.size() - 1; i >= 0; i--) {

            block = blockchain.get(i);

            if (block.getType().equals("OPBlock")) {
                opBlock = (OPBlock) block;
                if(land.equals(opBlock.getLand()))
                {
                    // Current owners are buyers in latest node of land
                    owners = opBlock.getBuyers();

                    // Checking if owners of land are sellers
                    if(sellers.size() != owners.size())
                        return false;
                    // otherwise match all
                    for(int j =0; j < sellers.size(); j++)
                    {
                        if(!sellers.contains(owners.get(i)))
                            return false;
                    }
                    return true;
                }
            }

            else if (block.getType().equals("POBlock")) { // Do nothing
//                poBlock = (POBlock) block;

            }

            else if (block.getType().equals("OOBlock")) { // Do nothing
//                ooBlock = (OOBlock) block;
            }

            else if (block.getType().equals("PPBlock")) {
                ppBlock = (PPBlock) block;

                if(land.equals(ppBlock.getLand()))
                {
                    // Current owners are buyers in latest node of land
                    owners = ppBlock.getBuyers();

                    // Checking if owners of land are sellers
                    if(sellers.size() != owners.size())
                        return false;
                    // otherwise match all
                    for(int j =0; j < sellers.size(); j++)
                    {
                        if(!sellers.contains(owners.get(j)))
                            return false;
                    }
                    return true;

                }
            }
        } // for ends
        return false;
    }

    private boolean checkIfOwnersO(ArrayList<Organization> sellers, Land land)
    {
        OPBlock opBlock = null;
        POBlock poBlock = null;
        PPBlock ppBlock = null;
        OOBlock ooBlock = null;
        Block block;
        ArrayList<Organization> owners = new ArrayList<Organization>(); // To match owners of land with sellers

        // Iterating through last to new node. Because latest node buyers are current owners
        for(int i = blockchain.size() - 1; i >= 0; i--) {

            block = blockchain.get(i);

            if (block.getType().equals("OPBlock")) { // Do nothing
//                opBlock = (OPBlock) block;
            }

            else if (block.getType().equals("POBlock")) {
                poBlock = (POBlock) block;
                if(land.equals(poBlock.getLand()))
                {
                    // Current owners are buyers in latest node of land
                    owners = poBlock.getBuyers();

                    // Checking if owners of land are sellers
                    if(sellers.size() != owners.size())
                        return false;
                    // otherwise match all
                    for(int j =0; j < sellers.size(); j++)
                    {
                        if(!sellers.contains(owners.get(j)))
                            return false;
                    }
                    return true;
                }
            }

            else if (block.getType().equals("OOBlock")) {
                ooBlock = (OOBlock) block;
                if(land.equals(ooBlock.getLand()))
                {
                    // Current owners are buyers in latest node of land
                    owners = ooBlock.getBuyers();

                    // Checking if owners of land are sellers
                    if(sellers.size() != owners.size())
                        return false;
                    // otherwise match all
                    for(int j =0; j < sellers.size(); j++)
                    {
                        if(!sellers.contains(owners.get(j)))
                            return false;
                    }
                    return true;
                }

            }

            else if (block.getType().equals("PPBlock")) { // Do nothing
//                ppBlock = (PPBlock) block;
            }
        }
        return false;
    }

    public void inputAndAddBlock(){

        int numOfSellers = 0;
        int numOfBuyers = 0;
        int numOfHeirs = 0;
        Land land = null;
        String CNICOfVerifier = null;
        int marketValue = 0;
        String sellerType = null;
        String buyerType = null;
        boolean mortgageFlag = false;
        ArrayList<Person> buyersP = new ArrayList<Person>(2);
        ArrayList<Person> sellersP = new ArrayList<Person>(2);
        ArrayList<Organization> buyersO = new ArrayList<Organization>(2);
        ArrayList<Organization> sellersO = new ArrayList<Organization>(2);
        ArrayList<Person> heirs = new ArrayList<Person>();

        boolean flag = true;
        while(flag) {
            try {
                System.out.print("Enter number of sellers: ");
                numOfSellers = scanner.nextInt();

                System.out.print("Enter number of buyers: ");
                numOfBuyers = scanner.nextInt();

                System.out.print("Enter number of heirs: ");
                numOfHeirs = scanner.nextInt();

                for(int i =0; i < numOfHeirs; i++)
                {
                    System.out.println("Enter details for Heir # " + i+1 +": ");
                    heirs.add(inputHeir());
                }

                System.out.println("Enter Land Details:");
                land = inputLand();
                System.out.print("\tEnter Market Value of Land: ");
                marketValue = scanner.nextInt();

                System.out.print("Enter CNIC of Verifier: ");
                CNICOfVerifier = scanner.next();



                // Taking mortgage flag input
                boolean flag2 = true;
                while (flag2) {
                    System.out.print("Is it mortgaged land? (Y/N): ");
                    String input = scanner.next().toLowerCase();
                    if (input.equals("y")) {
                        mortgageFlag = true;
                        flag2 = false;
                    } else if (input.equals("n")){
                        mortgageFlag = false;
                        flag2 = false;
                    } else
                        flag2 = true;
                }

                flag2 = true;
                while (flag2) {
                    System.out.print("Select Seller Type (Organization/Person): ");
                    String input = scanner.next().toLowerCase();
                    if (input.equals("organization")) {
                        sellerType = "organization";
                        flag2 = false;
                    } else if (input.equals("person")) {
                        sellerType = "person";
                        flag2 = false;
                    } else
                        flag2 = true;
                }

                flag2 = true;
                while (flag2) {
                    System.out.print("Select Buyer Type (Organization/Person): ");
                    String input = scanner.next().toLowerCase();
                    if (input.equals("organization")) {
                        buyerType = "organization";
                        flag2 = false;
                    } else if (input.equals("person")) {
                        buyerType = "person";
                        flag2 = false;
                    } else
                        flag2 = true;
                }

                flag = false;
            }
            catch (Exception e)
            {
                System.err.println("Wrong details entered. Try again.");
                flag = true;
            }
        }


        if(sellerType.equals("person")){
            for(int i =0; i < numOfSellers; i++)
            {
                System.out.println("Enter details for Seller # " + (i+1) + " : ");
                sellersP.add(inputPerson());
            }
        }

        else if(sellerType.equals("organization")){
            for(int i =0; i < numOfSellers; i++)
            {
                System.out.println("Enter details for Seller # " + (i+1) + " : ");
                sellersO.add(inputOrganizationOwner());
            }
        }


        if(buyerType.equals("person")){
            for(int i =0; i < numOfBuyers; i++)
            {
                System.out.println("Enter details for Buyer # " + (i+1) + " : ");
                buyersP.add(inputPerson());
            }
        }

        else if(buyerType.equals("organization")){
            for(int i =0; i < numOfBuyers; i++)
            {
                System.out.println("Enter details for Buyer # " + (i+1) + " : ");
                buyersO.add(inputOrganizationOwner());
            }
        }


        if(sellerType.equals("person") && buyerType.equals("organization"))
        {
            if(addPOBlock(sellersP, buyersO, heirs, land, CNICOfVerifier, mortgageFlag, marketValue))
                System.out.println("Transaction was successfully completed.");
            else
                System.out.println("Transaction could not be completed. Try again with correct data.");
        }

        else if(sellerType.equals("organization") && buyerType.equals("person"))
        {
            if(addOPBlock(sellersO, buyersP, heirs, land, CNICOfVerifier, mortgageFlag, marketValue))
                System.out.println("Transaction was successfully completed.");
            else
                System.out.println("Transaction could not be completed. Try again with correct data.");
        }

        else if(sellerType.equals("person") && buyerType.equals("person"))
        {
            if(addPPBlock(sellersP, buyersP, heirs, land, CNICOfVerifier, mortgageFlag, marketValue))
                System.out.println("Transaction was successfully completed.");
            else
                System.out.println("Transaction could not be completed. Try again with correct data.");
        }

        else if(sellerType.equals("organization") && buyerType.equals("organization"))
        {
            if(addOOBlock(buyersO, sellersO, heirs, land, CNICOfVerifier, mortgageFlag, marketValue))
                System.out.println("Transaction was successfully completed.");
            else
                System.out.println("Transaction could not be completed. Try again with correct data.");
        }

        this.saveBlockchain();
    }

    private Person inputPerson()
    {
        boolean flag = true;
        String CNIC = null;
        String firstName = null;
        String lastName = null;
        Date DOB = null;

        while(flag) {
            try {
                System.out.print("\tEnter CNIC: ");
                CNIC = scanner.next();
                System.out.print("\tEnter First Name: ");
                firstName = scanner.next();
                System.out.print("\tEnter Last Name: ");
                lastName = scanner.next();
                System.out.print("\tEnter Date of Birth (DD-MM-YYYY): ");
                String DOBStr = scanner.next();
                DOB = null; // Date of birth
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                //Parsing the String
                DOB = dateFormat.parse(DOBStr);
                flag = false;
                scanner.nextLine(); // Discarding new line character from stream
            }
            catch (Exception e) {
                flag = true;
                System.err.println("Wrong input! Try again.\n");
            }
        }

        return new Person(CNIC, firstName, lastName, DOB);
    }

    private Land inputLand()
    {
        boolean flag = true;
        String address1 = null;
        String address2 = "";
        String city = null;
        String country = null;
        String town = null;
        String type = null;
        double longitude = 0;
        double latitude = 0;
        double area = 0;

        scanner.nextLine(); // Discarding new line character
        while(flag) {
            try {
                scanner.nextLine(); // Discarding char from stream
                System.out.print("\tEnter Address 1: ");
                address1 = scanner.nextLine();

                System.out.print("\tEnter Address 2: ");
                address2 = scanner.nextLine();

                System.out.print("\tEnter Town: ");
                town = scanner.nextLine();

                System.out.print("\tEnter City: ");
                city = scanner.next();
                scanner.nextLine(); // discards enter character

                System.out.print("\tEnter Country: ");
                country = scanner.next();
                scanner.nextLine(); // discards enter character

                boolean flag2 = true;
                while(flag2)
                {
                    System.out.println("Enter Type of Land (Residential, Agricultural, Commercial): ");
                    type = scanner.next().toLowerCase().trim();
                    if( !(type.equals("commercial") || type.equals("agricultural") || type.equals("residential")) )
                        flag2 = true;
                    else
                        flag2 = false;
                }

                System.out.println("Enter Longitude:");
                longitude = scanner.nextDouble();

                System.out.println("Enter Latitude:");
                latitude = scanner.nextDouble();

                System.out.println("Enter area of land (m^2):");
                area = scanner.nextDouble();
                flag = false;
            }
            catch (Exception e) {
                flag = true;
                System.err.println("Wrong input! Try again.\n");
            }
        }

        Land land = null;
        try {
            land = new Land(address1, address2, city, town, country, type, latitude, longitude, area);
        }
        catch(Exception e)
        {
            System.err.println("Land object could not be created.");
            e.printStackTrace();
        }

        return land;
    }

    private Person inputHeir()
    {
        return inputPerson();
    }

    private Organization inputOrganizationOwner()
    {
        boolean flag = true;
        String organizationName = null;
        String city = null;
        String town = null;
        String street = null;
        String country = null;

        while(flag) {
            try {
                scanner.nextLine(); // Discarding new line character
                System.out.print("\tEnter Organization Name: ");
                organizationName = scanner.nextLine();
                System.out.print("\tEnter Organization City: ");
                city = scanner.next();
                scanner.nextLine(); // discards enter character
                System.out.print("\tEnter Organization Town: ");
                town = scanner.nextLine();
                System.out.print("\tEnter Organization Street: ");
                street = scanner.nextLine();
                System.out.print("\tEnter Organization Country: ");
                country = scanner.next();
                scanner.nextLine(); // discards enter character
                flag = false;
            }
            catch(Exception e)
            {
                flag = true;
                System.err.println("Wrong input! Try again.\n");
            }
        }
        return new Organization(organizationName, city, town, street, country);
    }

    public boolean addPPBlock(ArrayList<Person> sellers, ArrayList<Person> buyers, ArrayList<Person> heirs, Land land,
                              String CNICOfVerifier, boolean mortgageFlag, int marketValue)
    {
        // Given sellers are not owners of land
        if(checkIfOwnersP(sellers, land) == false) {

            return false;
        }

        PPBlock block = null;
        try {
            block = new PPBlock(previousHash, land, marketValue, mortgageFlag, CNICOfVerifier, sellers, buyers, heirs);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        blockchain.add(block);
        previousHash = block.getHash();
        return true;
    }

    public boolean addOOBlock(ArrayList<Organization> sellers, ArrayList<Organization> buyers, ArrayList<Person> heirs, Land land,
                              String CNICOfVerifier, boolean mortgageFlag, int marketValue)
    {
        // Given sellers are not owners of land
        if(checkIfOwnersO(sellers, land) == false)
            return false;

        OOBlock block = null;
        try {
            block = new OOBlock(previousHash, land, marketValue, mortgageFlag, CNICOfVerifier, sellers, buyers, heirs);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        blockchain.add(block);

        previousHash = block.getHash();
        return true;
    }

    public boolean addPOBlock(ArrayList<Person> sellers, ArrayList<Organization> buyers, ArrayList<Person> heirs, Land land,
                              String CNICOfVerifier, boolean mortgageFlag, int marketValue)
    {
        // Given sellers are not owners of land
        if(checkIfOwnersP(sellers, land) == false)
            return false;

        POBlock block = null;
        try {
            block = new POBlock(previousHash, land, marketValue, mortgageFlag, CNICOfVerifier, sellers, buyers, heirs);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        blockchain.add(block);
        previousHash = block.getHash();
        return true;
    }

    public boolean addOPBlock(ArrayList<Organization> sellers, ArrayList<Person> buyers, ArrayList<Person> heirs, Land land,
                              String CNICOfVerifier, boolean mortgageFlag, int marketValue)
    {
        // Given sellers are not owners of land
        if(checkIfOwnersO(sellers, land) == false)
            return false;

        OPBlock block = null;
        try {
            block = new OPBlock(previousHash, land, marketValue, mortgageFlag, CNICOfVerifier, sellers, buyers, heirs);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        blockchain.add(block);
        previousHash = block.getHash();
        return true;
    }


    public LinkedList<Block> getBlockchain() {
        return blockchain;
    }

    public void setBlockchain(LinkedList<Block> blockchain) {
        this.blockchain = blockchain;
    }

    public ArrayList<Person> castToPerson(ArrayList<Object> objectArrayList)
    {
        ArrayList<Person> persons = new ArrayList<Person>();
        for(Object obj: objectArrayList)
            persons.add((Person) obj);
        return persons;
    }

    public ArrayList<Organization> castToOrganization(ArrayList<Object> objectArrayList)
    {
        ArrayList<Organization> organizations = new ArrayList<Organization>();
        for(Object obj: objectArrayList)
            organizations.add((Organization) obj);
        return organizations;
    }


    public void importPersonOwners()
    {

        // Adding Person Ownership blocks to blockchain
        OwnershipChain ownershipChainP = new OwnershipChain();
        ownershipChainP.readPersonOwnerships();

        // Iterating through ownerships and storing owner & land details in blockchain
        PPBlock tempPPBlock = null;
        for(Ownership ownership: ownershipChainP.getOwnerships())
        {
            try {
                tempPPBlock = new PPBlock(previousHash, ownership.getLand(), -1, false, "-1",
                        null, castToPerson(ownership.getOwners()), ownership.getHeirs());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            previousHash = tempPPBlock.getHash();
            blockchain.add(tempPPBlock);
        }
    }

    public void importOrganizationOwners()
    {

        // Empty
        ArrayList<Person> sellers = new ArrayList<Person>();

        // Adding Organization Ownership blocks to blockchain
        OwnershipChain ownershipChainO = new OwnershipChain();
        ownershipChainO.readOrganizationOwnerships();

        // Iterating through ownerships and storing owner & land details in blockchain
        POBlock tempPOBlock = null;
        for(Ownership ownership: ownershipChainO.getOwnerships())
        {
            try{
                tempPOBlock = new POBlock(previousHash, ownership.getLand(), -1, false, "-1",
                        sellers, castToOrganization(ownership.getOwners()), ownership.getHeirs());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            previousHash = tempPOBlock.getHash();
            blockchain.add(tempPOBlock);
        }
    }

    public static Date parseDate(String dateStr)
    {
        Date DOB = null; // Date of birth
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            //Parsing the String
            DOB = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return DOB;
    }
}
