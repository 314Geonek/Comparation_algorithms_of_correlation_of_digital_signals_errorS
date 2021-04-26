package com.example.errors_correlation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ParityControl {
    private static int counterOfDetectedDistortions; // w kontorli parzystości 1 lub 0
    private static final int counterOfControlBits = 1;
    public static int getCounterOfControlBits() {
        return counterOfControlBits;
    }
    public static List<Integer> encode(List<Integer> bitsList)
    {
        List<Integer> encodedList = new ArrayList<>(bitsList);
        int sum = encodedList.stream().mapToInt(i -> i).sum();
        if(sum % 2 == 0)
            encodedList.add(0,1);
        else encodedList.add(0,0);
        return encodedList;
    }
    public static List<Integer> decode(List<Integer> bitsList)
    {
        int sum = bitsList.stream().mapToInt(i -> i).sum();
        counterOfDetectedDistortions = sum % 2 == 0 ? 1 : 0;
        return bitsList.subList(1,bitsList.size());
    }

    public static int getCounterOfDetectedDistortions() {
        return counterOfDetectedDistortions;
    }
}
