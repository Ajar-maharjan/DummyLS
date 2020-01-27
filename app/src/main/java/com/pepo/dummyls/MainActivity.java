package com.pepo.dummyls;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pepo.dummyls.API.DummyAPI;
import com.pepo.dummyls.Model.User;
import com.pepo.dummyls.ServerResponse.UserResponse;
import com.pepo.dummyls.StrictMode.StrictModeClass;
import com.pepo.dummyls.URL.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin, btnSignup;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    signUp();
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    StrictModeClass.StrictMode();
                    if(logIn()) {
                        Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MainActivity.this, "unable to login", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    private void signUp() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        User user = new User(username,password);
        DummyAPI dummyAPI = URL.getInstance().create(DummyAPI.class);
        Call<UserResponse> signUpCall = dummyAPI.registerUser(user);

        signUpCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(!response.isSuccessful()){
                Toast.makeText(MainActivity.this, "code : "+ response.code(), Toast.LENGTH_SHORT).show();
                return;}
                Toast.makeText(MainActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error : "+ t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean logIn(){
        boolean flag = false;
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        DummyAPI dummyAPI = URL.getInstance().create(DummyAPI.class);
        Call<UserResponse> loginCall = dummyAPI.checkUser(username,password);
        try {
            Response<UserResponse> loginResponse = loginCall.execute();
            if (loginResponse.isSuccessful() && loginResponse.body().getStatus().equals("Login success!")) {
                URL.token += loginResponse.body().getToken();
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    private boolean validate() {
        if (TextUtils.isEmpty(etUsername.getText())) {
            etUsername.setError("Enter username");
            etUsername.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(etPassword.getText())) {
            etPassword.setError("Enter password");
            etPassword.requestFocus();
            return false;
        }
        return true;
    }
}
