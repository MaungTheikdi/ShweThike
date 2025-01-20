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
    private String purchase_date;
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
}
