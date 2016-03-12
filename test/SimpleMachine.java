package test;

public class SimpleMachine {

	public final int memSize = 256;
	public int [] memory = new int[memSize];
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
	final int SWITCH_REGS = 16;	// swap a and b;
	final int QUIT = 17;
	
	public int runCycle()
	{
		int operator = memory[pc];
		int tmp = 0;
		
		if (pc>memSize-4) return 1; // quit due to being out of bounds.
			
		switch (operator)
		{
		case NO_OP: break;
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
		case SWITCH_REGS: tmp = regA; regA=regB; regB=tmp; pc++; break;
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
	
}