/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z80emu;

/**
 *
 * @author ZkoR
 */
public class Memory {
    
    private final int capacity; //(number of bytes)
    private byte data[];
    private final boolean writable;

    public Memory(int capacity, boolean writable) {
        byte temp[]= new byte[capacity];
        
        //fill with zeros
        for(int i =0 ; i<capacity ; i++){
            temp[i]=0x00;
        }
        
        this.capacity = capacity;
        this.data = temp;
        this.writable = writable;
    }

    public byte[] getData() {
        return data;
    }
    
    public byte getOffSet(int position){
        return data[position];
    }

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

    public boolean isWritable() {
        return writable;
    }
   
    
}
