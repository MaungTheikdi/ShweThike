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
import com.theikdi.shwethike.model.Sale;
import com.theikdi.shwethike.sale.SaleAdapter;
import com.theikdi.shwethike.util.Theikdi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TodaySaleFragment extends Fragment {
    RecyclerView recyclerView;
    SaleAdapter adapter;
    List<Sale> saleList;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView tvNoData;

    //int PRODUCT_ID;
    String date;

    public static TodaySaleFragment newInstance(String param1, String param2) {
        TodaySaleFragment fragment = new TodaySaleFragment();
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
        View view = inflater.inflate(R.layout.fragment_today_sale, container, false);

        tvNoData = view.findViewById(R.id.no_data_list);
        tvNoData.setVisibility(View.GONE);

        date = Theikdi.currentDate();
        //PRODUCT_ID = getActivity().getIntent().getIntExtra("product_id",-1);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchSales(date);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        recyclerView = view.findViewById(R.id.recycler_view);
        saleList = new ArrayList<>();
        adapter = new SaleAdapter(saleList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        if (!date.isEmpty()) {
            fetchSales(date);
        }


        return view;
    }

    private void fetchSales(String date) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Sale>> call = apiService.getSaleByDate(date);
        call.enqueue(new Callback<List<Sale>>() {
            @Override
            public void onResponse(Call<List<Sale>> call, Response<List<Sale>> response) {
                if (response.isSuccessful() && response.body() != null){
                    saleList.clear();
                    saleList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    if (saleList.isEmpty()){
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        tvNoData.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(getContext(), "Error: Unable to fetch data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Sale>> call, Throwable t) {
                Toast.makeText(getContext(), "Failure:"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}