package com.home.sha1;

/**
* SHA1 Hash-function
* @Author: Danylo Artomov
* The code bellow ia an intellectual property the use of which is prohibited for commercial purposes
*/


import java.util.LinkedList;
import java.util.List;

public class SHA1 {
    private static final int[] K = {0x5A827999, 0x6ED9EBA1, 0x8F1BBCDC, 0xCA62C1D6};
    private static int[] H;

    /**
     * @param M String to be  hashed
     * @return hash-value of message M recieved with SHA-1 algorithm
     */
    public static String getHash(String M) {
        H = new int[]{0x67452301, 0xefcdab89, 0x98badcfe, 0x10325476, 0xc3d2e1f0};

        //converting message to binary array
        StringBuilder newBinaryMessage = convertToBinary(M);

        //creating words 16 in each block (inner list - words, outer list - blocks)
        List<List<Integer>> words = creatingWords(newBinaryMessage);

        //applying functions, swapping(main logic of SHA-1 function)
        hashing(words);

        //counting num of zeros need to be added to H(length of each H should be 8 symbols, so if we have value 0x8CE
        //we should add to it beginning 5 zeros)
        int[] numOfZerosAdding = countNumOfZeros();

        //concatenating all H-s and returning result of hash-function
        return concat(numOfZerosAdding);
    }

    private static StringBuilder convertToBinary(String M) {
        StringBuilder binaryMessage = new StringBuilder();
        char[] chars = M.toCharArray();
        for (char aChar : chars) {
            binaryMessage.append(String.format("%8s", Integer.toBinaryString(aChar)).replaceAll(" ", "0"));
        }
        long l = binaryMessage.length();

        long numZeros = 512 - ((l + 65) % 512);

        StringBuilder newBinaryMessage = new StringBuilder();
        newBinaryMessage.append(binaryMessage).append("1");
        for (int i = 0; i < numZeros; i++) {
            newBinaryMessage.append("0");
        }
        newBinaryMessage.append("0".repeat(64 - Integer.toBinaryString(binaryMessage.length()).length()));

        newBinaryMessage.append(Integer.toBinaryString(binaryMessage.length()));
        return newBinaryMessage;
    }

    private static List<List<Integer>> creatingWords(StringBuilder newBinaryMessage) {

        List<List<Integer>> words = new LinkedList<>();
        for (int i = 0, j = 0; i < newBinaryMessage.length(); i += 512, j++) {
            words.add(new LinkedList<>());
            for (int k = 0; k < 512; k += 32) {
                words.get(j).add(Integer.valueOf(newBinaryMessage.substring(i + k + 1, i + k + 32), 2));
                if (newBinaryMessage.charAt(i + k) == '1') {
                    int a = words.get(j).get(words.get(j).size() - 1);
                    a |= 0X80000000;
                    words.get(j).set(words.get(j).size() - 1, a);
                }
            }
        }
        return words;
    }

    private static void hashing(List<List<Integer>> words) {
        List<Integer> newWords;
        for (List<Integer> word : words) {
            newWords = new LinkedList<>();
            for (int i = 0; i < 16; i++) {
                newWords.add(word.get(i));
            }
            for (int i = 16; i < 80; i++) {
                newWords.add(Integer.rotateLeft(newWords.get(i - 3) ^ newWords.get(i - 8) ^ newWords.get(i - 14) ^ newWords.get(i - 16), 1));
            }
            int a = H[0];
            int b = H[1];
            int c = H[2];
            int d = H[3];
            int e = H[4];

            for (int t = 0; t < 80; t++) {
                if (t < 20) {
                    int temp = Integer.rotateLeft(a, 5) + ((b & c) | (~b & d)) + e + newWords.get(t) + K[0];
                    e = d;
                    d = c;
                    c = Integer.rotateLeft(b, 30);
                    b = a;
                    a = temp;
                } else if (t < 40) {
                    int temp = Integer.rotateLeft(a, 5) + (b ^ c ^ d) + e + newWords.get(t) + K[1];
                    e = d;
                    d = c;
                    c = Integer.rotateLeft(b, 30);
                    b = a;
                    a = temp;
                } else if (t < 60) {
                    int temp = Integer.rotateLeft(a, 5) + ((b & c) | (b & d) | (c & d)) + e + newWords.get(t) + K[2];
                    e = d;
                    d = c;
                    c = Integer.rotateLeft(b, 30);
                    b = a;
                    a = temp;
                } else {
                    int temp = Integer.rotateLeft(a, 5) + (b ^ c ^ d) + e + newWords.get(t) + K[3];
                    e = d;
                    d = c;
                    c = Integer.rotateLeft(b, 30);
                    b = a;
                    a = temp;
                }
            }

            H[0] += a;
            H[1] += b;
            H[2] += c;
            H[3] += d;
            H[4] += e;
        }
    }

    private static int[] countNumOfZeros() {
        int[] numOfZerosAdding = new int[5];
        for (int i = 0; i < 5; i++) {
            int length = Integer.toHexString(H[i]).length();
            if (length < 8) {
                numOfZerosAdding[i] = 8 - length;
            } else {
                numOfZerosAdding[i] = 0;
            }
        }
        return numOfZerosAdding;
    }

    private static String concat(int[] numOfZerosAdding) {
        StringBuilder hash = new StringBuilder();
        for (int i = 0; i < H.length; i++) {
            if (numOfZerosAdding[i] != 0) {
                hash.append("0".repeat(numOfZerosAdding[i]));
            }
            hash.append(Integer.toHexString(H[i]));
        }
        return hash.toString();
    }
}
