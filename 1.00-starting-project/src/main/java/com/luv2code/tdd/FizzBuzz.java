package com.luv2code.tdd;

public class FizzBuzz {

    public static String compute(int i) {
        StringBuilder res =  new StringBuilder();
        if (i % 3 == 0) {
            res.append("Fizz");
        }
        if (i % 5 == 0) {
            res.append("Buzz");
        }
        if(res.isEmpty()){
            res.append(i);
        }
        return res.toString();
    }
    /*
    public static String compute(int i) {
        if(i % 3 == 0 && i % 5 == 0) {
            return "FizzBuzz";
        } else if(i % 3 == 0) {
            return "Fizz";
        } else if(i % 5 == 0) {
            return "Buzz";
        } else {
            return Integer.toString(i);
        }
    }
     */
}
