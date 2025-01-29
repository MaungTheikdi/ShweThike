package com.theikdi.shwethike;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.theikdi.shwethike.API.ApiService;
import com.theikdi.shwethike.API.RetrofitClient;
import com.theikdi.shwethike.model.User;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordChangeActivity extends AppCompatActivity {

    TextInputEditText userName, password;
    Button btnUpdate;
    SharedPreferences sharedPreferences;
    String USER_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_password_change);


        sharedPreferences = getSharedPreferences("USER_DETAILS", MODE_PRIVATE);
        USER_NAME = sharedPreferences.getString("username", "");

        userName = findViewById(R.id.user_name);
        password = findViewById(R.id.password);
        btnUpdate = findViewById(R.id.btn_update);

        userName.setText(USER_NAME);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String USERNAME = userName.getText().toString();
                String PASSWORD = password.getText().toString();
                String SECRET_CODE = RetrofitClient.SECRET_KEY;

                User user = new User(USERNAME, PASSWORD, SECRET_CODE);
                updateUserPassword(user);
            }
        });
    }

    private void updateUserPassword(User user) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.changePassword(user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        String status = jsonResponse.getString("status");
                        String message = jsonResponse.getString("message");
                        if ("success".equals(status)) {
                            Toast.makeText(PasswordChangeActivity.this, message, Toast.LENGTH_SHORT).show();
                            Logout();
                        } else {
                            Toast.makeText(PasswordChangeActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(PasswordChangeActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PasswordChangeActivity.this, "Update failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(PasswordChangeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Logout(){
        sharedPreferences.edit().clear().apply();
        Intent intent = new Intent(PasswordChangeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}