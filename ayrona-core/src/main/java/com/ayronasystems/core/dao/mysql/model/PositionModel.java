package com.ayronasystems.core.dao.mysql.model;

import com.ayronasystems.core.definition.Direction;
import com.ayronasystems.core.definition.Symbol;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by gorkemgok on 15/05/16.
 */
@Entity
@Table(name = "positions")
public class PositionModel {

    private String id;

    private Direction direction;

    private Symbol symbol;

    private double actualOpenPrice;

    private double expectedOpenPrice;

    private Date actualOpenDate;

    private Date expectedOpenDate;

    private double actualClosePrice;

    private double expectedClosePrice;

    private Date actualCloseDate;

    private Date expectedCloseDate;

    private double takeProfit;

    private double stopLoss;

    private String description;

    private String initiator;

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public Direction getDirection () {
        return direction;
    }

    public void setDirection (Direction direction) {
        this.direction = direction;
    }

    public Symbol getSymbol () {
        return symbol;
    }

    public void setSymbol (Symbol symbol) {
        this.symbol = symbol;
    }

    public double getActualOpenPrice () {
        return actualOpenPrice;
    }

    public void setActualOpenPrice (double actualOpenPrice) {
        this.actualOpenPrice = actualOpenPrice;
    }

    public double getExpectedOpenPrice () {
        return expectedOpenPrice;
    }

    public void setExpectedOpenPrice (double expectedOpenPrice) {
        this.expectedOpenPrice = expectedOpenPrice;
    }

    public Date getActualOpenDate () {
        return actualOpenDate;
    }

    public void setActualOpenDate (Date actualOpenDate) {
        this.actualOpenDate = actualOpenDate;
    }

    public Date getExpectedOpenDate () {
        return expectedOpenDate;
    }

    public void setExpectedOpenDate (Date expectedOpenDate) {
        this.expectedOpenDate = expectedOpenDate;
    }

    public double getActualClosePrice () {
        return actualClosePrice;
    }

    public void setActualClosePrice (double actualClosePrice) {
        this.actualClosePrice = actualClosePrice;
    }

    public double getExpectedClosePrice () {
        return expectedClosePrice;
    }

    public void setExpectedClosePrice (double expectedClosePrice) {
        this.expectedClosePrice = expectedClosePrice;
    }

    public Date getActualCloseDate () {
        return actualCloseDate;
    }

    public void setActualCloseDate (Date actualCloseDate) {
        this.actualCloseDate = actualCloseDate;
    }

    public Date getExpectedCloseDate () {
        return expectedCloseDate;
    }

    public void setExpectedCloseDate (Date expectedCloseDate) {
        this.expectedCloseDate = expectedCloseDate;
    }

    public double getTakeProfit () {
        return takeProfit;
    }

    public void setTakeProfit (double takeProfit) {
        this.takeProfit = takeProfit;
    }

    public double getStopLoss () {
        return stopLoss;
    }

    public void setStopLoss (double stopLoss) {
        this.stopLoss = stopLoss;
    }

    public String getDescription () {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    public String getInitiator () {
        return initiator;
    }

    public void setInitiator (String initiator) {
        this.initiator = initiator;
    }
}
