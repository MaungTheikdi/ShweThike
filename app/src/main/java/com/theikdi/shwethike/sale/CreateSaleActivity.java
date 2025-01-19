package com.theikdi.shwethike.sale;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.StackView;
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
import com.theikdi.shwethike.customers.CustomerListActivity;
import com.theikdi.shwethike.model.Customer;
import com.theikdi.shwethike.model.Sale;
import com.theikdi.shwethike.model.StockView;
import com.theikdi.shwethike.stock.CreateStockActivity;
import com.theikdi.shwethike.stock.StockAdapter;
import com.theikdi.shwethike.util.Theikdi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateSaleActivity extends AppCompatActivity {

    TextView textViewCustomerSpinner, textViewProductSpinner;

    TextInputEditText edtStockName, edtStockDesc;
    TextInputEditText edtStockPrice, edtStockOut, edtReturnGasShellQty,edtTotalAmount,edtReturnAmount;
    TextInputEditText edtCustomerName;

    TextView tv_date, text_outstanding_qty, text_outstanding_amount;
    ImageView imgScanner;
    AppCompatButton saveStockOut;
    int STOCK_ID;
    int CUSTOMER_ID;
    String wareHouse;


    // For Stock Search
    Dialog dialog;
    StockAdapter adapter;
    List<StockView> stockArrayList = new ArrayList<>();

    //For Customer Search
    Dialog customerDialog;
    CustomerAdapter customerAdapter;
    List<Customer> customerList = new ArrayList<>();


    /*ListView listView;
    InStockAdapter adapterOut;
    public static ArrayList<InStockRecord> inStockArrayList = new ArrayList<>();
    String URL_STOCK_OUT = UrlAddress.STOCK_OUT_TODAY;
    InStockRecord inStockRecord;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_sale);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Views
        textViewCustomerSpinner = findViewById(R.id.textViewCustomerSpinner);
        textViewProductSpinner = findViewById(R.id.textViewProductSpinner);
        edtStockName = findViewById(R.id.edtStockName);
        edtStockDesc = findViewById(R.id.edtStockDesc);
        edtStockPrice = findViewById(R.id.edtStockPrice);
        edtCustomerName = findViewById(R.id.edtCustomerName);
        edtStockOut = findViewById(R.id.edtStockOut);
        edtReturnGasShellQty = findViewById(R.id.edtReturnGasShellQty);
        edtTotalAmount = findViewById(R.id.edtTotalAmount);
        edtReturnAmount = findViewById(R.id.edtReturnAmount);
        imgScanner = findViewById(R.id.img_scanner);
        saveStockOut = findViewById(R.id.saveStockOut);
        text_outstanding_qty = findViewById(R.id.text_outstanding_qty);
        text_outstanding_amount = findViewById(R.id.text_outstanding_amount);
        tv_date = findViewById(R.id.tv_date);

        // Set current date
        tv_date.setText(Theikdi.currentDate());

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show date picker dialog
                Theikdi.showDatePickerDialog(CreateSaleActivity.this, tv_date);
            }
        });

        // edtStockOut change calculate the edtTotalAmount
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

        // Initialize Dialog
        textViewProductSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog=new Dialog(CreateSaleActivity.this);
                dialog.setContentView(R.layout.dialog_search_spinner_product);
                int myWidth = v.getWidth()*2;
                dialog.getWindow().setLayout(myWidth,1500);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                RecyclerView recyclerView = dialog.findViewById(R.id.list_view);
                SearchView searchView = dialog.findViewById(R.id.search_view_name);
                TextView btnAddCustomer = dialog.findViewById(R.id.go_to_add_customer);
                TextView btnClose = dialog.findViewById(R.id.close_dialog);

                LinearLayoutManager layoutManager = new LinearLayoutManager(CreateSaleActivity.this);
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
                        wareHouse = stockView.getShop_name();
                        edtStockPrice.setText(Theikdi.numberFormat(stockView.getSale_price()));

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
                        intent.setClass(getApplicationContext(), CreateCustomerActivity.class);
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
        textViewCustomerSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerDialog=new Dialog(CreateSaleActivity.this);
                customerDialog.setContentView(R.layout.dialog_search_spinner_customer);
                int myWidth = v.getWidth()*2;
                customerDialog.getWindow().setLayout(myWidth,1500);
                customerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                customerDialog.show();


                RecyclerView recyclerView = customerDialog.findViewById(R.id.list_view);
                SearchView searchView = customerDialog.findViewById(R.id.search_view_name);
                TextView btnAddCustomer = customerDialog.findViewById(R.id.go_to_add_customer);
                TextView btnClose = customerDialog.findViewById(R.id.close_dialog);

                LinearLayoutManager layoutManager = new LinearLayoutManager(CreateSaleActivity.this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);

                fetchCustomerData();

                customerAdapter = new CustomerAdapter(customerList);
                recyclerView.setAdapter(customerAdapter);

                customerAdapter.setOnItemClickListener(new CustomerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Customer customer) {
                        CUSTOMER_ID = customer.getCustomer_id();
                        edtCustomerName.setText(customer.getCustomer_name());
                        text_outstanding_qty.setText(Theikdi.numberFormat(customer.getOutstanding_gas_shell_qty()));
                        text_outstanding_amount.setText(Theikdi.numberFormat(customer.getOutstanding_amount()));

                        customerDialog.dismiss();
                    }
                });

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        //adapter.filter(query);
                        customerAdapter.getFilter().filter(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        //adapter.filter(newText);
                        customerAdapter.getFilter().filter(newText);
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
                        customerDialog.dismiss();
                    }
                });
            }
        });


        saveStockOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    int quantity = Integer.parseInt(edtStockOut.getText().toString());
                    int return_quantity = Integer.parseInt(edtReturnGasShellQty.getText().toString());
                    // text_outstanding_qty + quantity - return_quantity
                    int outstanding_quantity = Integer.parseInt(text_outstanding_qty.getText().toString().replace(",", "")) + quantity - return_quantity;
                    int price = Integer.parseInt(edtStockPrice.getText().toString().trim().replace(",", ""));
                    int totalAmount = Integer.parseInt(edtTotalAmount.getText().toString().replace(",", ""));
                    int received_amount = Integer.parseInt(edtReturnAmount.getText().toString());
                    // text_outstanding_amount + total - received_amount
                    int outstanding_amount = Integer.parseInt(text_outstanding_amount.getText().toString().replace(",", "")) + totalAmount - received_amount;
                    String sale_date = tv_date.getText().toString();
                    String due_date = Theikdi.getDueDate(sale_date);

                    Sale sale = new Sale(
                            STOCK_ID,
                            CUSTOMER_ID,
                            quantity,
                            return_quantity,
                            outstanding_quantity,
                            price,
                            totalAmount,
                            received_amount,
                            outstanding_amount,
                            sale_date,
                            due_date
                            );
                    saveSale(sale);
                }
            }
        });

    }

    private void saveSale(Sale sale) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.addSale(sale);
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
                    Toast.makeText(CreateSaleActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CreateSaleActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateForm() {
        if (edtCustomerName.getText().toString().trim().isEmpty()) {
            edtCustomerName.setError("Please select a customer");
            return false;
        }

        if (edtStockName.getText().toString().trim().isEmpty()) {
            edtStockName.setError("Please select a product");
            return false;
        }

        if (edtStockOut.getText().toString().trim().isEmpty()) {
            edtStockOut.setError("Please enter quantity");
            return false;
        } /*else {
            try {
                double quantity = Double.parseDouble(edtStockOut.getText().toString().trim());
                if (quantity <= 0) {
                    edtStockOut.setError("Quantity must be greater than 0");
                    return false;
                }
            } catch (NumberFormatException e) {
                edtStockOut.setError("Invalid quantity");
                return false;
            }
        }*/

        if (edtStockPrice.getText().toString().trim().isEmpty()) {
            edtStockPrice.setError("Price must be specified");
            return false;
        }

        if (edtReturnGasShellQty.getText().toString().trim().isEmpty()) {
            edtReturnAmount.setError("Please select a product");
            return false;
        }

        if (edtTotalAmount.getText().toString().trim().isEmpty()) {
            edtReturnAmount.setError("Please total amount");
            return false;
        }

        if (edtReturnAmount.getText().toString().trim().isEmpty()) {
            edtReturnAmount.setError("Please return amount");
            return false;
        }

        return true;
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


    private void fetchCustomerData() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Customer>> call = apiService.getCustomerList();
        call.enqueue(new Callback<List<Customer>>() {
            @Override
            public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                if (response.isSuccessful() && response.body()!= null) {
                    customerList.clear();
                    customerList.addAll(response.body());
                    customerAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(CreateSaleActivity.this,  "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Customer>> call, Throwable t) {
                Log.e("API Error", t.getMessage());
                Toast.makeText(CreateSaleActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



}