package com.theikdi.shwethike.model;

public class Product {
    private String product_barcode;
    private String product_name;
    private int purchase_price;
    private int sale_price;
    private int purchase_qty;
    private int sale_qty;
    private int instock;
    public Product(String product_barcode, String product_name, int purchase_price, int sale_price, int purchase_qty, int sale_qty, int instock){
        this.product_barcode = product_barcode;
        this.product_name = product_name;
        this.purchase_price = purchase_price;
        this.sale_price = sale_price;
        this.purchase_qty = purchase_qty;
        this.sale_qty = sale_qty;
        this.instock = instock;
    }
}
