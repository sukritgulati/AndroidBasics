package com.example.sukritgulati.temperatureconverter;

import android.support.v7.app.AppCompatActivity;
import java.text.DecimalFormat;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    RadioButton radioToCelsius;
    RadioButton radioToFahrenheit;
    EditText inputText;
    EditText outputText;
    TextView historyText;
    float inputValue;
    float outputValue;
    String outputString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //binding the view
        inputText = (EditText)findViewById(R.id.editTextInput);
        outputText = (EditText)findViewById(R.id.editTextOutput);
        radioToCelsius = (RadioButton) findViewById(R.id.radioFirst);
        radioToFahrenheit = (RadioButton) findViewById(R.id.radioSecond);
        historyText = (TextView) findViewById(R.id.textViewHistory);
        historyText.setMovementMethod(new ScrollingMovementMethod());
    }

    public void convertBtnClicked(View v){
        //check for validation
        if(inputText.getText().toString().matches("")){
            return;
        }
        inputValue = Float.parseFloat(inputText.getText().toString());
        if(radioToCelsius.isChecked()){
            outputString = " F to C: -> ";
            outputValue = (float) ((inputValue - 32.0)*(5.0/9.0));
        }else{
            outputString = " C to F: -> ";
            outputValue = (float)((inputValue * 9.0/5.0) + 32.0);
        }
        // rounding to one decimal
        BigDecimal outputRoundedValue = new BigDecimal(outputValue).setScale(1,BigDecimal.ROUND_HALF_UP);
        outputValue = outputRoundedValue.floatValue();
        //parsing value to the views
        outputText.setText(String.valueOf(outputValue));
        outputString = outputString + String.valueOf(outputValue)+"\n";
        historyText.setText(outputString + historyText.getText());

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("HISTORY", historyText.getText().toString());

        // Call super last
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Call super first
        super.onRestoreInstanceState(savedInstanceState);
        historyText.setText(savedInstanceState.getString("HISTORY"));
    }
}
