package com.example.pc.calculator;

class MathEval {

// *************************************************************************************************
// INSTANCE PROPERTIES
// *************************************************************************************************

    private Operator[] operators;                   // operators in effect for this parser
    private String expression;                      // expression being evaluated
    private int offset;                             // used when returning from a higher precedence sub-expression evaluation

// *************************************************************************************************
// INSTANCE CREATE/DELETE
// *************************************************************************************************

    /**
     * Create a math evaluator.
     */
    MathEval() {
        super();
        operators = new Operator[256];
        DefaultImpl.registerOperators(this);
        offset = 0;
    }

// *************************************************************************************************
// INSTANCE METHODS - ACCESSORS
// *************************************************************************************************

    /**
     * Set a operator.
     */
    private MathEval setOperator(Operator opr) {
        if (opr.symbol >= operators.length) {                                                              // extend the array if necessary
            Operator[] noa = new Operator[opr.symbol + (opr.symbol % 255) + 1];                            // use allocation pages of 256
            System.arraycopy(operators, 0, noa, 0, operators.length);
            operators = noa;
        }
        operators[opr.symbol] = opr;
        return this;
    }

// *************************************************************************************************
// INSTANCE METHODS
// *************************************************************************************************

    /**
     * Evaluate this expression.
     */
    double evaluate(String exp) throws NumberFormatException, ArithmeticException {
        expression = exp;
        offset = 0;
        return _evaluate(0, (exp.length() - 1));
    }

// *************************************************************************************************
// STATIC PROPERTIES
// *************************************************************************************************

    /**
     * Operator/operand on on the left.
     */
    private static final int LEFT_SIDE = 'L';
    /**
     * Operator/operand on on the right.
     */
    private static final int RIGHT_SIDE = 'R';
    /**
     * Operator/operand side is immaterial.
     */
    private static final int NO_SIDE = 'B';

    static private final Operator OPERAND = new Operator('\0', 0, 0, NO_SIDE, false, null);          // special "non-operator" representing an operand character

// *************************************************************************************************
// INSTANCE METHODS - PRIVATE IMPLEMENTATION
// *************************************************************************************************

    /**
     * Evaluate a complete (sub-)expression.
     *
     * @param beg Inclusive begin offset for subexpression.
     * @param end Inclusive end offset for subexpression.
     */
    private double _evaluate(int beg, int end) throws NumberFormatException, ArithmeticException {
        return _evaluate(beg, end, 0.0, OPERAND, getOperator('='));
    }

    /**
     * Evaluate the next operand of an expression.
     *
     * @param beg Inclusive begin offset for subexpression.
     * @param end Inclusive end offset for subexpression.
     * @param pnd Pending operator (operator previous to this subexpression).
     * @param lft Left-value with which to initialize this subexpression.
     * @param cur Current operator (the operator for this subexpression).
     */
    private double _evaluate(int beg, int end, double lft, Operator pnd, Operator cur)
            throws NumberFormatException, ArithmeticException {
        Operator nxt = OPERAND;                                                // next operator
        int ofs;                                                                // current expression offset

        for (ofs = beg; (ofs = skipWhitespace(expression, ofs, end)) <= end; ofs++) {
            double rgt = Double.NaN;                                    // next operand (right-value) to process

            for (beg = ofs; ofs <= end; ofs++) {
                char chr = expression.charAt(ofs);
                if ((nxt = getOperator(chr)) != OPERAND) {
                    if (nxt.internal) {
                        nxt = OPERAND;
                    }                                                   // must kill operator to prevent spurious "Expression ends with a blank sub-expression" at end of function
                    else {
                        break;
                    }
                } else if (chr == ')' || chr == ',') {
                    break;
                }
            }

            char ch0 = expression.charAt(beg);
            boolean alp = Character.isLetter(ch0);

            if (beg == ofs && (cur.unary == LEFT_SIDE || nxt.unary == RIGHT_SIDE)) {
                rgt = Double.NaN;                                                                     // left-binding unary operator; right value will not be used and should be blank
            } else if (ch0 == '(') {
                rgt = _evaluate(beg + 1, end);
                ofs = skipWhitespace(expression, offset + 1, end);                                        // skip past ')' and any following whitespace
                nxt = (ofs <= end ? getOperator(expression.charAt(ofs)) : OPERAND);                     // modify next operator
            } else if (alp && nxt.symbol == '(') {
                ofs = skipWhitespace(expression, offset + 1, end);                                      // skip past ')' and any following whitespace
                nxt = (ofs <= end ? getOperator(expression.charAt(ofs)) : OPERAND);                     // modify next operator
            } else {
                try {
                    if (stringOfsEq(expression, beg, "0x")) {
                        rgt = (double) Long.parseLong(expression.substring(beg + 2, ofs).trim(), 16);
                    } else {
                        rgt = Double.parseDouble(expression.substring(beg, ofs).trim());
                    }
                } catch (NumberFormatException thr) {
                    throw exception(beg, "Invalid numeric value \"" +
                            expression.substring(beg, ofs).trim() + "\"");
                }
            }

            if (opPrecedence(cur, LEFT_SIDE) < opPrecedence(nxt, RIGHT_SIDE)) {                           // correct even for last (non-operator) character, since non-operators have the artificial "precedence" zero
                rgt = _evaluate((ofs + 1), end, rgt, cur, nxt);                                           // from after operator to end of current subexpression
                ofs = offset;                                                                             // modify offset to after subexpression
                nxt = (ofs <= end ? getOperator(expression.charAt(ofs)) : OPERAND);
            }

            lft = doOperation(beg, lft, cur, rgt);

            cur = nxt;
            if (opPrecedence(pnd, LEFT_SIDE) >= opPrecedence(cur, RIGHT_SIDE)) {
                break;
            }
            if (cur.symbol == '(') {
                ofs--;
            }
        }
        if (ofs > end && cur != OPERAND) {
            if (cur.unary == LEFT_SIDE) {
                lft = doOperation(beg, lft, cur, Double.NaN);
            } else {
                throw exception(ofs, "Expression ends with a blank operand after operator '"
                        + nxt.symbol + "'");
            }
        }
        offset = ofs;
        return lft;
    }

    private Operator getOperator(char chr) {
        if (chr < operators.length) {
            Operator opr = operators[chr];
            if (opr != null) {
                return opr;
            }
        }
        return OPERAND;
    }

    private int opPrecedence(Operator opr, int sid) {
        if (opr == null) {
            return Integer.MIN_VALUE;
        } else if (opr.unary == NO_SIDE || opr.unary != sid) {
            return (sid == LEFT_SIDE ? opr.precedenceL : opr.precedenceR);
        } else {
            return Integer.MAX_VALUE;
        }
    }

    private double doOperation(int beg, double lft, Operator opr, double rgt) {
        if (opr.unary != RIGHT_SIDE && Double.isNaN(lft)) {
            throw exception(beg, "Mathematical NaN detected in right-operand");
        }
        if (opr.unary != LEFT_SIDE && Double.isNaN(rgt)) {
            throw exception(beg, "Mathematical NaN detected in left-operand");
        }

        try {
            return opr.handler.evaluateOperator(lft, opr.symbol, rgt);
        } catch (ArithmeticException thr) {
            throw exception(beg, "Mathematical expression \"" + expression +
                    "\" failed to evaluate", thr);
        } catch (UnsupportedOperationException thr) {
            int tmp = beg;
            while (tmp > 0 && getOperator(expression.charAt(tmp)) == null) {
                tmp--;
            }
            throw exception(tmp, "Operator \"" + opr.symbol + "\" not handled by math engine (Programmer error: The list of operators is inconsistent within the engine)");
        }
    }

    private ArithmeticException exception(int ofs, String txt) {
        return new ArithmeticException(txt + " at offset " + ofs +
                " in expression \"" + expression + "\"");
    }

    private ArithmeticException exception(int ofs, String txt, Throwable thr) {
        return new ArithmeticException(txt + " at offset " + ofs + " in expression \""
                + expression + "\"" + " (Cause: "
                + (thr.getMessage() != null ? thr.getMessage() : thr.toString()) + ")");
    }

    private boolean stringOfsEq(String str, int ofs, String val) {
        return str.regionMatches(true, ofs, val, 0, val.length());
    }

    private int skipWhitespace(String exp, int ofs, int end) {
        while (ofs <= end && Character.isWhitespace(exp.charAt(ofs))) {
            ofs++;
        }
        return ofs;
    }

// *************************************************************************************************
//  NESTED CLASSES - OPERATOR
// *************************************************************************************************

    /**
     * Operator Structure.
     * <p>
     * This class is immutable and threadsafe, but note that whether it can be used in multiple MathEval instances (as
     * opposed to for multiple operators in one instance) depends on the threadsafety of the handler it contains.
     */
    private static final class Operator {
        final char symbol;
        final int precedenceL;
        final int precedenceR;
        final int unary;
        final boolean internal;
        final OperatorHandler handler;

        /**
         * Create a binary operator with the same precedence on the left and right.
         */
        Operator(char sym, int prc, OperatorHandler hnd) {
            this(sym, prc, prc, NO_SIDE, false, hnd);
        }

        /**
         * Create an operator which may have different left and right precedence and/or may be unary.
         * <p>
         * Using different precedence for one side allows affinity binding such that consecutive operators are evaluated left to right.
         * <p>
         * Marking an operator as unary binds the precedence for the specified side such that it always has maximum precedence when considered from the opposite side.
         */
        Operator(char sym, int prclft, int prcrgt, int unibnd, OperatorHandler hnd) {
            this(sym, prclft, prcrgt, unibnd, false, hnd);

            if (prclft < 0 || prclft > 99) {
                throw new IllegalArgumentException("Operator precendence must be 0 - 99");
            }
            if (prcrgt < 0 || prcrgt > 99) {
                throw new IllegalArgumentException("Operator precendence must be 0 - 99");
            }
            if (handler == null) {
                throw new IllegalArgumentException("Operator handler is required");
            }
        }

        Operator(char sym, int prclft, int prcrgt, int unibnd, boolean intern, OperatorHandler hnd) {
            symbol = sym;
            precedenceL = prclft;
            precedenceR = prcrgt;
            unary = unibnd;
            internal = intern;
            handler = hnd;
        }

        public String toString() {
            return ("MathOperator['" + symbol + "']");
        }
    }

// *************************************************************************************************
// NESTED CLASSES - OPERATION EVALUATOR INTERFACE
// *************************************************************************************************

    interface OperatorHandler {
        double evaluateOperator(double lft, char opr, double rgt) throws ArithmeticException;
    }

// *************************************************************************************************
// STATIC NESTED CLASSES - DEFAULT OPERATOR IMPLEMENTATION
// *************************************************************************************************

    /**
     * An implementation of the default supported operations.
     */
    private static class DefaultImpl
            implements OperatorHandler {
        private DefaultImpl() {
        }

        // To add/remove operators change evaluateOperator() and registration
        public double evaluateOperator(double lft, char opr, double rgt) {
            switch (opr) {
                case '=':
                    return rgt;
                case '√':
                    return Math.sqrt(rgt);
                case '*':
                    return lft * rgt;
                case '(':
                    return lft * rgt;
                case '/':
                    return lft / rgt;
                case '+':
                    return lft + rgt;
                case '-':
                    return lft - rgt;
                default:
                    throw new UnsupportedOperationException("MathEval internal operator " +
                            "setup is incorrect - internal operator \"" + opr + "\" not handled");
            }
        }

        static final DefaultImpl INSTANCE = new DefaultImpl();

        static private final Operator OPR_EQU = new Operator('=', 99, 99, RIGHT_SIDE, true,
                DefaultImpl.INSTANCE);
        static private final Operator OPR_SQR = new Operator('√', 60, 60, RIGHT_SIDE,
                DefaultImpl.INSTANCE);
        static private final Operator OPR_MLT1 = new Operator('*', 40, DefaultImpl.INSTANCE);
        static private final Operator OPR_BKT = new Operator('(', 40, DefaultImpl.INSTANCE);
        static private final Operator OPR_DIV1 = new Operator('/', 40, DefaultImpl.INSTANCE);
        static private final Operator OPR_ADD = new Operator('+', 20, DefaultImpl.INSTANCE);
        static private final Operator OPR_SUB = new Operator('-', 20, DefaultImpl.INSTANCE);

        // To add/remove operators change evaluateOperator() and registration
        static void registerOperators(MathEval tgt) {
            tgt.setOperator(OPR_EQU);
            tgt.setOperator(OPR_SQR);
            tgt.setOperator(OPR_MLT1);
            tgt.setOperator(OPR_BKT);
            tgt.setOperator(OPR_DIV1);
            tgt.setOperator(OPR_ADD);
            tgt.setOperator(OPR_SUB);
        }
    }
}