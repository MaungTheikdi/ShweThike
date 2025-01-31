package com.theikdi.shwethike.reports;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
import com.theikdi.shwethike.customers.CreateCustomerActivity;
import com.theikdi.shwethike.customers.CustomerAdapter;
import com.theikdi.shwethike.customers.CustomerListActivity;
import com.theikdi.shwethike.model.Customer;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerCreditActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    SearchView searchView;

    RecyclerView recyclerView;
    CustomerAdapter customerAdapter;
    List<Customer> customers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_credit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerview);
        searchView = findViewById(R.id.search_view_name);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        customers = new ArrayList<>();
        customerAdapter = new CustomerAdapter(customers);

        recyclerView.setAdapter(customerAdapter);

        fetchCustomerData(1,1);

        customerAdapter.setOnItemClickListener(new CustomerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Customer customer) {
                bottomSheetDialog(customer);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchCustomerData(1,1);
            swipeRefreshLayout.setRefreshing(false);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                customerAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customerAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void bottomSheetDialog(Customer customer) {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(CustomerCreditActivity.this, R.style.TheikdiBottomSheetDialog);
        //View bottomSheetView = LayoutInflater.from(this).inflate( R.layout.bottom_sheet_customer_details, false);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_customer_details, null);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

        TextView customerName = bottomSheetView.findViewById(R.id.customerName);
        TextView customerCategory = bottomSheetView.findViewById(R.id.customerCategory);
        TextView address = bottomSheetView.findViewById(R.id.address);
        TextView phone = bottomSheetView.findViewById(R.id.phone);
        TextView outstandingAmount = bottomSheetView.findViewById(R.id.outstandingAmount);
        Button buttonUpdate = bottomSheetView.findViewById(R.id.update);


        customerName.setText(customer.getCustomer_name());
        customerCategory.setText(customer.getCustomer_category());
        address.setText(customer.getAddress());
        phone.setText(customer.getPhone());
        outstandingAmount.setText("Outstanding Amount: " + String.valueOf(customer.getOutstanding_amount()));

        buttonUpdate.setOnClickListener(view -> {
            // get the customer data and carry to CreateCustomerActivity
            Intent intent = new Intent(CustomerCreditActivity.this, CreateCustomerActivity.class);
            intent.putExtra("customer_id", customer.getCustomer_id());
            intent.putExtra("customer_name", customer.getCustomer_name());
            intent.putExtra("customer_category", customer.getCustomer_category());
            intent.putExtra("address", customer.getAddress());
            intent.putExtra("phone", customer.getPhone());
            intent.putExtra("gas_shell_qty", customer.getOutstanding_gas_shell_qty());
            intent.putExtra("outstanding_amount", customer.getOutstanding_amount());
            intent.putExtra("due_date", customer.getDue_date());
            startActivity(intent);
            bottomSheetDialog.dismiss();
            // updateCustomer(customer);
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Phone call to customer
                String phoneNumber = phone.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            }
        });
    }

    private void fetchCustomerData(int outstandingQty, int outstandingAmount) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Customer>> call = apiService.getCustomerByCredit(outstandingQty,outstandingAmount);
        call.enqueue(new Callback<List<Customer>>() {
            @Override
            public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                if (response.isSuccessful() && response.body()!= null) {
                    customers.clear();
                    customers.addAll(response.body());
                    customerAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(CustomerCreditActivity.this,  "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Customer>> call, Throwable t) {
                Log.e("API Error", t.getMessage());
                Toast.makeText(CustomerCreditActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchCustomerData(1,1);
    }
}