package com.theikdi.shwethike.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.theikdi.shwethike.API.ApiService;
import com.theikdi.shwethike.API.RetrofitClient;
import com.theikdi.shwethike.R;
import com.theikdi.shwethike.model.DashboardResponse;
import com.theikdi.shwethike.util.Theikdi;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;

public class DashboardActivity extends AppCompatActivity {
    TextView outstandingQty, outstandingAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        outstandingQty = findViewById(R.id.outstanding_qty);
        outstandingAmount = findViewById(R.id.outstanding_amount);

        fetchDashboardData();
    }

    private void fetchDashboardData() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        //Call<DashboardResponse> call = apiService.getDashboardData();
        apiService.getDashboardData().enqueue(new retrofit2.Callback<DashboardResponse>() {
            @Override
            public void onResponse(Call<DashboardResponse> call, retrofit2.Response<DashboardResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DashboardResponse dashboardData = response.body();
                    DashboardResponse.TotalOutstanding totalOutstanding = dashboardData.totalOutstanding.get(0);
                    outstandingQty.setText(Theikdi.numberFormatString(totalOutstanding.outstanding_gas_shell_qty));
                    outstandingAmount.setText(Theikdi.numberFormatString(totalOutstanding.outstanding_amount));
                    setupCharts(dashboardData);
                } else {
                    // Handle errors
                    Log.e("API_ERROR", "Response unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<DashboardResponse> call, Throwable t) {
                // Handle failure
                t.printStackTrace();
            }
        });
    }

    private void setupCharts(DashboardResponse data) {
        List<BarEntry> salesEntries = data.lastSevenDaysSales.stream()
                .map(d -> new BarEntry(data.lastSevenDaysSales.indexOf(d), Float.parseFloat(d.totalAmount)))
                .collect(Collectors.toList());
        List<String> salesDates = data.lastSevenDaysSales.stream()
                .map(d -> d.date)
                .collect(Collectors.toList());

        setupBarChartWithDates(
                findViewById(R.id.lastSevenDaysSalesChart),
                salesEntries,
                salesDates,
                "Last 7 Days Sales"
        );


        List<BarEntry> purchasesEntries = data.lastSevenDaysPurchase.stream()
                .map(d -> new BarEntry(data.lastSevenDaysPurchase.indexOf(d), Float.parseFloat(d.totalAmount)))
                .collect(Collectors.toList());
        List<String> purchaseDates = data.lastSevenDaysPurchase.stream()
                .map(d -> d.purchase_date)
                .collect(Collectors.toList());

        setupBarChartWithDates(
                findViewById(R.id.lastSevenDaysPurchaseChart),
                purchasesEntries,
                purchaseDates,
                "Last 7 Days Purchase"
        );


        List<BarEntry> monthlyPurchasesEntries = data.monthlyPurchases.stream()
                .map(d -> new BarEntry(data.monthlyPurchases.indexOf(d), Float.parseFloat(d.totalAmount)))
                .collect(Collectors.toList());
        List<String> monthlyPurchaseDates = data.monthlyPurchases.stream()
                .map(d -> d.month)
                .collect(Collectors.toList());

        setupBarChartWithDates(
                findViewById(R.id.monthlyPurchaseChart),
                monthlyPurchasesEntries,
                monthlyPurchaseDates,
                "Monthly Purchase"
        );


        List<BarEntry> monthlySaleEntries = data.monthlySales.stream()
                .map(d -> new BarEntry(data.monthlySales.indexOf(d), Float.parseFloat(d.totalAmount)))
                .collect(Collectors.toList());
        List<String> monthlySaleDates = data.monthlySales.stream()
                .map(d -> d.month)
                .collect(Collectors.toList());

        setupBarChartWithDates(
                findViewById(R.id.monthlySalesChart),
                monthlySaleEntries,
                monthlySaleDates,
                "Monthly Sales"
        );


        List<BarEntry> monthlyProfit = data.monthlyProfit.stream()
                .map(d -> new BarEntry(data.monthlyProfit.indexOf(d), Float.parseFloat(d.profit)))
                .collect(Collectors.toList());
        List<String> monthlyProfitMonth = data.monthlyProfit.stream()
                .map(d -> d.month)
                .collect(Collectors.toList());

        setupBarChartWithDates(
                findViewById(R.id.monthlyProfitChart),
                monthlyProfit,
                monthlyProfitMonth,
                "Monthly Profit"
        );


    }

    private void setupBarChartWithDates(BarChart chart, List<BarEntry> entries, List<String> dates, String label) {
        BarDataSet dataSet = new BarDataSet(entries, label);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        BarData barData = new BarData(dataSet);

        chart.setData(barData);

        // Description
        Description description = new Description();
        description.setText(label);
        chart.setDescription(description);

        // Customize the bar chart
        chart.setFitBars(true); // Makes the bars fit within the chart properly
        chart.animateY(1000); // Add animation on Y axis when loading data

        // Enable legend
        chart.getLegend().setEnabled(true);
        chart.getLegend().setTextColor(Color.BLACK); // Set the legend text color
        chart.getLegend().setTextSize(14f); // Set the legend text size

        // Format Y-axis values
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setValueFormatter(new MyValueFormatter());
        chart.getAxisRight().setEnabled(false); // Disable the right Y-axis

        // Customize X-Axis labels
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        // Touchable
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(false);

        chart.invalidate(); // Refresh chart
    }



    // Custom ValueFormatter to format Y-axis values
    private class MyValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            // Format the value as a whole number
            return String.valueOf((int) value);
        }
    }

}