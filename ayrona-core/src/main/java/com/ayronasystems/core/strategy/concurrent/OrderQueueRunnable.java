package com.ayronasystems.core.strategy.concurrent;

import com.ayronasystems.core.concurrent.QueueRunnable;
import com.ayronasystems.core.strategy.OrderHandler;

/**
 * Created by gorkemg on 10.06.2016.
 */
public class OrderQueueRunnable implements QueueRunnable<OrderQueueElement>{

    private OrderHandler orderHandler;

    public OrderQueueRunnable(OrderHandler orderHandler) {
        this.orderHandler = orderHandler;
    }

    public void process(OrderQueueElement element) {
        orderHandler.process (element.getOrderList(),
                element.getInitiator(),
                element.getAccountBindInfo().getAccount (),
                element.getAccountBindInfo().getLot (),
                element.getTakeProfit(),
                element.getStopLoss()
        );
    }
}
