public class TestFile {
   public static void main(String[] args) {
      
      System.out.println("Testing File");
      
      Validator v = new Validator();
      String[] premises = { "p^q", "p>r", "q>r" };
      String conclusion = "r";

      boolean isValid = v.validate(premises, conclusion);
   }
}