package com.example.errors_correlation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.errors_correlation.HorizontalListAdapter.ListItemAdapter;
import com.example.errors_correlation.HorizontalListAdapter.itemList;

import java.util.ArrayList;
import java.util.List;

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
    private ListItemAdapter listItemAdapter;
    private RecyclerView recyclerView;
    private List<itemList> encodedBitListTest = new ArrayList<>();
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
        arrayList.add("CRC16");
        arrayList.add("CRC32");
        arrayList.add("CRC-ITU");
        arrayList.add("SDLC Reverse");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        methodSelectorSpinner.setAdapter(arrayAdapter);
        recyclerView = findViewById(R.id.horizontalListRecyclerView);
        listItemAdapter = new ListItemAdapter(encodedBitListTest, MainActivity.this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(listItemAdapter);
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
    @SuppressLint("WrongConstant")
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
                break;
            case "CRC16":
                CRC.encode(encodedList,"11000000000000011");
                break;
            case "CRC32":
                CRC.encode(encodedList,"100000100110000010001110110110111");
                break;
            case "CRC-ITU":
                CRC.encode(encodedList,"10001000000100001");
                break;
            case "SDLC Reverse":
                CRC.encode(encodedList,"10000100000010001");
                break;
        }
        String output="";
        numberOfBitsToLieNumberPicker.setMinValue(1);
        encodedBitListTest.clear();
        numberOfBitsToLieNumberPicker.setMaxValue(encodedList.size());
        for (int i: encodedList) {
            encodedBitListTest.add(new itemList((byte)i, getColor(R.color.lime)));
        }
        listItemAdapter.notifyDataSetChanged();
    }

    private void reverseOneBit(int index)
    {   encodedBitListTest.set(index, new itemList((encodedBitListTest.get(index).bit ==  1 ? (byte) 0 : (byte)1),(encodedBitListTest.get(index).color ==  getColor(R.color.lime) ? getColor(R.color.pink) :getColor( R.color.lime) )));
        listItemAdapter.notifyItemChanged(index);
    }

    public void reverseNBits(View view) {
        if(encodedBitListTest == null)
            return;
        int max = encodedBitListTest.size();
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
        decodedList = new ArrayList<Byte>();
        listItemAdapter.getHorizontalList().forEach(bit ->{
            decodedList.add(bit.bit);
        });
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
            case "CRC16":
                CRC.decode(decodedList,"11000000000000011");
                decodedDataTextView.setText(decodedList.toString());
                break;
            case "CRC32":
                CRC.decode(decodedList,"100000100110000010001110110110111");
                decodedDataTextView.setText(decodedList.toString());
                break;
            case "CRC-ITU":
                CRC.decode(decodedList,"10001000000100001");
                decodedDataTextView.setText(decodedList.toString());
                break;
            case "SDLC Reverse":
                CRC.decode(decodedList,"10000100000010001");
                decodedDataTextView.setText(decodedList.toString());
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