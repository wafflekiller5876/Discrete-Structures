//Calvin Hirschler
//4/23/2023
import java.util.ArrayList;
import java.util.Iterator;

public class PA6_Java_Template {
   /* Factorizes the number passed into n into its prime factors and returns those
    * factors as a list.
    *
    * n - integer to factor
    *
    * Returns an array list containing the integer prime factors of n
    */ 
   public static ArrayList<Integer> TrialDivision(int n) {
      ArrayList<Integer> a = new ArrayList<Integer>();
      
      //initial prime check
      if (IsPrime(n)) {
         a.add(1);
         a.add(n);
         return a;
      }
      //prime factorization loop
      int temp = n;
      while(!IsPrime(temp)) {
         for (int i = 2; i < temp; i++) {
            if (IsPrime(i) && temp % i == 0) {
               //System.out.println(temp + " Is divisible by " + i);
               a.add(i);
               temp /= i;
               i = 1;
            }
         }
      }
      a.add(temp);
      return a;
   }
   public static boolean IsPrime(int n) {
      //mod 6 skip
      if (n > 3 && n % 6 != 1 && n % 6 != 5)
         return false;
      //square root method
      int squareRoot = (int)Math.sqrt(n) + 1;
      
      for (int i = 2; i < squareRoot; i++){
         if (n % i == 0)
            return false;
      }
      return true;
   }
   
   /* This is the entry point for the program when you are running it on your
    * local machine. This function will not be executed when it is graded by
    * Gradescope.
    */
   public static void main(String[] args) {
      int numToFactorize = 58765301;
      
      ArrayList<Integer> v = TrialDivision(numToFactorize);
      Iterator<Integer> iter = v.iterator();
      
      while(iter.hasNext()) {
         System.out.println(iter.next());
      }
   }
}
