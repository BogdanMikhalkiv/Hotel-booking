package com.example.hotelbookingsystem;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class mainL {
    public static List<String> summaryRanges(int[] nums) {


        List<String> list = new ArrayList<>();
        if (nums.length>0) {
            String s = String.valueOf(nums[0]);
            for (int i = 0; i < nums.length; i++) {
                if (i < nums.length - 1) {
                    if ((nums[i + 1] - nums[i] != 1)) {
                        s += (Integer.parseInt(String.valueOf(s.charAt(0))) == nums[i]) ? "" : "->" + nums[i];
                        list.add(s);
                        s = String.valueOf(nums[i + 1]);
                        System.out.println(s);
                    }
                } else {
                    if (nums[i] - nums[i - 1] == 1) {
                        System.out.println("я тут");
                        s += "->" + nums[i];
                        list.add(s);
                    } else {
                        list.add(s);
                    }
                }
            }
        }
        return list;
    }

        public static int getInt() {
            try {
                String[] students = {"Harry", "Paul"};
                System.out.println(students[2]);
            } catch (Exception e) {
                System.out.println("dfdfdf");
                return 10;
            } finally {
                return 20;
            }
        }

    public static boolean isAnagram(String s, String t) {
        if (s.length() == t.length()) {
            StringBuilder sorted1 = s.chars()
                    .sorted()
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append);
            StringBuilder sorted2 = t.chars()
                    .sorted()
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append);
            if (sorted1.toString().equals(sorted2.toString())){
                return true;
            }
        }
        return false;
    }
//
    public static int longestConsecutive(int[] nums) {
        int count = 0;
        int countMax = 0;

        if (nums != null) {
            int []  sortedUnique = IntStream.of(nums).distinct().sorted().toArray();

            for (int i = 0; i < sortedUnique.length; i++) {
                System.out.print(sortedUnique[i] + " ");
            }
            System.out.println();

            if (sortedUnique.length == 1 || (sortedUnique.length == 1 && sortedUnique[0] == 0 ) ){
                return 1;
            }
            for (int i = 0; i < sortedUnique.length-1; i++) {
                if ((sortedUnique[i+1] - sortedUnique[i]) == 1) {
                    count++;
                    System.out.println(i);
                    System.out.println(countMax + " cmx");
                    System.out.println(count + " c");
                    if ( i == sortedUnique.length-2 && countMax <= count) {
                        System.out.println(count + "dfdfdfdf");
                        countMax = count+1;
                    }
                } else if ((sortedUnique[i+1] - sortedUnique[i]) > 1) {
                    count++;
                    if (countMax < count){
                        countMax = count;
                    }
                    count = 0;
                }
            }
        }
        return countMax;
    }

    public static boolean canJump(int[] nums) {
        boolean res = false;
        boolean resZero = true;
        int i = 0;
//        if(nums.length < 2 && nums[0] ==0) {
//            res = true;
//        } else if (nums.length < 2 && nums[0] > 0){
//            resZero = false;
//        }else{
        if(nums.length ==1) {
            res = true;
        } else{
            while(!res && i < nums.length) {
                if(i + nums[i] < nums.length ) {
                    if ((nums[i + nums[i]] == 0) && ( nums[i]-1 >1)){
                        i+=nums[i]-1;
                    } else {
                        i+=nums[i];
                    }
                    if  (i == nums.length-1){
                        res = true;
                    } else{
                        if(nums[i] == 0 && nums.length > 1) {
                            System.out.println("asas");
                            res = true;
                            resZero = false;
                        }
                    }

                } else{
                    System.out.println("sssss");
                    res = true;
                    resZero = false;
                }
            }
        }



        return resZero == false ? resZero : res;


    }

    public static void main(String[] args) {
//        String s ="anagram";
//        String t = "nagaram";
//        System.out.println(isAnagram(s,t));

        SortedSet<Integer> ts = new TreeSet<>();
        int[] nums = {2,0};
        System.out.println(canJump(nums));


        String s  = "2025-05-23";
        LocalDate date = LocalDate.parse(s);
        System.out.println(LocalDate.now().isBefore( date));



    }

}

