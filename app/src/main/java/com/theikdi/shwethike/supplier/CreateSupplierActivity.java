package com.theikdi.shwethike.supplier;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.theikdi.shwethike.API.ApiService;
import com.theikdi.shwethike.API.RetrofitClient;
import com.theikdi.shwethike.R;
import com.theikdi.shwethike.customers.CreateCustomerActivity;
import com.theikdi.shwethike.model.Supplier;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateSupplierActivity extends AppCompatActivity {

    TextInputLayout textInputLayout1, textInputLayout3, textInputLayout4, textInputLayout5, textInputLayout6, textInputLayout7;
    TextInputEditText supplierName, supplierAddress, supplierPhone, outstandingQty, outstandingAmount, dueDate;
    AppCompatButton createSupplier, updateSupplier;
    int supplier_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_supplier);

        textInputLayout1 = findViewById(R.id.textInputLayout1);
        textInputLayout3 = findViewById(R.id.textInputLayout3);
        textInputLayout4 = findViewById(R.id.textInputLayout4);
        textInputLayout5 = findViewById(R.id.textInputLayout5);
        textInputLayout6 = findViewById(R.id.textInputLayout6);
        textInputLayout7 = findViewById(R.id.textInputLayout7);

        supplierName = findViewById(R.id.edtSupplierName);
        supplierAddress = findViewById(R.id.edtSupplierAddress);
        supplierPhone = findViewById(R.id.edtSupplierPhone);
        outstandingQty = findViewById(R.id.edtOutstandingQty);
        outstandingAmount = findViewById(R.id.edtOutstandingAmount);
        dueDate = findViewById(R.id.edtDueDate);
        createSupplier = findViewById(R.id.btnCreateSupplier);
        updateSupplier = findViewById(R.id.btnUpdateSupplier);

        updateSupplier.setVisibility(View.GONE);

        supplier_id = getIntent().getIntExtra("supplier_id", 0);
        String get_supplier_name = getIntent().getStringExtra("supplier_name");
        String get_supplier_address = getIntent().getStringExtra("supplier_address");
        String get_supplier_phone = getIntent().getStringExtra("supplier_phone");
        int get_outstanding_qty = getIntent().getIntExtra("outstanding_qty", 0);
        int get_outstanding_amount = getIntent().getIntExtra("outstanding_amount", 0);
        String get_due_date = getIntent().getStringExtra("due_date");


        if (supplier_id != 0) {
            supplierName.setText(get_supplier_name);
            supplierAddress.setText(get_supplier_address);
            supplierPhone.setText(get_supplier_phone);
            outstandingQty.setText(String.valueOf(get_outstanding_qty));
            outstandingAmount.setText(String.valueOf(get_outstanding_amount));
            dueDate.setText(get_due_date);
            createSupplier.setVisibility(View.GONE);
            updateSupplier.setVisibility(View.VISIBLE);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,7);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String due_date = simpleDateFormat.format(calendar.getTime());
        dueDate.setText(due_date);

        createSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String supplier_name = supplierName.getText().toString();
                String supplier_address = supplierAddress.getText().toString();
                String supplier_phone = supplierPhone.getText().toString();
                int outstanding_qty = Integer.parseInt(outstandingQty.getText().toString());
                int outstanding_amount = Integer.parseInt(outstandingAmount.getText().toString());
                String due_date = dueDate.getText().toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String date = simpleDateFormat.format(new java.util.Date());

                // save data to database
                Supplier supplier = new Supplier(0,supplier_name,supplier_address,supplier_phone,outstanding_qty,outstanding_amount,date,due_date);
                saveSupplierData(supplier);
            }
        });

        updateSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String supplier_name = supplierName.getText().toString();
                String supplier_address = supplierAddress.getText().toString();
                String supplier_phone = supplierPhone.getText().toString();
                int outstanding_qty = Integer.parseInt(outstandingQty.getText().toString());
                int outstanding_amount = Integer.parseInt(outstandingAmount.getText().toString());
                String due_date = dueDate.getText().toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String date = simpleDateFormat.format(new java.util.Date());

                // save data to database
                Supplier supplier = new Supplier(supplier_id,supplier_name,supplier_address,supplier_phone,outstanding_qty,outstanding_amount,date,due_date);
                updateSupplierData(supplier);

            }
        });


    }

    private void updateSupplierData(Supplier supplier) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.updateSupplier(supplier);
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
                    Toast.makeText(getApplicationContext(), "Response fail: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveSupplierData(Supplier supplier) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.createSupplier(supplier);
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
                } else {
                    Toast.makeText(getApplicationContext(), "Response error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Response fail: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}