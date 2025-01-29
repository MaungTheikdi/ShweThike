package com.theikdi.shwethike.reports;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.theikdi.shwethike.API.ApiService;
import com.theikdi.shwethike.API.RetrofitClient;
import com.theikdi.shwethike.R;
import com.theikdi.shwethike.history.HistoryActivity;
import com.theikdi.shwethike.model.StockView;
import com.theikdi.shwethike.stock.CreateStockActivity;
import com.theikdi.shwethike.stock.StockAdapter;
import com.theikdi.shwethike.stock.StockViewAllActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WarehouseActivity extends AppCompatActivity {

    private SearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    private StockAdapter adapter;
    public static List<StockView> stockViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_warehouse);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recycler_view);
        searchView = findViewById(R.id.search_view);

        stockViewList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StockAdapter(stockViewList);
        recyclerView.setAdapter(adapter);

        // shopId 1 is for warehouse and 2 is for shop
        fetchStockData(1);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchStockData(1);
                swipeRefreshLayout.setRefreshing(false); // Stop refreshing after data fetched
            }
        });
    }

    private void fetchStockData(int shopId) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<StockView>> call = apiService.getProductsByShop(shopId);

        call.enqueue(new Callback<List<StockView>>() {
            @Override
            public void onResponse(Call<List<StockView>> call, Response<List<StockView>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    stockViewList.clear();
                    stockViewList.addAll(response.body());
                    adapter.notifyDataSetChanged(); // Notify adapter of changes
                } else {
                    Toast.makeText(getApplicationContext(), "Error: Unable to fetch data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<StockView>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}