package org.qdu.storm.jdbcUtils;

public class Bean {
    private double longitude;
    private double latitude;


    public Bean(double longitude, double latitude) {
        super();
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    @Override
    public String toString() {
        return "Bean[" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ']';
    }
    public Bean() {}
}
