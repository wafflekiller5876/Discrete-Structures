// DO NOT DELETE THIS LINE!!!
package com.gradescope.validator;
import java.util.Hashtable;
import java.util.*;

// Notes on how to use evaluator():
// Call the evaluator with Evaluator.evaluate(<premise>, <variable_dict>). "premise"
// is a single string defining the premise or conclusion to test. "variable_dict" is a
// Hashtable<Character, Boolean>() object with the variable name as the key and true/false
// as the value. 

// The only valid operators for premise strings are '^' (and), 'V' (or--CAPITAL v), '~' (not),
// and '>' (implies), and you can use parentheses to override the order of operations as usual.
// All variables should be lowercase letters and each should only be one character long. Finally,
// do not include spaces in the string.

// For example, if you want to test the premise 'p implies q or not r', you should use 'p>qV~r' as
// your premise string.

public class Validator {
   // All of the logic to complete this assignment should be written in this function.
   // This method accepts two things: An array of strings called premiseList and a 
   // single string called conclusion. These strings should be formatted according to 
   // the structure definded above. Also, this needs to return a boolean variable: true if
   // the argument form is valid, and false if it is not valid.
      
   public boolean validate(String[] premiseList, String conclusion) {
      //initialize arrayList that will contain every char from each premise
      ArrayList<Character> allChars = new ArrayList<Character>();
      
      //gets all chars from each premise in the arrayList
      for (int i = 0; i < premiseList.length; i++)
      {
         char[] chars = premiseList[i].toCharArray();
         for (int c = 0; c < chars.length; c++)
         {
            if (!allChars.contains(chars[c]) && chars[c] != '^' && chars[c] != 'V' && chars[c] !='~' && chars[c] != '>' && chars[c] != '(' && chars[c] != ')')
               allChars.add(chars[c]); //add to list if not in it and not special character
         }
      }
      //adds conclusion to allChars arrayList
      char[] chars = conclusion.toCharArray();
      for (int c = 0; c < chars.length; c++)
         {
            if (!allChars.contains(chars[c]) && chars[c] != '^' && chars[c] != 'V' && chars[c] !='~' && chars[c] != '>' && chars[c] != '(' && chars[c] != ')')
            allChars.add(chars[c]); //add to list if not in it and not special character
      }
      
      //print each char in the allChars ArrayList for testing
      System.out.print("allChars ArrayList: ");
      for (char c : allChars) { System.out.print(c + " " ); }
      System.out.println("\n");
      
      //initializing hashTable
      Hashtable<Character, Boolean> vars = new Hashtable<>();
      for (char c : allChars) { vars.put(c, false); }
      
      //iterating through each hashTable
      //valid only if P1 && P2 && P3 => C
      int numOfChars = allChars.size();
      for (int i = 0; i < 1 << allChars.size(); i++) //loop 2^n times (every T/F combo)
      {
      //creating a binary number to use to assign T/F to hashTable values
         String binNum = Integer.toBinaryString(i);
         binNum = String.format("%32s", binNum).replaceAll(" ", "0"); //makes it a 32bit number
         binNum = binNum.substring(32-numOfChars, 32);
         System.out.print(binNum);
         char[] binNumArray = binNum.toCharArray(); //convert string to char array (eg: 1, 0, 0, 1)
         
         //setting values for hashTable depending on 1s and 0s of binNumArray (1 => true, 0 => false)
         for (int b = 0; b < numOfChars; b++) //for each bit
         {
            char currentChar = allChars.get(b); //char associated with bit
            if (binNumArray[b] == '1')
               vars.replace(currentChar, true);
            else
               vars.replace(currentChar, false);
            //System.out.print(" " + vars.get(currentChar));
         }
         System.out.print(": ");
         
         //evaluating every premise
         Boolean premiseBool = true;
         for (int p = 0; p < premiseList.length; p++)
         {
            try {
               if(!Evaluator.evaluate(premiseList[p], vars)) { //if fails evaluation
                  premiseBool = false;
                  System.out.println(premiseList[p] + " is false. C is vacuously true\n");

                  break;
               }
            }
            catch(Exception e) {System.out.println("Something went wrong"); }
         }
         //evaluating conclusion and comparing it to premise
         try {
            if (premiseBool && premiseBool != Evaluator.evaluate(conclusion, vars)) { //if premises DO NOT imply conclusion then return false
               System.out.println("Premises DO NOT => C");
               return false;
               }
               if (premiseBool)
                  System.out.println("Premises => C\n");
         }
         catch(Exception e) { System.out.println("Something went wrong"); }
      }
      System.out.println("Valid");
      return true;
   }
}