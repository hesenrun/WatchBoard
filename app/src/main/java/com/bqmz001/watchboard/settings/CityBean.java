package com.bqmz001.watchboard.settings;

import org.litepal.crud.LitePalSupport;

public class CityBean extends LitePalSupport {
    private long id;
    private String geoid;
    private String province;
    private String city;
    private String town;
    private String postId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGeoid() {
        return geoid;
    }

    public void setGeoid(String geoid) {
        this.geoid = geoid;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
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

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
