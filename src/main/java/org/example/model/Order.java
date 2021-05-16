package org.example.model;

import org.dizitart.no2.objects.Id;

import java.util.List;
import java.util.Objects;

public class Order {

    @Id
    private long id;
    private String shopName;
    private String customerName;
    private List<ProductToOrder> productsOrdered;

    public Order() {

    }

    public Order(String shopname, String customername, List<ProductToOrder> productsOrdered) {
        this.shopName = shopname;
        this.customerName = customername;
        this.productsOrdered = productsOrdered;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<ProductToOrder> getProductsOrdered() {
        return productsOrdered;
    }

    public void setProductsOrdered(List<ProductToOrder> productsOrdered) {
        this.productsOrdered = productsOrdered;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && shopName.equals(order.shopName) && customerName.equals(order.customerName) && Objects.equals(productsOrdered, order.productsOrdered);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shopName, customerName, productsOrdered);
    }

    @Override
    public String toString() {
        return "Order{" +
                "shopname='" + shopName + '\'' +
                ", customername='" + customerName + '\'' +
                ", productsOrd=" + productsOrdered +
                '}';
    }
}



