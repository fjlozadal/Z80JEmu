
package z80Jemu;

    ///////////////////////////////////
    //    Description of registers  ///
    ///////////////////////////////////
    

    /* 
    |Name||Length (bits) |  |    Details       |

    -------------------(Special purpose registers I)-------------|
    regPC          16         PROGRAM COUNTER
    regSP          16         STACK POINTER
    ------------------------(Main register set)------------------|
    regA           8      GENERAL PURPOSE REG(ACUMMULATOR)
    regF           8      GENERAL PURPOSE REG(FLAG)
    regB           8      GENERAL PURPOSE REG
    regC           8      GENERAL PURPOSE REG
    regD           8      GENERAL PURPOSE REG
    regE           8      GENERAL PURPOSE REG
    regH           8      GENERAL PURPOSE REG
    regL           8      GENERAL PURPOSE REG
    -------------------(Alternative register set)----------------|
    regAx           8     GENERAL PURPOSE REG(ACUMMULATOR)       
    regFx           8     GENERAL PURPOSE REG(FLAG)              
    regBx           8     GENERAL PURPOSE REG       
    regCx           8     GENERAL PURPOSE REG
    regDx           8     GENERAL PURPOSE REG
    regEx           8     GENERAL PURPOSE REG
    regHx           8     GENERAL PURPOSE REG
    regLx           8     GENERAL PURPOSE REG
    -------------------(Special purpose registers II)------------|
    IX             16      INDEXED ADDRESING REG
    IY             16      INDEXED ADDRESING REG
    I              7      INTERRUPT PAGE ADDRESS REG 
    R              8      MEMORY REFRESH REG
-   -------------------------------------------------------------|
    
    ///////////////////////////////////
    //    Description of flags      ///
    ///////////////////////////////////
    
    |Name| |Length(Bits)|        Details            |

    -------------------(  regF and regFx Registers  )-----------------|
    CF          1            CARRY FLAG 
    NF          1            ADD/SUBSTRACT FLAG
    PF          1            PARITY/OVERFLOW FLAG
    XF          1            NOT USED!
    HF          1            HALF-CARRY FLAG
    YF          1            NOT USED!
    ZF          1            ZERO FLAG
    SF          1            SIGN FLAG

   -------------------------------------------------------------------|
    

    */
import z80Jemu.Processor.IntMode;
public class ProcessorState {
    
   
    private int regA, regB, regC, regD, regE, regH, regL;
    // Flags SF, ZF, 5(YF), HF, 3(XF), PF y NF (ADDSUB), CF
    private int regF;
    
    // Last instruction modified flags
    private boolean flagQ;
   
    //Alternative Accumulator
    private int regAx;
    
    //Alternative flags
    private int regFx;
    
    // Alternative registers
    private int regBx, regCx, regDx, regEx, regHx, regLx;
    

    // Specific purpose registers
    private int regPC;
    private int regIX;
    private int regIY;
    private int regSP;
    private int regI;
    private int regR;
    
    //Interruption Flip-Flops
    private boolean ffIFF1 = false;
    private boolean ffIFF2 = false;
    
    // EI enable instructions AFTER execute
    // the next instruction (except if the next instruction is EI...)
    private boolean pendingEI = false;
    
    // NMI state
    private boolean activeNMI = false;
    // If INT is active
    // In 48  INT will be active during 32 clock cycles
    // In the 128 y superiores, will be active 36 clock cycles
    private boolean activeINT = false;
    // Interruption modes
    private IntMode modeINT = IntMode.IM0;
    // halted == true when CPU is executing  HALT
    private boolean halted = false;
    //Intern register
    private int memptr;
    
    
    /////////////////
    // Address Bus //
    /////////////////
    private Pin A[]; 
  
    
    ///////////////
    // Data Bus //
    //////////////
    private Pin D[];
    
    
    ////////////////////
    // System Control //
    ///////////////////
    
    private Pin M1;
    private Pin MREQ;
    private Pin IORQ;
    private Pin RD;
    private Pin WR;
    private Pin RFSH;
    
    
    /////////////////
    // CPU Control //
    /////////////////
    private Pin HALT;
    private Pin WAIT;
    private Pin INT;
    private Pin NMI;
    private Pin RESET;
    
    /////////////////////
    // CPU BUS Control //
    /////////////////////
    private Pin BUSRQ;
    private Pin BUSACK;
    
    /////////////////////
    // POWER and CLOCK //
    /////////////////////
    
    
    //Pin FIVE_VOLTS;
    //Pin GRND;
    //Pin CLK;

    public ProcessorState() {
        

        
        //Array of pins (16 Address bus pins)
        Pin tempA[] = new Pin[16];
   
        //set-up Address bus pins
        Pin temp;
        temp = new Pin(false,true,Pin.HIGH_LEVEL,0,"A0");
        tempA[0]=temp;
        temp = new Pin(false,true,Pin.HIGH_LEVEL,0,"A1");
        tempA[1]=temp;
        temp = new Pin(false,true,Pin.HIGH_LEVEL,0,"A2");
        tempA[2]=temp;
        temp = new Pin(false,true,Pin.HIGH_LEVEL,0,"A3");
        tempA[3]=temp;
        temp = new Pin(false,true,Pin.HIGH_LEVEL,0,"A4");
        tempA[4]=temp;
        temp = new Pin(false,true,Pin.HIGH_LEVEL,0,"A5");
        tempA[5]=temp;
        temp = new Pin(false,true,Pin.HIGH_LEVEL,0,"A6");
        tempA[6]=temp;
        temp = new Pin(false,true,Pin.HIGH_LEVEL,0,"A7");
        tempA[7]=temp;
        temp = new Pin(false,true,Pin.HIGH_LEVEL,0,"A8");
        tempA[8]=temp;
        temp = new Pin(false,true,Pin.HIGH_LEVEL,0,"A9");
        tempA[9]=temp;
        temp = new Pin(false,true,Pin.HIGH_LEVEL,0,"A10");
        tempA[10]=temp;
        temp = new Pin(false,true,Pin.HIGH_LEVEL,0,"A11");
        tempA[11]=temp;
        temp = new Pin(false,true,Pin.HIGH_LEVEL,0,"A12");
        tempA[12]=temp;
        temp = new Pin(false,true,Pin.HIGH_LEVEL,0,"A13");
        tempA[13]=temp;
        temp = new Pin(false,true,Pin.HIGH_LEVEL,0,"A14");
        tempA[14]=temp;
        temp = new Pin(false,true,Pin.HIGH_LEVEL,0,"A15");
        tempA[15]=temp;
        
        this.A = tempA;
        
        
        //Array of pins (n=8)
        Pin tempD[] = new Pin[8];
        
        //set-up Data bus pins
        temp = new Pin(true,true,Pin.HIGH_LEVEL,0,"D0");
        tempD[0]=temp;
        temp = new Pin(true,true,Pin.HIGH_LEVEL,0,"D1");
        tempD[1]=temp;
        temp = new Pin(true,true,Pin.HIGH_LEVEL,0,"D2");
        tempD[2]=temp;
        temp = new Pin(true,true,Pin.HIGH_LEVEL,0,"D3");
        tempD[3]=temp;
        temp = new Pin(true,true,Pin.HIGH_LEVEL,0,"D4");
        tempD[4]=temp;
        temp = new Pin(true,true,Pin.HIGH_LEVEL,0,"D5");
        tempD[5]=temp;
        temp = new Pin(true,true,Pin.HIGH_LEVEL,0,"D6");
        tempD[6]=temp;
        temp = new Pin(true,true,Pin.HIGH_LEVEL,0,"D7");
        tempD[7]=temp;
        this.D = tempD;
        
        //System Control
        temp= new Pin(false,true,Pin.LOW_LEVEL,1,"M1");
        this.M1 = temp;
        
        temp= new Pin(false,true,Pin.LOW_LEVEL,1,"MREQ");
        this.MREQ = temp;
        
        temp= new Pin(false,true,Pin.LOW_LEVEL,1,"IORQ");
        this.IORQ = temp;
        
        temp= new Pin(false,true,Pin.LOW_LEVEL,1,"RD");
        this.RD = temp;
        
        temp= new Pin(false,true,Pin.LOW_LEVEL,1,"WR");
        this.WR = temp;
        
        temp= new Pin(false,true,Pin.LOW_LEVEL,1,"RFSH");
        this.RFSH = temp;
        
        //CPU Control
        
        temp= new Pin(false,true,Pin.LOW_LEVEL,1,"HALT");
        this.HALT = temp;
        
        temp= new Pin(true,false,Pin.LOW_LEVEL,1,"WAIT");
        this.WAIT = temp;
        
        temp= new Pin(true,false,Pin.LOW_LEVEL,1,"INT");
        this.INT = temp;
        
        //**active in negative edge (flanco de bajada)** read z80 datasheet
         temp= new Pin(true,false,Pin.LOW_LEVEL,1,"NMI");
        this.NMI = temp;
        
        //**active negative edge (read documentation) 
        temp= new Pin(true,false,Pin.LOW_LEVEL,1,"RESET");
        this.RESET = temp;
        
        //Bus Control
        
         temp= new Pin(true,false,Pin.LOW_LEVEL,1,"BUSRQ");
        this.BUSRQ = temp;
        
         temp= new Pin(false,true,Pin.LOW_LEVEL,1,"BUSACK");
        this.BUSACK = temp;
    }

    public Pin[] getA() {
        return A;
    }

    public void setA(Pin[] A) {
        this.A = A;
    }

    public Pin[] getD() {
        return D;
    }

    public void setD(Pin[] D) {
        this.D = D;
    }

    public Pin getM1() {
        return M1;
    }

    public void setM1(Pin M1) {
        this.M1 = M1;
    }

    public Pin getMREQ() {
        return MREQ;
    }

    public void setMREQ(Pin MREQ) {
        this.MREQ = MREQ;
    }

    public Pin getIORQ() {
        return IORQ;
    }

    public void setIORQ(Pin IORQ) {
        this.IORQ = IORQ;
    }

    public Pin getRD() {
        return RD;
    }

    public void setRD(Pin RD) {
        this.RD = RD;
    }

    public Pin getWR() {
        return WR;
    }

    public void setWR(Pin WR) {
        this.WR = WR;
    }

    public Pin getRFSH() {
        return RFSH;
    }

    public void setRFSH(Pin RFSH) {
        this.RFSH = RFSH;
    }

    public Pin getHALT() {
        return HALT;
    }

    public void setHALT(Pin HALT) {
        this.HALT = HALT;
    }

    public Pin getWAIT() {
        return WAIT;
    }

    public void setWAIT(Pin WAIT) {
        this.WAIT = WAIT;
    }

    public Pin getINT() {
        return INT;
    }

    public void setINT(Pin INT) {
        this.INT = INT;
    }

    public Pin getNMI() {
        return NMI;
    }

    public void setNMI(Pin NMI) {
        this.NMI = NMI;
    }

    public Pin getRESET() {
        return RESET;
    }

    public void setRESET(Pin RESET) {
        this.RESET = RESET;
    }

    public Pin getBUSRQ() {
        return BUSRQ;
    }

    public void setBUSRQ(Pin BUSRQ) {
        this.BUSRQ = BUSRQ;
    }

    public Pin getBUSACK() {
        return BUSACK;
    }

    public void setBUSACK(Pin BUSACK) {
        this.BUSACK = BUSACK;
    }
    
    //-------------------------------
    
    
    // Acceso a registros de 8 bits
    public final int getRegA() {
        return regA;
    }

    public final void setRegA(int value) {
        regA = value & 0xff;
    }
    
    public final int getRegF() {
        return regF;
    }

    public final void setRegF(int value) {
        regF = value & 0xff;
    }

    public final int getRegB() {
        return regB;
    }

    public final void setRegB(int value) {
        regB = value & 0xff;
    }

    public final int getRegC() {
        return regC;
    }

    public final void setRegC(int value) {
        regC = value & 0xff;
    }

    public final int getRegD() {
        return regD;
    }

    public final void setRegD(int value) {
        regD = value & 0xff;
    }

    public final int getRegE() {
        return regE;
    }

    public final void setRegE(int value) {
        regE = value & 0xff;
    }

    public final int getRegH() {
        return regH;
    }

    public final void setRegH(int value) {
        regH = value & 0xff;
    }

    public final int getRegL() {
        return regL;
    }

    public final void setRegL(int value) {
        regL = value & 0xff;
    }

    // Acceso a registros alternativos de 8 bits
    public final int getRegAx() {
        return regAx;
    }

    public final void setRegAx(int value) {
        regAx = value & 0xff;
    }
    
    public final int getRegFx() {
        return regFx;
    }

    public final void setRegFx(int value) {
        regFx = value & 0xff;
    }

    public final int getRegBx() {
        return regBx;
    }

    public final void setRegBx(int value) {
        regBx = value & 0xff;
    }

    public final int getRegCx() {
        return regCx;
    }

    public final void setRegCx(int value) {
        regCx = value & 0xff;
    }

    public final int getRegDx() {
        return regDx;
    }

    public final void setRegDx(int value) {
        regDx = value & 0xff;
    }

    public final int getRegEx() {
        return regEx;
    }

    public final void setRegEx(int value) {
        regEx = value & 0xff;
    }

    public final int getRegHx() {
        return regHx;
    }

    public final void setRegHx(int value) {
        regHx = value & 0xff;
    }

    public final int getRegLx() {
        return regLx;
    }

    public final void setRegLx(int value) {
        regLx = value & 0xff;
    }

    // Acceso a registros de 16 bits
    public final int getRegAF() {
        return (regA << 8) | regF;
    }

    public final void setRegAF(int word) {
        regA = (word >>> 8) & 0xff;

        regF = word & 0xff;
    }

    public final int getRegAFx() {
        return (regAx << 8) | regFx;
    }

    public final void setRegAFx(int word) {
        regAx = (word >>> 8) & 0xff;
        regFx = word & 0xff;
    }

    public final int getRegBC() {
        return (regB << 8) | regC;
    }

    public final void setRegBC(int word) {
        regB = (word >>> 8) & 0xff;
        regC = word & 0xff;
    }

    public final int getRegBCx() {
        return (regBx << 8) | regCx;
    }

    public final void setRegBCx(int word) {
        regBx = (word >>> 8) & 0xff;
        regCx = word & 0xff;
    }

    public final int getRegDE() {
        return (regD << 8) | regE;
    }

    public final void setRegDE(int word) {
        regD = (word >>> 8) & 0xff;
        regE = word & 0xff;
    }

    public final int getRegDEx() {
        return (regDx << 8) | regEx;
    }

    public final void setRegDEx(int word) {
        regDx = (word >>> 8) & 0xff;
        regEx = word & 0xff;
    }

    public final int getRegHL() {
        return (regH << 8) | regL;
    }

    public final void setRegHL(int word) {
        regH = (word >>> 8) & 0xff;
        regL = word & 0xff;
    }

    public final int getRegHLx() {
        return (regHx << 8) | regLx;
    }

    public final void setRegHLx(int word) {
        regHx = (word >>> 8) & 0xff;
        regLx = word & 0xff;
    }

    // Acceso a registros de propósito específico
    public final int getRegPC() {
        return regPC;
    }

    public final void setRegPC(int address) {
        regPC = address & 0xffff;
    }

    public final int getRegSP() {
        return regSP;
    }

    public final void setRegSP(int word) {
        regSP = word & 0xffff;
    }

    public final int getRegIX() {
        return regIX;
    }

    public final void setRegIX(int word) {
        regIX = word & 0xffff;
    }

    public final int getRegIY() {
        return regIY;
    }

    public final void setRegIY(int word) {
        regIY = word & 0xffff;
    }

    public final int getRegI() {
        return regI;
    }

    public final void setRegI(int value) {
        regI = value & 0xff;
    }

    public final int getRegR() {
        return regR;
    }

    public final void setRegR(int value) {
        regR = value & 0xff;
    }

    // Acceso al registro oculto MEMPTR
    public final int getMemPtr() {
        return memptr;
    }

    public final void setMemPtr(int word) {
        memptr = word & 0xffff;
    }
    
    // Acceso a los flip-flops de interrupción
    public final boolean isIFF1() {
        return ffIFF1;
    }

    public final void setIFF1(boolean state) {
        ffIFF1 = state;
    }

    public final boolean isIFF2() {
        return ffIFF2;
    }

    public final void setIFF2(boolean state) {
        ffIFF2 = state;
    }

    public final boolean isNMI() {
        return activeNMI;
    }
    
    public final void setNMI(boolean nmi) {
        activeNMI = nmi;
    }

    // La línea de NMI se activa por impulso, no por nivel
    public final void triggerNMI() {
        activeNMI = true;
    }

    // La línea INT se activa por nivel
    public final boolean isINTLine() {
        return activeINT;
    }
    
    public final void setINTLine(boolean intLine) {
        activeINT = intLine;
    }

    //Acceso al modo de interrupción
    public final IntMode getIM() {
        return modeINT;
    }

    public final void setIM(IntMode mode) {
        modeINT = mode;
    }

    public final boolean isHalted() {
        return halted;
    }

    public void setHalted(boolean state) {
        halted = state;
    }
    
    public final boolean isPendingEI() {
        return pendingEI;
    }
    
    public final void setPendingEI(boolean state) {
        pendingEI = state;
    }

    /**
     * @return the flagQ
     */
    public boolean isFlagQ() {
        return flagQ;
    }

    /**
     * @param flagQ the flagQ to set
     */
    public void setFlagQ(boolean flagQ) {
        this.flagQ = flagQ;
    }


  
    
}
