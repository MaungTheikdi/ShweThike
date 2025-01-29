package com.theikdi.shwethike.expense;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.theikdi.shwethike.API.ApiService;
import com.theikdi.shwethike.API.RetrofitClient;
import com.theikdi.shwethike.R;
import com.theikdi.shwethike.model.Expense;
import com.theikdi.shwethike.util.Theikdi;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateExpenseActivity extends AppCompatActivity {

    TextView tvDate;
    TextInputEditText edtDescription, edtAmount;
    AppCompatButton saveButton, updateButton;

    SharedPreferences sharedPreferences;
    int SHOP_ID;
    int USER_ID;
    int EXPENSE_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_expense);

        sharedPreferences = getSharedPreferences("USER_DETAILS", MODE_PRIVATE);

        SHOP_ID = sharedPreferences.getInt("shopId", -1);
        USER_ID = sharedPreferences.getInt("userId", -1);

        tvDate = findViewById(R.id.tv_date);
        edtDescription = findViewById(R.id.edtDescription);
        edtAmount = findViewById(R.id.edtAmount);
        saveButton = findViewById(R.id.btnSave);
        updateButton = findViewById(R.id.btnUpdate);
        updateButton.setVisibility(View.GONE);

        tvDate.setText(Theikdi.currentDate());

        EXPENSE_ID = getIntent().getIntExtra("expense_id",-1);
        if(EXPENSE_ID!= -1) {
            // Load expense data from intent
            String description = getIntent().getStringExtra("description");
            int amount = getIntent().getIntExtra("amount",-1);
            String date = getIntent().getStringExtra("date");
            USER_ID = getIntent().getIntExtra("user_id",-1);
            edtDescription.setText(description);
            edtAmount.setText(String.valueOf(amount));
            tvDate.setText(date);
            saveButton.setVisibility(View.GONE);
            updateButton.setVisibility(View.VISIBLE);
        }


        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Theikdi.showDatePickerDialog(CreateExpenseActivity.this, tvDate);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save expense to the database
                String description = edtDescription.getText().toString().trim();
                int amount = Integer.parseInt(edtAmount.getText().toString().trim());

                // Validate inputs
                if (description.isEmpty()) {
                    edtDescription.setError("Description is required");
                    return;
                }

                if (amount <= 0) {
                    edtAmount.setError("Amount should be greater than zero");
                    return;
                }

                Expense expense = new Expense(0, USER_ID, description, amount,tvDate.getText().toString());
                saveExpense(expense);
                finish();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update expense in the database
                int expenseId = getIntent().getIntExtra("expense_id",-1);
                String description = edtDescription.getText().toString().trim();
                int amount = Integer.parseInt(edtAmount.getText().toString().trim());

                // Validate inputs
                if (description.isEmpty()) {
                    edtDescription.setError("Description is required");
                    return;
                }

                if (amount <= 0) {
                    edtAmount.setError("Amount should be greater than zero");
                    return;
                }

                Expense expense = new Expense(expenseId, USER_ID, description, amount, tvDate.getText().toString());
                updateExpense(expense);
                finish();
            }
        });
    }

    private void saveExpense(Expense expense) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.addExpenses(expense);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        String status = jsonResponse.getString("status");
                        String message = jsonResponse.getString("message");

                        if (status.equalsIgnoreCase("success")) {
                            Theikdi.showToast(CreateExpenseActivity.this, message);
                        } else {
                            Theikdi.showToast(CreateExpenseActivity.this, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Theikdi.showToast(CreateExpenseActivity.this, "Error: " + t.getMessage());
            }
        });
    }

    private void updateExpense(Expense expense){
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.updateExpense(expense);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        String status = jsonResponse.getString("status");
                        String message = jsonResponse.getString("message");

                        if (status.equalsIgnoreCase("success")) {
                            Theikdi.showToast(CreateExpenseActivity.this, message);
                        } else {
                            Theikdi.showToast(CreateExpenseActivity.this, message);
                        }
                    } catch (Exception e) {
                        Theikdi.showToast(CreateExpenseActivity.this,e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Theikdi.showToast(CreateExpenseActivity.this, "Error: " + t.getMessage());
            }
        });
    }
}