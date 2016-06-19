package com.ayronasystems.core.integration.ataonline;

import com.ayronasystems.core.Order;
import com.ayronasystems.core.definition.Direction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by gorkemgok on 20/06/16.
 */
public class OrderPayload {

    private static Logger log = LoggerFactory.getLogger (OrderPayload.class);

    @JsonProperty(value = "CustomerExtId")
    private String customerExtId;
    @JsonProperty(value = "AccountExtId")
    private int accountExtId;
    @JsonProperty(value = "Password")
    private String password = "";
    @JsonProperty(value = "Contract")
    private String contract = "F_XAUTRYM0616S0";
    @JsonProperty(value = "Price")
    private double price;
    @JsonProperty(value = "Quantity")
    private double lot;
    @JsonProperty(value = "OrderDateDay")
    private int orderDateDay;
    @JsonProperty(value = "OrderDateMonth")
    private int orderDateMonth;
    @JsonProperty(value = "OrderDateYear")
    private int orderDateYear;
    @JsonProperty(value = "ExpireDateDay")
    private int expireDateDay;
    @JsonProperty(value = "ExpireDateMonth")
    private int expireDateMonth;
    @JsonProperty(value = "ExpireDateYear")
    private int expireDateYear;
    @JsonProperty(value = "DateType")
    private String dateType = "DAY"; //DAY->Günlük, VUC->İptal Edilene Kadar Geçerli, DTD->Tarihli, SNS->Seanslık, ATO->Açılış, IOC->Kalanı İptal Et, FOK->Gerçekleşmezse İptal Et, CLS->Kapanış
    @JsonProperty(value = "TransactionType")
    private  String transactionType = "KPY"; //KPY->Kalanı Pasife Yaz, GIE->Gerçekleşmezse İptal Et, KIE->Kalanı İptal Et, SAR->Şarta Bağlı
    @JsonProperty(value = "ConditionalPrice")
    private double conditionalPrice;
    @JsonProperty(value = "PriceType")
    private String priceType = "PYS"; //LMT->Limitli, PYS->Piyasa, EIF->Piyasa-En iyi fiyat
    @JsonProperty(value = "IsLong")
    private boolean isLong;
    @JsonProperty(value = "Username")
    private String userName;

    public Optional<String> toJson(){
        ObjectMapper objectMapper = new ObjectMapper ();
        try {
            return Optional.of (objectMapper.writeValueAsString (this));
        } catch ( JsonProcessingException e ) {
            log.error ("OrderPayload can not be converted to json.", e);
        }
        return Optional.absent ();
    }

    public static OrderPayload createInstance (Order.Type order, Direction direction, Date date, double price, String accountNo, double lot){
        OrderPayload orderPayload = new OrderPayload ();
        orderPayload.customerExtId = accountNo;
        orderPayload.lot = lot;
        orderPayload.price = price;
        Calendar cal = Calendar.getInstance ();
        cal.setTime (date);
        orderPayload.orderDateDay = cal.get (Calendar.DAY_OF_MONTH);
        orderPayload.orderDateMonth = cal.get (Calendar.MONTH);
        orderPayload.orderDateYear = cal.get (Calendar.YEAR);
        orderPayload.isLong = order.equals (Order.Type.OPEN) ?
                direction.equals (Direction.LONG) : !direction.equals (Direction.LONG);
        return orderPayload;
    }

    public String getCustomerExtId () {
        return customerExtId;
    }

    public void setCustomerExtId (String customerExtId) {
        this.customerExtId = customerExtId;
    }

    public int getAccountExtId () {
        return accountExtId;
    }

    public void setAccountExtId (int accountExtId) {
        this.accountExtId = accountExtId;
    }

    public String getPassword () {
        return password;
    }

    public void setPassword (String password) {
        this.password = password;
    }

    public String getContract () {
        return contract;
    }

    public void setContract (String contract) {
        this.contract = contract;
    }

    public double getPrice () {
        return price;
    }

    public void setPrice (double price) {
        this.price = price;
    }

    public double getLot () {
        return lot;
    }

    public void setLot (double lot) {
        this.lot = lot;
    }

    public int getOrderDateDay () {
        return orderDateDay;
    }

    public void setOrderDateDay (int orderDateDay) {
        this.orderDateDay = orderDateDay;
    }

    public int getOrderDateMonth () {
        return orderDateMonth;
    }

    public void setOrderDateMonth (int orderDateMonth) {
        this.orderDateMonth = orderDateMonth;
    }

    public int getOrderDateYear () {
        return orderDateYear;
    }

    public void setOrderDateYear (int orderDateYear) {
        this.orderDateYear = orderDateYear;
    }

    public int getExpireDateDay () {
        return expireDateDay;
    }

    public void setExpireDateDay (int expireDateDay) {
        this.expireDateDay = expireDateDay;
    }

    public int getExpireDateMonth () {
        return expireDateMonth;
    }

    public void setExpireDateMonth (int expireDateMonth) {
        this.expireDateMonth = expireDateMonth;
    }

    public int getExpireDateYear () {
        return expireDateYear;
    }

    public void setExpireDateYear (int expireDateYear) {
        this.expireDateYear = expireDateYear;
    }

    public String getDateType () {
        return dateType;
    }

    public void setDateType (String dateType) {
        this.dateType = dateType;
    }

    public String getTransactionType () {
        return transactionType;
    }

    public void setTransactionType (String transactionType) {
        this.transactionType = transactionType;
    }

    public double getConditionalPrice () {
        return conditionalPrice;
    }

    public void setConditionalPrice (double conditionalPrice) {
        this.conditionalPrice = conditionalPrice;
    }

    public String getPriceType () {
        return priceType;
    }

    public void setPriceType (String priceType) {
        this.priceType = priceType;
    }

    @JsonIgnore
    public boolean isLong () {
        return isLong;
    }

    public void setLong (boolean aLong) {
        isLong = aLong;
    }

    public String getUserName () {
        return userName;
    }

    public void setUserName (String userName) {
        this.userName = userName;
    }
}
