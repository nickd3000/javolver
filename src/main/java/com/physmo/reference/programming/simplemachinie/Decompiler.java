package com.physmo.reference.programming.simplemachinie;

public class Decompiler {
    Microcode microcode = new Microcode();

    public String decompile(SimpleMachine2 sm, int pcStart) {
        String output = "";
        //for (int i=0;i<)

        int pc = pcStart;
        int fetchedByte = 0;
        int targetByte = 0;
        while (pc < sm.memory.length) {
            int fetched = sm.memory[pc++];

            MicroOp[] microOps = microcode.getInstructionCode(fetched);
            if (microOps==null) continue;

            String name = microcode.getInstructionName(fetched);

            if (name != null && !name.equals("NOP")) output += System.lineSeparator() + pc + " - " + name;
            if (doesInstructionFetchByte(microOps)) {
                fetchedByte = sm.memory[pc++];

                output += "  - " + fetchedByte;
                if (fetchedByte<sm.memSize) {
                    targetByte = sm.memory[fetchedByte];
                    output += "("+targetByte+")";
                }
            }
            //output += System.lineSeparator();
        }
        output += System.lineSeparator();
        return output;
    }

    public boolean doesInstructionFetchByte(MicroOp[] microOps) {
        if (microOps == null) return false;
        for (MicroOp microOp : microOps) {
            if (microOp == MicroOp.FETCH_ADDRESS) return true;
            if (microOp == MicroOp.FETCH_BYTE) return true;
        }
        return false;
    }

}
