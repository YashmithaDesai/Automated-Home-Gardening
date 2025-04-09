package com.example.gardening;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TulsiActivity extends BaseActivity {
    private TextView soilMoistureTextView;
    private TextView humidityTextView;
    private EditText precipitationEditText;
    private TextView resultTextView;
    private Spinner weekSpinner, potSizeSpinner;
    private String potSizeCategory;
    private String soilType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tulsi);

        // Get the pot size category and soil type from intent
        potSizeCategory = getIntent().getStringExtra("POT_SIZE_CATEGORY");
        soilType = getIntent().getStringExtra("SOIL_TYPE");

        // Initialize views
        soilMoistureTextView = findViewById(R.id.soilMoistureTextView);
        humidityTextView = findViewById(R.id.humidityTextView);
        precipitationEditText = findViewById(R.id.precipitationEditText);
        resultTextView = findViewById(R.id.resultTextView);

        // Initialize Spinners
        weekSpinner = findViewById(R.id.weekSpinner);
        potSizeSpinner = findViewById(R.id.potSizeSpinner);

        // Set up week spinner
        String[] weekOptions = {"<3 weeks", ">3 weeks & <8 weeks", ">8 weeks"};
        weekSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, weekOptions));

        // Set up pot size spinner based on category
        String[] potSizes;
        switch (potSizeCategory.toLowerCase()) {
            case "small":
                potSizes = new String[]{"0.01", "0.015", "0.02", "0.025"};
                break;
            case "medium":
                potSizes = new String[]{"0.03", "0.05", "0.06", "0.08"};
                break;
            case "large":
                potSizes = new String[]{"0.1", "0.25", "0.5", "1.0"};
                break;
            default:
                potSizes = new String[]{"0.01", "0.015", "0.02", "0.025"};
                break;
        }
        potSizeSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, potSizes));

        String blynkUrl = "https://blr1.blynk.cloud/external/api/get?token=08HSHBGfNPa53CbXaFQIOWsqQ-c4xDhP&V1";
        new FetchBlynkDataTask(this).fetchDataFromBlynk(blynkUrl);
        setupNavigationBar();

        // Calculate Water Requirement on button click
        findViewById(R.id.calculateButton).setOnClickListener(v -> calculateWaterRequirement());
    }

    private void calculateWaterRequirement() {
        try {
            String weekSelection = weekSpinner.getSelectedItem().toString();
            String potSizeSelection = potSizeSpinner.getSelectedItem().toString();
            String precipitationInput = precipitationEditText.getText().toString();
            String soilMoistureText = soilMoistureTextView.getText().toString();

            if (TextUtils.isEmpty(precipitationInput)) {
                Toast.makeText(this, "Please enter precipitation value", Toast.LENGTH_SHORT).show();
                return;
            }

            float soilMoisture = extractSoilMoistureValue(soilMoistureText);
            float precipitation = Float.parseFloat(precipitationInput);
            float potArea = Float.parseFloat(potSizeSelection);

            // Determine base water requirement
            float baseWaterRequirement;
            if ("sandy loam".equalsIgnoreCase(soilType)) {
                // Specific logic for sandy loamy soil
                if (weekSelection.equals("<3 weeks")) {
                    baseWaterRequirement = 0.75f;
                } else if (weekSelection.equals(">3 weeks & <8 weeks")) {
                    baseWaterRequirement = 1.5f;
                } else { // >8 weeks
                    baseWaterRequirement = 1.5f;
                }
            } else {
                // Default logic for loamy soil
                if (weekSelection.equals("<3 weeks")) {
                    baseWaterRequirement = 0.2f;
                } else if (weekSelection.equals(">3 weeks & <8 weeks")) {
                    baseWaterRequirement = 0.3f;
                } else {
                    baseWaterRequirement = 0.5f;
                }
            }

            float precipitationContribution = precipitation * potArea;
            float soilMoistureAdjustment = (100 - soilMoisture) / 100.0f;
            float waterNeeded = baseWaterRequirement * potArea * soilMoistureAdjustment - precipitationContribution;
            waterNeeded = Math.max(0, waterNeeded);

            resultTextView.setText(String.format("Water Needed: %.2f Litres", waterNeeded));
        } catch (Exception e) {
            Toast.makeText(this, "Error calculating water requirement", Toast.LENGTH_SHORT).show();
        }
    }

    private float extractSoilMoistureValue(String text) {
        try {
            if (text.contains(":") && text.contains("%")) {
                String[] parts = text.split(":");
                return Float.parseFloat(parts[1].replace("%", "").trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 50.0f;
    }
}
