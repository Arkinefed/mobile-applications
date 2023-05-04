package com.x.calculator;

public enum Calculator {
    INSTANCE;

    public enum Operation1Arg {
        PLUS_MINUS,
        SIN,
        COS,
        TG,
        SQRT,
        POW_2,
        LN,
        LOG
    }

    public enum Operation2Arg {
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE,
        PERCENT,
        POW_Y,
    }

    private String number = "0";
    private double result = 0.0;

    private Double first = null;
    private Double second = null;
    private Operation2Arg op = null;

    public String getNumber() {
        return number;
    }

    public void inputNumber(String other) {
        if (number.equals("0")) {
            number = other;
        } else {
            number += other;
        }

        clearClicked = false;
    }

    public boolean inputPoint() {
        if (number.indexOf('.') == -1) {
            number += ".";

            clearClicked = false;

            return true;
        }

        return false;
    }

    public boolean inputOperation1Arg(Operation1Arg operation) {
        double x = Double.parseDouble(number);
        boolean res = false;

        switch (operation) {
            case PLUS_MINUS:
                x = -x;
                res = true;
                break;
            case SIN:
                x = Math.sin(Math.toRadians(x));
                res = true;
                break;
            case COS:
                x = Math.cos(Math.toRadians(x));
                res = true;
                break;
            case TG:
                x = Math.tan(Math.toRadians(x));
                res = true;
                break;
            case SQRT:
                if (x >= 0) {
                    x = Math.sqrt(x);
                    res = true;
                }

                break;
            case POW_2:
                x = Math.pow(x, 2);
                res = true;
                break;
            case LN:
                if (x > 0) {
                    x = Math.log(x);
                    res = true;
                }

                break;
            case LOG:
                if (x > 0) {
                    x = Math.log10(x);
                    res = true;
                }

                break;
        }

        if (res) {
            number = Double.toString(x);
        }

        return res;
    }

    public boolean inputOperation2Arg(Operation2Arg operation) {
        if (first != null) {
            if (number.equals("0")) {
                op = operation;

                return true;
            }

            return false;
        }

        if (number.equals("0")) {
            return false;
        }

        first = Double.parseDouble(number);
        number = "0";
        op = operation;

        return true;
    }

    public boolean equals() {
        if (first != null && op != null) {
            second = Double.parseDouble(number);

            switch (op) {
                case ADD:
                    result = first + second;
                    break;
                case SUBTRACT:
                    result = first - second;
                    break;
                case MULTIPLY:
                    result = first * second;
                    break;
                case DIVIDE:
                    if (second.equals(0.0) || second.equals(-0.0)) {
                        return false;
                    }

                    result = first / second;
                    break;
                case PERCENT:
                    if (second.equals(0.0) || second.equals(-0.0)) {
                        return false;
                    }

                    result = (first / second) * 100;
                    break;
                case POW_Y:
                    result = Math.pow(first, second);

                    break;
            }

            number = Double.toString(result);

            first = null;
            second = null;
            op = null;

            return true;
        }

        return false;
    }

    boolean clearClicked = false;

    public void clear() {
        if (clearClicked) {
            allClear();
            clearClicked = false;
        } else {
            number = "0";
            clearClicked = true;
        }
    }

    public void allClear() {
        number = "0";
        result = 0.0;

        first = null;
        second = null;
        op = null;
    }
}
