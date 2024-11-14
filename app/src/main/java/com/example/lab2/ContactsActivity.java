package com.example.lab2;

import android.content.ContentResolver;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import android.content.pm.PackageManager;
import android.Manifest;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ContactsActivity extends AppCompatActivity {

    private static final int REQUEST_READ_CONTACTS = 1;
    private TextView tvContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        tvContacts = findViewById(R.id.tvContacts);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        } else {
            displayContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayContacts();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displayContacts() {
        ContentResolver contentResolver = getContentResolver();
        List<String> contacts = Utils.getContactsWithEndingA(contentResolver);
        StringBuilder contactsList = new StringBuilder();
        for (String contact : contacts) {
            contactsList.append(contact).append("\n");
        }
        tvContacts.setText(contactsList.toString());
    }
}
