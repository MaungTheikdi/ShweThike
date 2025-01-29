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

    String customer_name;
    String product_name;
    String shop_name;
    String product_description;
    int sale_id;

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
    // getters and setters

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getReturn_quantity() {
        return return_quantity;
    }

    public void setReturn_quantity(int return_quantity) {
        this.return_quantity = return_quantity;
    }

    public int getOutstanding_quantity() {
        return outstanding_quantity;
    }

    public void setOutstanding_quantity(int outstanding_quantity) {
        this.outstanding_quantity = outstanding_quantity;
    }

    public int getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(int unit_price) {
        this.unit_price = unit_price;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public int getReceive_amount() {
        return receive_amount;
    }

    public void setReceive_amount(int receive_amount) {
        this.receive_amount = receive_amount;
    }

    public int getOutstanding_amount() {
        return outstanding_amount;
    }

    public void setOutstanding_amount(int outstanding_amount) {
        this.outstanding_amount = outstanding_amount;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getShop_name() {
        return shop_name;
    }
    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }
    public String getProduct_description() {
        return product_description;
    }
    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }
    public int getSale_id() {
        return sale_id;
    }
    public void setSale_id(int sale_id) {
        this.sale_id = sale_id;
    }
}
