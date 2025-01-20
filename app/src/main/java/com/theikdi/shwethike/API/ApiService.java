package com.theikdi.shwethike.API;

import com.theikdi.shwethike.APIResponse.LoginResponse;
import com.theikdi.shwethike.model.Customer;
import com.theikdi.shwethike.model.Expense;
import com.theikdi.shwethike.model.Product;
import com.theikdi.shwethike.model.Purchase;
import com.theikdi.shwethike.model.Sale;
import com.theikdi.shwethike.model.StockView;
import com.theikdi.shwethike.model.Supplier;
import com.theikdi.shwethike.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("register.php")
    Call<ResponseBody> registerUser (@Body User user);
    @POST("login.php")
    Call<LoginResponse> loginUser (@Body User user);

    @GET("products.php")
    Call<List<StockView>> getStockList();
    @POST("products.php")
    Call<ResponseBody> addProduct (@Body Product product);


    @GET("customers.php")
    Call<List<Customer>> getCustomerList();
    @POST("customers.php")
    Call<ResponseBody> createCustomer (@Body Customer customer);
    @PUT("customers.php")
    Call<ResponseBody> updateCustomer (@Body Customer customer);
    @DELETE("customers.php")
    Call<ResponseBody> deleteCustomer (@Body Customer customer);

    @GET("suppliers.php")
    Call<List<Supplier>> getSupplierList();
    @POST("suppliers.php")
    Call<ResponseBody> createSupplier (@Body Supplier supplier);
    @PUT("suppliers.php")
    Call<ResponseBody> updateSupplier (@Body Supplier supplier);
    @DELETE("suppliers.php")
    Call<ResponseBody> deleteSupplier (@Path("supplier_id") int supplierId);




    @POST("sales.php")
    Call<ResponseBody> addSale (@Body Sale sale);

    @POST("purchases.php")
    Call<ResponseBody> addPurchase (@Body Purchase purchase);


    @POST("expenses.php")
    Call<ResponseBody> addExpenses (@Body Expense expense);
    @GET("expenses.php")
    Call<List<Expense>> getExpensesList();
    @PUT("expenses.php")
    Call<ResponseBody> updateExpense (@Body Expense expense);
    @DELETE("expenses.php")
    Call<ResponseBody> deleteExpense (@Path("expense_id") int expenseId);

}
