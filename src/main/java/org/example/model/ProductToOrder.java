package org.example.model;

import java.util.Objects;

public class ProductToOrder {

    // TODO: Remove public modifier.
    public Product prod;
    public int quantity;

    public ProductToOrder() {

    }

    public ProductToOrder(Product prod,int quantity)
    {
        this.prod=prod;
        this.quantity=quantity;
    }

    public Product getProd() {
        return prod;
    }

    public void setProd(Product prod) {
        this.prod = prod;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }



    @Override
    public String toString() {
        return "ProductToOrder{" +
                "prod=" + prod +
                ", quantity=" + quantity +'\'' +
        '}';
    }


    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        ProductToOrder that = (ProductToOrder) object;
        return quantity == that.quantity && prod.equals(that.prod);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), prod, quantity);
    }
}
