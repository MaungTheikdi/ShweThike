package com.theikdi.shwethike.stock;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.theikdi.shwethike.API.ApiService;
import com.theikdi.shwethike.API.RetrofitClient;
import com.theikdi.shwethike.R;
import com.theikdi.shwethike.barcode.ScannerActivity;
import com.theikdi.shwethike.model.Product;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateStockActivity extends AppCompatActivity {

    TextInputLayout textInputLayout1, textInputLayout2, textInputLayout3, textInputLayout4, textInputLayout5, textInputLayout6, textInputLayout7, textInputLayout8;
    TextInputEditText edtProductBarcode, edtProductName, edtPurchasePrice, edtSalesPrice, edtPurchaseQty, edtSalesQty, edtInStock, edtDescription;
    AppCompatButton btnAddProduct;

    ImageView imgQRCode;
    SharedPreferences sharedPreferences;
    int SHOP_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_stock);

        sharedPreferences = getSharedPreferences("USER_DETAILS", MODE_PRIVATE);

        SHOP_ID = sharedPreferences.getInt("shopId", -1);

        textInputLayout1 = findViewById(R.id.textInputLayout1);
        textInputLayout2 = findViewById(R.id.textInputLayout2);
        textInputLayout3 = findViewById(R.id.textInputLayout3);
        textInputLayout4 = findViewById(R.id.textInputLayout4);
        textInputLayout5 = findViewById(R.id.textInputLayout5);
        textInputLayout6 = findViewById(R.id.textInputLayout6);
        textInputLayout7 = findViewById(R.id.textInputLayout7);
        textInputLayout8 = findViewById(R.id.textInputLayout8);

        imgQRCode = findViewById(R.id.imgQRCode);
        
        edtProductBarcode = findViewById(R.id.edtProductBarcode);
        edtProductName = findViewById(R.id.edtProductName);
        edtPurchasePrice = findViewById(R.id.edtPurchasePrice);
        edtSalesPrice = findViewById(R.id.edtSalePrice);
        edtPurchaseQty = findViewById(R.id.edtPurchaseQuantity);
        edtSalesQty = findViewById(R.id.edtSaleQuantity);
        edtInStock = findViewById(R.id.edtInstock);
        edtDescription = findViewById(R.id.edtDescription);
        btnAddProduct = findViewById(R.id.btnAddProduct);

        edtProductBarcode.setText(String.valueOf(SHOP_ID));

        
        

        edtPurchaseQty.setVisibility(View.GONE);
        edtSalesQty.setVisibility(View.GONE);

        imgQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        });
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String barcode = edtProductBarcode.getText().toString();
                String name = edtProductName.getText().toString();
                String description = edtDescription.getText().toString();
                String purchasePriceStr = edtPurchasePrice.getText().toString();
                String salesPriceStr = edtSalesPrice.getText().toString();
                String inStockStr = edtInStock.getText().toString();
            
                if (barcode.isEmpty()) {
                    textInputLayout1.setError("Please enter the product barcode");
                } else if (name.isEmpty()) {
                    textInputLayout2.setError("Please enter the product name");
                } else if (purchasePriceStr.isEmpty()) {
                    textInputLayout3.setError("Please enter the purchase price");
                } else if (salesPriceStr.isEmpty()) {
                    textInputLayout4.setError("Please enter the sales price");
                } else if (inStockStr.isEmpty()) {
                    textInputLayout7.setError("Please enter the in-stock quantity");
                } else {
                    int purchasePrice = Integer.parseInt(purchasePriceStr);
                    int salesPrice = Integer.parseInt(salesPriceStr);
                    int inStock = Integer.parseInt(inStockStr);
                    Product product = new Product(barcode, name, description, purchasePrice, salesPrice, 0, 0, inStock, SHOP_ID);
                    saveProduct(product);
                }
            }
        });

    }

    public void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Press volume up button to turn on flash");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(ScannerActivity.class);
        barLauncher.launch(options);
    }
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result->{
        String barcodeStr = "-";
        if(result.getContents() != null){
            //result.getContents();
            //Toast.makeText(Home.this, "" + result.getContents(), Toast.LENGTH_SHORT).show();
            barcodeStr = result.getContents();
            //retrieveData(barcodeStr);
            edtProductBarcode.setText(barcodeStr);

        }else{
            //alertDia("Error", "Scan failed. Try again.");

            if(getIntent().hasExtra("shortcut")){
                finishAffinity();
            }
        }
    });

    private void saveProduct(Product product){
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.addProduct(product);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        String status = jsonResponse.getString("status");
                        String message = jsonResponse.getString("message");
                        if ("success".equals(status)) {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            clearText();
                        } else {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(StockActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateStockActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CreateStockActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearText(){
        edtProductBarcode.setText("");
        edtProductName.setText("");
        edtPurchasePrice.setText("");
        edtSalesPrice.setText("");
        edtInStock.setText("");

        textInputLayout1.setError(null);
        textInputLayout2.setError(null);
        textInputLayout3.setError(null);
        textInputLayout4.setError(null);
        textInputLayout7.setError(null);
    }

}