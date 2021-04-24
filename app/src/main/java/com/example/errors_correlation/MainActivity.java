package com.example.errors_correlation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Spinner methodSelectorSpinner;
    private EditText inputBitsEditText;
    private TextView codedDataTextView;
    private TextView decodedDataTextView;
    private NumberPicker numberOfBitsToLieNumberPicker;
    private int[] inputBitsArray;
    private Button lieNBits;
    private int[] codedBitsArray;
    private NumberPicker lengthOfGeneratedSeriesNumberPicker;
    private TextView codedDataAfterCorrelationTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lieNBits = findViewById(R.id.lieNBitsButton);
        methodSelectorSpinner = findViewById(R.id.methodSelectorSpinner);
        inputBitsEditText = findViewById(R.id.inputBitsEditText);
        codedDataTextView = findViewById(R.id.codedDataTextView);
        codedDataAfterCorrelationTextView = findViewById(R.id.codedDataAfterCorrelationTextView);
        lengthOfGeneratedSeriesNumberPicker = findViewById(R.id.lengthOfGeneratedSeriesNumberPicker);
        lengthOfGeneratedSeriesNumberPicker.setMinValue(1);
        lengthOfGeneratedSeriesNumberPicker.setMaxValue(8);
        numberOfBitsToLieNumberPicker = findViewById(R.id.numbersOfBitsToLieNumberPicker);
        numberOfBitsToLieNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                lieNBits.setText("Reverse " + newVal + " bits");
            }
        });
        String[] values = {"8","16","24","32","40","48","56","64"};
        lengthOfGeneratedSeriesNumberPicker.setDisplayedValues(values);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Hamming");
        arrayList.add("ANDROID");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        methodSelectorSpinner.setAdapter(arrayAdapter);

    }

    public void generateCode(View view) {
        int codeLength = lengthOfGeneratedSeriesNumberPicker.getValue() * 8;
        String generatedCode = "";
        for(int i = 0; i < codeLength ; i++)
        {
            generatedCode = generatedCode.concat(Integer.toString((int)(Math.random() * (2))));
        }
        inputBitsEditText.setText(generatedCode);
    }
    public void numbersToIntArray()
    {
        String inputText = inputBitsEditText.getText().toString();
        inputBitsArray = new int[inputText.length()];
        for(int i=0;i<inputText.length();i++)
            inputBitsArray[i] = Integer.valueOf(inputText.substring(i,i+1));

    }
    public void encode(View view) {
        numbersToIntArray();
        if(inputBitsArray.length%8!=0 || inputBitsArray.length==0)
            return;
        String encodingMethod = methodSelectorSpinner.getSelectedItem().toString();
        switch (encodingMethod)
        {
            case "Hamming": codedBitsArray = HammingMethod.encodeHamming(inputBitsArray);
            break;
            //other methods soon
            default: break;
        }
        String output="";
        numberOfBitsToLieNumberPicker.setMinValue(1);
        numberOfBitsToLieNumberPicker.setMaxValue(codedBitsArray.length);
        for (int i:codedBitsArray) {
            output = output.concat(Integer.toString(i));
        }
        codedDataTextView.setText(output);

    }

    private void reverseOneBit(int index)
    {
        codedBitsArray[index] = codedBitsArray[index] == 1 ? 0 : 1;
        String output="";
        for (int i:codedBitsArray) {
            output = output.concat(Integer.toString(i));
        }
        codedDataTextView.setText(output);

    }

    public void reverseNBits(View view) {
        if(codedBitsArray == null)
            return;
        int max = codedBitsArray.length;
        int numbersOfBitsToReverse = numberOfBitsToLieNumberPicker.getValue();
        List<Integer> listOfBitsToReverse = new ArrayList<>();
        while(true)
        {
            int bit = (int)(Math.random()*max);
            if(!listOfBitsToReverse.contains(bit))
            {
                listOfBitsToReverse.add(bit);
                if(listOfBitsToReverse.size()==numbersOfBitsToReverse)
                    break;
            }
        }
        listOfBitsToReverse.forEach( i ->{
            reverseOneBit(i);
        });
    }
}