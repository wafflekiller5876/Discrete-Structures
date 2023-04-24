//package com.gradescope.Validator;

import java.util.*;

final public class Evaluator {
    final private static char[] operators = {'^', 'V', '~', '>'};
    
    // Evaluates an infix logical expression 
    public static boolean evaluate(String arg, Hashtable<Character, Boolean> vars) throws Exception {
        LinkedList<Character> tokens = lex(arg);
        LinkedList<Character> ast = parse(tokens);
        boolean value = eval_rpn(ast, vars);
        return value;
    }

    private static LinkedList<Character> lex(String arg) {
        LinkedList<Character> tokens = new LinkedList<Character>();
        
        for (char c : arg.toCharArray())
            tokens.add(c);
        
        return tokens;
    }
    
    public static LinkedList<Character> parse(LinkedList<Character> tokens) throws Exception{
        // Turns an infix arithmetic string into an RPN representation.
        // Uses the Shunting yard algorithm. This is used to resolve operator
        // precedence and handle parentheses.

        LinkedList<Character> output = new LinkedList<Character>();
        Stack<Character> operator_stack = new Stack<Character>();

        while (tokens.size() > 0) {
            // Get next token
            char token = tokens.remove();
      
            if (isOperator(token)) {
                int precedence = getPrecedence(token);
               
                // Pop operators off the stack and push them into the output queue until the next operator
                // in the stack has a higher precedence (i.e. the operators popped off the stack will be
                // executed before the current operator)
                while (operator_stack.size() > 0 && precedence <= getPrecedence(operator_stack.peek())) {
                    output.add(operator_stack.pop());
                }

                operator_stack.push(token);
            } else if (token == '(') {
                operator_stack.push(token);
            } else if (token == ')') {
                // Remove operators until the left parenthesis is found
                while (operator_stack.peek() != '(') {
                    output.add(operator_stack.pop());
                }

                operator_stack.pop(); // Pop the left paren
            } else if (isVariable(token)) {
                output.add(token);
            } else {
               throw new Exception(String.format("Token \'%c\' is not a valid token.", token));
            }
        }
      
        // Add the rest of the operators into the output in precedence order
        while (operator_stack.size() > 0)
            output.add(operator_stack.pop());
        
        return output;
    }
    
    // Evaluates the given parsed text according to reverse polish notation
    private static boolean eval_rpn(LinkedList<Character> rpn, Hashtable<Character, Boolean> vars) throws Exception {
        Stack<Boolean> stack = new Stack<Boolean>();
        char token = ' ';
        
        try {
           while (rpn.size() > 0) {
               token = rpn.pop();
               
               if (isVariable(token)) {
                   stack.push(vars.get(token));
               } else if (token == '^') {
                   boolean operand1 = stack.pop();
                   boolean operand2 = stack.pop();
                   
                   stack.push(operand1 && operand2);
               } else if (token == 'V') {
                   boolean operand1 = stack.pop();
                   boolean operand2 = stack.pop();
                   
                   stack.push(operand1 || operand2);
               } else if (token == '~') {
                   boolean operand = stack.pop();
                   
                   stack.push(!operand);
               } else if (token == '>') {
                   boolean operand1 = stack.pop();
                   boolean operand2 = stack.pop();
                   
                   stack.push(!operand2 || operand1);
               }
           }
        } catch (EmptyStackException e) {
           throw new Exception(String.format("Operator \'%c\' did not have enough operands to operate", token));
        }
        
        // Check to see if the stack length is the correct length
        if (stack.size() > 1)
           throw new Exception(String.format("Not enough operators in argument; there were %i operands remaining", stack.size()));
        
        if (stack.size() == 0)
           throw new Exception("Error when evaluating argument. No operands remaining");
        
        // Return the only value on the stack (i.e., the result)
        return stack.pop();
    }
    
    private static boolean isVariable(char token) {
      if ((int) token >= 97 && (int) token <= 122)
         return true;
         
      return false;
   }

    private static boolean isOperator(char token) {
        for (int i = 0; i < 4; i++) {
            if (token == operators[i]) {
                return true;
            }
        }

        return false;
    }

    // Returns a number denoting when in the order of operations the operator
    // will be calculated (i.e. * before +). Higher number -> higher precedence
    private static int getPrecedence(char operator) {
       switch (operator) {
           case '>':
               return 0;
           case '^':
           case 'V':
               return 1;
           case '~':
               return 2;
           case '(':
           case ')':
              return -1;
           default:
              return -5;
        }
    }

}