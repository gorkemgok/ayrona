package com.ayronasystems.core.strategy;

import com.ayronasystems.core.account.Account;
import com.ayronasystems.core.Order;
import com.ayronasystems.core.Position;

import java.util.List;

/**
 * Created by gorkemgok on 18/05/16.
 */
public class BasicOrderHandler implements OrderHandler{

    public List<Position> process (List<Order> orders, Initiator initiator, Account account, double lot, double takeProfit, double stopLoss) {
        for ( Order order : orders ) {
            if (order.getOrder () == Order.Type.OPEN){
                Position position = Position.builder (initiator)
                                    .accountName (account.getName ())
                                    .symbol (order.getSymbol ())
                                    .lot (lot)
                                    .direction (order.getDirection ())
                                    .openDate (order.getDate ())
                                    .openPrice (order.getPrice ())
                                    .takeProfit (takeProfit)
                                    .stopLoss (stopLoss)
                                    .description (order.getDescription ())
                                    .build ();
                account.openPosition (position);
            }else{
                List<Position> openPositions = account.getOpenPositions (order.getSymbol (), initiator);
                for (Position position : openPositions){
                    if (position.getDirection ().equals (order.getDirection ())){
                        position.close (order.getDate (), order.getPrice ());
                        account.closePosition (position, order.getDate (), order.getPrice ());
                    }
                }
            }
        }
        return account.getPositions ();
    }
}
