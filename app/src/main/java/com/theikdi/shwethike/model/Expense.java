package com.theikdi.shwethike.model;

public class Expense {
    //expense_id	user_id	description	amount	date
    private int expense_id;
    private int user_id;
    private String user_name;
    private String description;
    private int amount;
    private String date;
    public Expense(int expense_id, int user_id, String description, int amount, String date) {
        this.expense_id = expense_id;
        this.user_id = user_id;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }
    // getters and setters...
    public int getExpense_id() {
        return expense_id;
    }

    public void setExpense_id(int expense_id) {
        this.expense_id = expense_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
