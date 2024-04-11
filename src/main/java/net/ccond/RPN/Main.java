package net.ccond.RPN;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.EmptyStackException;
import java.util.Stack;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println();
        System.out.println("==== net.ccond.RPN Calculator version 1.0 ====");
        System.out.println();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String inputExpression;
        while (true) {
            System.out.print(" expression: ");
            inputExpression = reader.readLine();
            if (inputExpression != null && !inputExpression.equalsIgnoreCase("exit")) {
                double result = evaluate(inputExpression);
            } else break;
        }
    }

    static Stack<Double> operands = new Stack<>();

    static double evaluate(String expression) {
        Stack<Double> originalStack = new Stack<>();
        for (double d : operands) originalStack.push(d);
        try {
            if (expression.isBlank() || expression.isEmpty()) return Double.NaN;
            String[] tokens = expression.split(" ");
            for (String token : tokens) {
                if (token == null || token.isEmpty() || token.isBlank()) continue;
                if (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/")) {
                    double op2 = operands.pop();
                    double op1 = operands.pop();

                    System.out.print("Pushing " + op1 + " " + token + " " + op2 + " = ");

                    switch (token) {
                        case "*" -> operands.push(op1 * op2);
                        case "+" -> operands.push(op1 + op2);
                        case "-" -> operands.push(op1 - op2);
                        case "/" -> operands.push(op1 / op2);
                        default -> throw new IllegalStateException("Unexpected value: " + token);
                    }

                    System.out.println(operands.peek());

                } else if (token.equals("^")) {
                    double op2 = operands.pop();
                    double op1 = operands.pop();
                    operands.push(Math.pow(op1, op2));
                } else if (token.equalsIgnoreCase("print")) {
                    if (operands.empty()) {
                        System.out.println("Stack is empty");
                        return Double.NaN;
                    } else {
                        System.out.print("Stack:");
                        final int stackSize = operands.size();
                        for (int i = 0; i < stackSize; i++) {
                            if (stackSize != 2 || i == 1)
                                System.out.print(" | ");
                            //else System.out.print(" ");
                            double d = operands.get(i);
                            if (i == stackSize - 2) System.out.print(" { ");
                            System.out.print(d);
                        }
                        if (stackSize >= 2)
                            System.out.println(" }");
                        else System.out.println(" | ");
                    }
                } else if (token.equalsIgnoreCase("clear")) {
                    operands.clear();
                    return Double.NaN;
                } else {
                    try {
                        System.out.println("Pushing " + token + " to the stack");
                        operands.push(Double.parseDouble(token));
                    } catch (NumberFormatException ex) {
                        operands.push(Double.NaN);
                    }
                }
            }
            return operands.peek();
        } catch (EmptyStackException e) {
            System.out.println("Cannot perform operation: not enough operands.");
            // restore stack
            operands = originalStack;
            return Double.NaN;
        }
    }
}