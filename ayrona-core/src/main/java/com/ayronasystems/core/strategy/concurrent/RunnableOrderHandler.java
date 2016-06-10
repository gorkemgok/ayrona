package com.ayronasystems.core.strategy.concurrent;

import com.ayronasystems.core.Order;
import com.ayronasystems.core.account.AccountBindInfo;
import com.ayronasystems.core.strategy.BasicOrderHandler;
import com.ayronasystems.core.strategy.Initiator;
import com.ayronasystems.core.strategy.OrderHandler;

import java.util.List;

/**
 * Created by gorkemg on 10.06.2016.
 */
public class RunnableOrderHandler extends BasicOrderHandler implements Runnable{

    private AccountBindInfo accountBindInfo;

    private List<Order> orderList;

    private double takeProfit;

    private double stopLoss;

    private Initiator initiator;

    public RunnableOrderHandler(List<Order> orderList, Initiator initiator, AccountBindInfo accountBindInfo, double takeProfit, double stopLoss) {
        this.orderList = orderList;
        this.initiator = initiator;
        this.accountBindInfo = accountBindInfo;
        this.takeProfit = takeProfit;
        this.stopLoss = stopLoss;
    }

    public void run() {
        super.process(orderList, initiator, accountBindInfo.getAccount(), accountBindInfo.getLot(), takeProfit, stopLoss);
    }
}
