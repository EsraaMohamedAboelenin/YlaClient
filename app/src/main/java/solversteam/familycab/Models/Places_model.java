package solversteam.familycab.Models;

/**
 * Created by mosta on 9/17/2017.
 */

public class Places_model {
    private Double latitude,longitude;
    private String name;
    private int recoruce;

    public Places_model(Double latitude, Double longitude, String name, int recoruce) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.recoruce = recoruce;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRecoruce() {
        return recoruce;
    }

    public void setRecoruce(int recoruce) {
        this.recoruce = recoruce;
    }
}
