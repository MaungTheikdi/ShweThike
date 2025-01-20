package com.theikdi.shwethike.supplier;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theikdi.shwethike.API.ApiService;
import com.theikdi.shwethike.API.RetrofitClient;
import com.theikdi.shwethike.R;
import com.theikdi.shwethike.customers.CreateCustomerActivity;
import com.theikdi.shwethike.customers.CustomerListActivity;
import com.theikdi.shwethike.model.Customer;
import com.theikdi.shwethike.model.Supplier;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupplierListActivity extends AppCompatActivity {

    SearchView searchView;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    SupplierAdapter supplierAdapter;
    List<Supplier> supplierList;
    FloatingActionButton fabButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_supplier_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        searchView = findViewById(R.id.search_view);
        recyclerView = findViewById(R.id.recyclerview);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        fabButton = findViewById(R.id.fabButton);

        supplierList = new ArrayList<>();
        // Initialize RecyclerView and Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        supplierAdapter = new SupplierAdapter(supplierList);
        recyclerView.setAdapter(supplierAdapter);

        // Fetch Supplier data from API
        fetchSupplierData();

        // Set up SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                supplierAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                supplierAdapter.getFilter().filter(newText);
                return false;
            }
        });
        supplierAdapter.setOnItemClickListener(new SupplierAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Supplier supplier) {
                bottomSheetDialog(supplier);
            }
        });

        // Set up SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchSupplierData();
            swipeRefreshLayout.setRefreshing(false);
        });

        fabButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(SupplierListActivity.this, CreateSupplierActivity.class);
            startActivity(intent);
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
                    // Handle error
                }
            }

            @Override
            public void onFailure(Call<List<Supplier>> call, Throwable t) {

            }
        });
    }

    private void bottomSheetDialog(Supplier supplier) {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(SupplierListActivity.this, R.style.TheikdiBottomSheetDialog);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_customer_details, null);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

        TextView customerName = bottomSheetView.findViewById(R.id.customerName);
        TextView customerCategory = bottomSheetView.findViewById(R.id.customerCategory);
        TextView address = bottomSheetView.findViewById(R.id.address);
        TextView phone = bottomSheetView.findViewById(R.id.phone);
        TextView outstandingAmount = bottomSheetView.findViewById(R.id.outstandingAmount);
        Button buttonUpdate = bottomSheetView.findViewById(R.id.update);


        customerName.setText(supplier.getSupplier_name());
        customerCategory.setText("ပေးရန် အိုးခွံ: " + String.valueOf(supplier.getOutstanding_gas_shell_qty()));
        address.setText(supplier.getAddress());
        phone.setText(supplier.getPhone());
        outstandingAmount.setText("ပေးရန်ငွေ: " + String.valueOf(supplier.getOutstanding_amount()));

        buttonUpdate.setOnClickListener(view -> {
            // get the customer data and carry to CreateCustomerActivity
            Intent intent = new Intent(SupplierListActivity.this, CreateSupplierActivity.class);
            intent.putExtra("supplier_id", supplier.getSupplier_id());
            intent.putExtra("supplier_name", supplier.getSupplier_name());
            intent.putExtra("supplier_address", supplier.getAddress());
            intent.putExtra("supplier_phone", supplier.getPhone());
            intent.putExtra("outstanding_qty", supplier.getOutstanding_gas_shell_qty());
            intent.putExtra("outstanding_amount", supplier.getOutstanding_amount());
            intent.putExtra("due_date", supplier.getDue_date());
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
}