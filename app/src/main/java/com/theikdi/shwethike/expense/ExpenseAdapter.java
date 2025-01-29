package com.theikdi.shwethike.expense;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.theikdi.shwethike.R;
import com.theikdi.shwethike.model.Expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> implements Filterable {
    private List<Expense> expenses;
    private List<Expense> expensesFilter;
    private OnItemClickListener onItemClickListener;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()){
                    expensesFilter = expenses;
                } else {
                    List<Expense> filteredList = new ArrayList<>();
                    for (Expense row : expenses){
                        if (row.getUser_name().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getDescription().contains(constraint) ||
                                row.getDate().contains(constraint)){
                            filteredList.add(row);
                        }
                    }
                    expensesFilter = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = expensesFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                expensesFilter = (ArrayList<Expense>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public ExpenseAdapter(List<Expense> expenses) {
        this.expenses = expenses;
        this.expensesFilter = expenses;
    }
    public interface OnItemClickListener {
        void onItemClick(Expense expense);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }



    @NonNull
    @Override
    public ExpenseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_expense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapter.ViewHolder holder, int position) {
        Expense expense = expensesFilter.get(position);
        holder.username.setText(expense.getUser_name());
        holder.description.setText(expense.getDescription());
        holder.amount.setText(String.valueOf(expense.getAmount()));
        holder.date.setText(expense.getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!= null){
                    onItemClickListener.onItemClick(expense);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return expensesFilter.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, description, amount, date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.user_name);
            description = itemView.findViewById(R.id.description);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
        }
    }
}
