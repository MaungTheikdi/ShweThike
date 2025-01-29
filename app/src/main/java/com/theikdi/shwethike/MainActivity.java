package com.theikdi.shwethike;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;
import com.nambimobile.widgets.efab.ExpandableFabLayout;
import com.nambimobile.widgets.efab.FabOption;
import com.theikdi.shwethike.customers.CustomerListActivity;
import com.theikdi.shwethike.dashboard.DashboardActivity;
import com.theikdi.shwethike.expense.ExpenseListActivity;
import com.theikdi.shwethike.history.HistoryActivity;
import com.theikdi.shwethike.purchase.CreatePurchaseActivity;
import com.theikdi.shwethike.reports.CustomerCreditActivity;
import com.theikdi.shwethike.reports.ShopActivity;
import com.theikdi.shwethike.reports.WarehouseActivity;
import com.theikdi.shwethike.sale.CreateSaleActivity;
import com.theikdi.shwethike.sale.SaleListActivity;
import com.theikdi.shwethike.stock.CreateStockActivity;
import com.theikdi.shwethike.stock.StockViewAllActivity;
import com.theikdi.shwethike.supplier.SupplierListActivity;
import com.theikdi.shwethike.today.TodayActivity;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String USER_NAME;
    int SHOP_ID, USER_ID;

    CardView cardStock, cardCustomer;
    MaterialCardView cardViewAll;

    private Intent intent;

    private ExpandableFabLayout expandable_fab_layout;
    private FabOption createSaleOption, createPurchaseOption, createStockOption;

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
        TextView textShopInfo = (TextView) findViewById(R.id.text_shop_info);
        LinearLayout linearLayoutCreate = (LinearLayout) findViewById(R.id.linearLayoutCreate);
        //textShopInfo.setVisibility(View.GONE);
        //linearLayoutCreate.setVisibility(View.GONE);
        expandable_fab_layout = findViewById(R.id.expandable_fab_layout);
        expandable_fab_layout.setVisibility(View.GONE);

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

        ImageView imgBtnChangePass;
        imgBtnChangePass = findViewById(R.id.imgBtnChangePass);
        imgBtnChangePass.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PasswordChangeActivity.class);
            startActivity(intent);
        });

        ImageView imgBtnNotification;
        imgBtnNotification = findViewById(R.id.imgBtnNotification);
        imgBtnNotification.setOnClickListener(v->{
            intent.setClass(MainActivity.this, CustomerCreditActivity.class);
            startActivity(intent);
        });

        ImageView imgBtnDashboard;
        imgBtnDashboard = findViewById(R.id.imgBtnDashboard);
        imgBtnDashboard.setOnClickListener(v -> {
            intent.setClass(MainActivity.this, DashboardActivity.class);
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

        MaterialCardView cardSupplier = (MaterialCardView) findViewById(R.id.cardSupplier);
        cardSupplier.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SupplierListActivity.class);
            startActivity(intent);
        });

        MaterialCardView cardPurchase = (MaterialCardView) findViewById(R.id.cardPurchase);
        cardPurchase.setOnClickListener(v -> {
            intent.setClass(MainActivity.this, CreatePurchaseActivity.class);
            startActivity(intent);
        });

        MaterialCardView cardExpenses;
        cardExpenses = findViewById(R.id.cardExpenses);
        cardExpenses.setOnClickListener(v -> {
            intent.setClass(MainActivity.this, ExpenseListActivity.class);
            startActivity(intent);
        });

        MaterialCardView cardWarehouse;
        cardWarehouse = findViewById(R.id.cardWarehouse);
        cardWarehouse.setOnClickListener(v -> {
            intent.setClass(MainActivity.this, WarehouseActivity.class);
            startActivity(intent);
        });

        MaterialCardView cardShop;
        cardShop = findViewById(R.id.cardShop);
        cardShop.setOnClickListener(v -> {
            intent.setClass(MainActivity.this, ShopActivity.class);
            startActivity(intent);
        });

        MaterialCardView cardToday;
        cardToday = findViewById(R.id.cardToday);
        cardToday.setOnClickListener(v -> {
            intent.setClass(MainActivity.this, TodayActivity.class);
            startActivity(intent);
        });

        MaterialCardView cardHistory;
        cardHistory = findViewById(R.id.cardHistory);
        cardHistory.setOnClickListener(v -> {
            intent.setClass(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
        MaterialCardView cardCustomerCredit;
        cardCustomerCredit = findViewById(R.id.cardCustomerCredit);
        cardCustomerCredit.setOnClickListener(v -> {
            intent.setClass(MainActivity.this, CustomerCreditActivity.class);
            startActivity(intent);
        });

        createSaleOption = findViewById(R.id.create_sale_fabOption);
        createPurchaseOption = findViewById(R.id.create_purchase_fabOption);
        createStockOption = findViewById(R.id.create_stock_fabOption);

        createSaleOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(MainActivity.this, CreateSaleActivity.class);
                startActivity(intent);
            }
        });

        createPurchaseOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(MainActivity.this, CreatePurchaseActivity.class);
                startActivity(intent);
            }
        });

        createStockOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(MainActivity.this, CreateStockActivity.class);
                startActivity(intent);
            }
        });
    }

    private void Logout(){
        sharedPreferences.edit().clear().apply();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}