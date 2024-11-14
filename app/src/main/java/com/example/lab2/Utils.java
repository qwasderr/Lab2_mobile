package com.example.lab2;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<String> getContactsWithEndingA(ContentResolver contentResolver) {
        List<String> contacts = new ArrayList<>();

        Cursor cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID},
                null,
                null,
                null
        );

        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    if (displayName != null && (displayName.endsWith("а") || displayName.endsWith("a"))) {
                        contacts.add(displayName);
                    }
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return contacts;
    }
}
