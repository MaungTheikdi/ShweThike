package com.theikdi.shwethike.supplier;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.theikdi.shwethike.R;
import com.theikdi.shwethike.model.Supplier;

import java.util.ArrayList;
import java.util.List;

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.ViewHolder> implements Filterable {
    private List<Supplier> supplierList;
    private List<Supplier> supplierListFiltered;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Supplier supplier);
    }
    public void setOnItemClickListener(SupplierAdapter.OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    public SupplierAdapter(List<Supplier> supplierList) {
        this.supplierList = supplierList;
        this.supplierListFiltered = supplierList;
    }
    @NonNull
    @Override
    public SupplierAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_supplier,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupplierAdapter.ViewHolder holder, int position) {
        Supplier supplier = supplierListFiltered.get(position);
        holder.supplierName.setText(supplier.getSupplier_name());
        holder.address.setText(supplier.getAddress());
        holder.phone.setText(supplier.getPhone());
        holder.amount.setText(String.valueOf(supplier.getOutstanding_amount()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!= null){
                    onItemClickListener.onItemClick(supplier);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return supplierListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    supplierListFiltered = supplierList;
                } else {
                    List<Supplier> filterList = new ArrayList<>();
                    for (Supplier row : supplierList){
                        if (row.getSupplier_name().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getAddress().contains(constraint) ||
                                row.getPhone().contains(constraint)){
                            filterList.add(row);
                        }
                    }
                    supplierListFiltered = filterList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = supplierListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                supplierListFiltered = (ArrayList<Supplier>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView supplierName, address, phone, amount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            supplierName = itemView.findViewById(R.id.supplierName);
            address = itemView.findViewById(R.id.address);
            phone = itemView.findViewById(R.id.phone);
            amount = itemView.findViewById(R.id.outstandingAmount);
        }
    }
}
