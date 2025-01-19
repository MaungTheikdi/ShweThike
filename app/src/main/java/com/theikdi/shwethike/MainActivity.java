package com.theikdi.shwethike;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;
import com.theikdi.shwethike.customers.CustomerListActivity;
import com.theikdi.shwethike.sale.CreateSaleActivity;
import com.theikdi.shwethike.stock.CreateStockActivity;
import com.theikdi.shwethike.stock.StockViewAllActivity;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String USER_NAME;
    int SHOP_ID, USER_ID;

    CardView cardStock, cardCustomer;
    MaterialCardView cardViewAll;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cardCustomer = findViewById(R.id.cardCustomer);
        cardViewAll = findViewById(R.id.cardViewAll);

        sharedPreferences = getSharedPreferences("USER_DETAILS", MODE_PRIVATE);

        // Check if user is logged in
        if (!sharedPreferences.getBoolean("is_logged_in", false)) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            USER_NAME = sharedPreferences.getString("user_name", "");
            SHOP_ID = sharedPreferences.getInt("shopId", -1);
            USER_ID = sharedPreferences.getInt("userId", -1);
        }

        intent = new Intent();

        cardStock = findViewById(R.id.cardStock);
        cardStock.setOnClickListener(v -> {
            intent.setClass(MainActivity.this, CreateStockActivity.class);
            startActivity(intent);
            //finish();
        });

        ImageView logoutButton = (ImageView) findViewById(R.id.logoutImageView);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });

        cardCustomer.setOnClickListener(v -> {
            intent.setClass(MainActivity.this, CustomerListActivity.class);
            startActivity(intent);
            //finish();
        });

        cardViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(MainActivity.this, StockViewAllActivity.class);
                startActivity(intent);
            }
        });

        MaterialCardView cardSale = (MaterialCardView) findViewById(R.id.cardSale);
        cardSale.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateSaleActivity.class);
            startActivity(intent);
        });
    }

    private void Logout(){
        sharedPreferences.edit().clear().apply();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}