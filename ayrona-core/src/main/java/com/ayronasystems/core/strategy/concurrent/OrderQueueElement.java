package com.ayronasystems.core.strategy.concurrent;

import com.ayronasystems.core.Order;
import com.ayronasystems.core.account.AccountBindInfo;
import com.ayronasystems.core.strategy.Initiator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gorkemg on 10.06.2016.
 */
public class OrderQueueElement {

    private List<Order> orderList;

    private Initiator initiator;

    private AccountBindInfo accountBindInfo;

    private double takeProfit;

    private double stopLoss;

    public OrderQueueElement(List<Order> orderList, Initiator initiator, AccountBindInfo accountBindInfo, double takeProfit, double stopLoss) {
        this.orderList = orderList;
        this.initiator = initiator;
        this.accountBindInfo = accountBindInfo;
        this.takeProfit = takeProfit;
        this.stopLoss = stopLoss;
    }

    public List<Order> getOrderList() {
        return new ArrayList<Order>(orderList);
    }

    public Initiator getInitiator() {
        return initiator;
    }

    public AccountBindInfo getAccountBindInfo() {
        return accountBindInfo;
    }

    public double getTakeProfit() {
        return takeProfit;
    }

    public double getStopLoss() {
        return stopLoss;
    }
}
