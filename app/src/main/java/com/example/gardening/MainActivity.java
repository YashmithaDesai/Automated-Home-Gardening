package com.example.gardening;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

    private Spinner spinnerA;
    private Spinner spinnerB;
    private Spinner spinnerC;
    private Button enterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupNavigationBar();

        // Initialize the Spinners and Button
        spinnerA = findViewById(R.id.spinnerA);
        spinnerB = findViewById(R.id.spinnerB);
        spinnerC = findViewById(R.id.spinnerC);
        enterButton = findViewById(R.id.enterButton);

        // Set up adapters for the spinners
        ArrayAdapter<CharSequence> adapterA = ArrayAdapter.createFromResource(this,
                R.array.spinner_a_options, android.R.layout.simple_spinner_item);
        adapterA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerA.setAdapter(adapterA);

        ArrayAdapter<CharSequence> adapterB = ArrayAdapter.createFromResource(this,
                R.array.spinner_b_options, android.R.layout.simple_spinner_item);
        adapterB.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerB.setAdapter(adapterB);

        ArrayAdapter<CharSequence> adapterC = ArrayAdapter.createFromResource(this,
                R.array.spinner_c_options, android.R.layout.simple_spinner_item);
        adapterC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerC.setAdapter(adapterC);

        // Set up the Enter button click listener
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedA = spinnerA.getSelectedItem().toString();
                String selectedB = spinnerB.getSelectedItem().toString();
                String selectedC = spinnerC.getSelectedItem().toString();

                // Check if all inputs are selected
                if ("Choose a Flower".equals(selectedA) || "Choose Soil Type".equals(selectedB) || "Choose pot size".equals(selectedC)) {
                    Toast.makeText(MainActivity.this, "Please select options from all spinners.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ("Rose".equalsIgnoreCase(selectedA)) {
                    // Check for soil type
                    if ("Sandy Loam".equalsIgnoreCase(selectedB)) {
                        Toast.makeText(MainActivity.this, "Not ideal: Loamy soil is better for Rose.", Toast.LENGTH_SHORT).show();
                    } else if ("Black".equalsIgnoreCase(selectedB)) {
                        Toast.makeText(MainActivity.this, "This soil type is not suitable for Rose.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Navigate to the Rose page
                        Intent intent = new Intent(MainActivity.this, RoseActivity.class);
                        intent.putExtra("POT_SIZE_CATEGORY", selectedC); // Pass the pot size selection
                        startActivity(intent);
                    }
                } else if ("Tulsi".equalsIgnoreCase(selectedA)) {
                    // Check for soil type
                    if ("Black".equalsIgnoreCase(selectedB)) {
                        Toast.makeText(MainActivity.this, "This soil type is not suitable for Tulsi.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Navigate to the Tulsi page
                        Intent intent = new Intent(MainActivity.this, TulsiActivity.class);
                        intent.putExtra("POT_SIZE_CATEGORY", selectedC);
                        intent.putExtra("SOIL_TYPE", selectedB);
                        startActivity(intent);
                    }
                } else if ("Hibiscus".equalsIgnoreCase(selectedA)) {
                    // Check for soil type
                    if ("Black".equalsIgnoreCase(selectedB)) {
                        Toast.makeText(MainActivity.this, "This soil type is not suitable for Hibiscus.", Toast.LENGTH_SHORT).show();
                    } else if ("Loamy".equalsIgnoreCase(selectedB) || "Sandy Loam".equalsIgnoreCase(selectedB)) {
                        // Navigate to the Hibiscus page
                        Intent intent = new Intent(MainActivity.this, HibiscusActivity.class);
                        intent.putExtra("POT_SIZE_CATEGORY", selectedC);
                        intent.putExtra("SOIL_TYPE", selectedB);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Unknown soil type for Hibiscus.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Selected: " + selectedA + ", " + selectedB + ", " + selectedC, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
