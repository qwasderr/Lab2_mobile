package com.example.lab2;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DatabaseActivity extends AppCompatActivity {

    private TextView tvData;
    private TextView tvAverageValue;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        tvData = findViewById(R.id.tvData);
        tvAverageValue = findViewById(R.id.tvAverageValue);

        dbHelper = new DatabaseHelper(this);

        dbHelper.importDataFromCSV(this);

        displayHighProductionYears();

        displayAverageValue();
    }

    private void displayHighProductionYears() {
        tvData.setText("");

        StringBuilder data = new StringBuilder();

        Cursor cursor = dbHelper.getHighProductionYears();

        if (cursor.getCount() > 0) {
            data.append("Years with high production:\n");
            while (cursor.moveToNext()) {
                int year = cursor.getInt(0);
                int production = cursor.getInt(1);
                data.append("Year: ").append(year)
                        .append(", Production: ").append(production).append(" tons\n");
            }
        } else {
            data.append("No records");
        }

        tvData.setText(data.toString());

        cursor.close();
    }
    private void displayAverageValue() {
        double averageValue = dbHelper.getAverageValue();
        tvAverageValue.setText("Average: " + String.format("%.2f", averageValue) + " million UAH");
    }
}
