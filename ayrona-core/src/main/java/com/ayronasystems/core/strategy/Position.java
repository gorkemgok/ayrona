package com.ayronasystems.core.strategy;

import com.ayronasystems.core.definition.Direction;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.strategy.Initiator;
import com.ayronasystems.core.util.DateUtils;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * Created by gorkemgok on 12/05/16.
 */
public class Position {

    public static class Builder{

        private Position position;

        public Builder (Initiator initiator) {
            position = new Position ();
            position.initiator = initiator;
        }

        public Builder direction(Direction direction){
            position.direction = direction;
            return this;
        }

        public Builder symbol(Symbol symbol){
            position.symbol = symbol;
            return this;
        }

        public Builder lot(double lot){
            position.lot = lot;
            return this;
        }

        public Builder openDate(Date date){
            position.openDate = date;
            position.idealOpenDate = date;
            return this;
        }

        public Builder openPrice(double price){
            position.openPrice = price;
            position.idealOpenPrice = price;
            return this;
        }

        public Builder description(String description){
            position.description = description;
            return this;
        }

        public Builder takeProfit(double price){
            position.takeProfit = price;
            return this;
        }

        public Builder stopLoss(double price){
            position.stopLoss = price;
            return this;
        }

        public Builder accountName(String accountName){
            position.accountName = accountName;
            return this;
        }

        public Position build(){
            return position;
        }

    }

    private String id;

    private String remoteId;

    private Direction direction;

    private Symbol symbol;

    private double lot;

    private double takeProfit;

    private double stopLoss;

    private double openPrice;

    private double closePrice;

    private Date openDate;

    private Date closeDate;

    private double idealOpenPrice;

    private double idealClosePrice;

    private Date idealOpenDate;

    private Date idealCloseDate;

    private String description;

    private Initiator initiator;

    private String accountName;

    private Position () {
    }

    public String getAccountName () {
        return accountName;
    }

    public String getRemoteId () {
        return remoteId;
    }

    public void setRemoteId (String remoteId) {
        this.remoteId = remoteId;
    }

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public double calculateProfit(){
        return direction == Direction.LONG ? closePrice - openPrice : openPrice - closePrice;
    }

    public double getIdealOpenPrice () {
        return idealOpenPrice;
    }

    public double getIdealClosePrice () {
        return idealClosePrice;
    }

    public Date getIdealOpenDate () {
        return idealOpenDate;
    }

    public Date getIdealCloseDate () {
        return idealCloseDate;
    }

    public void setOpenPrice (double openPrice) {
        this.openPrice = openPrice;
    }

    public void setClosePrice (double closePrice) {
        this.closePrice = closePrice;
    }

    public void setOpenDate (Date openDate) {
        this.openDate = openDate;
    }

    public void setCloseDate (Date closeDate) {
        this.closeDate = closeDate;
    }

    public static Builder builder(Initiator initiator){
        return new Builder (initiator);
    }

    public Direction getDirection () {
        return direction;
    }

    public Initiator getInitiator () {
        return initiator;
    }

    public Symbol getSymbol () {
        return symbol;
    }

    public double getLot () {
        return lot;
    }

    public double getTakeProfit () {
        return takeProfit;
    }

    public double getStopLoss () {
        return stopLoss;
    }

    public double getOpenPrice () {
        return openPrice;
    }

    public double getClosePrice () {
        return closePrice;
    }

    public String getDescription () {
        return description;
    }

    public Date getOpenDate () {
        return openDate;
    }

    public Date getCloseDate () {
        return closeDate;
    }

    public void close(Date closeDate, double closePrice){
        this.closeDate = closeDate;
        this.closePrice = closePrice;

        if (idealCloseDate == null) {
            this.idealCloseDate = closeDate;
            this.idealClosePrice = closePrice;
        }
    }

    public boolean isClosed(){
        return closeDate != null;
    }

    public double getProfit(){
        if (isClosed ()) {
            return (direction == Direction.SHORT ? -100 : 100) * (closePrice - openPrice) / openPrice;
        }else{
            return 0;
        }
    }

    @Override
    public String toString () {
        DecimalFormat df = new DecimalFormat ("#.##");
        return "Position{" +
                "symbol=" + symbol +
                " " + direction + " " +
                ", profit=\t"+df.format (getProfit ())+"%"+
                ", lot=" + lot +
                ", takeProfit=" + takeProfit +
                ", stopLoss=" + stopLoss +
                ", openPrice=" + openPrice +
                ", closePrice=" + (isClosed () ? closePrice : "OPEN") +
                ", openDate=" + DateUtils.formatDate (idealOpenDate) +
                ", closeDate=" + (isClosed () ? DateUtils.formatDate (closeDate): "OPEN") +
                ", description='" + description + '\'' +
                ", initiator=" + initiator +
                '}';
    }

    public boolean isSame (Position position) {

        if ( Double.compare (position.lot, lot) != 0 ) {
            return false;
        }
        if ( Double.compare (position.takeProfit, takeProfit) != 0 ) {
            return false;
        }
        if ( Double.compare (position.stopLoss, stopLoss) != 0 ) {
            return false;
        }
        if ( Double.compare (position.openPrice, openPrice) != 0 ) {
            return false;
        }
        if ( Double.compare (position.closePrice, closePrice) != 0 ) {
            return false;
        }
        if ( Double.compare (position.idealOpenPrice, idealOpenPrice) != 0 ) {
            return false;
        }
        if ( Double.compare (position.idealClosePrice, idealClosePrice) != 0 ) {
            return false;
        }
        if ( remoteId != null ? !remoteId.equals (position.remoteId) : position.remoteId != null ) {
            return false;
        }
        if ( direction != position.direction ) {
            return false;
        }
        if ( symbol != position.symbol ) {
            return false;
        }
        if ( openDate != null ? !openDate.equals (position.openDate) : position.openDate != null ) {
            return false;
        }
        if ( closeDate != null ? !closeDate.equals (position.closeDate) : position.closeDate != null ) {
            return false;
        }
        if ( idealOpenDate != null ? !idealOpenDate.equals (position.idealOpenDate) : position.idealOpenDate != null ) {
            return false;
        }
        if ( idealCloseDate != null ? !idealCloseDate.equals (
                position.idealCloseDate) : position.idealCloseDate != null ) {
            return false;
        }
        if ( description != null ? !description.equals (position.description) : position.description != null ) {
            return false;
        }
        return true;

    }
}
