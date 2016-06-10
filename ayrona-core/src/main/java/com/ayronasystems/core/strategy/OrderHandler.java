package com.ayronasystems.core.strategy;

import com.ayronasystems.core.account.Account;
import com.ayronasystems.core.Position;
import com.ayronasystems.core.Order;

import java.util.List;

/**
 * Created by gorkemgok on 14/05/16.
 */
public interface OrderHandler {

    List<Position> process(List<Order> orders, Initiator initiator, Account account, double lot, double takeProfit, double stopLoss);

}
