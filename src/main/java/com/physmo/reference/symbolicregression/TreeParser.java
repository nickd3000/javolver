package com.physmo.reference.symbolicregression;

public class TreeParser {

    int rollingIndex = 0;

    //double[] dna = new double[30];
    double varX, varY, varZ;
    double cA, cB, cD, cC;
    int maxDepth = 4;
    Operator[] _terminalList = null;
    Operator[] _nonTerminalList = null;

    public TreeParser(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public TreeNode buildTree(double[] dna) {
        return buildTree(0, dna, "");
    }

    public TreeNode buildTree(int depth, double[] dna, String locator) {
        TreeNode treeNode = new TreeNode();

        if (depth<1) {
            treeNode.operator = getOperatorFromDoubleNonTerminalOnly(dna[getIndexFromLocatorString(locator)]);
        } else
        if (depth >= maxDepth)
            treeNode.operator = getOperatorFromDoubleTerminalOnly(dna[getIndexFromLocatorString(locator)]);
        else
            treeNode.operator = getOperatorFromDouble(dna[getIndexFromLocatorString(locator)]);


        if (treeNode.operator.inputCount > 0) treeNode.children[0] = buildTree(depth + 1, dna, locator+"L");
        if (treeNode.operator.inputCount > 1) treeNode.children[1] = buildTree(depth + 1, dna, locator+"R");
        return treeNode;
    }

    public static int getIndexFromLocatorString(String locator) {
        int layer = locator.length();
        if (layer==0) return 0;
        int accumulator = 0;

        for (int i=0;i<layer;i++) {
            accumulator+=(int) Math.pow(2, i);
        }

        int xPos = 0;
        for (int i=0;i<locator.length();i++) {
            xPos+=(int) Math.pow(2, i);
            if (locator.charAt(i)=='R') xPos+=(i)+1;
        }

//        System.out.println("input:"+locator);
//        System.out.println("layer:"+layer);
//        System.out.println("Accumulator: "+accumulator);
//        System.out.println("xpos:"+xPos);
//        System.out.println();

        return xPos;
    }

    boolean operatorAvailable(double[] dna) {
        if (rollingIndex < dna.length) return true;
        return false;
    }

    static double operatorScale = 3;
    static Remapper operatorRemapper = new Remapper(0,1,0,10);

    public static Operator getOperatorFromDouble(double d) {
        int numOperators = Operator.values().length;
        int index = (int) (operatorRemapper.remap(Math.abs(d)));
//        if (index < 0) index = 0;
//        if (index > numOperators - 1) index = numOperators - 1;
        index = index % numOperators;
        return Operator.values()[index];
    }

    public static double getDoubleFromOperator(Operator operator) {
        return operator.ordinal()/operatorScale;
    }


    public Operator[] get_terminalList() {

        if (_terminalList == null) {
            int numTerminals = 0;
            for (Operator operator : Operator.values()) {
                if (operator.inputCount == 0) numTerminals++;
            }
            _terminalList = new Operator[numTerminals];
            int index = 0;
            for (Operator operator : Operator.values()) {
                if (operator.inputCount == 0) _terminalList[index++] = operator;
            }
        }

        return _terminalList;
    }

    public Operator[] get_nonTerminalList() {

        if (_nonTerminalList == null) {
            int numTerminals = 0;
            for (Operator operator : Operator.values()) {
                if (operator.inputCount >0) numTerminals++;
            }
            _nonTerminalList = new Operator[numTerminals];
            int index = 0;
            for (Operator operator : Operator.values()) {
                if (operator.inputCount > 0) _nonTerminalList[index++] = operator;
            }
        }

        return _nonTerminalList;
    }

    Operator getOperatorFromDoubleTerminalOnly(double d) {
        Operator[] terminalList = get_terminalList();

        int numOperators = terminalList.length;
        int index = (int) (Math.abs(d)*8);
        if (index < 0) index = 0;

        //if (index > numOperators - 1) index = numOperators - 1;
        index = index%(numOperators );

        return terminalList[index];
    }

    Operator getOperatorFromDoubleNonTerminalOnly(double d) {
        Operator[] terminalList = get_nonTerminalList();

        int numOperators = terminalList.length;
        int index = (int) (Math.abs(d)*10);
        if (index < 0) index = 0;

        //if (index > numOperators - 1) index = numOperators - 1;
        index = index%(numOperators - 1);

        return terminalList[index];
    }

    public int countNodes(TreeNode treeNode) {
        int total = 1;
        Operator operator = treeNode.operator;
        if (operator.inputCount == 2) {
            total += countNodes(treeNode.children[0]);
            total += countNodes(treeNode.children[1]);
        } else if (operator.inputCount == 1) {
            total += countNodes(treeNode.children[0]);
        }
        return total;
    }

    public String describe(TreeNode treeNode) {
        Operator operator = treeNode.operator;

        String s1 = "";
        String s2 = "";

        if (operator.inputCount == 2) {
            s1 = describe(treeNode.children[0]);
            s2 = describe(treeNode.children[1]);
        } else if (operator.inputCount == 1) {
            s1 = describe(treeNode.children[0]);
        }

        switch (operator) {
            case ADD:
                return "(" + s1 + "+" + s2 + ")";
            case SUB:
                return "(" + s1 + "-" + s2 + ")";
            case MUL:
                return "(" + s1 + "*" + s2 + ")";
            case DIV:
                return "(" + s1 + "/" + s2 + ")";
            case VAR_X:
                return "X";
            case VAR_Y:
                return "Y";
            case VAR_Z:
                return "Z";
            case CONST_A:
                return "" + formatDouble(cA) + "";
            case CONST_B:
                return "" + formatDouble(cB) + "";
            case CONST_C:
                return "" + formatDouble(cC) + "";
            case CONST_D:
                return "" + formatDouble(cD) + "";
            case COS:
                return "cos(" + s1 + ")";
            case SIN:
                return "sin(" + s1 + ")";
            case ONE:
                return " 1 ";
            case TWO:
                return " 2 ";
            default:
                return "a";
        }
        //return "";
    }

    public String formatDouble(double d) {
        return String.format("%1$,.2f", d);
    }

    public double parse(TreeNode treeNode) {
        Operator operator = treeNode.operator;

        double v1 = 0;
        double v2 = 0;

        if (operator.inputCount == 2) {
            v1 = parse(treeNode.children[0]);
            v2 = parse(treeNode.children[1]);
        } else if (operator.inputCount == 1) {
            v1 = parse(treeNode.children[0]);
        }

        switch (operator) {
            case ADD:
                return v1 + v2;
            case SUB:
                return v1 - v2;
            case MUL:
                return v1 * v2;
            case DIV:
                if (v2 == 0) return 0;
                return v1 / v2;
            case SIN:
                return Math.sin(v1);
            case COS:
                return Math.cos(v1);
            // Variables
            case VAR_X:
                return varX;
            case VAR_Y:
                return varY;
            case VAR_Z:
                return varZ;
            // Constants
            case CONST_A:
                return cA;
            case CONST_B:
                return cB;
            case CONST_C:
                return cC;
            case CONST_D:
                return cD;
            case ONE:
                return 1;
            case TWO:
                return 2;

        }


        return 0;
    }
}
