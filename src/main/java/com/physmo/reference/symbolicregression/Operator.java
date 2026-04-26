package com.physmo.reference.symbolicregression;

public enum Operator {
    ADD(2),
    SUB(2),
    MUL(2),
    DIV(2),
    SIN(1),
    COS(1),
    ONE(0),
    TWO(0),
    VAR_X(0),
    VAR_Y(0),
    VAR_Z(0),
    CONST_A(0),
    CONST_B(0),
    CONST_C(0),
    CONST_D(0);

    int inputCount;

    Operator(int inputCount) {
        this.inputCount = inputCount;
    }

    public int getInputCount() {
        return inputCount;
    }

}
