package com.ayronasystems.core;

import com.ayronasystems.core.definition.Direction;
import com.ayronasystems.core.definition.Symbol;
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
            return this;
        }

        public Builder openPrice(double price){
            position.openPrice = price;
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

        public Position build(){
            return position;
        }

    }

    private String id;

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

    private Position () {
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

    public void setIdealOpenPrice (double idealOpenPrice) {
        this.idealOpenPrice = idealOpenPrice;
    }

    public double getIdealClosePrice () {
        return idealClosePrice;
    }

    public void setIdealClosePrice (double idealClosePrice) {
        this.idealClosePrice = idealClosePrice;
    }

    public Date getIdealOpenDate () {
        return idealOpenDate;
    }

    public void setIdealOpenDate (Date idealOpenDate) {
        this.idealOpenDate = idealOpenDate;
    }

    public Date getIdealCloseDate () {
        return idealCloseDate;
    }

    public void setIdealCloseDate (Date idealCloseDate) {
        this.idealCloseDate = idealCloseDate;
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
                ", openDate=" + DateUtils.formatDate (openDate) +
                ", closeDate=" + (isClosed () ? DateUtils.formatDate (closeDate): "OPEN") +
                ", description='" + description + '\'' +
                ", initiator=" + initiator +
                '}';
    }
}
