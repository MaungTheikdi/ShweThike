package com.theikdi.shwethike.purchase;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.theikdi.shwethike.API.ApiService;
import com.theikdi.shwethike.API.RetrofitClient;
import com.theikdi.shwethike.R;
import com.theikdi.shwethike.customers.CreateCustomerActivity;
import com.theikdi.shwethike.customers.CustomerAdapter;
import com.theikdi.shwethike.model.Customer;
import com.theikdi.shwethike.model.Purchase;
import com.theikdi.shwethike.model.Sale;
import com.theikdi.shwethike.model.StockView;
import com.theikdi.shwethike.model.Supplier;
import com.theikdi.shwethike.sale.CreateSaleActivity;
import com.theikdi.shwethike.stock.CreateStockActivity;
import com.theikdi.shwethike.stock.StockAdapter;
import com.theikdi.shwethike.supplier.SupplierAdapter;
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

public class CreatePurchaseActivity extends AppCompatActivity {

    TextView textViewSupplierSpinner, textViewProductSpinner;

    TextInputEditText edtStockName, edtStockDesc;
    TextInputEditText edtStockPrice, edtStockOut, edtPaidGasShellQty,edtTotalAmount,edtPaidAmount;
    TextInputEditText edtSupplierName;

    TextView tv_date, text_outstanding_qty, text_outstanding_amount;
    ImageView imgScanner;
    AppCompatButton save;
    int STOCK_ID;
    int SUPPLIER_ID;

    // For Stock Search
    Dialog dialog;
    StockAdapter adapter;
    List<StockView> stockArrayList = new ArrayList<>();

    //For Supplier Search
    Dialog supplierDialog;
    SupplierAdapter supplierAdapter;
    List<Supplier> supplierList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_purchase);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
        // set title
        setTitle("အဝယ် စာရင်းထည့်");


        // Initialize Views
        textViewSupplierSpinner = findViewById(R.id.textViewSupplierSpinner);
        textViewProductSpinner = findViewById(R.id.textViewProductSpinner);
        edtStockName = findViewById(R.id.edtStockName);
        edtStockDesc = findViewById(R.id.edtStockDesc);
        edtStockPrice = findViewById(R.id.edtStockPrice);
        edtSupplierName = findViewById(R.id.edtSupplierName);
        edtStockOut = findViewById(R.id.edtStockOut);
        edtPaidGasShellQty = findViewById(R.id.edtPaidGasShellQty);
        edtTotalAmount = findViewById(R.id.edtTotalAmount);
        edtPaidAmount = findViewById(R.id.edtPaidAmount);
        imgScanner = findViewById(R.id.img_scanner);
        save = findViewById(R.id.savePurchase);
        text_outstanding_qty = findViewById(R.id.text_outstanding_qty);
        text_outstanding_amount = findViewById(R.id.text_outstanding_amount);
        tv_date = findViewById(R.id.tv_date);

        // Set current date
        tv_date.setText(Theikdi.currentDate());

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show date picker dialog
                Theikdi.showDatePickerDialog(CreatePurchaseActivity.this, tv_date);
            }
        });

        edtStockOut.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!edtStockOut.getText().toString().isEmpty() && !edtStockPrice.getText().toString().isEmpty()) {
                    int stockOut = Integer.parseInt(edtStockOut.getText().toString().replace(",", ""));
                    int stockPrice = Integer.parseInt(edtStockPrice.getText().toString().replace(",", ""));
                    int totalAmount = stockOut * stockPrice;
                    edtTotalAmount.setText(Theikdi.numberFormat(totalAmount));
                } else {
                    edtTotalAmount.setText(""); // Clear the total amount if inputs are empty
                }
            }
        });

        textViewProductSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog=new Dialog(CreatePurchaseActivity.this);
                dialog.setContentView(R.layout.dialog_search_spinner_product);
                int myWidth = v.getWidth()*2;
                dialog.getWindow().setLayout(myWidth,1500);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                RecyclerView recyclerView = dialog.findViewById(R.id.list_view);
                SearchView searchView = dialog.findViewById(R.id.search_view_name);
                TextView btnAddCustomer = dialog.findViewById(R.id.go_to_add_customer);
                TextView btnClose = dialog.findViewById(R.id.close_dialog);

                LinearLayoutManager layoutManager = new LinearLayoutManager(CreatePurchaseActivity.this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);

                fetchStockData();

                adapter = new StockAdapter(stockArrayList);
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new StockAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(StockView stockView) {
                        STOCK_ID = stockView.getProduct_id();
                        edtStockName.setText(stockView.getProduct_name());
                        edtStockDesc.setText(stockView.getDescription());
                        edtStockPrice.setText(Theikdi.numberFormat(stockView.getSale_price()));
                        if (stockView.getProduct_id() == 2){
                            edtStockOut.setText("0");
                            edtStockOut.setEnabled(false);
                            edtTotalAmount.setText("0");
                            edtTotalAmount.setEnabled(false);
                        }
                        dialog.dismiss();
                    }
                });
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        adapter.getFilter().filter(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        adapter.getFilter().filter(newText);
                        return false;
                    }
                });

                btnAddCustomer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), CreateStockActivity.class);
                        startActivity(intent);
                    }
                });

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        // Initialize Customer Search
        textViewSupplierSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supplierDialog=new Dialog(CreatePurchaseActivity.this);
                supplierDialog.setContentView(R.layout.dialog_search_spinner_customer);
                int myWidth = v.getWidth()*2;
                supplierDialog.getWindow().setLayout(myWidth,1500);
                supplierDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                supplierDialog.show();


                RecyclerView recyclerView = supplierDialog.findViewById(R.id.list_view);
                SearchView searchView = supplierDialog.findViewById(R.id.search_view_name);
                TextView btnAddCustomer = supplierDialog.findViewById(R.id.go_to_add_customer);
                TextView btnClose = supplierDialog.findViewById(R.id.close_dialog);

                LinearLayoutManager layoutManager = new LinearLayoutManager(CreatePurchaseActivity.this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);

                fetchSupplierData();

                supplierAdapter = new SupplierAdapter(supplierList);
                recyclerView.setAdapter(supplierAdapter);

                supplierAdapter.setOnItemClickListener(new SupplierAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Supplier supplier) {
                        SUPPLIER_ID = supplier.getSupplier_id();
                        edtSupplierName.setText(supplier.getSupplier_name());
                        text_outstanding_qty.setText(Theikdi.numberFormat(supplier.getOutstanding_gas_shell_qty()));
                        text_outstanding_amount.setText(Theikdi.numberFormat(supplier.getOutstanding_amount()));

                        supplierDialog.dismiss();
                    }
                });

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        //adapter.filter(query);
                        supplierAdapter.getFilter().filter(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        //adapter.filter(newText);
                        supplierAdapter.getFilter().filter(newText);
                        return false;
                    }

                });



                btnAddCustomer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), CreateCustomerActivity.class);
                        startActivity(intent);
                    }
                });

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        supplierDialog.dismiss();
                    }
                });
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    int quantity = Integer.parseInt(edtStockOut.getText().toString().trim().replace(",", ""));
                    int paid_quantity = Integer.parseInt(edtPaidGasShellQty.getText().toString().trim().replace(",", ""));
                    // text_outstanding_qty + quantity - return_quantity
                    int outstanding_quantity = Integer.parseInt(text_outstanding_qty.getText().toString().trim().replace(",", "")) + quantity - paid_quantity;
                    int price = Integer.parseInt(edtStockPrice.getText().toString().trim().replace(",", ""));
                    int totalAmount = Integer.parseInt(edtTotalAmount.getText().toString().trim().replace(",", ""));
                    int paid_amount = Integer.parseInt(edtPaidAmount.getText().toString().trim().replace(",", ""));
                    // text_outstanding_amount + total - received_amount
                    int outstanding_amount = Integer.parseInt(text_outstanding_amount.getText().toString().trim().replace(",", "")) + totalAmount - paid_amount;
                    String sale_date = tv_date.getText().toString();

                    Purchase purchase = new Purchase(
                            0,
                            SUPPLIER_ID,
                            STOCK_ID,
                            price,
                            quantity,
                            totalAmount,
                            paid_amount,
                            paid_quantity,
                            outstanding_quantity,
                            outstanding_amount,
                            sale_date
                    );
                    savePurchase(purchase);
                }
            }
        });

    }

    private void savePurchase(Purchase purchase) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.addPurchase(purchase);
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
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(StockActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchStockData() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<StockView>> call = apiService.getStockList();
        call.enqueue(new Callback<List<StockView>>() {
            @Override
            public void onResponse(Call<List<StockView>> call, Response<List<StockView>> response) {
                if (response.isSuccessful() && response.body() != null){
                    stockArrayList.clear();
                    stockArrayList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<StockView>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Fail: "+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchSupplierData() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Supplier>> call = apiService.getSupplierList();
        call.enqueue(new Callback<List<Supplier>>() {
            @Override
            public void onResponse(Call<List<Supplier>> call, Response<List<Supplier>> response) {
                if (response.isSuccessful() && response.body()!= null) {
                    supplierList.clear();
                    supplierList.addAll(response.body());
                    supplierAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(),  "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Supplier>> call, Throwable t) {
                Log.e("API Error", t.getMessage());
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean validateForm() {
        if (edtSupplierName.getText().toString().trim().isEmpty()) {
            edtSupplierName.setError("Please select a supplier");
            return false;
        }

        if (edtStockName.getText().toString().trim().isEmpty()) {
            edtStockName.setError("Please select a product");
            return false;
        }

        if (edtStockOut.getText().toString().trim().isEmpty()) {
            edtStockOut.setError("Please enter quantity");
            return false;
        }

        if (edtStockPrice.getText().toString().trim().isEmpty()) {
            edtStockPrice.setError("Price must be specified");
            return false;
        }

        if (edtPaidGasShellQty.getText().toString().trim().isEmpty()) {
            edtPaidGasShellQty.setError("Please select a paid gas shell");
            return false;
        }

        if (edtPaidAmount.getText().toString().trim().isEmpty()) {
            edtPaidAmount.setError("Please total amount");
            return false;
        }

        if (edtTotalAmount.getText().toString().trim().isEmpty()) {
            edtTotalAmount.setError("Please return amount");
            return false;
        }

        return true;
    }
}