package com.theikdi.shwethike.model;

public class Purchase {
    /*	purchase_id	supplier_id	product_id	purchase_price	purchase_quantity	total_amount	paid_amount	outstanding_quantity	outstanding_amount	purchase_date*/
    private int purchase_id;
    private int supplier_id;
    private int product_id;
    private int purchase_price;
    private int purchase_quantity;
    private int total_amount;
    private int paid_amount;
    private int paid_quantity;
    private int outstanding_quantity;
    private int outstanding_amount;
    private String purchase_date; //

    String supplier_name;
    String product_name;
    String shop_name;
    String product_description;

    public Purchase(int purchase_id, int supplier_id, int product_id, int purchase_price, int purchase_quantity, int total_amount, int paid_amount, int paid_quantity, int outstanding_quantity, int outstanding_amount, String purchase_date) {
        this.purchase_id = purchase_id;
        this.supplier_id = supplier_id;
        this.product_id = product_id;
        this.purchase_price = purchase_price;
        this.purchase_quantity = purchase_quantity;
        this.total_amount = total_amount;
        this.paid_amount = paid_amount;
        this.paid_quantity = paid_quantity;
        this.outstanding_quantity = outstanding_quantity;
        this.outstanding_amount = outstanding_amount;
        this.purchase_date = purchase_date;
    }

    public int getPurchase_id() {
        return purchase_id;
    }

    public void setPurchase_id(int purchase_id) {
        this.purchase_id = purchase_id;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getPurchase_price() {
        return purchase_price;
    }

    public void setPurchase_price(int purchase_price) {
        this.purchase_price = purchase_price;
    }

    public int getPurchase_quantity() {
        return purchase_quantity;
    }

    public void setPurchase_quantity(int purchase_quantity) {
        this.purchase_quantity = purchase_quantity;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public int getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(int paid_amount) {
        this.paid_amount = paid_amount;
    }

    public int getPaid_quantity() {
        return paid_quantity;
    }

    public void setPaid_quantity(int paid_quantity) {
        this.paid_quantity = paid_quantity;
    }

    public int getOutstanding_quantity() {
        return outstanding_quantity;
    }

    public void setOutstanding_quantity(int outstanding_quantity) {
        this.outstanding_quantity = outstanding_quantity;
    }

    public int getOutstanding_amount() {
        return outstanding_amount;
    }

    public void setOutstanding_amount(int outstanding_amount) {
        this.outstanding_amount = outstanding_amount;
    }

    public String getPurchase_date() {
        return purchase_date;
    }

    public void setPurchase_date(String purchase_date) {
        this.purchase_date = purchase_date;
    }
    public String getSupplier_name() {
        return supplier_name;
    }
    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
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

}
