/*
Most Useful methods:
    1. Search through ownership files for owners of a land:
        findPersonOwners(Land land)
        findOrganizationOwners(Land land)

    2. For reading all ownerships from file and saving them in arraylist:
         readPersonOwnerships()
         readOrganizationOwnerships()

        After calling any of these, use:
            getOwnerships() - This will return arraylist of ownerships
                Then use getOwners() on objects in arraylist to get owners
                    (Those objects are needed to be casted in Organization or Person - Depending on method used from above)
 */
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OwnershipChain implements Serializable {
    static final long serialVersionUID = 1L;

    private ArrayList<Ownership> ownerships = new ArrayList<Ownership>(100);

    private static String personOwnerhipsFile = "Data\\personOwnerships.csv";
    private static String organizationOwnerhipsFile = "Data\\organizationOwnerships.csv";
    private static BufferedReader br = null;

    // This method is for demonstrating readPersonOwnerships() method
    void displayLandsAndPersons()
    {
        this.readPersonOwnerships();
        ArrayList<Ownership>  ownerships = this.getOwnerships();
        for(Ownership ownership: ownerships)
        {
            System.out.println("Land details:");
            System.out.println(ownership.getLand().toString());
            System.out.println("Owner (Person) Details:");
            ownership.printOwners();
            System.out.println("\n\n");
        }
    }

    // This method is for demonstrating readOrganizationOwnerships() method
    void displayLandsAndOrganizations()
    {
        this.readOrganizationOwnerships();
        ArrayList<Ownership> ownerships = this.getOwnerships();
        for(Ownership ownership: ownerships)
        {
            System.out.println("Land details:");
            System.out.println(ownership.getLand().toString());
            System.out.println("Owner (Organization) Details:");
            ownership.printOwners();
            System.out.println("\n\n");
        }
    }

    /*
    * Each ownership object stores land details and details of its owners.
    * This method returns an arraylist of such objects read from a file.
    *
    * Owners can be of two types:
     * 1. Person
     * 2. Organization
    *
    * BEFORE USING THIS METHOD, USE One of these:
    * 1. readOrganizationOwnerships
    * 2. readPersonOwnerships
    *
    * Depending on method used, owner objects need to be casted into Organization or Person Object
    * ( Owner objects can be found using ownership.getOwners() )
     */

    public ArrayList<Ownership> getOwnerships() {
        return ownerships;
    }

    public void setOwnerships(ArrayList<Ownership> ownerships) {
        this.ownerships = ownerships;
    }

    // This method returns organizations who are owner of given land
    public static ArrayList<Organization> findOrganizationOwners(Land land)
    {
        ArrayList<Organization> organizationOwners = new ArrayList<Organization>(1);
        String line = "";
        String cvsSplitBy = ":";
        Ownership ownership = null; // to import details from file line by line and save them in ownerships arraylist

        try {

            br = new BufferedReader(new FileReader(organizationOwnerhipsFile));
            while ((line = br.readLine()) != null) {

                // using ':' to separate Land, Owners and Heirs (as in file)
                // if multiple owners, then they are separated by comma
                String[] ownershipData = (line.split(cvsSplitBy));
                String[] owners = ownershipData[1].split(",");
                String[] heirs = ownershipData[2].split(",");
                Land tempLand = null;
                Organization organization = null;

                tempLand = getLand(ownershipData[0]);

                // if given land details found in file then save its owners and return them
                if(tempLand.equals(land))
                {
                    try
                    {
                        ownership = new Ownership(land, "organization", owners.length, heirs.length);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    for(int i =0; i < owners.length; i++) {
                        insertOrganizationsOwnersInOwnershipObj(organizationOwners, owners[i]);
                    }

                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return organizationOwners;
    }

    private static void insertOrganizationsOwnersInOwnershipObj(ArrayList<Organization> organizationOwners, String ownershipData) {
        Organization organization;
        organization = getOrganization(ownershipData);
        organizationOwners.add(organization);
    }

    private static Organization getOrganization(String ownershipData) {
        Organization organization;
        String[] ownerDetails = ownershipData.split(";");
        organization = new Organization(ownerDetails[0], ownerDetails[1], ownerDetails[2], ownerDetails[3],
                ownerDetails[4]);
        return organization;
    }

    public static ArrayList<Person> findPersonOwners(Land land)
    {
        ArrayList<Person> personOwners = new ArrayList<Person>(1);
        String line = "";
        String cvsSplitBy = ":";
        Ownership ownership = null; // to import details from file line by line and save them in ownerships arraylist

        try {

            br = new BufferedReader(new FileReader(personOwnerhipsFile));
            while ((line = br.readLine()) != null) {

                // using ':' to separate land, heirs and owners
                // if multiple owners then they are also separated by comma
                String[] ownershipData = (line.split(cvsSplitBy));
                String[] owners = ownershipData[1].split(",");
                String[] heirs = ownershipData[1].split(",");
                Land tempLand = null;
                Person person = null;

                tempLand = getLand(ownershipData[0]);

                // if given land details found in file then save its owners and return them
                if(tempLand.equals(land))
                {
                    try
                    {
                        ownership = new Ownership(land, "person", owners.length, heirs.length);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    for(int i =1; i < ownershipData.length; i++) {
                        insertPersonOwnersInOwnershipObj(personOwners, ownershipData[1]);
                    }

                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return personOwners;
    }

    private static Land getLand(String landData) {
        String[] landDetails = landData.split(";");
        Land tempLand = null;
        try {
            tempLand = new Land(landDetails[0], landDetails[1], landDetails[2], landDetails[3], landDetails[4],
                    landDetails[5], Double.parseDouble(landDetails[6]), Double.parseDouble(landDetails[7]),
                    Double.parseDouble(landDetails[8]));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return tempLand;
    }

    private static void insertPersonOwnersInOwnershipObj(ArrayList<Person> personOwners, String owners) {
        Person person = null;
        String[] ownersData = (owners.split(",")); // all owners data is separated by ','
        for(int i = 0; i < ownersData.length; i++) {
            person = getPerson(ownersData[i]);
            personOwners.add(person);
        }
    }

    private static Person getPerson(String owner) {
        Person person;
        String[] ownerDetails = owner.split(";");
        Date DOB = null; // Date of birth
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            //Parsing date from String
            DOB = dateFormat.parse(ownerDetails[3]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        person = new Person(ownerDetails[0], ownerDetails[1], ownerDetails[2], DOB);
        return person;
    }

    /*
    * This method builds an arraylist (named ownerships) of ownership objects from file in which ownership details of persons are stored.
    * Another method named "displayLandsAndPersons()" can be used to display land and their owner details
     */
    public void readPersonOwnerships()
    {
        String line = "";
        String cvsSplitBy = ":";
        Ownership ownership = null; // to import details from file line by line and save them in ownerships arraylist

        try {

            br = new BufferedReader(new FileReader(personOwnerhipsFile));
            while ((line = br.readLine()) != null) {
                String[] ownershipData = line.split(cvsSplitBy);

                String[] landDetails = ownershipData[0].split(";");
                String[] ownersData = (ownershipData[1].split(",")); // all owners data is separated by ','
                String[] heirsData = (ownershipData[2].split(",")); // all heirs data is separated by ','

                Land land = null;
                Person owner = null;
                Person heir = null;

                for(int i =0; i < ownershipData.length;i++)
                {
                    if(i ==0) // This slot has land details
                    {
                        try {
                            land = new Land(landDetails[0], landDetails[1], landDetails[2], landDetails[3], landDetails[4],
                                    landDetails[5], Double.parseDouble(landDetails[6]), Double.parseDouble(landDetails[7]),
                                    Double.parseDouble(landDetails[8]));
                            ownership = new Ownership(land, "person", ownersData.length, heirsData.length);
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                    else if (i == 1)// this slot has data of all owners
                    {
                        for(int j =0; j < ownersData.length; j++) {
                            owner = getPerson(ownersData[j]);
                            ownership.getOwners().add(owner);
                        }
                    }

                    else if (i == 2) // This slot has data of all heirs
                    {
                        for(int j =0; j < heirsData.length; j++) {
                            heir = getPerson(heirsData[j]);
                            ownership.getHeirs().add(heir);
                        }
                    }
                }
                ownerships.add(ownership); // Collecting ownerships for later operations
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
     * This method builds an arraylist (named ownerships) of ownership objects from file in which ownership details of organizations are stored.
     * Another method named "displayLandsAndOrganizations()" can be used to display land and their owner details
     */
    public void readOrganizationOwnerships()
    {
        String line = "";
        String cvsSplitBy = ":";
        Ownership ownership = null; // to import details from file line by line and save them in ownerships arraylist

        try {

            br = new BufferedReader(new FileReader(organizationOwnerhipsFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] ownershipData = line.split(cvsSplitBy);
                String[] ownerDetails = ownershipData[1].split(",");
                String[] heirDetails = ownershipData[2].split(",");

                Land land = null;
                Organization organization = null;
                Person heir = null;
                for(int i =0; i < ownershipData.length;i++)
                {
                    if(i ==0) // This slot has land details
                    {
                        String[] landDetails = ownershipData[i].split(";");
                        try {
                            land = new Land(landDetails[0], landDetails[1], landDetails[2], landDetails[3], landDetails[4],
                                    landDetails[5], Double.parseDouble(landDetails[6]), Double.parseDouble(landDetails[7]),
                                    Double.parseDouble(landDetails[8]));
                            ownership = new Ownership(land, "organization", ownerDetails.length, heirDetails.length);
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else if (i == 1)// all other slots have owner details
                    {
                        for (int j =0; j < ownerDetails.length; j++) {
                            organization = getOrganization(ownerDetails[j]);

                            ownership.getOwners().add(organization);
                        }
                    }
                    else if (i == 2)
                    {
                        for(int j = 0; j < heirDetails.length; j++)
                        {
                            heir = getPerson(heirDetails[j]);
                            ownership.getHeirs().add(heir);
                        }
                    }
                }
                ownerships.add(ownership); // Collecting ownerships for later operations
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
