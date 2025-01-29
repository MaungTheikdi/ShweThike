package com.theikdi.shwethike.sale;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.theikdi.shwethike.R;
import com.theikdi.shwethike.model.Sale;
import com.theikdi.shwethike.util.Theikdi;

import java.util.List;

public class SaleAdapter extends RecyclerView.Adapter<SaleAdapter.ViewHolder> implements Filterable {
    private List<Sale> saleList;
    private List<Sale> saleListFiltered;
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(Sale sale);
    }
    public void setOnItemClickListener(SaleAdapter.OnItemClickListener listener){
        this.onItemClickListener = listener;
    }
    public SaleAdapter(List<Sale> saleList) {
        this.saleList = saleList;
        this.saleListFiltered = saleList;
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                return null;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

            }
        };
    }

    @NonNull
    @Override
    public SaleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_sale,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaleAdapter.ViewHolder holder, int position) {
        Sale sale = saleListFiltered.get(position);
        holder.date.setText(Theikdi.yyyymmddToMMMdd(sale.getDate()));
        holder.stockName.setText(sale.getProduct_name());

        holder.stockQuantity.setText(String.valueOf(sale.getQuantity()));
        holder.stockPrice.setText(Theikdi.numberFormat(sale.getUnit_price()));

        holder.stockDescription.setText(sale.getProduct_description());

        holder.customer_name.setText(sale.getCustomer_name());
        holder.totalAmount.setText(Theikdi.numberFormat(sale.getTotal_amount()));
        holder.warehouse.setText(sale.getShop_name());

        if(onItemClickListener!= null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(sale);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return saleListFiltered.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, stockName, stockDescription, stockQuantity, stockPrice, warehouse, totalAmount, customer_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.in_stock_date);
            stockName = itemView.findViewById(R.id.stock_name);
            stockDescription = itemView.findViewById(R.id.stock_description);
            stockQuantity = itemView.findViewById(R.id.stock_qty);
            stockPrice = itemView.findViewById(R.id.stock_price);
            warehouse = itemView.findViewById(R.id.stock_warehouse);
            totalAmount = itemView.findViewById(R.id.total_amount);
            customer_name = itemView.findViewById(R.id.customer_name);
        }
    }
}
