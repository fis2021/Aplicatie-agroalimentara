package org.example.model;


import java.util.Objects;

public class OrderStatus {
    private Order order;
    private String status;

    public OrderStatus() {

    }

    public OrderStatus(Order o, String status) {
        this.order = o;
        this.status = status;
    }

    public Order getOrder() {
        return order;
    }

    public String getStatus() {
        return status;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o1) {
        if (this == o1) return true;
        if (o1 == null || getClass() != o1.getClass()) return false;
        OrderStatus that = (OrderStatus) o1;
        return Objects.equals(order, that.order) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, status);
    }

    @Override
    public String toString() {
        return "OrderStatus{" +
                "order=" + order +
                ", status='" + status + '\'' +
                '}';
    }
}
