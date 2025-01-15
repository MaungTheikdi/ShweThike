package com.theikdi.shwethike.API;

import com.theikdi.shwethike.APIResponse.LoginResponse;
import com.theikdi.shwethike.model.Customer;
import com.theikdi.shwethike.model.Product;
import com.theikdi.shwethike.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("register.php")
    Call<ResponseBody> registerUser (@Body User user);

    @POST("login.php")
    Call<LoginResponse> loginUser (@Body User user);

    @POST("products.php")
    Call<ResponseBody> addProduct (@Body Product product);

    @GET("customers.php")
    Call<List<Customer>> getCustomerList();

    @POST("customers.php")
    Call<ResponseBody> createCustomer (@Body Customer customer);

    /*@PUT("customers.php/{customer_id}")
    Call<ResponseBody> updateCustomer (@Body Customer customer, @Path("customer_id") int customerId);
     */
    @PUT("customers.php")
    Call<ResponseBody> updateCustomer (@Body Customer customer);
}
