package com.physmo.reference.programming.simplemachinie;

public class Decompiler {
    Microcode microcode = new Microcode();

    public String decompile(SimpleMachine2 sm, int pcStart) {
        StringBuilder output = new StringBuilder();

        int pc = pcStart;
        int fetchedByte = 0;
        int targetByte = 0;

        int realInstructions=0;

        while (pc < sm.memory.length) {
            int fetched = sm.memory[pc++];

            MicroOp[] microOps = microcode.getInstructionCode(fetched);
            if (microOps == null) continue;

            String name = microcode.getInstructionName(fetched);

            if (name == null || name.contains("NOP")) continue;

            realInstructions++;

            output.append(System.lineSeparator()).append(pc).append(" - ").append(name);


            if (doesInstructionFetchByte(microOps)) {
                fetchedByte = sm.memory[pc++];

                output.append("  BYTE=").append(fetchedByte);

                if (doesInstructionFetchAddress(microOps)) {
                    if (fetchedByte < sm.memSize) {
                        targetByte = sm.memory[fetchedByte];
                        output.append(" #(").append(targetByte).append(")");
                    }
                }
            }

        }

        output.append(System.lineSeparator());
        output.append("Real Instructions:").append(realInstructions);
        output.append(System.lineSeparator());

        return output.toString();
    }

    public boolean doesInstructionFetchByte(MicroOp[] microOps) {
        if (microOps == null) return false;
        for (MicroOp microOp : microOps) {
            if (microOp == MicroOp.FETCH_ADDRESS) return true;
            if (microOp == MicroOp.FETCH_BYTE) return true;
        }
        return false;
    }

    public boolean doesInstructionFetchAddress(MicroOp[] microOps) {
        if (microOps == null) return false;
        for (MicroOp microOp : microOps) {
            if (microOp == MicroOp.FETCH_ADDRESS) return true;
        }
        return false;
    }
}
