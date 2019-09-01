package solversteam.familycab.Models;

public class Country {

    private String ID;
    private String Name;
    private String phonecountryCode;
    private String iso_code;
    private String country_img,currency;
    private String latitude,longitude;

    public Country(String ID,String Name,String latitude,String longitude) {
        this.ID=ID;
        this.Name=Name;
        this.longitude=longitude;
        this.latitude=latitude;
    }

    public Country(String ID,String Name) {
        this.ID=ID;
        this.Name=Name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Country(String ID, String Name, String phonecountryCode, String iso_code, String country_img, String currency) {
        this.ID = ID;
        this.Name = Name;
        this.phonecountryCode = phonecountryCode;
       this. iso_code = iso_code;
        this.country_img=country_img;
        this.currency=currency;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        Name = Name;
    }

    public String getPhonecountryCode() {
        return phonecountryCode;
    }

    public void setPhonecountryCode(String phonecountryCode) {
        this.phonecountryCode = phonecountryCode;
    }

    public String getIso_code() {
        return iso_code;
    }

    public void setIso_code(String iso_code) {
        this.iso_code = iso_code;
    }

    public String getCountry_img() {
        return country_img;
    }

    public void setCountry_img(String country_img) {
        this.country_img = country_img;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
