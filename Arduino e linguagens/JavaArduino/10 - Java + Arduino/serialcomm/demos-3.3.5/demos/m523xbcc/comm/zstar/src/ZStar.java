import java.io.IOException;

import jcontrol.comm.UART;

/**
 * Provides access to the M5235BCC's Z-Star interface. Basically
 * this class wraps a UART object working on port 2 (UART2 is
 * used on the M5235BCC to communicate with the Z-Star module).
 * 
 * @author Lorenz Witte
 */
public class ZStar extends UART {

    /**
     * Creates a new ZStar instance. It will use UART port 2 and
     * configures it to work with 9600 baud, 8N1, no flow control.
     * 
     * @throws IOException if port is already in use
     */
    public ZStar() throws IOException {
        super(2);
        this.setBaudrate( 9600);
        this.setParams( 0);
    }

}
