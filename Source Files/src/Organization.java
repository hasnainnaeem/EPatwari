import java.io.Serializable;
import java.util.Objects;

public class Organization implements Serializable {
    static final long serialVersionUID = 1L;

    private String name;
    private String city;
    private String town;
    private String street;
    private String country;

    public Organization(String name, String city, String town, String street, String country) {
        this.name = name;
        this.city = city;
        this.town = town;
        this.street = street;
        this.country = country;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", town='" + town + '\'' +
                ", Street='" + street + '\'' +
                ", Country='" + country + '\'' +
                '}';
    }

    // To compare organization objects
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(city, that.city) &&
                Objects.equals(town, that.town) &&
                Objects.equals(street, that.street) &&
                Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, city, town, street, country);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String gettown() {
        return town;
    }

    public void settown(String town) {
        this.town = town;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
