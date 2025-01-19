package com.theikdi.shwethike.customers;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.theikdi.shwethike.API.ApiService;
import com.theikdi.shwethike.API.RetrofitClient;
import com.theikdi.shwethike.R;
import com.theikdi.shwethike.model.Customer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCustomerActivity extends AppCompatActivity {

    TextInputLayout textInputLayout1, textInputLayout2, textInputLayout3, textInputLayout4, textInputLayout5, textInputLayout6, textInputLayout7;
    TextInputEditText edtCustomerName, edtCustomerCategory, edtCustomerAddress, edtCustomerPhone, edtOutstandingShellQty, edtOutstandingAmount;
    TextInputEditText edtDueDate;
    AppCompatButton btnCreateCustomer, btnUpdateCustomer;

    int customer_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_customer);

        textInputLayout1 = findViewById(R.id.textInputLayout1);
        textInputLayout2 = findViewById(R.id.textInputLayout2);
        textInputLayout3 = findViewById(R.id.textInputLayout3);
        textInputLayout4 = findViewById(R.id.textInputLayout4);
        textInputLayout5 = findViewById(R.id.textInputLayout5);
        textInputLayout6 = findViewById(R.id.textInputLayout6);

        edtCustomerName = findViewById(R.id.edtCustomerName);
        edtCustomerCategory = findViewById(R.id.edtCustomerCategory);
        edtCustomerAddress = findViewById(R.id.edtCustomerAddress);
        edtCustomerPhone = findViewById(R.id.edtCustomerPhone);
        edtOutstandingShellQty = findViewById(R.id.edtOutstandingGasShellQty);
        edtOutstandingAmount = findViewById(R.id.edtOutstandingAmount);
        edtDueDate = findViewById(R.id.edtDueDate);
        btnCreateCustomer = findViewById(R.id.btnCreateCustomer);
        btnUpdateCustomer = findViewById(R.id.btnUpdateCustomer);

        btnUpdateCustomer.setVisibility(View.GONE);

        //Intent intent = new Intent();
        customer_id = getIntent().getIntExtra("customer_id",0);
        String customer_name = getIntent().getStringExtra("customer_name");
        String customer_category = getIntent().getStringExtra("customer_category");
        String address = getIntent().getStringExtra("address");
        String phone = getIntent().getStringExtra("phone");
        int gas_shell_qty = getIntent().getIntExtra("gas_shell_qty",0);
        int outstanding_amount = getIntent().getIntExtra("outstanding_amount",0);
        String due_date_1 = getIntent().getStringExtra("due_date");

        if (customer_id != 0) {
            edtCustomerName.setText(customer_name);
            edtCustomerCategory.setText(customer_category);
            edtCustomerAddress.setText(address);
            edtCustomerPhone.setText(phone);
            edtOutstandingShellQty.setText(String.valueOf(gas_shell_qty));
            edtOutstandingAmount.setText(String.valueOf(outstanding_amount));
            edtDueDate.setText(due_date_1);
            btnCreateCustomer.setVisibility(View.GONE);
            btnUpdateCustomer.setVisibility(View.VISIBLE);
            //Toast.makeText(this, String.valueOf(customer_id), Toast.LENGTH_SHORT).show();
        }



        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,7);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String due_date = simpleDateFormat.format(calendar.getTime());
        edtDueDate.setText(due_date);

        btnCreateCustomer.setOnClickListener(view -> {
            int customerId = 0;
            String customerName = edtCustomerName.getText().toString();
            String customerCategory = edtCustomerCategory.getText().toString();
            String customerAddress = edtCustomerAddress.getText().toString();
            String customerPhone = edtCustomerPhone.getText().toString();
            int outstandingShellQuantity = Integer.parseInt(edtOutstandingShellQty.getText().toString());
            int outstandingAmount = Integer.parseInt(edtOutstandingAmount.getText().toString());
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = simpleDateFormat2.format(new Date());
            String dueDate = edtDueDate.getText().toString();


            Customer customer = new Customer(customerId,customerName, customerCategory, customerAddress, customerPhone, outstandingShellQuantity, outstandingAmount,formattedDate,dueDate);
            createCustomer(customer);
        });

        btnUpdateCustomer.setOnClickListener(view -> {
            String customerName = edtCustomerName.getText().toString();
            String customerCategory = edtCustomerCategory.getText().toString();
            String customerAddress = edtCustomerAddress.getText().toString();
            String customerPhone = edtCustomerPhone.getText().toString();
            int outstandingShellQuantity = Integer.parseInt(edtOutstandingShellQty.getText().toString());
            int outstandingAmount = Integer.parseInt(edtOutstandingAmount.getText().toString());
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = simpleDateFormat2.format(new Date());
            String dueDate = edtDueDate.getText().toString();


            Customer customer = new Customer(customer_id,customerName, customerCategory, customerAddress, customerPhone, outstandingShellQuantity, outstandingAmount,formattedDate,dueDate);
            updateCustomer(customer);
        });


    }

    private void createCustomer(Customer customer) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.createCustomer(customer);
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
                        } else {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(StockActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateCustomerActivity.this, "Response fail: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CreateCustomerActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateCustomer(Customer customer){
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.updateCustomer(customer);
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
                        } else {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(StockActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateCustomerActivity.this, "Response fail: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CreateCustomerActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}