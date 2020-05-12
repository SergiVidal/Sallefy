package vidal.sergi.sallefyv1.model;

import com.google.gson.annotations.SerializedName;

public class CurrentLoc {
    @SerializedName("latitude")
    private Double latitude;

    @SerializedName("longitude")
    private Double longitude;


    public CurrentLoc() {
    }

    public CurrentLoc(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
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

    @Override
    public String toString() {
        return "CurrentLoc{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
