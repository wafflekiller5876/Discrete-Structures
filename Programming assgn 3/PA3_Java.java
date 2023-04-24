//Calvin Hirschler
//COMP 3240 Spring 2023 Programming Assignment 3
public class PA3_Java {
   /* Multiplies matrix 1 by matrix 2
    */
   public static float[][] matrix_mult(float[][] mat1, float[][] mat2) {
      float[][] result = new float[mat1.length][mat2[0].length];
      float temp = 0; //initialize temp used in loop
      int rows1 = mat1.length, columns1 = mat1[0].length, rows2 = mat2.length, columns2 = mat2[0].length;
      //System.out.println(rows1 + " " + columns1 + " " + rows2 + " " + columns2);
      int xCount = 0, yCount = 0;
      for (int z = 0; z < rows1; z++) //loop through rows to redo them
      {
         for (int x = 0; x < columns2; x++) //loop through each row
         {
            for (int y = 0; y < columns1; y++) //loop through columns
            {
               temp += mat1[z][y] * mat2[y][x];
               //System.out.println(mat1[z][y] + "*" + mat2[y][x]);
               //System.out.println(x + " " + y + " "  + z);
            }
            result[xCount % rows2][yCount % columns2] = temp; //saves result and resets temp to 0
            //System.out.println("RESULT " + result[xCount % rows2][yCount % columns2] + " " + xCount % rows2 + " " +yCount % columns2);
            temp = 0;
            yCount++;
         }
         xCount++;
      }
      return result;
   }
   
   /* Create the transition matrix from the given observation points
    */
   public static float[][] calc_transition_matrix(String observation_string) {
      float[][] transition_matrix = new float[2][2];
      
      char currentChar, nextChar;
      int length = observation_string.length();
      for (int c = 0; c < length; c++)
      {
         if (c == length - 1) //at last chat in string
         {
            //user input check
            if (transition_matrix[0][0] == 0 || transition_matrix[1][0] == 0 || transition_matrix[0][1] == 0 || transition_matrix[1][1] == 0)
            {
               System.out.println("Make sure each transition happens in the string");
               throw new IllegalArgumentException();
            }
            float numOfD = transition_matrix[0][0] + transition_matrix[0][1];
            float numOfR = transition_matrix[1][0] + transition_matrix[1][1];
            transition_matrix[0][0] /= (numOfD);
            transition_matrix[1][0] /= (numOfD);
            transition_matrix[0][1] /= (numOfR);
            transition_matrix[1][1] /= (numOfR);
            //System.out.println("TRANSITION " + transition_matrix[0][0] + " " + transition_matrix[1][0] + " " +transition_matrix[0][1] + " " +transition_matrix[1][1]);

            return transition_matrix;
         }
         
         currentChar = observation_string.charAt(c); //c
         nextChar = observation_string.charAt(c + 1); //c + 1
         
         //D -> D
         if (currentChar == 'D' && nextChar == 'D')
            transition_matrix[0][0]++;
         //D -> R
         if (currentChar == 'D' && nextChar == 'R')
         transition_matrix[1][0]++;
         //R -> D
         if (currentChar == 'R' && nextChar == 'D')
         transition_matrix[0][1]++;
         //R -> R
         if (currentChar == 'R' && nextChar == 'R')
         transition_matrix[1][1]++;
         //user input check
         if (currentChar != 'R' && currentChar != 'D')
         {
            System.out.println("Only input 'R' and 'D' in string");
            throw new IllegalArgumentException();
         }
      }
      return transition_matrix;
   }
   
   /* Generates the forecast for the next 7 days given the transition matrix and the current weather
      The forecast should be a 2x7 matrix where each row is a forecast for a day
    */
   public static float[][] generate_forecast(float[][] transition_matrix, char curr_weather) {
      float[][] forecast = new float[7][2];
      float[][] tPower = transition_matrix; //initialize tPower matrix used below
      float[][] temp = new float[2][2];
      float[][] prediction = new float[2][1];
      float[][] temp2 = new float[2][1];
      //initialize prediction
      if (curr_weather == 'D')
         prediction[0][0] = 1;
      else
         prediction[1][0] = 1;
      //x == 0
      temp2 = matrix_mult(tPower, prediction);
      forecast[0][0] = temp2[0][0];
      forecast[0][1] = temp2[1][0];
      //System.out.println("TPOWER " + tPower[0][0] + " " + tPower[1][0] + " " +tPower[0][1] + " " +tPower[1][1]);

      //loop through each day   
      for (int x = 1; x < 7; x++)
      {
         temp = matrix_mult(tPower, transition_matrix);
         tPower = temp;
         //System.out.println("TPOWER " + tPower[0][0] + " " + tPower[1][0] + " " +tPower[0][1] + " " +tPower[1][1]);
         temp2 = matrix_mult(tPower, prediction);
         //System.out.println("PREDICTION " + temp2[0][0] + " " + temp2[1][0]);
         forecast[x][0] = temp2[0][0];
         forecast[x][1] = temp2[1][0];
      }
      return forecast;
   }
   
   /* Generates the climate prediction (i.e., steady state vector) given the transition matrix, current 
	  weather, and precision
    */
   public static float[] generate_climate_prediction(float[][] transition_matrix, char curr_weather, float precision) {
      float[] steady_state = new float[2];
      float[] prev_state = new float[2];
      float currentPrecision = 1; int count = 0;
      float[][] tPower = transition_matrix; //initialize tPower matrix used below
      float[][] temp = new float[2][2];
      float[][] prediction = new float[2][1];
      float[][] temp2 = new float[2][1];
      //user entry check
      if (precision > .1f)
      {
         System.out.println("precision must be less than .1");
         throw new IllegalArgumentException();
      }   
      //initialize prediction
      if (curr_weather == 'D')
         prediction[0][0] = 1;
      else
         prediction[1][0] = 1;
      //x == 0
      temp2 = matrix_mult(tPower, prediction);

      //loop through until desired precision   
      while (precision < currentPrecision || count > 1000)
      {
         temp = matrix_mult(tPower, transition_matrix);
         tPower = temp;
         temp2 = matrix_mult(tPower, prediction);
         prev_state[0] = steady_state[0]; //old state
         prev_state[1] = steady_state[1]; //old state
         steady_state[0] = temp2[0][0]; //new state
         steady_state[1] = temp2[1][0]; //new state
         //precision
         if (count != 0)
         {
            currentPrecision = Math.abs(prev_state[0] - steady_state[0]); //diff between states
            //System.out.println("Precision: " + currentPrecision);
         }
         count++;
      }
      return steady_state;
   }
   
   /* Print the forecasted weather predictions 
    */
   public static void print_predictions(float[][] forecast) {
      // Print first line
      System.out.println("[[" + forecast[0][0] + "," + forecast[0][1] + "],");
      
      // Print middle 5 lines
      for (int i = 1; i < forecast.length - 1; i++) {
         System.out.println(" [" + forecast[i][0] + "," + forecast[i][1] + "],");
      }
       
      // Print the last line
      System.out.println(" [" + forecast[6][0] + "," + forecast[6][1] + "]]");
   }
   
   /* Print the steady state vector containing the climate prediction
    */
   public static void print_steady_state(float[] steady_state) {
      System.out.println(steady_state[0]);
      System.out.println(steady_state[1]);
   }
   
   public static void main(String[] args) {
      String observation = "RRRRRDDRRRRRRRR";
      float precision = 0.001f;
      //char curr_weather = observation.charAt(observation.length()-1);
      char curr_weather = 'R';
      float[][] transition_matrix = new float[2][2];
      float[][] forecast = new float[7][2];
      float[] steady_state = new float[2];
      transition_matrix = calc_transition_matrix(observation);
      forecast = generate_forecast(transition_matrix, curr_weather);
      print_predictions(forecast);
      steady_state = generate_climate_prediction(transition_matrix, curr_weather, precision);
      print_steady_state(steady_state);
      generate_climate_prediction(transition_matrix, curr_weather, precision);
      /*
      System.out.println("TESTING METHOD...");
      float[][] mat1 = {{1,2}, {3,4}};
      float[][] mat2 = {{1,2}, {3,4}};
      mat1 = matrix_mult(mat1, mat2);
      System.out.println("MAT1 = "+ mat1[0][0] + " " + mat1[1][0] + " " + mat1[0][1] + " " + mat1[1][1]);*/
   }
}