package com.physmo.reference.programming.simplemachinie;

import java.util.Arrays;

public class SimpleMachine2 {
    static Microcode microcode = null;
    public final int memSize = 256 + 100;
    public int[] memory = new int[memSize];
    public int regA = 0;
    public int regB = 0;
    public int regC = 0;
    public int regD = 0;
    public int tempRegister = 0;
    public int addressBuffer = 0;
    public int pc = 0; // program counter.
    boolean flagEquals = false;
    boolean flagGT = false;
    boolean flagLT = false;

    public SimpleMachine2() {
        if (microcode == null) microcode = new Microcode();
    }

    public Microcode getMicrocode() {
        return microcode;
    }

    public void reset() {
        pc = 10;
        regA = 0;
        regB = 0;
        regC = 0;
        regD = 0;
        tempRegister = 0;
        addressBuffer = 0;
        flagEquals = false;
        flagGT = false;
        flagLT = false;
        Arrays.fill(memory, 0);
    }

    public int runCycle() {
        if (pc > memSize - 4 || pc < 0) return 1; // quit due to being out of bounds.

        int currentInstruction = memory[pc++];

        if (currentInstruction >= microcode.maxOps) return 0;
        if (currentInstruction < 0) return 0;

        if (currentInstruction == microcode.stopInstruction) return 1;

        MicroOp[] ops = microcode.getInstructionCode(currentInstruction);
        if (ops.length > 0) {
            for (MicroOp op : ops) {
                doMicroOp(op);
            }
        }

        return 0;
    }

    public int countNoOps() {
        int count = 0;
        for (int i = 0; i < memSize; i++) {
            int currentInstruction = memory[i];
            String name = microcode.getInstructionName(currentInstruction);
            if (name !=null && name.contains("NOP")) count++;
        }
        return count;
    }

    private void doMicroOp(MicroOp op) {
        int wrk = 0;
        switch (op) {
            case NOP:
                break;
            case FETCH_A:
                tempRegister = regA;
                break;
            case FETCH_B:
                tempRegister = regB;
                break;
            case FETCH_C:
                tempRegister = regC;
                break;
            case FETCH_D:
                tempRegister = regD;
                break;
            case FETCH_BYTE:
                tempRegister = memory[pc++];
                break;
            case FETCH_ADDRESS:
                addressBuffer = memory[pc++];
                break;
            case STORE_A:
                regA = tempRegister;
                break;
            case STORE_B:
                regB = tempRegister;
                break;
            case STORE_C:
                regC = tempRegister;
                break;
            case STORE_D:
                regD = tempRegister;
                break;

            case STORE_BYTE_IN_ADDRESS:
                if (isAddressInBounds(addressBuffer)) memory[addressBuffer] = tempRegister;
                break;
            case FETCH_BYTE_FROM_ADDRESS:
                if (isAddressInBounds(addressBuffer)) tempRegister = memory[addressBuffer];

                break;

            case ADD:
                regA = regA + tempRegister;
                break;
            case SUB:
                regA = regA - tempRegister;
                break;
            case MUL:
                regA = regA * tempRegister;
                break;
            case AND:
                regA = regA & tempRegister;
                break;
            case OR:
                regA = regA | tempRegister;
                break;
            case XOR:
                regA = regA ^ tempRegister;
                break;
            case SHL:
                regA = regA << tempRegister;
                break;
            case SHR:
                regA = regA >> tempRegister;
                break;
            case CMP: // Compare
                wrk = regA - tempRegister;

                if (wrk == 0) flagEquals = true;
                else flagEquals = false;

                if (wrk > 0) flagGT = true;
                else flagGT = false;

                if (wrk < 0) flagLT = true;
                else flagLT = false;

                break;

            case JUMP:
                pc = addressBuffer;
                break;
            case JUMP_NZ:
                if (flagEquals) pc = addressBuffer;
                break;
            case JUMP_Z:
                if (!flagEquals) pc = addressBuffer;
                break;
            case JUMP_GT:
                if (flagGT) pc = addressBuffer;
                break;
            case JUMP_LT:
                if (flagLT) pc = addressBuffer;
                break;
        }

    }

    public boolean isAddressInBounds(int address) {
        if (address < 0) return false;
        if (address >= memSize) return false;
        return true;
    }
}