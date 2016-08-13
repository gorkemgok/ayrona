package com.ayronasystems.core.strategy;

import com.ayronasystems.core.definition.Direction;
import com.ayronasystems.core.definition.Symbol;
import com.ayronasystems.core.util.DateUtils;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * Created by gorkemgok on 14/05/16.
 */
public class Order {

    public enum Type{
        OPEN, CLOSE, AMBIGUOUS;

        public Type inverse(){
            switch ( this ){
                case OPEN:
                    return CLOSE;
                case CLOSE:
                    return OPEN;
                default:
                    return AMBIGUOUS;
            }
        }
    }

    public enum Result{
        SUCCESSFUL, FAILED;
    }

    public static class Builder{

        private Order order;

        public Builder () {
            order = new Order ();
        }

        public Builder direction(Direction direction){
            order.direction = direction;
            return this;
        }

        public Builder symbol(Symbol symbol){
            order.symbol = symbol;
            return this;
        }


        public Builder date(Date date){
            order.date = date;
            return this;
        }

        public Builder price(double price){
            order.price = price;
            return this;
        }

        public Builder description(String description){
            order.description = description;
            return this;
        }

        public Builder order(Type order){
            this.order.order = order;
            return this;
        }

        public Order build(){
            return order;
        }

    }

    private Direction direction;

    private Type order;

    private Symbol symbol;

    private double price;

    private Date date;

    private String description;

    private Order () {
    }

    public static Builder builder(){
        return new Builder ();
    }

    public Type getOrder () {
        return order;
    }

    public Direction getDirection () {
        return direction;
    }

    public Symbol getSymbol () {
        return symbol;
    }

    public double getPrice () {
        return price;
    }

    public String getDescription () {
        return description;
    }

    public Date getDate () {
        return date;
    }

    public Order inverse(){
        return inverse (this);
    }

    public static Order inverse(Order order){
        return Order.builder ()
                    .order (order.getOrder ().inverse ())
                    .direction (order.getDirection ().inverse ())
                    .date (order.getDate ())
                    .price (order.getPrice ())
                    .symbol (order.getSymbol ())
                    .description (order.description)
                    .build ();
    }

    @Override
    public String toString () {
        DecimalFormat df = new DecimalFormat ("#.##");
        return "Order{" +
                "symbol=" + symbol +
                " " + order + " " + direction + " " +
                ", price=" + price +
                ", date=" + DateUtils.formatDate (date) +
                ", description='" + description + '\'' +
                '}';
    }
}
