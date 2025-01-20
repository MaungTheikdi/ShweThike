package com.theikdi.shwethike.model;

public class Sale {
    int product_id;
    int customer_id;
    int quantity;
    int return_quantity;
    int outstanding_quantity;
    int unit_price;
    int total_amount;
    int receive_amount;
    int outstanding_amount;
    String due_date;
    String date;
    // constructor
    public Sale(int product_id, int customer_id, int quantity, int return_quantity, int outstanding_quantity, int unit_price, int total_amount, int receive_amount, int outstanding_amount, String due_date, String date) {
        this.product_id = product_id;
        this.customer_id = customer_id;
        this.quantity = quantity;
        this.return_quantity = return_quantity;
        this.outstanding_quantity = outstanding_quantity;
        this.unit_price = unit_price;
        this.total_amount = total_amount;
        this.receive_amount = receive_amount;
        this.outstanding_amount = outstanding_amount;
        this.due_date = due_date;
        this.date = date;
    }
}
