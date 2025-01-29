package com.theikdi.shwethike.stock;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.theikdi.shwethike.R;
import com.theikdi.shwethike.model.Customer;
import com.theikdi.shwethike.model.StockView;
import com.theikdi.shwethike.util.Theikdi;
import java.util.ArrayList;
import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> implements Filterable {

    private List<StockView> stockViewsFull; // Original list
    private List<StockView> stockViewsFiltered;  // Filtered list
    private OnItemClickListener onItemClickListener;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    stockViewsFiltered = stockViewsFull;
                } else {
                    List<StockView> filteredList = new ArrayList<>();
                    for (StockView row : stockViewsFull) {
                        if (row.getProduct_name().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getDescription().contains(constraint)) {
                            filteredList.add(row);
                        }
                    }

                    stockViewsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = stockViewsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                stockViewsFiltered = (ArrayList<StockView>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnItemClickListener {
        void onItemClick(StockView stockView);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public StockAdapter(List<StockView> stockViews) {
        this.stockViewsFull = stockViews;
        this.stockViewsFiltered = stockViews; // Initialize filtered list
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_stock, parent, false);
        return new StockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        StockView stockView = stockViewsFiltered.get(position);

        holder.product_name.setText(stockView.getProduct_name());
        holder.description.setText(stockView.getDescription());
        holder.sale_price.setText(Theikdi.numberFormat(stockView.getSale_price()));
        holder.product_barcode.setText(stockView.getProduct_barcode());
        holder.shop_name.setText(stockView.getShop_name());
        holder.in_stock.setText(String.valueOf(stockView.getInstock()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null){
                    onItemClickListener.onItemClick(stockView);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return stockViewsFiltered.size();
    }

    public static class StockViewHolder extends RecyclerView.ViewHolder {
        TextView product_name, description, sale_price, product_barcode, shop_name, in_stock;

        public StockViewHolder(@NonNull View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.product_name);
            description = itemView.findViewById(R.id.description);
            sale_price = itemView.findViewById(R.id.sale_price);
            product_barcode = itemView.findViewById(R.id.product_barcode);
            shop_name = itemView.findViewById(R.id.shop_name);
            in_stock = itemView.findViewById(R.id.in_stock);
        }
    }

}

