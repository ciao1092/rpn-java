package net.ccond.RPN;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.EmptyStackException;
import java.util.Stack;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("net.ccond.RPN Calculator version 1.0");
        for (String arg : args) {
            if (arg.equalsIgnoreCase("--version")) {
                System.out.println();
            } else {
                System.out.println("Unsupported flag: " + arg);
                System.out.println("Supported flags: \n--version\tdisplays program version ");
            }
            return;
        }
        System.out.println();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String inputExpression;
        while (true) {
            System.out.print(" expression: ");
            inputExpression = reader.readLine();
            if (inputExpression != null && !inputExpression.equalsIgnoreCase("exit")) {
                double result = evaluate(inputExpression);
                if (!Double.isNaN(result))
                    System.out.println("Result: " + result);
            } else break;
        }
    }

    static Stack<Double> previousStack = new Stack<>();
    static Stack<Double> operands = new Stack<>();

    static void RestoreStack() {
        System.out.println("Restoring stack.");
        operands.clear();
        for (double d : previousStack) operands.push(d);
    }

    static void SaveStack() {
        previousStack.clear();
        for (double d : operands) previousStack.push(d);
    }

    /**
     * @param expression The expression to evaluate
     * @return The result of the evaluated expression
     */
    static double evaluate(String expression) {
        SaveStack();
        try {
            if (expression.isBlank() || expression.isEmpty()) return Double.NaN;
            String[] tokens = expression.split(" ");
            for (int t = 0; t < tokens.length; t++) {
                String token = tokens[t];
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
                    System.out.print("Pushing " + op1 + " ^ " + op2 + " = ");
                    operands.push(Math.pow(op1, op2));
                    System.out.println(operands.peek());
                } else if (token.equalsIgnoreCase("print")) {
                    if (operands.empty()) {
                        System.out.println("Stack is empty");
                        return Double.NaN;
                    } else {
                        System.out.print("Stack:");
                        final int stackSize = operands.size();
                        for (int i = 0; i < stackSize; i++) {
                            if (stackSize != 2 || i == 1) System.out.print(" | ");
                            //else System.out.print(" ");
                            double d = operands.get(i);
                            if (i == stackSize - 2) System.out.print(" { ");
                            System.out.print(d);
                        }
                        if (stackSize >= 2) System.out.println(" }");
                        else System.out.println(" | ");
                    }
                } else if (token.equalsIgnoreCase("clear")) {
                    if (t == tokens.length - 1) operands.clear();
                    else {
                        t++;

                        try {
                            int index = Integer.parseInt(tokens[t]) - 1;
                            operands.remove(index);
                        } catch (NumberFormatException e) {
                            System.out.println("[ E-PARSE ] '" + tokens[t] + "' is not a valid integer.");
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("[ E-NOSUCHIDX ] Stack index not found");
                        }
                    }
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
            System.out.println("[ E-TFA ] Too few arguments. ");
            RestoreStack();
            return Double.NaN;
        } catch (OutOfMemoryError e) {
            System.out.println("[ E-STACKOVERFLOW ] Stack overflow.\nOut of memory. ");
            return Double.NaN;
        }
    }
}