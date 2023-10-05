package com.example.mortgagemaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.inputmethod.InputMethodManager;
import java.util.Arrays;
import java.text.DecimalFormat;
import android.content.Context;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MortgageMaster extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public Button helpButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Help button process
        helpButton = findViewById(R.id.helpButton);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MortgageMaster.this,Help.class);
                startActivity(intent);
            }
        });
        // handle spinner items
        Spinner spinner = findViewById(R.id.spinnerPeriod);
        String[] periodArray = getResources().getStringArray(R.array.period);
        boolean[] enabledItems = generateEnabledItems(periodArray); // Generate enabled items array
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, periodArray, enabledItems);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


    }

    public void calculate(View view){
        TextView msg = findViewById(R.id.textViewMsg1);
        TextView result = findViewById(R.id.textViewResult);

        Spinner annual = findViewById(R.id.spinnerPeriod);
        String selectannual = annual.getSelectedItem().toString();

        EditText principalAmount = findViewById(R.id.textAmount);
        String selectprinciple = principalAmount.getText().toString();

        EditText interestRate = findViewById(R.id.textInterestRate);
        String selectinterestrate = interestRate.getText().toString();
        double emi = 0;

// Split the selected value based on spaces
        String[] parts = selectannual.split(" ");
        int  period = 0;

        if (parts.length >= 1) {
            try {
                // Try to parse the first part as an integer
                period = Integer.parseInt(parts[0]);
            } catch (NumberFormatException e) {
                // Handle the case where the first part is not a valid integer (optional)
            }
        }

        try {
            double p = Double.parseDouble(selectprinciple);
            double r = (Double.parseDouble(selectinterestrate) / 10) / 12;
            int n = period;

            // Calculate EMI
             emi = (p * r * Math.pow(1 + r, n)) / (Math.pow(1 + r, n) - 1)/12;

            // Format the result to display only two digits after the decimal point
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String formattedEmi = decimalFormat.format(emi);
            // Display the formatted result
            msg.setText("Calculations are based on monthly payment");
            result.setText(formattedEmi);
            hideKeyboard(view);
        } catch (NumberFormatException e) {
            // Handle parsing errors here (e.g., invalid input)
            msg.setText("Invalid input. Please check your principal and interest rate.");
            result.setText("");
            hideKeyboard(view);
        }

    }
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(),text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(adapterView.getContext(),"you must select amortization period",Toast.LENGTH_SHORT).show();
    }
    // disable the first spinner item
    private boolean[] generateEnabledItems(String[] items) {
        boolean[] enabledItems = new boolean[items.length];
        Arrays.fill(enabledItems, true); // Initialize all items as enabled
        // Disable specific items by their index
        enabledItems[0] = false; // For example, disabling the third item
        return enabledItems;
    }
}