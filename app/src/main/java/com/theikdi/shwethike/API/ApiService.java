package com.theikdi.shwethike.API;

import com.theikdi.shwethike.APIResponse.LoginResponse;
import com.theikdi.shwethike.APIResponse.ResponseModel;
import com.theikdi.shwethike.model.Customer;
import com.theikdi.shwethike.model.DashboardResponse;
import com.theikdi.shwethike.model.Expense;
import com.theikdi.shwethike.model.Product;
import com.theikdi.shwethike.model.Purchase;
import com.theikdi.shwethike.model.Sale;
import com.theikdi.shwethike.model.StockView;
import com.theikdi.shwethike.model.Supplier;
import com.theikdi.shwethike.model.User;
import com.theikdi.shwethike.util.Theikdi;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("register.php")
    Call<ResponseBody> registerUser (@Body User user);
    @POST("login.php")
    Call<LoginResponse> loginUser (@Body User user);
    @POST("changePassword.php")
    Call<ResponseBody> changePassword (@Body User user);

    @GET("dashboard.php")
    Call<DashboardResponse> getDashboardData();

    @GET("products.php")
    Call<List<StockView>> getStockList();
    @POST("products.php")
    Call<ResponseBody> addProduct (@Body Product product);
    @PUT("products.php")
    Call<ResponseBody> updateProduct (@Body Product product);
    @DELETE("products.php")
    Call<ResponseBody> deleteProduct (@Query("product_id") int productId,@Query("secret_code") String secretCode);
    @GET("products.php")
    Call<List<StockView>> getProductsByShop (@Query("shop_id") int shopId);


    @GET("customers.php")
    Call<List<Customer>> getCustomerList();
    @POST("customers.php")
    Call<ResponseBody> createCustomer (@Body Customer customer);
    @PUT("customers.php")
    Call<ResponseBody> updateCustomer (@Body Customer customer);
    @DELETE("customers.php")
    Call<ResponseBody> deleteCustomer (@Body Customer customer);
    @GET("customers.php")
    Call<List<Customer>> getCustomerByCredit (
            @Query("outstanding_gas_shell_qty") int outstanding_gas_shell_qty,
            @Query("outstanding_amount") int outstanding_amount);


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
    @GET("sales.php")
    Call<List<Sale>> getHistoryWithProductSale (@Query("product_id") int product);
    @GET("sales.php")
    Call<List<Sale>> getSaleByDate (@Query("date") String date);
    @GET("sales.php")
    Call<List<Sale>> getSales ();
    @DELETE("sales.php")
    Call<ResponseBody> deleteSale (@Query("sale_id") int sale_id, @Query("secret_code") String secretCode);


    @POST("purchases.php")
    Call<ResponseBody> addPurchase (@Body Purchase purchase);
    @GET("purchases.php")
    Call<List<Purchase>> getHistoryWithProductPurchase (@Query("product_id") int product);
    @GET("purchases.php")
    Call<List<Purchase>> getPurchaseByDate (@Query("date") String date);
    @GET("purchases.php")
    Call<List<Purchase>> getPurchases ();
    @DELETE("purchases.php")
    Call<ResponseBody> deletePurchase (@Query("purchase_id") int purchase_id, @Query("secret_code") String secretCode);


    @POST("expenses.php")
    Call<ResponseBody> addExpenses (@Body Expense expense);
    @GET("expenses.php")
    Call<List<Expense>> getExpensesList();
    @PUT("expenses.php")
    Call<ResponseBody> updateExpense (@Body Expense expense);
    @DELETE("expenses.php")
    Call<ResponseBody> deleteExpense (@Query("expense_id") int expense_id, @Query("secret_code") String secretCode);


}
