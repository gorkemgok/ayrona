package com.ayronasystems.core.strategy.concurrent;

import com.ayronasystems.core.account.AccountBinder;
import com.ayronasystems.core.strategy.Initiator;
import com.ayronasystems.core.strategy.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkemg on 10.06.2016.
 */
public class OrderQueueElement {

    private List<Order> orderList;

    private Initiator initiator;

    private AccountBinder accountBinder;

    private double takeProfit;

    private double stopLoss;

    public OrderQueueElement(List<Order> orderList, Initiator initiator, AccountBinder accountBinder, double takeProfit, double stopLoss) {
        this.orderList = orderList;
        this.initiator = initiator;
        this.accountBinder = accountBinder;
        this.takeProfit = takeProfit;
        this.stopLoss = stopLoss;
    }

    public List<Order> getOrderList() {
        return new ArrayList<Order>(orderList);
    }

    public Initiator getInitiator() {
        return initiator;
    }

    public AccountBinder getAccountBinder () {
        return accountBinder;
    }

    public double getTakeProfit() {
        return takeProfit;
    }

    public double getStopLoss() {
        return stopLoss;
    }
}
