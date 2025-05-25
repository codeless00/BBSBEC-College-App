package com.bbsbec;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;

import java.util.UUID;

public class CheckOut extends AppCompatActivity {

    TextView paymentAmount;
    Button paymentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_check_out);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.checkout_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        paymentAmount = (TextView) findViewById(R.id.paymentamount);
        paymentButton = (Button) findViewById(R.id.dopayment);

        String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
        int GOOGLE_PAY_REQUEST_CODE = 123;

        Uri uri =
                new Uri.Builder()
                        .scheme("upi")
                        .authority("pay")
                        .appendQueryParameter("pa", "sheehanrajaryan@oksbi")
                        .appendQueryParameter("pn", "Shreyanshi Roy")
                        .appendQueryParameter("mc", "your-merchant-code")
                        .appendQueryParameter("tr", "757opewvvs")
                        .appendQueryParameter("tn", "your-transaction-note")
                        .appendQueryParameter("am", "1")
                        .appendQueryParameter("cu", "INR")
                        .appendQueryParameter("url", "your-transaction-url")
                        .build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
        this.startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);

    }


    private boolean isGooglePayInstalled() {
        PackageManager pm = getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo("com.google.android.apps.nbu.paisa.user", PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "Google Pay Is Not Installed", Toast.LENGTH_LONG).show();
        }
        return installed;
    }

    private void payViaGooglePay() {
        if (isGooglePayInstalled()) {
            // Generate random unique number for transaction reference ID
            String transId = "UPI" + UUID.randomUUID().toString().substring(0, 8);

            Uri uri = new Uri.Builder()
                    .scheme("upi")
                    .authority("pay")
                    .appendQueryParameter("pa", "sheehanrajaryan@oksbi") // Google Pay email
                    .appendQueryParameter("pn", "Test Name") // Google Pay name
                    .appendQueryParameter("mc", "0000") // Merchant code, if applicable
                    .appendQueryParameter("tr", transId) // Transaction reference ID
                    .appendQueryParameter("tn", "Pay to Test Name") // Transaction note
                    .appendQueryParameter("am", "1") // Amount
                    .appendQueryParameter("cu", "INR") // Currency
                    .build();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage("tez.google.com");

            startActivityForResult(intent, 123546);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123456) {
            if (data != null) {
                String response = data.getStringExtra("response");
                String[] resArray = response.split("&");

                String txnId = resArray[0].split("=")[1];

                String responseCode = resArray[1].split("=")[1];

                String status = resArray[2].split("=")[1];

                String txnRef = resArray[3].split("=")[1];

                if (status.equals("SUCCESS")) {
                    Toast.makeText(this, "Payment Success", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Payment Failed", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Payment Failed", Toast.LENGTH_LONG).show();
            }
        }
    }
}