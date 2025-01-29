package com.theikdi.shwethike.stock;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
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
import com.theikdi.shwethike.expense.CreateExpenseActivity;
import com.theikdi.shwethike.expense.ExpenseListActivity;
import com.theikdi.shwethike.history.HistoryActivity;
import com.theikdi.shwethike.model.StockView;
import com.theikdi.shwethike.sale.CreateSaleActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockViewAllActivity extends AppCompatActivity {

    private SearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;


    private StockAdapter adapter;
    public static List<StockView> stockViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stock_view_all);
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

        fetchStockData();

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

        //adapter.setOnItemClickListener(this::bottomSheet);
        adapter.setOnItemClickListener(new StockAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(StockView stockView) {
                bottomSheet(stockView);
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchStockData();
                swipeRefreshLayout.setRefreshing(false); // Stop refreshing after data fetched
            }
        });

    }

    private void bottomSheet(StockView stockView) {
        // Create and show the bottom sheet dialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.TheikdiBottomSheetDialog);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_product, null);
        bottomSheetDialog.setContentView(bottomSheetView);
        TextView productName = bottomSheetView.findViewById(R.id.product_name);
        TextView editProduct = bottomSheetView.findViewById(R.id.edit_button);
        TextView viewHistory = bottomSheetView.findViewById(R.id.history_button);
        TextView deleteProduct = bottomSheetView.findViewById(R.id.delete_button);

        productName.setText(stockView.getProduct_name());
        editProduct.setOnClickListener(v -> {
            Intent intent = new Intent(StockViewAllActivity.this, CreateStockActivity.class);
            intent.putExtra("product_id", stockView.getProduct_id());
            intent.putExtra("product_barcode", stockView.getProduct_barcode());
            intent.putExtra("product_name", stockView.getProduct_name());
            intent.putExtra("product_description", stockView.getDescription());
            intent.putExtra("purchase_price", stockView.getPurchase_price());
            intent.putExtra("sale_price", stockView.getSale_price());
            intent.putExtra("in_stock", stockView.getInstock());
            intent.putExtra("purchase_qty", stockView.getPurchase_qty());
            intent.putExtra("sale_qty", stockView.getSale_qty());
            intent.putExtra("shop_id", stockView.getShop_id());

            startActivity(intent);
            bottomSheetDialog.dismiss();
        });
        viewHistory.setOnClickListener(v -> {
            Intent intent = new Intent(StockViewAllActivity.this, HistoryActivity.class);
            intent.putExtra("product_id", stockView.getProduct_id());
            startActivity(intent);
            bottomSheetDialog.dismiss();
        });
        deleteProduct.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(stockView.getProduct_name());
            builder.setMessage(stockView.getProduct_name() + " ကို ဖျက်မှာ သေချာလား?");
            builder.setPositiveButton("ဖျက်မယ်", (dialog, which) -> {
                deleteProduct(stockView.getProduct_id());
            });
            builder.setNegativeButton("မဖျက်တော့ပါ", (dialog, which) -> {

            });
            builder.create().show();
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.show();
    }

    private void fetchStockData() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<StockView>> call = apiService.getStockList();
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

    @Override
    protected void onResume() {
        super.onResume();
        fetchStockData();
    }

    private void deleteProduct(int productId) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.deleteProduct(productId,RetrofitClient.SECRET_KEY);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        String status = jsonResponse.getString("status");
                        String message = jsonResponse.getString("message");
                        if ("success".equals(status)) {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            fetchStockData();
                        } else {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(StockActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Response fail: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(StockViewAllActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}