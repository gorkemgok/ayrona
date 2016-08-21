package com.ayronasystems.core.strategy.concurrent;

import com.ayronasystems.core.account.AccountBinder;
import com.ayronasystems.core.strategy.BasicOrderHandler;
import com.ayronasystems.core.strategy.Initiator;
import com.ayronasystems.core.strategy.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by gorkemg on 10.06.2016.
 */
public class RunnableOrderHandler extends BasicOrderHandler implements Runnable{

    private static Logger log = LoggerFactory.getLogger (RunnableOrderHandler.class);

    private AccountBinder accountBinder;

    private List<Order> orderList;

    private double takeProfit;

    private double stopLoss;

    private Initiator initiator;

    public RunnableOrderHandler(List<Order> orderList, Initiator initiator, AccountBinder accountBinder, double takeProfit, double stopLoss) {
        this.orderList = orderList;
        this.initiator = initiator;
        this.accountBinder = accountBinder;
        this.takeProfit = takeProfit;
        this.stopLoss = stopLoss;
    }

    public void run() {
        try {
            super.process(orderList, initiator, accountBinder.getAccount(), accountBinder.getLot(), takeProfit, stopLoss);
        }catch ( Exception e){
            log.error ("Order handler error:",e);
        }
    }
}
