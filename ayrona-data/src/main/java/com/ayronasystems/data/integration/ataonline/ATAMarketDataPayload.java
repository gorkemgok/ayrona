package com.ayronasystems.data.integration.ataonline;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * Created by gorkemgok on 21/06/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ATAMarketDataPayload implements Serializable {

    private static final String DATE_FORMAT = "yyy-MM-dd'T'HH:mm:ss";

    public static final SimpleDateFormat SDF = new SimpleDateFormat (DATE_FORMAT);

    @JsonProperty(value = "Price")
    private double price;

    @JsonProperty(value = "Date")
    private String date;

    @JsonProperty(value = "Name")
    private String symbolCode;

    public double getPrice () {
        return price;
    }

    public void setPrice (double price) {
        this.price = price;
    }

    public String getDate () {
        return date;
    }

    public void setDate (String date) {
        this.date = date;
    }

    public String getSymbolCode () {
        return symbolCode;
    }

    public void setSymbolCode (String symbolCode) {
        this.symbolCode = symbolCode;
    }

    @Override
    public String toString () {
        return "MarketData{" +
                "price=" + price +
                ", date='" + date + '\'' +
                ", symbolCode='" + symbolCode + '\'' +
                '}';
    }
}
