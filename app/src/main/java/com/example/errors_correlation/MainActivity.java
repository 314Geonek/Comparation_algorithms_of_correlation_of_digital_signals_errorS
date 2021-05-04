package com.example.errors_correlation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.CRC32;

public class MainActivity extends AppCompatActivity {
    private Spinner methodSelectorSpinner;
    private EditText inputBitsEditText;
    private TextView codedDataTextView;
    private TextView sentBitsTextView;
    private TextView sentControlBitsTextView;
    private TextView errorsDetectedTextView;
    private TextView fixedErrorsTextView;
    private TextView decodedDataTextView;
    private TextView undetectedErrorsTextView;
    private NumberPicker numberOfBitsToLieNumberPicker;
    private List<Byte> inputBitsList;
    private AppCompatButton lieNBits;
    private List<Byte> encodedList, decodedList;
    private NumberPicker lengthOfGeneratedSeriesNumberPicker;
    private TextView codedDataAfterCorrelationTextView;
    private int sentBits, controlBits, errorsDetected, errorsFixed, errorsUndetected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lieNBits = findViewById(R.id.lieNBitsButton);
        decodedDataTextView = findViewById(R.id.decodedDataTextView);
        sentBitsTextView = findViewById(R.id.sentBitsTextView);
        sentControlBitsTextView = findViewById(R.id.sentControlBitsTextView);
        errorsDetectedTextView = findViewById(R.id.errorsDetectedTextView);
        fixedErrorsTextView = findViewById(R.id.fixedErrorsTextView);
        undetectedErrorsTextView = findViewById(R.id.undetectedErrorsTextView);
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
        arrayList.add("Kontrola parzystości");
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
    public void numberToByteList()
    {
        String inputText = inputBitsEditText.getText().toString();
        sentBits = inputText.length();
        inputBitsList = new ArrayList<>();
        for(int i=0;i<inputText.length();i++)
            inputBitsList.add(Byte.valueOf(inputText.substring(i,i+1)));
    }
    public void encode(View view) {
        numberToByteList();
//        if(inputBitsList.size() % 8 != 0 || inputBitsList.size() == 0)
//            return;

        encodedList = new ArrayList<>(inputBitsList);
        String encodingMethod = methodSelectorSpinner.getSelectedItem().toString();
        switch (encodingMethod)
        {
            case "Hamming": HammingMethod.calcHuffman(encodedList);
            controlBits = HammingMethod.getCounterOfRedundantBits();
            break;

            case "Kontrola parzystości":
                ParityControl.encode(encodedList);
                controlBits = ParityControl.getCounterOfControlBits();
                //other methods soon
            default: break;
        }
        String output="";
        numberOfBitsToLieNumberPicker.setMinValue(1);
        numberOfBitsToLieNumberPicker.setMaxValue(encodedList.size());
        for (int i: encodedList) {
            output = output.concat(Integer.toString(i));
        }
        codedDataTextView.setText(output);

    }

    private void reverseOneBit(int index)
    {   encodedList.set(index, (byte) (encodedList.get(index) == 1 ? 0 : 1));
        String output="";
        for (int i: encodedList) {
            output = output.concat(Integer.toString(i));
        }
        codedDataTextView.setText(output);
    }

    public void reverseNBits(View view) {
        if(encodedList == null)
            return;
        int max = encodedList.size();
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
        listOfBitsToReverse.forEach(this::reverseOneBit);
    }

    public void decode(View view) {
        decodedList = new ArrayList<>(encodedList);
        String encodingMethod = methodSelectorSpinner.getSelectedItem().toString();
        switch (encodingMethod)
        {
            case "Hamming":
                HammingMethod.decodeHamming(decodedList);
                String decodedText = "";
                for(int i: decodedList)
                {
                    decodedText = decodedText.concat(Integer.toString(i));
                }
                decodedDataTextView.setText(decodedText);
                sentControlBitsTextView.setText(getString(R.string.control_bits_sent).concat(String.valueOf(HammingMethod.getCounterOfRedundantBits())));
                sentBitsTextView.setText(getString(R.string.data_bits_sent).concat(Integer.toString(inputBitsList.size())));
                errorsDetectedTextView.setText(getString(R.string.detected_errors).concat(Integer.toString(HammingMethod.getErrorList().size())));
                fixedErrorsTextView.setText(getString(R.string.corrected_errors).concat(Integer.toString(HammingMethod.isLiedBid())));
                undetectedErrorsTextView.setText(getString(R.string.undetected_errors).concat(findUndetectedErrorsCounter(decodedList,inputBitsList)));
                break;

            case "Kontrola parzystości":
                ParityControl.decode(decodedList);
                decodedDataTextView.setText(decodedList.toString());
                sentControlBitsTextView.setText(getString(R.string.control_bits_sent).concat("1"));
                sentBitsTextView.setText(getString(R.string.data_bits_sent).concat(Integer.toString(inputBitsList.size())));
                errorsDetectedTextView.setText(getString(R.string.detected_errors).concat(Integer.toString(ParityControl.getCounterOfDetectedDistortions())));
                fixedErrorsTextView.setText(getString(R.string.corrected_errors).concat("0"));
                undetectedErrorsTextView.setText(getString(R.string.undetected_errors).concat(findUndetectedErrorsCounter(decodedList,inputBitsList)));
                break;
            default: break;
        }
    }
    private String findUndetectedErrorsCounter(List<Byte> a, List<Byte> b)
    {
        int counter = 0;
        for(int i=0;i<a.size();i++)
        {
            if(!a.get(i).equals(b.get(i)))
                counter++;
        }
        return Integer.toString(counter);
    }
}