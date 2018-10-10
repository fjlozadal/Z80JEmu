/*
 * ////////////////////////////////////////////////////////////////////////
 * //                        JAVA Z80 EMULATOR                           //
 * ////////////////////////////////////////////////////////////////////////
 */
package z80emu;
import java.util.Scanner;


public class Z80Emu {
    
    // HEX to Decimal
    private static int D(String hex){
        return Integer.parseInt(hex,16);
    }
    
    // Decimal to HEX
    private static String H(int decimal){
        return Integer.toHexString(decimal);
    }

   
    public  static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String hex = sc.next();
        System.out.println(D(hex));
        int dec = sc.nextInt();
        System.out.println(H(dec));
        
        
        
    }
    
}
