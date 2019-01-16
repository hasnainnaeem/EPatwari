import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Person implements Serializable {
    static final long serialVersionUID = 1L;

    private String CNIC;
    private String firstName;
    private String lastName;
    private Date DOB;

    public Person(String CNIC, String firstName, String lastName, Date DOB) {
        this.CNIC = CNIC;
        this.firstName = firstName;
        this.lastName = lastName;
        this.DOB = DOB;
    }

    public String getCNIC() {
        return CNIC;
    }

    public void setCNIC(String CNIC) {
        this.CNIC = CNIC;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDOB() {
        return DOB;
    }

    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }

    @Override
    public String toString() {
        return "Person{" +
                "CNIC=" + CNIC +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", DOB=" + DOB +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return CNIC.equals(person.CNIC);
    }

    @Override
    public int hashCode() {
        return Objects.hash(CNIC, firstName, lastName, DOB);
    }
}
