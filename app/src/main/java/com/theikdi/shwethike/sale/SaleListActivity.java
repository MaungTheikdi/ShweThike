package com.theikdi.shwethike.sale;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.theikdi.shwethike.API.ApiService;
import com.theikdi.shwethike.API.RetrofitClient;
import com.theikdi.shwethike.R;
import com.theikdi.shwethike.model.Sale;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaleListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SaleAdapter adapter;
    List<Sale> saleList;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView tvNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sale_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvNoData = findViewById(R.id.no_data_list);
        tvNoData.setVisibility(View.GONE);


        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                    fetchSales();
                    swipeRefreshLayout.setRefreshing(false);

            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        saleList = new ArrayList<>();
        adapter = new SaleAdapter(saleList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fetchSales();
    }

    private void fetchSales() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Sale>> call = apiService.getSales();
        call.enqueue(new Callback<List<Sale>>() {
            @Override
            public void onResponse(Call<List<Sale>> call, Response<List<Sale>> response) {
                if (response.isSuccessful() && response.body()!= null) {
                    saleList.clear();
                    saleList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    if (saleList.isEmpty()) {
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        tvNoData.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error: Unable to fetch data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Sale>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failure:" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}