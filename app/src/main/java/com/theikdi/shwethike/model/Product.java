package com.theikdi.shwethike.model;

public class Product {
    private int product_id;
    private String product_barcode;
    private String product_name;
    private String description;
    private int purchase_price;
    private int sale_price;
    private int purchase_qty;
    private int sale_qty;
    private int instock;
    private int shop_id;
    public Product(int product_id, String product_barcode, String product_name, String description, int purchase_price, int sale_price, int purchase_qty, int sale_qty, int instock, int shop_id) {
        this.product_id = product_id;
        this.product_barcode = product_barcode;
        this.product_name = product_name;
        this.description = description;
        this.purchase_price = purchase_price;
        this.sale_price = sale_price;
        this.purchase_qty = purchase_qty;
        this.sale_qty = sale_qty;
        this.instock = instock;
        this.shop_id = shop_id;
    }
}
