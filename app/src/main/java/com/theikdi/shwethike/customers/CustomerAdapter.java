package com.theikdi.shwethike.customers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.theikdi.shwethike.R;
import com.theikdi.shwethike.model.Customer;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {
    private List<Customer> customers;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(Customer customer);
    }
    public void setOnItemClickListener(CustomerAdapter.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public CustomerAdapter(List<Customer> customers) {
        this.customers = customers;
    }

    @NonNull
    @Override
    public CustomerAdapter.CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerAdapter.CustomerViewHolder holder, int position) {
        Customer customer = customers.get(position);
        holder.customerName.setText(customer.getCustomer_name());
        holder.customerCategory.setText(customer.getCustomer_category());
        holder.address.setText(customer.getAddress());
        holder.phone.setText(customer.getPhone());
        holder.outstandingShellQty.setText("ရရန်အိုးခွံ - " + String.valueOf(customer.getOutstanding_gas_shell_qty()));
        holder.outstandingAmount.setText("ရရန်ငွေ - "+String.valueOf(customer.getOutstanding_amount()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null){
                    onItemClickListener.onItemClick(customer);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView customerName, customerCategory, address, phone, outstandingShellQty, outstandingAmount;
        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customerName);
            customerCategory = itemView.findViewById(R.id.customerCategory);
            address = itemView.findViewById(R.id.address);
            phone = itemView.findViewById(R.id.phone);
            outstandingShellQty = itemView.findViewById(R.id.outstandingShellQty);
            outstandingAmount = itemView.findViewById(R.id.outstandingAmount);
        }
    }


}
