package com.theikdi.shwethike.model;

public class Customer {
    private int customer_id;
    private String customer_name;
    private String customer_category;
    private String address;
    private String phone;
    private int outstanding_gas_shell_qty;
    private int outstanding_amount;
    private String modified_date;
    private String due_date;
    // constructor
    public Customer(int customer_id, String customer_name, String customer_category, String address, String phone, int outstanding_gas_shell_qty, int outstanding_amount, String modified_date, String due_date) {
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.customer_category = customer_category;
        this.address = address;
        this.phone = phone;
        this.outstanding_gas_shell_qty = outstanding_gas_shell_qty;
        this.outstanding_amount = outstanding_amount;
        this.modified_date = modified_date;
        this.due_date = due_date;
    }
    // getters and setters

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_category() {
        return customer_category;
    }

    public void setCustomer_category(String customer_category) {
        this.customer_category = customer_category;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getOutstanding_gas_shell_qty() {
        return outstanding_gas_shell_qty;
    }

    public void setOutstanding_gas_shell_qty(int outstanding_gas_shell_qty) {
        this.outstanding_gas_shell_qty = outstanding_gas_shell_qty;
    }

    public int getOutstanding_amount() {
        return outstanding_amount;
    }

    public void setOutstanding_amount(int outstanding_amount) {
        this.outstanding_amount = outstanding_amount;
    }

    public String getModified_date() {
        return modified_date;
    }

    public void setModified_date(String modified_date) {
        this.modified_date = modified_date;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }
}
