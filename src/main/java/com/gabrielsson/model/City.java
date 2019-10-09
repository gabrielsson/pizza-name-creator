package com.gabrielsson.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"city_ascii","country","iso2","iso3","id"})
public class City {
    /*
      "city": "Brindisi",
  "city_ascii": "Brindisi",
  "lat": "40.6403",
  "lng": "17.9300",
  "country": "Italy",
  "iso2": "IT",
  "iso3": "ITA",
  "admin_name": "Puglia",
  "capital": "minor",
  "population": "104437",
  "id": "1380201254"
     */
    private String city;
    private String admin_name;
    private String capital;
    private int population;
    private double lat;
    private double lng;

    public String getCity() {
        return city;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public String getCapital() {
        return capital;
    }

    public int getPopulation() {
        return population;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
