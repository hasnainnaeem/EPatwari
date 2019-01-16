/*
Written by: Muhammad Hasnain Naeem
            Syed Muazzam Ali Kazmi
            Syed Ayan Haider
Date of starting project: 12/12/2018
Date, Hours, Cups of tea/coffee: 12/12/2018, 1, 0
Date, Hours, Cups Of TEA, Line Count:  13/12/2018, 6, 2, 769 - 115 blank lines
Date, Hours, Cups Of TEA, Line Count:  15/12/2018, 9, 3, 1791 - 274 lines
Date, Hours, Cups Of TEA, Line Count:  16/12/2018, 7, 2, 2307 - 274 lines
Date, Hours, Cups Of TEA, Line Count:  18/12/2018, 4, 1, 2495 - 274 lines
Date not updated
 */
import javax.naming.CompositeName;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class main {
    private static Blockchain epatwari = new Blockchain();
    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args){

        System.out.println(HashUtil.getHash("hasnain naeem"));
        displayOptions();
    }

    private static void displayOptions(){
        while (true) {
            try {
                int choice = 0;
                while (choice < 1 || choice > 6) {
                    System.out.println("\n\n*****************************************************");
                    System.out.println("Select option:");
                    System.out.println("\t1. Do Transaction");
                    System.out.println("\t2. Display Blockchain Data");
                    System.out.println("\t3. Find Properties Owned by a Person(s)");
                    System.out.println("\t4. Find Properties Owned by an Organization(s)");
                    System.out.println("\t5. Copy Blockchain Data to MySQL Database");
                    System.out.println("\t6. Exit");
                    System.out.print(">");
                    choice = scanner.nextInt();
                    System.out.println("*****************************************************");

                }

                if (choice == 1)
                    epatwari.inputAndAddBlock();
                else if (choice == 2)
                    epatwari.printBlockchain();
                else if (choice == 3) {

                    int numOfOwners;
                    System.out.println("Enter number of owners:");
                    numOfOwners = scanner.nextInt();

                    LinkedList<String> CNICs = new LinkedList<>();
                    for (int i = 0; i < numOfOwners; i++) {
                        System.out.println("Enter CNIC # " + i + ":");
                        CNICs.add(scanner.next());
                    }
                    try {
                        System.out.println("Lands owned by given Person:");
                        Land.printLands(epatwari.getLandsOfOwners(CNICs, "person"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (choice == 4) {

                    int numOfOwners;
                    System.out.println("Enter number of owners:");
                    numOfOwners = scanner.nextInt();

                    scanner.nextLine(); // Discarding new line character from stream

                    LinkedList<String> names = new LinkedList<String>();
                    for (int i = 0; i < numOfOwners; i++) {
                        System.out.println("Enter Name # " + i + ":");
                        names.add(scanner.nextLine());
                    }
                    try {
                        System.out.println("Lands owned by given Organization:");
                        Land.printLands(epatwari.getLandsOfOwners(names, "organization"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (choice == 5) {
                    int importChoice = 0;
                    while(importChoice != 1 && importChoice != 2) {
                        System.out.println("Select Import Option:");
                        System.out.println("\t1. Import All Data");
                        System.out.println("\t2. Import New Nodes Only");
                        System.out.print("> ");
                        importChoice = scanner.nextInt();
                    }

                    if(importChoice == 1)
                        connectAndExportToMysql(true);
                    else if(importChoice == 2)
                        connectAndExportToMysql(false);

                } else if (choice == 6)
                    break;
            } catch (Exception e) {
                System.out.println("Wrong input. Try Again.");
            }
        }
    }

    /*
    This method is used to export data of Blockchain into Mysql Database. Data changes whenever a transaction is done. So,
    this should be used before analyzing data. Database can be used to extract different kinds of information from database;
     */
    public static void connectAndExportToMysql(Boolean readFromStart)
    {
        int SID = 0;
        int blockNumber = 0;
        int startingNode = 0;
        if(!readFromStart) { // Don't import nodes which were import last time
            int data[] = readFromFile();
            SID = data[1];
            blockNumber = data[0];
            startingNode = data[0];
        }

        System.out.println("Connecting to Mysql Server to export data.");

        String queryForLand = " insert into property (PID, address1, address2, latitude, longitude, town, " +
                "city, country)"
                + " values (?, ?, ?, ?, ?, ?, ?, ?)";
        String queryForOrganization = " insert into organizations (orgname, street, town, city, country, sid)"
                + " values (?, ?, ?, ?, ?, ?)";
        String queryForPerson = " insert into person (CNIC, fname, lname, DOB, SID)"
                + " values (?, ?, ?, ?, ?)";
        String queryForGovOfficial = " insert into GovOfficial (CNIC)"
                + " values (?)";
        String queryForHeir = " insert into heir (currHash, Time_stamp, sid)"
                + " values (?, ?, ?)";
        String queryForBuyer = " insert into buyer (currHash, Time_stamp, sid)"
                + " values (?, ?, ?)";
        String queryForSeller = " insert into seller (currHash, Time_stamp, sid)"
                + " values (?, ?, ?)";
        String queryForTransaction = " insert into transactions (time_stamp, currHash, prevHash, marketValue, mortgageFlag," +
                "official_cnic, pid, typeofblock)"
                + " values (?, ?, ?, ?, ?, ?, ?, ?)";
        String queryForSomeone = " insert into someone (SID)"
                + " values (?)";
        String queryForAgricultural = " insert into agricultural (PID)"
                + " values (?)";
        String queryForCommercial = " insert into commercial (PID)"
                + " values (?)";
        String queryForResidential = " insert into residential (PID)"
                + " values (?)";

        try {

            // Connecting to database
            Connection conn = null;
            String url = "jdbc:mysql://127.0.0.1:3306/epatwari";
            Properties info = new Properties();
            info.put("user", "root");
            info.put("password", "hasnain");

            conn = DriverManager.getConnection(url, info);

            // Getting prepared statements ready
            // Someone: Person, Organization
            PreparedStatement PSForSomeone = conn.prepareStatement(queryForSomeone);
            PreparedStatement PSForPerson = conn.prepareStatement(queryForPerson);
            PreparedStatement PSForOrganization = conn.prepareStatement(queryForOrganization);

            // For government official
            PreparedStatement PSForGovOfficial = conn.prepareStatement(queryForGovOfficial);

            // Buyers, Sellers, Heirs
            PreparedStatement PSForBuyer = conn.prepareStatement(queryForBuyer);
            PreparedStatement PSForSeller = conn.prepareStatement(queryForSeller);
            PreparedStatement PSForHeir = conn.prepareStatement(queryForHeir);
            PreparedStatement PSForTransaction = conn.prepareStatement(queryForTransaction);

            // Property
            PreparedStatement PSForLand = conn.prepareStatement(queryForLand);
            PreparedStatement PSForResidential = conn.prepareStatement(queryForResidential);
            PreparedStatement PSForForCommercial = conn.prepareStatement(queryForCommercial);
            PreparedStatement PSForAgriculatural = conn.prepareStatement(queryForAgricultural);



            if (conn != null) {
                System.out.println("Successfully connected to database.");
            }

            // Inserting data
            LinkedList<Block> blockchain = epatwari.getBlockchain();

            OPBlock opBlock = null;
            POBlock poBlock = null;
            PPBlock ppBlock = null;
            OOBlock ooBlock = null;

            Land land = null;
            String CNICOfVerifier = null;
            int marketValue = 0;
            String typeOfBlock = null;
            boolean mortgageFlag = false;
            ArrayList<Person> buyersP = new ArrayList<Person>(2);
            ArrayList<Person> sellersP = new ArrayList<Person>(2);
            ArrayList<Organization> buyersO = new ArrayList<Organization>(2);
            ArrayList<Organization> sellersO = new ArrayList<Organization>(2);
            ArrayList<Person> heirs = new ArrayList<Person>();

            Set<Integer> buyerSID = new LinkedHashSet<Integer>();
            Set<Integer> sellerSID = new LinkedHashSet<Integer>();
            Set<Integer> heirSID = new LinkedHashSet<Integer>();
            boolean exceptionFlag = false;


            // GAME BEGINS
            Block block;
            for(int i = startingNode; i < blockchain.size(); i++) {
                block = blockchain.get(i);

                // Inserting land data of current block
                land = block.getLand();
                PSForLand.setInt(1, blockNumber);
                PSForLand.setString (2, land.getAddress1());
                PSForLand.setString   (3, land.getAddress2());
                float latitude = (float) land.getLatitude();
                PSForLand.setFloat(4, latitude);
                float longitude = (float) land.getLongitude();
                PSForLand.setFloat(5, longitude);
                PSForLand.setString (6, land.getTown());
                PSForLand.setString (7, land.getCity());
                PSForLand.setString (8, land.getCountry());

                // inserting land data
                try {
                    PSForLand.execute();
                }

                // Land data is already present in database
                catch (SQLIntegrityConstraintViolationException e){
                }


                // inserting Government official data
                PSForGovOfficial.setString(1, block.getCNICOfVerifier());

                try {
                    PSForGovOfficial.execute();
                }

                // ignore - data is already present in database
                catch (SQLIntegrityConstraintViolationException e){
                }

                // Casting each block according to its type
                // OPBLock
                if (block.getType().equals("OPBlock")) {
                    opBlock = (OPBlock) block;

                    // Generate a unique SID after addition of each buyer, seller, heir
                    buyersP = opBlock.getBuyers();
                    sellersO = opBlock.getSellers();
                    heirs = opBlock.getHeirs();

                    CNICOfVerifier = opBlock.getCNICOfVerifier();
                    marketValue = (int) opBlock.getMarketValue();
                    mortgageFlag = opBlock.isMortgageFlag();
                    typeOfBlock = "opblock";

                    for(Person buyer: buyersP)
                    {
                        try {
                            PSForPerson.setString(1, buyer.getCNIC());
                            PSForPerson.setString(2, buyer.getFirstName());
                            PSForPerson.setString(3, buyer.getLastName());

                            // Converting java.util.date to mysql date
                            java.sql.Date DOB = new java.sql.Date(buyer.getDOB().getTime());
                            PSForPerson.setDate(4, DOB);
                            PSForPerson.setInt(5, SID);
                            // Person is also someone
                            PSForSomeone.setInt(1, SID);
                            buyerSID.add(SID);
                            SID++;
                            PSForPerson.execute();
                            PSForSomeone.execute();
                        }
                        catch (SQLIntegrityConstraintViolationException e){
                            --SID;
                        }
                    }
                    for(int sid: buyerSID)
                    {
                        PSForBuyer.setString(1, opBlock.getHash());
                        PSForBuyer.setTimestamp(2, opBlock.getTimestamp());
                        PSForBuyer.setInt(3, sid);
                        PSForBuyer.execute();
                    }
                    try {
                        for (Organization seller : sellersO) {
                            try {
                                PSForOrganization.setString(1, seller.getName());
                                PSForOrganization.setString(2, seller.getStreet());
                                PSForOrganization.setString(3, seller.gettown());
                                PSForOrganization.setString(4, seller.getCity());
                                PSForOrganization.setString(5, seller.getCountry());
                                PSForOrganization.setInt(6, SID);

                                // Organization is someone also
                                PSForSomeone.setInt(1, SID);
                                sellerSID.add(SID);
                                SID++;
                                PSForOrganization.execute();
                                PSForSomeone.execute();
                            } catch (SQLIntegrityConstraintViolationException e) {
                                --SID;
                            } catch (NullPointerException e) {
                                break;
                            }
                        }
                    }
                    catch (NullPointerException e)
                    {
                    }
                    for(int sid: sellerSID)
                    {
                        PSForSeller.setString(1, opBlock.getHash());
                        PSForSeller.setTimestamp(2, opBlock.getTimestamp());
                        PSForSeller.setInt(3, sid);
                        PSForSeller.execute();
                    }
                    try {
                        for (Person heir : heirs) {
                            try {
                                PSForPerson.setString(1, heir.getCNIC());
                                PSForPerson.setString(2, heir.getFirstName());
                                PSForPerson.setString(3, heir.getLastName());

                                // Converting java.util.date to mysql date
                                java.sql.Date DOB = new java.sql.Date(heir.getDOB().getTime());
                                PSForPerson.setDate(4, DOB);
                                PSForPerson.setInt(5, SID);

                                // heir is someone too
                                PSForSomeone.setInt(1, SID);
                                // heir is also someone
                                heirSID.add(SID);
                                SID++;
                                PSForPerson.execute();
                                PSForSomeone.execute();
                            } catch (SQLIntegrityConstraintViolationException e) {
                                --SID;
                            }
                        }
                    }
                    catch (NullPointerException e)
                    {
                    }
                    for(int sid: heirSID)
                    {
                        PSForHeir.setString(1, opBlock.getHash());
                        PSForHeir.setTimestamp(2, opBlock.getTimestamp());
                        PSForHeir.setInt(3, sid);
                        PSForHeir.execute();
                    }
                }

                // POBlock
                else if (block.getType().equals("POBlock")) {
                    poBlock = (POBlock) block;

                    // Generate a unique SID after addition of each buyer, seller, heir
                    buyersO = poBlock.getBuyers();
                    sellersP = poBlock.getSellers();
                    heirs = poBlock.getHeirs();

                    CNICOfVerifier = poBlock.getCNICOfVerifier();
                    marketValue = (int) poBlock.getMarketValue();
                    mortgageFlag = poBlock.isMortgageFlag();
                    typeOfBlock = "poblock";

                    for(Organization buyer: buyersO)
                    {
                        try {
                            PSForOrganization.setString(1, buyer.getName());
                            PSForOrganization.setString(2, buyer.getStreet());
                            PSForOrganization.setString(3, buyer.gettown());
                            PSForOrganization.setString(4, buyer.getCity());
                            PSForOrganization.setString(5, buyer.getCountry());
                            PSForOrganization.setInt(6, SID);
                            PSForSomeone.setInt(1, SID);
                            buyerSID.add(SID);
                            SID++;
                            PSForOrganization.execute();
                            PSForSomeone.execute();
                        }
                        catch (SQLIntegrityConstraintViolationException e){
                            --SID;
                        }
                    }
                    for(int sid: buyerSID)
                    {
                        PSForBuyer.setString(1, poBlock.getHash());
                        PSForBuyer.setTimestamp(2, poBlock.getTimestamp());
                        PSForBuyer.setInt(3, sid);
                        PSForBuyer.execute();
                    }
                    try {
                        for (Person seller : sellersP) {
                            try {
                                PSForPerson.setString(1, seller.getCNIC());
                                PSForPerson.setString(2, seller.getFirstName());
                                PSForPerson.setString(3, seller.getLastName());

                                // Converting java.util.date to mysql date
                                java.sql.Date DOB = new java.sql.Date(seller.getDOB().getTime());
                                PSForPerson.setDate(4, DOB);
                                PSForPerson.setInt(5, SID);
                                PSForSomeone.setInt(1, SID);
                                sellerSID.add(SID);
                                SID++;
                                PSForOrganization.execute();
                                PSForSomeone.execute();
                            } catch (SQLIntegrityConstraintViolationException e) {
                                --SID;
                            } catch (NullPointerException e) {
                                break;
                            }
                        }
                    }
                    catch (NullPointerException e)
                    {
                    }
                    for(int sid: sellerSID)
                    {
                        PSForSeller.setString(1, poBlock.getHash());
                        PSForSeller.setTimestamp(2, poBlock.getTimestamp());
                        PSForSeller.setInt(3, sid);
                        PSForSeller.execute();
                    }
                    try {
                        for (Person heir : heirs) {
                            try {
                                PSForPerson.setString(1, heir.getCNIC());
                                PSForPerson.setString(2, heir.getFirstName());
                                PSForPerson.setString(3, heir.getLastName());

                                // Converting java.util.date to mysql date
                                java.sql.Date DOB = new java.sql.Date(heir.getDOB().getTime());
                                PSForPerson.setDate(4, DOB);
                                PSForPerson.setInt(5, SID);
                                PSForSomeone.setInt(1, SID);
                                heirSID.add(SID);
                                SID++;
                                PSForPerson.execute();
                                PSForSomeone.execute();
                            } catch (SQLIntegrityConstraintViolationException e) {
                                --SID;
                            }
                        }
                    }
                    catch (NullPointerException e)
                    {
                    }
                    for(int sid: heirSID)
                    {
                        PSForHeir.setString(1, poBlock.getHash());
                        PSForHeir.setTimestamp(2, poBlock.getTimestamp());
                        PSForHeir.setInt(3, sid);
                        PSForHeir.execute();
                    }
                }

                // OOBlock
                else if (block.getType().equals("OOBlock")) {
                    ooBlock = (OOBlock) block;

                    // Generate a unique SID after addition of each buyer, seller, heir
                    buyersO = ooBlock.getBuyers();
                    sellersO = ooBlock.getSellers();
                    heirs = ooBlock.getHeirs();

                    CNICOfVerifier = ooBlock.getCNICOfVerifier();
                    marketValue = (int) ooBlock.getMarketValue();
                    mortgageFlag = ooBlock.isMortgageFlag();
                    typeOfBlock = "ooBlock";

                    for(Organization buyer: buyersO)
                    {
                        try {
                            PSForOrganization.setString(1, buyer.getName());
                            PSForOrganization.setString(2, buyer.getStreet());
                            PSForOrganization.setString(3, buyer.gettown());
                            PSForOrganization.setString(4, buyer.getCity());
                            PSForOrganization.setString(5, buyer.getCountry());
                            PSForOrganization.setInt(6, SID);
                            PSForSomeone.setInt(1, SID);
                            buyerSID.add(SID);
                            SID++;
                            PSForOrganization.execute();
                            PSForSomeone.execute();
                        }
                        catch (SQLIntegrityConstraintViolationException e){
                            --SID;
                        }
                    }
                    for(int sid: buyerSID)
                    {
                        PSForBuyer.setString(1, ooBlock.getHash());
                        PSForBuyer.setTimestamp(2, ooBlock.getTimestamp());
                        PSForBuyer.setInt(3, sid);
                        PSForBuyer.execute();
                    }
                    try {
                        for (Organization seller : sellersO) {
                            try {
                                PSForOrganization.setString(1, seller.getName());
                                PSForOrganization.setString(2, seller.getStreet());
                                PSForOrganization.setString(3, seller.gettown());
                                PSForOrganization.setString(4, seller.getCity());
                                PSForOrganization.setString(5, seller.getCountry());
                                PSForOrganization.setInt(6, SID);
                                PSForSomeone.setInt(1, SID);
                                sellerSID.add(SID);
                                SID++;
                                PSForSomeone.execute();
                                PSForOrganization.execute();
                            } catch (SQLIntegrityConstraintViolationException e) {
                                --SID;
                            } catch (NullPointerException e) {
                                break;
                            }
                        }
                    }
                    catch (NullPointerException e)
                    {
                    }
                    for(int sid: sellerSID)
                    {
                        PSForSeller.setString(1, ooBlock.getHash());
                        PSForSeller.setTimestamp(2, ooBlock.getTimestamp());
                        PSForSeller.setInt(3, sid);
                        PSForSeller.execute();
                    }
                    try {
                        for (Person heir : heirs) {
                            try {
                                PSForPerson.setString(1, heir.getCNIC());
                                PSForPerson.setString(2, heir.getFirstName());
                                PSForPerson.setString(3, heir.getLastName());

                                // Converting java.util.date to mysql date
                                java.sql.Date DOB = new java.sql.Date(heir.getDOB().getTime());
                                PSForPerson.setDate(4, DOB);
                                PSForPerson.setInt(5, SID);
                                PSForSomeone.setInt(1, SID);
                                heirSID.add(SID);
                                SID++;
                                PSForPerson.execute();
                                PSForSomeone.execute();
                            } catch (SQLIntegrityConstraintViolationException e) {
                                --SID;
                            }
                        }
                    }
                    catch (NullPointerException e)
                    {
                    }
                    for(int sid: heirSID)
                    {
                        PSForHeir.setString(1, ooBlock.getHash());
                        PSForHeir.setTimestamp(2, ooBlock.getTimestamp());
                        PSForHeir.setInt(3, sid);
                        PSForHeir.execute();
                    }
                }

                // PPBlock
                else if (block.getType().equals("PPBlock")) {
                    ppBlock = (PPBlock) block;

                    // Generate a unique SID after addition of each buyer, seller, heir
                    buyersP = ppBlock.getBuyers();
                    sellersP = ppBlock.getSellers();
                    heirs = ppBlock.getHeirs();

                    CNICOfVerifier = ppBlock.getCNICOfVerifier();
                    marketValue = (int) ppBlock.getMarketValue();
                    mortgageFlag = ppBlock.isMortgageFlag();
                    typeOfBlock = "ppBlock";

                    for(Person buyer: buyersP)
                    {
                        try {
                            PSForPerson.setString(1, buyer.getCNIC());
                            PSForPerson.setString(2, buyer.getFirstName());
                            PSForPerson.setString(3, buyer.getLastName());

                            // Converting java.util.date to mysql date
                            java.sql.Date DOB = new java.sql.Date(buyer.getDOB().getTime());
                            PSForPerson.setDate(4, DOB);
                            PSForPerson.setInt(5, SID);
                            PSForSomeone.setInt(1, SID);
                            buyerSID.add(SID);
                            SID++;
                            PSForPerson.execute();
                            PSForSomeone.execute();
                        }
                        catch (SQLIntegrityConstraintViolationException e){
                            --SID;
                        }
                    }
                    for(int sid: buyerSID)
                    {
                        PSForBuyer.setString(1, ppBlock.getHash());
                        PSForBuyer.setTimestamp(2, ppBlock.getTimestamp());
                        PSForBuyer.setInt(3, sid);
                        PSForBuyer.execute();
                    }
                    try {
                        for (Person seller : sellersP) {
                            try {
                                PSForPerson.setString(1, seller.getCNIC());
                                PSForPerson.setString(2, seller.getFirstName());
                                PSForPerson.setString(3, seller.getLastName());

                                // Converting java.util.date to mysql date
                                java.sql.Date DOB = new java.sql.Date(seller.getDOB().getTime());
                                PSForPerson.setDate(4, DOB);
                                PSForPerson.setInt(5, SID);
                                PSForSomeone.setInt(1, SID);
                                sellerSID.add(SID);
                                SID++;
                                PSForPerson.execute();
                                PSForSomeone.execute();
                            } catch (SQLIntegrityConstraintViolationException e) {
                                --SID;
                            } catch (NullPointerException e) {
                                break;
                            }
                        }
                    }
                    catch (NullPointerException e)
                    {
                    }
                    for(int sid: sellerSID)
                    {
                        PSForSeller.setString(1, ppBlock.getHash());
                        PSForSeller.setTimestamp(2, ppBlock.getTimestamp());
                        PSForSeller.setInt(3, sid);
                        PSForSeller.execute();
                    }
                    try {
                        for (Person heir : heirs) {
                            try {
                                PSForPerson.setString(1, heir.getCNIC());
                                PSForPerson.setString(2, heir.getFirstName());
                                PSForPerson.setString(3, heir.getLastName());

                                // Converting java.util.date to mysql date
                                java.sql.Date DOB = new java.sql.Date(heir.getDOB().getTime());
                                PSForPerson.setDate(4, DOB);
                                PSForPerson.setInt(5, SID);
                                PSForSomeone.setInt(1, SID);
                                heirSID.add(SID);
                                SID++;
                                PSForPerson.execute();
                                PSForSomeone.execute();
                            } catch (SQLIntegrityConstraintViolationException e) {
                                --SID;
                            }
                        }
                    }
                    catch (NullPointerException e)
                    {
                    }
                    for(int sid: heirSID)
                    {
                        PSForHeir.setString(1, ppBlock.getHash());
                        PSForHeir.setTimestamp(2, ppBlock.getTimestamp());
                        PSForHeir.setInt(3, sid);
                        PSForHeir.execute();
                    }
                }

                // Inserting transaction data
                PSForTransaction.setTimestamp(1, block.getTimestamp());
                PSForTransaction.setString(2, block.getHash());
                PSForTransaction.setString(3, block.getPreviousHash());
                PSForTransaction.setLong(4, block.getMarketValue());
                int mortgageInt = block.isMortgageFlag() ? 1 : 0;
                PSForTransaction.setInt(5, mortgageInt);
                PSForTransaction.setString(6, block.getCNICOfVerifier());
                PSForTransaction.setInt(7, blockNumber);
                PSForTransaction.setString(8, block.getType().toLowerCase());

                if(land.getType().toLowerCase().equals("agricultural")) {
                    PSForAgriculatural.setInt(1, blockNumber);
                    PSForAgriculatural.execute();
                }
                else if(land.getType().toLowerCase().equals("commercial")){
                    PSForForCommercial.setInt(1, blockNumber);
                    PSForForCommercial.execute();
                }
                else if(land.getType().toLowerCase().equals("residential")) {
                    PSForResidential.setInt(1, blockNumber);
                    PSForResidential.execute();
                }

                PSForTransaction.execute();


                // Clear temporary lists after each iteration
                try {
                    sellersO.clear();
                }
                catch(NullPointerException e)
                {}

                try {
                    sellersP.clear();
                }
                catch(NullPointerException e)
                {}


                try {
                    buyersO.clear();
                }
                catch(NullPointerException e)
                {}

                try {
                    buyersP.clear();
                }
                catch(NullPointerException e)
                {}


                try {
                    heirs.clear();
                }
                catch(NullPointerException e)
                {}

                buyerSID.clear();
                sellerSID.clear();
                heirSID.clear();
                blockNumber++;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        int array[] = {blockNumber, SID};
        writeToFile(array);
        System.out.println("Successfully imported blockchain data to database.");
    }

    public static void writeToFile(int data[]) {

        String fileName = "Data\\SoftwareData\\softwareFile.txt";

        try {
            FileWriter fileWriter =
                    new FileWriter(fileName);

            BufferedWriter bufferedWriter =
                    new BufferedWriter(fileWriter);

            String dataToWrite = Integer.toString(data[0]) + " " + Integer.toString(data[1]);
            bufferedWriter.write(dataToWrite);
            bufferedWriter.close();
        }
        catch(IOException e) {
            System.out.println(
                    "Error writing to file '"
                            + fileName + "'");
            e.printStackTrace();
        }
    }

    public static int[] readFromFile() {

        String fileName = "Data\\SoftwareData\\softwareFile.txt";

        int[] readData = new int[2];

        try {
            FileReader fileReader =
                    new FileReader(fileName);
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            String[] data = bufferedReader.readLine().split(" ");
            readData[0] = Integer.parseInt(data[0]);
            readData[1] = Integer.parseInt(data[1]);

            bufferedReader.close();
        }
        catch(FileNotFoundException e) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        }
        catch(IOException e) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
            e.printStackTrace();
        }
    return readData;
    }

}
