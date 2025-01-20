package com.theikdi.shwethike.model;

public class Supplier {
    //supplier_id	supplier_name	address	phone	outstanding_amount	due_date
    private int supplier_id;
    private String supplier_name;
    private String address;
    private String phone;
    private int outstanding_gas_shell_qty;
    private int outstanding_amount;
    private String modified_date;
    private String due_date;
    public Supplier(int supplier_id, String supplier_name, String address, String phone, int outstanding_gas_shell_qty, int outstanding_amount, String modified_date, String due_date) {
        this.supplier_id = supplier_id;
        this.supplier_name = supplier_name;
        this.address = address;
        this.phone = phone;
        this.outstanding_gas_shell_qty = outstanding_gas_shell_qty;
        this.outstanding_amount = outstanding_amount;
        this.modified_date = modified_date;
        this.due_date = due_date;
    }
    // getters and setters

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
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

    public int getOutstanding_gas_shell_qty() {
        return outstanding_gas_shell_qty;
    }

    public void setOutstanding_gas_shell_qty(int outstanding_gas_shell_qty) {
        this.outstanding_gas_shell_qty = outstanding_gas_shell_qty;
    }

    public String getModified_date() {
        return modified_date;
    }

    public void setModified_date(String modified_date) {
        this.modified_date = modified_date;
    }
}
