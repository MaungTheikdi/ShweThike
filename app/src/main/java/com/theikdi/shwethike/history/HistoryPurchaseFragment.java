package com.theikdi.shwethike.history;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.theikdi.shwethike.API.ApiService;
import com.theikdi.shwethike.API.RetrofitClient;
import com.theikdi.shwethike.R;
import com.theikdi.shwethike.model.Purchase;
import com.theikdi.shwethike.model.Sale;
import com.theikdi.shwethike.purchase.PurchaseAdapter;
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


public class HistoryPurchaseFragment extends Fragment {

    RecyclerView recyclerView;
    PurchaseAdapter adapter;
    List<Purchase> purchaseList;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView tvNoData;

    int PRODUCT_ID;


    public static HistoryPurchaseFragment newInstance(String param1, String param2) {
        HistoryPurchaseFragment fragment = new HistoryPurchaseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_purchase, container, false);

        tvNoData = view.findViewById(R.id.no_data_list);
        tvNoData.setVisibility(View.GONE);

        PRODUCT_ID = getActivity().getIntent().getIntExtra("product_id",-1);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (PRODUCT_ID == -1) {
                    fetchPurchases();
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    fetchPurchase(PRODUCT_ID);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        recyclerView = view.findViewById(R.id.recycler_view);
        purchaseList = new ArrayList<>();
        adapter = new PurchaseAdapter(purchaseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        if (PRODUCT_ID == -1) {
            fetchPurchases();
        } else {
            fetchPurchase(PRODUCT_ID);
        }

        adapter.setOnItemClickListener(new PurchaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Purchase purchase) {
                bottomSheetDialog(purchase);
            }
        });

        return view;
    }

    private void fetchPurchase(int productId) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Purchase>> call = apiService.getHistoryWithProductPurchase(productId);
        call.enqueue(new Callback<List<Purchase>>() {
            @Override
            public void onResponse(Call<List<Purchase>> call, Response<List<Purchase>> response) {
                if (response.isSuccessful() && response.body() != null){
                    purchaseList.clear();
                    purchaseList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    if (purchaseList.isEmpty()){
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        tvNoData.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(getContext(), "Error: Unable to fetch data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Purchase>> call, Throwable t) {
                Toast.makeText(getContext(), "Failure:"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchPurchases(){
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Purchase>> call = apiService.getPurchases();
        call.enqueue(new Callback<List<Purchase>>() {
            @Override
            public void onResponse(Call<List<Purchase>> call, Response<List<Purchase>> response) {
                if (response.isSuccessful() && response.body()!= null){
                    purchaseList.clear();
                    purchaseList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    if (purchaseList.isEmpty()){
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        tvNoData.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(getContext(), "Error: Unable to fetch data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Purchase>> call, Throwable t) {
                Toast.makeText(getContext(), "Failure:"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void deleteDialog(Purchase sale) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(sale.getProduct_name());
        builder.setMessage(sale.getSupplier_name() +"\n" + sale.getTotal_amount() + "\n ဖျက်ပါက Supplier အကြွေး နှင့် ရရန် အိုးခွံ ပြင်ရန် မမေ့ပါနဲ့ ");
        builder.setPositiveButton("Cancel", (dialog, which) -> {

        });

        builder.setNeutralButton("Delete", (dialog, which) -> {
            deleteSale(sale.getPurchase_id());
        });
        builder.create().show();
    }
    private void deleteSale(int saleId) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.deletePurchase(saleId, RetrofitClient.SECRET_KEY);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        String status = jsonResponse.getString("status");
                        String message = jsonResponse.getString("message");
                        if ("success".equals(status)) {
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            if (PRODUCT_ID == -1) {
                                fetchPurchases();
                            } else {
                                fetchPurchase(PRODUCT_ID);
                            }
                        } else {
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(StockActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Response fail: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void bottomSheetDialog(Purchase sale) {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.TheikdiBottomSheetDialog);
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


        TextView status = bottomSheetView.findViewById(R.id.info_text);
        status.setText("ဖျက်ပါက Supplier အကြွေး နှင့် ပေးရန် အိုးခွံ ပြင်ရန် မမေ့ပါနဲ့ ");

        buttonUpdate.setText("Delete");

        customerName.setText(sale.getProduct_name());
        customerCategory.setText(sale.getSupplier_name());
        address.setText(Theikdi.numberFormat(sale.getPurchase_quantity()) + " x " + Theikdi.numberFormat(sale.getPurchase_price()));
        phone.setText(Theikdi.numberFormat(sale.getTotal_amount()) + " Ks");
        outstandingAmount.setText(sale.getShop_name());

        buttonUpdate.setOnClickListener(view -> {
            deleteDialog(sale);
            bottomSheetDialog.dismiss();
            // updateCustomer(customer);
        });


    }

}