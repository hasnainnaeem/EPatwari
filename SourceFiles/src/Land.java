import java.io.Serializable;
import java.util.LinkedList;
import java.util.Objects;

public class Land implements Serializable {
    static final long serialVersionUID = 1L;

    private String address1;
    private String address2;
    private String city;
    private String town;
    private String type;
    private String country;
    private double latitude;
    private double longitude;
    private double area;
    public Land(String address1, String address2, String city, String town, String country, String type,
                    double latitude, double longitude, double area) throws Exception
    {
        type = type.toLowerCase();
        if (type.equals("commercial") || type.equals("residential") || type.equals("agricultural"))
            this.type = type;
        else
        {
            throw new Exception("Land type is not correct. It must be commercial, residential or agricultural");
        }
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.town = town;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.area = area;
    }

    public static void printLands(LinkedList<Land> lands){
        System.out.println("\n\t\t\t_____________________________________\n");
        for(Land land: lands)
        {
            System.out.println(land.toString());
            System.out.println("\n\t\t\t_____________________________________\n");
        }
    }

    @Override
    public String toString() {
        return "Land{" +
                "address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", city='" + city + '\'' +
                ", town='" + town + '\'' +
                ", type='" + type + '\'' +
                ", country='" + country + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", area=" + area +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Land land = (Land) o;
        return Double.compare(land.latitude, latitude) == 0 &&
                Double.compare(land.longitude, longitude) == 0 &&
                Double.compare(land.area, area) == 0 &&
                address1.equals(land.address1) &&
                Objects.equals(address2, land.address2) &&
                city.equals(land.city) &&
                town.equals(land.town) &&
                type.equals(land.type) &&
                country.equals(land.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address1, address2, city, town, type, country, latitude, longitude, area);
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
