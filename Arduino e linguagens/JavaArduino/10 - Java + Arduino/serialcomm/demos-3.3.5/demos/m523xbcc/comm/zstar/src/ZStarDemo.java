import java.io.IOException;

import jcontrol.io.Console;

/**
 * Java file created by JControl/IDE
 *
 * @author roebbenack
 * @date 05.04.07 12:58
 *
 */
public class ZStarDemo {
 
    public ZStarDemo() throws IOException {

        Console.out.println("creating uart instances");        
        ZStar zstar = new ZStar();
    
        Console.out.println("starting sniffer...");        
        byte bytes[] = new byte[100];
        while ( true ) {
            int readbytes =  zstar.read(bytes);
            if (readbytes!=0) {
                for (int i=0; i<readbytes; i++) {
                    Console.out.print("0x"+Integer.toHexString(bytes[i])+" ");
                }
                Console.out.println();
            }
        }

    }

    public static void main(String[] args) throws IOException {
        new ZStarDemo();
    }
}
