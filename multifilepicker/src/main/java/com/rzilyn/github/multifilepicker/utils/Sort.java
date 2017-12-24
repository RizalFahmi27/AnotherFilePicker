package com.rzilyn.github.multifilepicker.utils;

/**
 * Created by Rizal Fahmi on 19-Dec-17.
 */

public class Sort {

    private Type type = Type.BY_NAME;
    private Order order = Order.ASCENDING;

    public Type getType(){
        return type;
    }

    public Order getOrder(){
        return order;
    }

    public void setType(Type type){
        this.type = type;
    }

    public void setType(Type type, boolean switchOrder){
        this.type = type;
        if(switchOrder)
            switchOrder();
    }

    public void setOrder(Order order){
        this.order = order;
    }

    public void switchOrder(){
        if(order == Order.ASCENDING)
            order = Order.DESCENDING;
        else if(order == Order.DESCENDING)
            order = Order.ASCENDING;
    }

    public enum Type {
        BY_NAME,

        BY_DATE,

        BY_TYPE,

        BY_SIZE;
    }

    public enum Order {

        ASCENDING,

        DESCENDING
    }

}
