package org.opencredo.batch.modules.domain;


import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ORDER_TABLE")
public class Order {

    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private OrderStatus status = OrderStatus.REQUESTED;

    private BigDecimal price;


    public Order(){}

    public Order(OrderStatus status, BigDecimal price) {
        this.status = status;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", status=" + status +
                ", price=" + price +
                '}';
    }
}
