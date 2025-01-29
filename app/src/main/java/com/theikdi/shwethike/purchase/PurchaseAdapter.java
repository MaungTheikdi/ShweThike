package com.theikdi.shwethike.purchase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.theikdi.shwethike.R;
import com.theikdi.shwethike.model.Purchase;
import com.theikdi.shwethike.util.Theikdi;

import java.util.List;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.ViewHolder> implements Filterable {
    private List<Purchase> purchaseList;
    private List<Purchase> purchaseListFiltered;
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(Purchase purchase);
    }
    public void setOnItemClickListener(PurchaseAdapter.OnItemClickListener listener){
        this.onItemClickListener = listener;
    }
    public PurchaseAdapter(List<Purchase> purchaseList) {
        this.purchaseList = purchaseList;
        this.purchaseListFiltered = purchaseList;
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
    public PurchaseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_purchase, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseAdapter.ViewHolder holder, int position) {
        Purchase purchase = purchaseListFiltered.get(position);
        holder.date.setText(Theikdi.yyyymmddToMMMdd(purchase.getPurchase_date()));
        holder.stockName.setText(purchase.getProduct_name());
        holder.stockDescription.setText(purchase.getProduct_description());
        holder.stockQuantity.setText(String.valueOf(purchase.getPurchase_quantity()));
        holder.stockPrice.setText(Theikdi.numberFormat(purchase.getPurchase_price()));

        holder.supplier_name.setText(purchase.getSupplier_name());
        holder.totalAmount.setText(Theikdi.numberFormat(purchase.getTotal_amount()));
        holder.warehouse.setText(purchase.getShop_name());

        //holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(purchase));
        if(onItemClickListener!= null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(purchase);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return purchaseListFiltered.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, stockName, stockDescription, stockQuantity, stockPrice, warehouse, totalAmount, supplier_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.in_stock_date);
            stockName = itemView.findViewById(R.id.stock_name);
            stockDescription = itemView.findViewById(R.id.stock_description);
            stockQuantity = itemView.findViewById(R.id.stock_qty);
            stockPrice = itemView.findViewById(R.id.stock_price);
            warehouse = itemView.findViewById(R.id.stock_warehouse);
            totalAmount = itemView.findViewById(R.id.total_amount);
            supplier_name = itemView.findViewById(R.id.supplier_name);
        }
    }
}
