
package z80Jemu;

/**
 *
 * @author ZkoR
 * 
 * 
 */




public class Memory {
    
    private final int capacity; //(number of bytes)
    private byte data[];
    private final boolean writable;
    private byte ports[];
    private long time_cycles = 0;
    
    public Memory(){//32k memory by default
        capacity = 0x10000;
        data = new byte[capacity];
        ports = new byte[capacity];
        writable = true;   
    }

    public Memory(int capacity, boolean writable, int portsize) {
        byte temp[]= new byte[capacity];
        this.capacity = capacity;
        
        this.data = temp;
        temp= new byte[portsize];
        
        this.writable = writable;
        this.ports= temp;
    }
    
    // Memory dump 
    public byte[] getData() {
        return data;
    }
    
    //read exactly one byte
    public byte getOffSet(int address){ 
        return data[address];
    }
    
    //Load data to memory
    public void setData(byte[] data) {
        if (this.writable==true){
            this.data = data;
        }else{
            System.out.println(":( You're writing a ROM Memory...!");
        }
    }

    
    public int getCapacity() {
        return capacity;
    }
    
    //Is rom / ram?
    public boolean isWritable() {
        return writable;
    }
    
    
    public int fetchOpcode(int address) {
        // 3 clocks to fetch opcode from RAM and 1 execution clock
        time_cycles += 4;
        return data[address] & 0xff;
    }
    
    //Read one byte
    public int peek8(int address) {
        time_cycles += 3; // 3 clocks for read byte from RAM
        return data[address] & 0xff;
    }
    
    //Write one byte
    public void poke8(int address, int value) {
        time_cycles += 3; // 3 clocks for write byte to RAM
        data[address] = (byte)value;
    }
    
    //Read a word (16 bits)
    public int peek16(int address) {
        int lsb = peek8(address);
        int msb = peek8(address + 1);
        return (msb << 8) | lsb;
    }
    
    //Write a word (16bits)
    public void poke16(int address, int word) {
        poke8(address, word);
        poke8(address + 1, word >>> 8);
    }

    //Read a byte from bus
    public int inPort(int port) {
        time_cycles += 4; // 4 clocks for read byte from bus
        return ports[port] & 0xff;
    }
    
    //Write a byte from bus
    public void outPort(int port, int value) {
        time_cycles += 4; // 4 clocks for write byte to bus
        ports[port] = (byte)value;
    }
    
    
    public void addressOnBus(int address, int time_cycles) {
        // Additional clocks to be added on some instructions
        // Not to be changed, really.
        this.time_cycles += time_cycles;
    }

    public void interruptHandlingTime(int time_cycles) {
        // Additional clocks to be added on INT & NMI
        // Not to be changed, really.
        this.time_cycles += time_cycles;
    }

    public boolean isActiveINT() {
        return false;
    }

    public long getTstates() {
        return time_cycles;
    }

    public void reset() {
        time_cycles = 0;
    }

    public void setPorts(byte[] ports) {
        this.ports = ports;
    }
}
