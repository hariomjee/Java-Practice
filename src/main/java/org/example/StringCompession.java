package org.example;

import java.util.HashMap;

public class StringCompession {
    public static String compession(String input){
        StringBuilder result= new StringBuilder();
        HashMap<Character,Integer> map= new HashMap<>();
        for(int i=0;i<input.length();i++){
            char ch= input.charAt(i);
            map.put(ch,map.getOrDefault(ch,0)+1);
        }

        for(char ch: map.keySet()){
            result.append(ch);
            result.append(map.get(ch));
        }
        System.out.println(map);
        return result.toString();
       // return "Working....";
    }
}
