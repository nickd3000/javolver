package com.physmo.javolver.examples;

public class SimpleMachine {

    public final int memSize = 256+10;
    public int [] memory = new int[memSize];
    public int [] memoryHits = new int[memSize];
    public int maxMemoryHits = 0;
    public String console = "";
    int pc = 0; // program counter.
    int cmp = 0; // comparison flag. (0:equal 1:greater 2:less)
    int regA = 0;
    int regB = 0;

    final int NO_OP = 0;		// Do nothing
    final int COPY_A_B = 1;		// Copy reg a to reg b
    final int COPY_B_A = 2;
    final int COPY_M_A = 3;		// Copy next memory location data to A
    final int COPY_A_M = 4;
    final int COPY_M_B = 5;
    final int COPY_B_M = 6;
    final int CMP_A_B = 7;		// Compare A and B, store result in comparison flag.
    final int JMP = 8;
    final int JMPZ = 9;
    final int JMPNZ = 10;
    final int JMPGT = 11;
    final int JMPLT = 12;
    final int PRINT_A = 13;
    final int ADD_A_B = 14;		// result in A
    final int SUB_A_B = 15;		// result in a
    final int DIV_A_B = 16;		// result in a
    final int MUL_A_B = 17;		// result in a
    final int SWITCH_REGS = 18;	// swap a and b;
    final int JMP_A = 19;
    final int QUIT = 20;
    final int LAST_OP = 21;

    public int runCycle()
    {
        if (pc>memSize-4 || pc<0) return 1; // quit due to being out of bounds.


        int operator = memory[pc];
        int tmp = 0;


        memoryHits[pc]++;
        if (memoryHits[pc]>maxMemoryHits) maxMemoryHits=memoryHits[pc];

        switch (operator)
        {
            case NO_OP: pc++; break;
            case COPY_A_B: regB = regA; pc++; break;
            case COPY_B_A: regA = regB; pc++; break;
            case COPY_M_A: regA = memory[pc+1]; pc+=2; break;
            case COPY_A_M: memory[pc+1] = regA; pc+=2; break;
            case COPY_M_B: regB = memory[pc+1]; pc+=2; break;
            case COPY_B_M: memory[pc+1] = regB; pc+=2; break;
            case CMP_A_B:
                if (regA==regB) cmp=0;
                else if (regA>regB) cmp=1;
                else cmp=2;
                pc++; break;
            case JMP: pc=memory[pc+1]; break;
            case JMPZ: if (cmp==0) pc=memory[pc+1]; else pc+=2; break;
            case JMPNZ: if (cmp!=0) pc=memory[pc+1]; else pc+=2; break;
            case JMPGT: if (cmp==1) pc=memory[pc+1]; else pc+=2; break;
            case JMPLT: if (cmp==2) pc=memory[pc+1]; else pc+=2; break;
            case PRINT_A: safePrint(regA); pc++; break;
            case ADD_A_B: regA = regA+regB; pc++; break;
            case SUB_A_B: regA = regA-regB; pc++; break;
            case DIV_A_B: regA = (regB!=0)?regA/regB:0; pc++; break;
            case MUL_A_B: regA = regA*regB; pc++; break;
            case SWITCH_REGS: tmp = regA; regA=regB; regB=tmp; pc++; break;
            case JMP_A: pc=regA; break;
            //case QUIT: return 1;
            default: pc++;
        }


        return 0;
    }

    public void safePrint(int i)
    {
        char c = (char)i;

        if (c<'A' || c>'Z') return;

        //System.out.print((char)i);
        console = console + (char)i;
    }

    public int getMaxHits()
    {

        return maxMemoryHits;
    }
}