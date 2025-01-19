package com.theikdi.shwethike.model;

public class StockView {
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
    private String shop_name;
    public StockView(int product_id, String product_barcode, String product_name, String description, int purchase_price, int sale_price, int purchase_qty, int sale_qty, int instock, int shop_id, String shop_name){
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
        this.shop_name = shop_name;
    }

    public int getProduct_id() {
        return product_id;
    }

    public String getProduct_barcode() {
        return product_barcode;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getDescription() {
        return description;
    }

    public int getPurchase_price() {
        return purchase_price;
    }

    public int getSale_price() {
        return sale_price;
    }

    public int getPurchase_qty() {
        return purchase_qty;
    }

    public int getSale_qty() {
        return sale_qty;
    }

    public int getInstock() {
        return instock;
    }

    public int getShop_id() {
        return shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }
}
