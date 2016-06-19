package com.ayronasystems.core.dao.model;

import com.ayronasystems.core.BasicInitiator;
import com.ayronasystems.core.Position;
import com.ayronasystems.core.account.Account;
import com.ayronasystems.core.definition.Direction;
import com.ayronasystems.core.definition.Symbol;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.utils.IndexDirection;

import java.util.Date;

/**
 * Created by gorkemgok on 18/06/16.
 */
@Entity("position")
public class PositionModel extends BaseModel{

    @Indexed(value = IndexDirection.DESC, name = "remoteId")
    private String remoteId;

    @Indexed(name = "accountId")
    private String accountId;

    private String accountName;

    @Indexed(name = "strategyId")
    private String strategyId;

    private String strategyName;

    private Symbol symbol;

    private double lot;

    private double takeProfit;

    private double stopLoss;

    private Direction direction;

    @Indexed(name = "isClosed")
    private boolean isClosed;

    private Date openDate;

    private Date closeDate;

    private Date idealOpenDate;

    private Date idealCloseDate;

    private double openPrice;

    private double closePrice;

    private double idealOpenPrice;

    private double idealClosePrice;

    private String description;

    public String getAccountName () {
        return accountName;
    }

    public void setAccountName (String accountName) {
        this.accountName = accountName;
    }

    public String getStrategyName () {
        return strategyName;
    }

    public void setStrategyName (String strategyName) {
        this.strategyName = strategyName;
    }

    public String getRemoteId () {
        return remoteId;
    }

    public void setRemoteId (String remoteId) {
        this.remoteId = remoteId;
    }

    public String getAccountId () {
        return accountId;
    }

    public void setAccountId (String accountId) {
        this.accountId = accountId;
    }

    public String getStrategyId () {
        return strategyId;
    }

    public void setStrategyId (String strategyId) {
        this.strategyId = strategyId;
    }

    public Symbol getSymbol () {
        return symbol;
    }

    public void setSymbol (Symbol symbol) {
        this.symbol = symbol;
    }

    public double getLot () {
        return lot;
    }

    public void setLot (double lot) {
        this.lot = lot;
    }

    public Date getOpenDate () {
        return openDate;
    }

    public void setOpenDate (Date openDate) {
        this.openDate = openDate;
    }

    public Date getCloseDate () {
        return closeDate;
    }

    public void setCloseDate (Date closeDate) {
        this.closeDate = closeDate;
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

    public double getOpenPrice () {
        return openPrice;
    }

    public void setOpenPrice (double openPrice) {
        this.openPrice = openPrice;
    }

    public double getClosePrice () {
        return closePrice;
    }

    public void setClosePrice (double closePrice) {
        this.closePrice = closePrice;
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

    public Direction getDirection () {
        return direction;
    }

    public void setDirection (Direction direction) {
        this.direction = direction;
    }

    public boolean isClosed () {
        return isClosed;
    }

    public void setClosed (boolean closed) {
        isClosed = closed;
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

    public Position toPosition(){
        Position position = Position
                .builder (new BasicInitiator (strategyId, strategyName))
                .accountName (accountName)
                .symbol (symbol)
                .direction (direction)
                .lot (lot)
                .description (description)
                .takeProfit (takeProfit)
                .stopLoss (stopLoss)
                .openDate (idealOpenDate)
                .openPrice (idealOpenPrice).build ();
        position.setId (getId ());
        position.setOpenPrice (openPrice);
        position.setOpenDate (openDate);
        position.setRemoteId (remoteId);
        if (isClosed ()){
            position.close (idealCloseDate, idealClosePrice);
            position.setCloseDate (closeDate);
            position.setClosePrice (closePrice);
        }
        return position;
    }

    public static PositionModel valueOf(Position position, Account account){
        PositionModel positionModel = new PositionModel ();
        positionModel.setId (position.getId ());
        positionModel.setSymbol (position.getSymbol ());
        positionModel.setRemoteId (position.getRemoteId ());
        positionModel.setAccountId (account.getId ());
        positionModel.setStrategyId (position.getInitiator ().getId ());
        positionModel.setStrategyName (position.getInitiator ().getName ());
        positionModel.setLot (position.getLot ());
        positionModel.setDirection (position.getDirection ());
        positionModel.setClosed (position.isClosed ());
        positionModel.setOpenDate (position.getOpenDate ());
        positionModel.setCloseDate (position.getCloseDate ());
        positionModel.setIdealOpenDate (position.getIdealOpenDate ());
        positionModel.setIdealCloseDate (position.getIdealCloseDate ());
        positionModel.setOpenPrice (position.getOpenPrice ());
        positionModel.setClosePrice (position.getClosePrice ());
        positionModel.setIdealOpenPrice (position.getIdealOpenPrice ());
        positionModel.setIdealClosePrice (position.getIdealClosePrice ());
        positionModel.setTakeProfit (position.getTakeProfit ());
        positionModel.setStopLoss (position.getStopLoss ());
        positionModel.setDescription (position.getDescription ());
        return positionModel;
    }
}
