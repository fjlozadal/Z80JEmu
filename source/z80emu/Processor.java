/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z80emu;

    ///////////////////////////////////
    //    Description of registers  ///
    ///////////////////////////////////
    

    /* 
    |Name||Length(Bytes)| |Position in registers.data[]| |              Details                |

    -------------------(Special purpose registers I)-----------------------------------------
    PC          2                    0                   PROGRAM COUNTER
    SP          2                    2                   STACK POINTER
    ------------------------(Main register set)------------------------------------------------
    A           1                    4                   GENERAL PURPOSE REG(ACUMMULATOR)
    F           1                    5                   GENERAL PURPOSE REG(FLAG)
    B           1                    6                   GENERAL PURPOSE REG(ACUMMULATOR)
    C           1                    7                   GENERAL PURPOSE REG(FLAG)
    D           1                    8                   GENERAL PURPOSE REG(ACUMMULATOR)
    E           1                    9                   GENERAL PURPOSE REG(FLAG)
    H           1                    10                  GENERAL PURPOSE REG(ACUMMULATOR)
    L           1                    11                  GENERAL PURPOSE REG(FLAG)
    -------------------(Alternative register set)----------------------------------------
    A'           1                   12                 GENERAL PURPOSE REG(ACUMMULATOR)
    F'           1                   13                 GENERAL PURPOSE REG(FLAG)
    B'           1                   14                 GENERAL PURPOSE REG(ACUMMULATOR)
    C'           1                   15                 GENERAL PURPOSE REG(FLAG)
    D'           1                   16                 GENERAL PURPOSE REG(ACUMMULATOR)
    E'           1                   17                 GENERAL PURPOSE REG(FLAG)
    H'           1                   18                 GENERAL PURPOSE REG(ACUMMULATOR)
    L'           1                   19                 GENERAL PURPOSE REG(FLAG)
    -------------------(Special purpose registers II)-----------------------------------------
    IX           2                   20                 INDEXED ADDRESING REG
    IY           2                   22                 INDEXED ADDRESING REG
    I            2                   24                 INTERRUPT PAGE ADDRESS REG 
    R            2                   25                 MEMORY REFRESH REG
-   -------------------------------------------------------------------------------------------
    
    ///////////////////////////////////
    //    Description of flags      ///
    ///////////////////////////////////
    
    |Name||Length(Bits)| |Position in flags[]| |              Details                |

    -------------------(Special purpose registers I)-----------------------------------------
    CF          1                    0                   CARRY FLAG 
    NF          1                    1                   ADD/SUBSTRACT FLAG
    PF          1                    2                   PARITY/OVERFLOW FLAG
    XF          1                    3                   NOT USED!
    HF          1                    4                   HALF-CARRY FLAG
    YF          1                    5                   NOT USED!
    ZF          1                    6                   ZERO FLAG
    SF          1                    7                   SIGN FLAG

------------------------(Main register set)------------------------------------------------
    

    */

public class Processor {
    ///////////////////////////
    //   CONSTANTS(DEFINES)  //
    ///////////////////////////
    public static final int POS_REG_PC = 0;
    public static final int POS_REG_SP = 2;
    
    public static final int POS_REG_A = 4;
    public static final int POS_REG_F = 5;
    public static final int POS_REG_B = 6;
    public static final int POS_REG_C = 7;
    public static final int POS_REG_D = 8;
    public static final int POS_REG_E = 9;
    public static final int POS_REG_H = 10;
    public static final int POS_REG_L = 11;
    
    public static final int POS_REG_A_ALT = 12;
    public static final int POS_REG_F_ALT = 13;
    public static final int POS_REG_B_ALT = 14;
    public static final int POS_REG_C_ALT = 15;
    public static final int POS_REG_D_ALT= 16;
    public static final int POS_REG_E_ALT= 17;
    public static final int POS_REG_H_ALT= 18;
    public static final int POS_REG_L_ALT= 19;
    
    public static final int POS_REG_IX= 20;
    public static final int POS_REG_IY= 22;
    public static final int POS_REG_I= 24;
    public static final int POS_REG_R= 25;
    
    public static final int POS_FLAG_C= 0;
    public static final int POS_FLAG_N= 1;
    public static final int POS_FLAG_P= 2;
    public static final int POS_FLAG_X= 3;
    public static final int POS_FLAG_H= 4; 
    public static final int POS_FLAG_Y= 5;
    public static final int POS_FLAG_Z= 6;
    public static final int POS_FLAG_S= 7;
    
    public static final int POS_FLAG_C_ALT= 0;
    public static final int POS_FLAG_N_ALT= 1;
    public static final int POS_FLAG_P_ALT= 2;
    public static final int POS_FLAG_X_ALT= 3;
    public static final int POS_FLAG_H_ALT= 4; 
    public static final int POS_FLAG_Y_ALT= 5;
    public static final int POS_FLAG_Z_ALT= 6;
    public static final int POS_FLAG_S_ALT= 7;
    
    


    
    
     ////////////////
    //   Memories  //
    /////////////////
    private Memory registers;
    
    ////////////////
    //   Flags  //
    /////////////////
    private boolean flags[];
    
    
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

    public Processor() {
        
        //Processor registers(26 bytes of regs)

        Memory reg= new Memory(26,true);
        for(int i=0; i<26;i++)reg.getData()[i]= 0x00;
        this.registers = reg;
        
        //Processor flags (8X2 flags of 1bit)  
        boolean tempFlags[] = new boolean[16];
        for(int i=0;i<16;i++)tempFlags[i]=false;
        this.flags = tempFlags;
        
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
    
    
  
    
}
