package com.example.errors_correlation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ParityControl {
    private static int counterOfDetectedDistortions; //czy wykryto błąd
    public static List<Integer> encode(List<Integer> bitsList)
    {
        int sum = bitsList.stream().mapToInt(i -> i).sum();
        if(sum % 2 == 0)
            bitsList.add(0,1);
        else bitsList.add(0,0);
        return bitsList;
    }
    public static List<Integer> decode(List<Integer> bitsList)
    {
        int sum = bitsList.stream().mapToInt(i -> i).sum();
        counterOfDetectedDistortions = sum % 2 == 0 ? 1 : 0;
        bitsList.remove(0);
        return bitsList;
    }

    public static int getCounterOfDetectedDistortions() {
        return counterOfDetectedDistortions;
    }
}
