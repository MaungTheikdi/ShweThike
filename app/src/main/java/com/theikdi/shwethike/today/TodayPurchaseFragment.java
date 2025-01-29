package com.theikdi.shwethike.today;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.theikdi.shwethike.API.ApiService;
import com.theikdi.shwethike.API.RetrofitClient;
import com.theikdi.shwethike.R;
import com.theikdi.shwethike.model.Purchase;
import com.theikdi.shwethike.purchase.PurchaseAdapter;
import com.theikdi.shwethike.util.Theikdi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TodayPurchaseFragment extends Fragment {
    RecyclerView recyclerView;
    PurchaseAdapter adapter;
    List<Purchase> purchaseList;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView tvNoData;

    //int PRODUCT_ID;
    String date;

    public static TodayPurchaseFragment newInstance(String param1, String param2) {
        TodayPurchaseFragment fragment = new TodayPurchaseFragment();
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
        View view = inflater.inflate(R.layout.fragment_today_purchase, container, false);

        tvNoData = view.findViewById(R.id.no_data_list);
        tvNoData.setVisibility(View.GONE);


        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //date = sdf.format(new java.util.Date());
        date = Theikdi.currentDate();


        //PRODUCT_ID = getActivity().getIntent().getIntExtra("product_id",-1);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPurchase(date);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        recyclerView = view.findViewById(R.id.recycler_view);
        purchaseList = new ArrayList<>();
        adapter = new PurchaseAdapter(purchaseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        if (!date.isEmpty()) {
            fetchPurchase(date);
        }


        return view;
    }

    private void fetchPurchase(String date) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Purchase>> call = apiService.getPurchaseByDate(date);
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
}