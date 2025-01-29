package com.theikdi.shwethike.expense;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theikdi.shwethike.API.ApiService;
import com.theikdi.shwethike.API.RetrofitClient;
import com.theikdi.shwethike.APIResponse.ResponseModel;
import com.theikdi.shwethike.R;
import com.theikdi.shwethike.model.Expense;
import com.theikdi.shwethike.util.Theikdi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseListActivity extends AppCompatActivity {

    FloatingActionButton fabButton;
    SwipeRefreshLayout swipeRefreshLayout;
    SearchView searchView;

    RecyclerView recyclerView;
    ExpenseAdapter expenseAdapter;
    List<Expense> expenseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_expense_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        fabButton = findViewById(R.id.fabButton);
        searchView = findViewById(R.id.search_view);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        expenseList = new ArrayList<>();
        expenseAdapter = new ExpenseAdapter(expenseList);

        recyclerView.setAdapter(expenseAdapter);

        fetchExpenseData();

        expenseAdapter.setOnItemClickListener(new ExpenseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Expense expense) {
                bottomSheetDialog(expense);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                expenseAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                expenseAdapter.getFilter().filter(newText);
                return false;
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchExpenseData();
                swipeRefreshLayout.setRefreshing(false); // Stop refreshing after data fetched
            }
        });

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExpenseListActivity.this,CreateExpenseActivity.class);
                startActivity(intent);
            }
        });
    }

    private void bottomSheetDialog(Expense expense) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(expense.getUser_name());
        builder.setMessage(expense.getDescription() + " " + expense.getAmount() + "\n" + expense.getDate());
        builder.setPositiveButton("Cancel", (dialog, which) -> {

        });
        builder.setNegativeButton("Edit", (dialog, which) -> {
            Intent intent = new Intent(ExpenseListActivity.this, CreateExpenseActivity.class);
            intent.putExtra("expense_id", expense.getExpense_id());
            intent.putExtra("user_id", expense.getUser_id());
            intent.putExtra("description", expense.getDescription());
            intent.putExtra("amount", expense.getAmount());
            intent.putExtra("date", expense.getDate());
            startActivity(intent);
        });
        builder.setNeutralButton("Delete", (dialog, which) -> {
            deleteExpense(expense.getExpense_id());
            //Theikdi.showToast(getApplicationContext(), "ID: " + expense.getExpense_id());
        });
        builder.create().show();
    }

    private void deleteExpense(int expenseId) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.deleteExpense(expenseId, RetrofitClient.SECRET_KEY);
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
                            //clearText();
                            fetchExpenseData();
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
                Toast.makeText(ExpenseListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchExpenseData() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Expense>> call = apiService.getExpensesList();
        call.enqueue(new Callback<List<Expense>>() {
            @Override
            public void onResponse(Call<List<Expense>> call, Response<List<Expense>> response) {
                if (response.isSuccessful() && response.body() != null){
                    expenseList.clear();
                    expenseList.addAll(response.body());
                    expenseAdapter.notifyDataSetChanged(); // Notify adapter of changes
                } else {
                    Toast.makeText(getApplicationContext(),  "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Expense>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchExpenseData();
    }
}