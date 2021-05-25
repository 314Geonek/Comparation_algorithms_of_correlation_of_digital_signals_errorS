package com.example.errors_correlation;

import java.util.ArrayList;
import java.util.List;

public class CRC {
    private static int counterOfControlBits;
    public static int getCounterOfControlBits() {
        return counterOfControlBits;
    }
    public static void encode(List<Byte> inputList, String polynomial){
        List<Byte> polynomialCrc = numberToByteList(polynomial);
        int crcCount = polynomialCrc.size() - 1;
        List<Byte> crc = new ArrayList<>(inputList);
        for (int i = 0; i < crcCount; i++) {
            crc.add(Byte.valueOf("0"));
        }
        while (crc.size() > crcCount) {
            if (crc.get(0).equals(Byte.valueOf("1"))) {
                for (int j = 0; j < polynomialCrc.size(); j++) {
                    crc.set(j, (byte) (crc.get(j) ^ polynomialCrc.get(j)));
                }
            } else {
                crc.remove(0);
            }
        }
        while (crc.size() < crcCount) {
            crc.add(0, Byte.valueOf("0"));
        }

        inputList.addAll(crc);
    }
    public static void decode(List<Byte> transmission, String polynomial){
        List<Byte> polynomialCrc = numberToByteList(polynomial);
        int crcCount = polynomialCrc.size() - 1;
        while (transmission.size() > crcCount) {
            if (transmission.get(0).equals(Byte.valueOf("1"))) {
                for (int j = 0; j < polynomialCrc.size(); j++) {
                    transmission.set(j, (byte) (transmission.get(j) ^ polynomialCrc.get(j)));
                }
            } else {
                transmission.remove(0);
            }
        }
    }
    public static List<Byte> numberToByteList(String inputText) {

        List<Byte> bitsList = new ArrayList<>();
        for (int i = 0; i < inputText.length(); i++)
            bitsList.add(Byte.valueOf(inputText.substring(i, i + 1)));
        return bitsList;
    }
}
