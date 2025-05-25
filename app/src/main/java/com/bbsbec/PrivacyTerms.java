package com.bbsbec;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import java.util.Objects;

public class PrivacyTerms extends AppCompatActivity {

    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_privacy_terms);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.privacy_terms), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Window window = getWindow();
        View decorview = window.getDecorView();
        WindowInsetsControllerCompat wic = new WindowInsetsControllerCompat(window, decorview);
        wic.setAppearanceLightStatusBars(false);

        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.status_bar));

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#162242")));

        text = (TextView) findViewById(R.id.privacy_terms_textview);

        String privacy = "<h2>Privacy Policy for BBSBEC App</h2>" +
                "<p><strong>Effective Date:</strong> 15-08-2024</p>" +

                "<p>Thank you for using the BBSBEC App (\"Baba Banda Singh Engineering College App\"). This Privacy Policy outlines how we handle information in relation to the use of the App. By using the App, you agree to the terms of this Privacy Policy.</p>" +

                "<h3>1. Information We Collect</h3>" +
                "<p>The Baba Banda Singh Engineering College App does not collect, store, or share any personal information from users. All data provided within the App, including syllabus, notes, previous year question papers (PYQ), and timetables, is publicly accessible and does not require user registration or login.</p>" +

                "<h3>2. Use of Information</h3>" +
                "<p>Since the App does not collect any user data, there is no information to use, process, or analyze. The App is purely informational, providing users with access to academic resources related to Baba Banda Singh Engineering College.</p>" +

                "<h3>3. Data Sharing</h3>" +
                "<p>As the App does not collect or store any personal information, there is no user data to share with third parties.</p>" +

                "<h3>4. Cookies and Tracking Technologies</h3>" +
                "<p>The App does not use cookies or any other tracking technologies to monitor user activity.</p>" +

                "<h3>5. Third-Party Links</h3>" +
                "<p>The App may contain links to external websites or resources. Please note that we are not responsible for the content or privacy practices of these third-party websites. We encourage users to read the privacy policies of any third-party sites they visit.</p>" +

                "<h3>6. Security</h3>" +
                "<p>Since no personal data is collected or stored by the App, there are no specific security measures related to user data. The content provided in the App is publicly accessible to all users.</p>" +

                "<h3>7. Changes to This Privacy Policy</h3>" +
                "<p>We may update this Privacy Policy from time to time. Any changes will be posted within the App, and the \"Effective Date\" at the top of the policy will be updated accordingly. We encourage users to review this policy periodically to stay informed about any changes.</p>" +

                "<h3>8. Contact Us</h3>" +
                "<p>If you have any questions or concerns about this Privacy Policy, please contact us at:</p>" +
                "<p><strong>Baba Banda Singh Engineering College</strong><br>" +
                "Fatehgarh Sahib, Punjab, India<br>" +
                "principal@bbsbec.edu.in</p>";



        String terms = "<h2>Terms of Use for BBSBEC App</h2>" +
                "<p><strong>Effective Date:</strong> 15-08-2024</p>" +

                "<p>Welcome to the BBSBEC App (\"Baba Banda Singh Engineering College App\"). By accessing or using the App, you agree to comply with and be bound by these Terms of Use. Please read them carefully.</p>" +

                "<h3>1. Acceptance of Terms</h3>" +
                "<p>By using the App, you acknowledge that you have read, understood, and agree to be bound by these Terms of Use. If you do not agree with any part of these terms, please do not use the App.</p>" +

                "<h3>2. Use of Content</h3>" +
                "<p>The App provides access to various academic resources, including syllabus, notes, previous year question papers (PYQ), and timetables. All content is made available for educational purposes only.</p>" +


                "<p><strong> Syllabus:</strong> The syllabus provided in the App is publicly available through the Punjab Technical University (PTU) and is used here for the convenience of students.</p>" +
                "<p><strong> Timetable:</strong> The timetable included in the App is created by Baba Banda Singh Engineering College and is provided to help students manage their schedules.</p>" +
                "<p><strong> Notes:</strong> Some notes in the App are handwritten, while others may be sourced from external websites. Any notes obtained from other websites are the intellectual property of those respective websites. We use these notes strictly for educational purposes, and they are not used for commercial gain.</p>" +
                "<p><strong> Previous Year Question Papers (PYQ):</strong> The PYQ section provides access to past exam papers, which are opened in a WebView of Google Drive. Some PDFs may contain watermarks from their original owners. These materials are used solely for educational purposes, and no profit or monetary gain is intended.</p>" +


                "<h3>3. Intellectual Property Rights</h3>" +
                "<p>All intellectual property rights related to the App's original content, including design, graphics, and text, are owned by Baba Banda Singh Engineering College or are used with permission. Unauthorized use of this content is prohibited.</p>" +
                "<p>Any third-party content used in the App (e.g., notes or PDFs with watermarks) remains the intellectual property of the respective owners. The use of such content is strictly for educational purposes, and we do not claim ownership over it.</p>" +

                "<h3>4. No Advertising or In-App Purchases</h3>" +
                "<p>The App does not contain any form of advertising, nor does it offer in-app purchases. The sole purpose of the App is to provide educational resources to students of Baba Banda Singh Engineering College.</p>" +

                "<h3>5. Limitation of Liability</h3>" +
                "<p>The App and its content are provided \"as is\" without any warranties, express or implied. While we strive to ensure the accuracy and reliability of the information provided, we do not guarantee that the App will be free from errors or interruptions.</p>" +
                "<p>Baba Banda Singh Engineering College shall not be held liable for any direct, indirect, incidental, or consequential damages arising from the use or inability to use the App or its content.</p>" +

                "<h3>6. External Links and Third-Party Content</h3>" +
                "<p>The App may contain links to external websites or display content from third-party sources. We are not responsible for the content, accuracy, or privacy practices of these external sites. Users are encouraged to review the terms and privacy policies of any third-party sites they visit.</p>" +

                "<h3>7. Changes to the Terms of Use</h3>" +
                "<p>We may update these Terms of Use from time to time. Any changes will be posted within the App, and the \"Effective Date\" at the top of this document will be updated accordingly. Your continued use of the App after any changes indicates your acceptance of the new terms.</p>" +

                "<h3>8. Governing Law</h3>" +
                "<p>These Terms of Use shall be governed by and construed in accordance with the laws of India. Any disputes arising out of or relating to these terms or the use of the App shall be subject to the exclusive jurisdiction of the courts located in Fatehgarh Sahib, Punjab, India.</p>" +

                "<h3>9. Contact Information</h3>" +
                "<p>If you have any questions or concerns about these Terms of Use, please contact us at:</p>" +
                "<p><strong>Baba Banda Singh Engineering College</strong><br>" +
                "Fatehgarh Sahib, Punjab, India<br>" +
                "principal@bbsbec.edu.in</p>";

        Intent i = getIntent();
        String page = i.getStringExtra("PAGE");
        if (Objects.equals(page, "privacy")){
            getSupportActionBar().setTitle("Privacy Policy");
            text.setText(Html.fromHtml(privacy, Html.FROM_HTML_MODE_LEGACY));
        } else if (Objects.equals(page, "terms")) {
            getSupportActionBar().setTitle("Terms Of Use");
            text.setText(Html.fromHtml(terms, Html.FROM_HTML_MODE_LEGACY));
        }

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}