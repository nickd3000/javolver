package com.physmo.reference.programming.simplemachinie;

public enum MicroOp {
    NOP,

    FETCH_A,        // Read register A to temp
    FETCH_B,        // Read register B to temp
    FETCH_C,        // Read register C to temp
    FETCH_D,        // Read register D to temp
    FETCH_BYTE,     // Read next byte to temp
    FETCH_ADDRESS,  // Read next byte to address buffer
    FETCH_BYTE_FROM_ADDRESS,
    STORE_A,        // Store temp in A
    STORE_B,        // Store temp in B
    STORE_C,        // Store temp in C
    STORE_D,        // Store temp in D
    STORE_BYTE_IN_ADDRESS,

    ADD,            // Add temp to A
    SUB,
    CMP,            // Compare A with temp
    MUL,
    OR,
    XOR,
    AND,
    SHL, // Shift left
    SHR, // Shift right

    JUMP,
    JUMP_NZ,
    JUMP_LT,
    JUMP_GT,
    JUMP_Z,

    STOP
}
