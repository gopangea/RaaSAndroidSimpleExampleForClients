package com.example.raascardtokenizerandroidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.pangea.raas.data.models.CardInformation;
import com.pangea.raas.data.models.TokenResponse;
import com.pangea.raas.domain.CallBack;
import com.pangea.raas.domain.Pangea;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class MainActivityJava extends AppCompatActivity {

    public static final String TAG = "MainActivityJava";


    Button getDataClientBase64 = null;
    TextView dataClientBase64 = null;
    TextInputEditText Tenant = null;
    TextInputEditText cardNumber = null;
    TextInputEditText cvv = null;
    MaterialTextView token = null;
    Button getTokenBtn = null;
    TextView sessionIDTv = null;
    TextInputEditText publicKey = null;
    Button goToJava = null;
    Pangea mPangea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindWidgets();
        setUI();
        setListeners();
        mPangea = Pangea.Companion.createSession(getApplicationContext(), true, "4254372383834", Pangea.Environment.INTEGRATION);
        displaySession(mPangea);
    }

    private void bindWidgets() {
        getDataClientBase64 = findViewById(R.id.getDataClientBase64);
        dataClientBase64 = findViewById(R.id.dataClientBase64);
        Tenant = findViewById(R.id.Tenant);
        cardNumber = findViewById(R.id.cardNumber);
        cvv = findViewById(R.id.cvv);
        token = findViewById(R.id.token);
        getTokenBtn = findViewById(R.id.getTokenBtn);
        sessionIDTv = findViewById(R.id.sessionIDTv);
        publicKey = findViewById(R.id.publicKey);
        goToJava = findViewById(R.id.goToJava);

    }


    private void setListeners() {
        setTokenButton();
        setGetClientButton();
        setGoToJavaButton();
    }

    private void setUI() {
        setPublicKey();
        hideGoToJava();
    }

    private void hideGoToJava(){
        goToJava.setVisibility(View.GONE);
    }


    private void displaySession(Pangea pangea) {
        sessionIDTv.setText(pangea.getSessionId());
    }

    @SuppressLint("SetTextI18n")
    private void setPublicKey() {
        publicKey.setText("WRITE OUT HERE YOUR PUBLIC KEY");
        //Example format for public key:
        //publicKey.setText("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8BMIIBCgKCAQEAs4sA7kSYb7obZpjpB56+RB0hcW7v0pvCvFqel1EsbPiD8bWXUq4sfZ3cEZlCHgcdC8UcxFMAYBQxKUReVV9TUMkMFLEdZKJ2KnaWYBX01tqRftpWAMLhS+IcXnq902ActCT/o5rNrm0bNLQMuqNFvBGJGzXTAubjGMwAbr5CzxmYhWW246Od4/yXSZE0v3dRljM+AOFc5mL3tf0TEQOXDYfYHvq/6yozonyA3NyZp/Brsa+SXQFiCoZWtvUZDFKSMnxGwW2dEAhhgMDxMUULvaXoRsRcaGqgSmv2kSf9AAGFEgdM/peWTOlMQD/mBVm1Pq5Lz0PLsZmfMIiB0v4OwwIDAQAB");
    }


    private void setGoToJavaButton() {
    }

    private void setTokenButton() {
        getTokenBtn.setOnClickListener(v -> {
            token.setText("");
            createToken();
        });
    }

    private void setGetClientButton() {
        getDataClientBase64.setOnClickListener(v -> {
            dataClientBase64.setText("");
            getSessionClient(mPangea);
        });
    }

    private void getSessionClient(Pangea pangea) {
        pangea.getClientData(new CallBack<String>() {
            @Override
            public void onResponse(String clientInfo) {
                System.out.println(clientInfo);
                dataClientBase64.setText(clientInfo);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(String result, @Nullable Throwable throwable) {
                dataClientBase64.setText("error");
                System.out.println("getting 64 encoded  failed" + Objects.requireNonNull(throwable).getLocalizedMessage());
            }
        });

    }

    private void createToken() {
        CardInformation cardInformation = new CardInformation(
                Objects.requireNonNull(publicKey.getText()).toString(),
                Objects.requireNonNull(Tenant.getText()).toString(),
                Objects.requireNonNull(cardNumber.getText()).toString(),
                Objects.requireNonNull(cvv.getText()).toString());
        mPangea.createToken(cardInformation, new CallBack<TokenResponse>() {
            @Override
            public void onResponse(TokenResponse tokenResponse) {
                Log.e(TAG, "onResponse: The token is: " + tokenResponse.token);
                token.setText(tokenResponse.token);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(TokenResponse tokenResponse, @Nullable Throwable throwable) {
                Log.e(TAG, "the error is: " + Objects.requireNonNull(throwable).getLocalizedMessage());
                token.setText(tokenResponse.token + "   " + Objects.requireNonNull(throwable).getLocalizedMessage());
            }
        });
    }


}