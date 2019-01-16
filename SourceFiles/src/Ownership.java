import java.io.Serializable;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Ownership implements Serializable {
    static final long serialVersionUID = 1L;

    Scanner scanner;
    private String type;
    private Land land;
    private int numOfOwners;
    private int numOfHeirs;
    private ArrayList<Object> owners;
    private ArrayList<Person> heirs;

    public Ownership(Land land, String type, int numOfOwners, int numOfHeirs) throws Exception {
        type = type.toLowerCase(); // person or organization

        if(type.equals("person"))
        {
            this.type = type;
        }
        else if (type.equals("organization"))
        {
            this.type = type;
        }
        else
            throw new Exception("Wrong owner type. It can be person or organization only.");

        this.land = land;
        if(numOfOwners <= 0)
            throw new Exception("Number of owners of a land can not be less than 1.");
        if(numOfOwners <= -1)
            throw new Exception("Number of heirs of a land can not be less than 1.");

        this.numOfOwners = numOfOwners;
        this.numOfHeirs = numOfHeirs;
        scanner = new Scanner(System.in);
        owners = new ArrayList<Object>();
        heirs = new ArrayList<Person>();
    }

    void test()
    {
        try
        {
            Land land1 = new Land("21 A", null, "sahiwal", "farid town", "Pakistan",
                    "commercial", 55.55, 88.88, 555);
            Ownership ownership1 = new Ownership(land1, "Organization", 3, 0);
            ownership1.initPersonOwners();
            ownership1.initHeirs();
            ownership1.printOwners();
            System.out.println(ownership1.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private Person inputPersonOwner()
    {
        System.out.print("\tEnter CNIC: ");
        String CNIC = scanner.next();
        System.out.print("\tEnter First Name: ");
        String firstName = scanner.next();
        System.out.print("\tEnter Last Name: ");
        String lastName = scanner.next();
        System.out.print("\tEnter Date of Birth: ");
        String DOBStr = scanner.next();
        Date DOB = null; // Date of birth
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            //Parsing the String
            DOB = dateFormat.parse(DOBStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Person(CNIC, firstName, lastName, DOB);
    }

    private Person inputHeir()
    {
        return inputPersonOwner();
    }

    private Organization inputOrganizationOwner()
    {
        System.out.print("\tEnter Organization Name: ");
        String organizationName = scanner.nextLine();
        System.out.print("\tEnter Organization City: ");
        String city = scanner.next();
        scanner.nextLine(); // discards enter character
        System.out.print("\tEnter Organization Town: ");
        String town = scanner.nextLine();
        System.out.print("\tEnter Organization Street: ");
        String street = scanner.nextLine();
        System.out.print("\tEnter Organization Country: ");
        String country = scanner.next();
        scanner.nextLine(); // discards enter character

        return new Organization(organizationName, city, town, street, country);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void initPersonOwners() throws Exception
    {
        switch(type)
        {
            case "person": {
                for (int i = 0; i < numOfOwners; i++)
                {
                    System.out.println("Enter Details of Owner Person # " + i + ": ");
                    owners.add(inputPersonOwner());
                }
            }
            break;
            case "organization": {
                for(int i = 0; i < numOfOwners; i++)
                {
                    System.out.println("Enter Details of Owner Organization # " + i + ": ");
                    owners.add(inputOrganizationOwner());
                }
            }
            break;
        }
    }

    public void initHeirs()
    {
        for (int i = 0; i < numOfOwners; i++)
        {
            System.out.println("Enter Details of Heir Person # " + i + ": ");
            heirs.add(inputHeir());
        }
    }

    void printOwners()
    {
        Person person;
        Organization organization;
        for(int i = 0; i < numOfOwners; i++)
        {
            switch(type) {
                case "person":
                {
                    person = (Person) owners.get(i);
                    System.out.println(person.getFirstName());
                }
                break;
                case "organization":
                {
                    organization = (Organization) owners.get(i);
                    System.out.println(organization.getName());
                }
            }
        }
    }

    void printHeirs()
    {
        for(Person heir: heirs)
        {
            System.out.println(heir.toString());
        }
    }

    public ArrayList<Object> getOwners() {
        return owners;
    }

    public void setOwners(ArrayList<Object> owners) {
        this.owners = owners;
    }

    public Land getLand() {
        return land;
    }

    public void setLand(Land land) {
        this.land = land;
    }

    public ArrayList<Person> getHeirs() {
        return heirs;
    }

    public void setHeirs(ArrayList<Person> heirs) {
        this.heirs = heirs;
    }

    @Override
    public String toString() {
        return "Ownership{"+
                " type='" + type + '\'' +
                ", land= { " + land.toString() + " }" +
                ", numOfOwners=" + numOfOwners +
                '}';
    }
}
