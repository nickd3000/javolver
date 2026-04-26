package com.physmo.reference.programming.simplemachinie;

import java.util.HashMap;
import java.util.Map;

import static com.physmo.reference.programming.simplemachinie.MicroOp.ADD;
import static com.physmo.reference.programming.simplemachinie.MicroOp.AND;
import static com.physmo.reference.programming.simplemachinie.MicroOp.CMP;
import static com.physmo.reference.programming.simplemachinie.MicroOp.FETCH_A;
import static com.physmo.reference.programming.simplemachinie.MicroOp.FETCH_ADDRESS;
import static com.physmo.reference.programming.simplemachinie.MicroOp.FETCH_B;
import static com.physmo.reference.programming.simplemachinie.MicroOp.FETCH_BYTE;
import static com.physmo.reference.programming.simplemachinie.MicroOp.FETCH_BYTE_FROM_ADDRESS;
import static com.physmo.reference.programming.simplemachinie.MicroOp.FETCH_C;
import static com.physmo.reference.programming.simplemachinie.MicroOp.FETCH_D;
import static com.physmo.reference.programming.simplemachinie.MicroOp.JUMP;
import static com.physmo.reference.programming.simplemachinie.MicroOp.JUMP_GT;
import static com.physmo.reference.programming.simplemachinie.MicroOp.JUMP_LT;
import static com.physmo.reference.programming.simplemachinie.MicroOp.JUMP_NZ;
import static com.physmo.reference.programming.simplemachinie.MicroOp.JUMP_Z;
import static com.physmo.reference.programming.simplemachinie.MicroOp.MUL;
import static com.physmo.reference.programming.simplemachinie.MicroOp.NOP;
import static com.physmo.reference.programming.simplemachinie.MicroOp.OR;
import static com.physmo.reference.programming.simplemachinie.MicroOp.SHL;
import static com.physmo.reference.programming.simplemachinie.MicroOp.SHR;
import static com.physmo.reference.programming.simplemachinie.MicroOp.STOP;
import static com.physmo.reference.programming.simplemachinie.MicroOp.STORE_A;
import static com.physmo.reference.programming.simplemachinie.MicroOp.STORE_B;
import static com.physmo.reference.programming.simplemachinie.MicroOp.STORE_BYTE_IN_ADDRESS;
import static com.physmo.reference.programming.simplemachinie.MicroOp.STORE_C;
import static com.physmo.reference.programming.simplemachinie.MicroOp.STORE_D;
import static com.physmo.reference.programming.simplemachinie.MicroOp.SUB;
import static com.physmo.reference.programming.simplemachinie.MicroOp.XOR;

public class Microcode {

    public int maxOps = 0;
    public int stopInstruction = 0;
    MicroOp[][] n = new MicroOp[256][];
    Map<Integer, String> names = new HashMap<>();

    public Microcode() {
        int id = 0;

        define(id++, "NOP 1", NOP);


        //define(id++, "LD A,A", FETCH_A, STORE_A);
        define(id++, "LD A,B", FETCH_B, STORE_A);
        define(id++, "LD A,C", FETCH_C, STORE_A);
        define(id++, "LD A,D", FETCH_D, STORE_A);
        define(id++, "LD B,A", FETCH_A, STORE_B);
        //define(id++, "LD B,B", FETCH_B, STORE_B);
        define(id++, "LD B,C", FETCH_C, STORE_B);
        define(id++, "LD B,D", FETCH_D, STORE_B);
        define(id++, "LD C,A", FETCH_A, STORE_C);
        define(id++, "LD C,B", FETCH_B, STORE_C);
        //define(id++, "LD C,C", FETCH_C, STORE_C);
        define(id++, "LD C,D", FETCH_D, STORE_C);
        define(id++, "LD D,A", FETCH_A, STORE_D);
        define(id++, "LD D,B", FETCH_B, STORE_D);
        define(id++, "LD D,C", FETCH_C, STORE_D);
        //define(id++, "LD D,D", FETCH_D, STORE_D);

        define(id++, "NOP 2", NOP);

        define(id++, "LD A,pBYTE", FETCH_ADDRESS, FETCH_BYTE_FROM_ADDRESS, STORE_A); // Load A with value from address
        define(id++, "LD pBYTE,A", FETCH_ADDRESS, FETCH_A, STORE_BYTE_IN_ADDRESS); // Load A to address

        define(id++, "LD B,pBYTE", FETCH_ADDRESS, FETCH_BYTE_FROM_ADDRESS, STORE_B); // Load A with value from address
        define(id++, "LD pBYTE,B", FETCH_ADDRESS, FETCH_B, STORE_BYTE_IN_ADDRESS); // Load A to address

        define(id++, "LD C,pBYTE", FETCH_ADDRESS, FETCH_BYTE_FROM_ADDRESS, STORE_C); // Load A with value from address
        define(id++, "LD pBYTE,C", FETCH_ADDRESS, FETCH_C, STORE_BYTE_IN_ADDRESS); // Load A to address

        define(id++, "LD D,pBYTE", FETCH_ADDRESS, FETCH_BYTE_FROM_ADDRESS, STORE_D); // Load A with value from address
        define(id++, "LD pBYTE,D", FETCH_ADDRESS, FETCH_D, STORE_BYTE_IN_ADDRESS); // Load A to address

        define(id++, "LD pBYTE,BYTE", FETCH_ADDRESS, FETCH_BYTE, STORE_BYTE_IN_ADDRESS);

        define(id++, "NOP 3", NOP);

        define(id++, "LD A,BYTE", FETCH_BYTE, STORE_A);
        define(id++, "LD B,BYTE", FETCH_BYTE, STORE_B);
        define(id++, "LD C,BYTE", FETCH_BYTE, STORE_C);
        define(id++, "LD D,BYTE", FETCH_BYTE, STORE_D);

        define(id++, "ADD A,A", FETCH_A, ADD);
        define(id++, "ADD A,B", FETCH_B, ADD);
        define(id++, "ADD A,C", FETCH_C, ADD);
        define(id++, "ADD A,D", FETCH_D, ADD);

        define(id++, "SUB A,A", FETCH_A, SUB);
        define(id++, "SUB A,B", FETCH_B, SUB);
        define(id++, "SUB A,C", FETCH_C, SUB);
        define(id++, "SUB A,D", FETCH_D, SUB);

        define(id++, "NOP 4", NOP);

        define(id++, "MUL A,A", FETCH_A, MUL);
        define(id++, "MUL A,B", FETCH_B, MUL);
        define(id++, "MUL A,C", FETCH_C, MUL);
        define(id++, "MUL A,D", FETCH_D, MUL);

        define(id++, "CMP A,A", FETCH_A, CMP);
        define(id++, "CMP A,B", FETCH_B, CMP);
        define(id++, "CMP A,C", FETCH_C, CMP);
        define(id++, "CMP A,D", FETCH_D, CMP);

        define(id++, "NOP 5", NOP);

        define(id++, "OR A,A", FETCH_A, OR);
        define(id++, "OR A,B", FETCH_B, OR);
        define(id++, "OR A,C", FETCH_C, OR);
        define(id++, "OR A,D", FETCH_D, OR);

        define(id++, "XOR A,A", FETCH_A, XOR);
        define(id++, "XOR A,B", FETCH_B, XOR);
        define(id++, "XOR A,C", FETCH_C, XOR);
        define(id++, "XOR A,D", FETCH_D, XOR);

        define(id++, "AND A,A", FETCH_A, AND);
        define(id++, "AND A,B", FETCH_B, AND);
        define(id++, "AND A,C", FETCH_C, AND);
        define(id++, "AND A,D", FETCH_D, AND);
        define(id++, "SHL A,B", FETCH_B, SHL);
        define(id++, "SHL A,C", FETCH_C, SHL);
        define(id++, "SHL A,D", FETCH_D, SHL);
        define(id++, "SHR A,B", FETCH_B, SHR);
        define(id++, "SHR A,C", FETCH_C, SHR);
        define(id++, "SHR A,D", FETCH_D, SHR);

        define(id++, "NOP 6", NOP);

        define(id++, "JP NZ,byte", FETCH_ADDRESS, JUMP_NZ);
        define(id++, "JP Z,byte", FETCH_ADDRESS, JUMP_Z);
        define(id++, "JP byte", FETCH_ADDRESS, JUMP);
        define(id++, "JP GT,byte", FETCH_ADDRESS, JUMP_GT);
        define(id++, "JP LT,byte", FETCH_ADDRESS, JUMP_LT);

        define(id++, "NOP 7", NOP);

        define(id++, "STOP", STOP);
        stopInstruction = id - 1;

        maxOps = id;
    }

    public int getOpcodeFromName(String name) {
        for (Integer i : names.keySet()) {
            if (names.get(i).equals(name)) return i;
        }
        return 0;
    }

    public void define(int opCode, String name, MicroOp... microcodes) {
        n[opCode] = microcodes;
        names.put(opCode, name);
    }

    public MicroOp[] getInstructionCode(int instruction) {
        //if (instruction>=n.length) return null;
        int index = ((instruction%n.length)+n.length)%n.length;
        return n[index];
    }

    public String getInstructionName(int instruction) {
        return names.get(instruction);
    }

}
