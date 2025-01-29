package com.theikdi.shwethike.model;

import java.util.List;

public class DashboardResponse {
    public List<SalesData> lastSevenDaysSales;
    public List<PurchaseData> lastSevenDaysPurchase;
    public List<MonthlySaleData> monthlySales;
    public List<MonthlyPurchaseData> monthlyPurchases;
    public List<TotalOutstanding> totalOutstanding;
    public List<MonthlyProfit> monthlyProfit;

    public static class SalesData {
        public String date;
        public String totalAmount;
    }

    public static class PurchaseData {
        public String purchase_date;
        public String totalAmount;
    }

    public static class MonthlySaleData {
        public String month;
        public String totalAmount;
    }

    public static class MonthlyPurchaseData {
        public String month;
        public String totalAmount;
    }

    public static class TotalOutstanding {
        public String outstanding_gas_shell_qty;
        public String outstanding_amount;
    }

    public static class MonthlyProfit {
        public String month;
        public String profit;
    }
}
