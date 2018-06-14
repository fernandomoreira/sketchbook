package jcontrol.bus.i2c;
/*
 * VM6101.java
 * Copyright (C) 2000-2007 DOMOLOGIC Home Automation GmbH
 *
 * This    library    is free    software; you can redistribute it and/or
 * modify it under the terms of    the    GNU    Lesser General Public
 * License as published    by the Free    Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This    library    is distributed in the hope that    it will    be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A    PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received    a copy of the GNU Lesser General Public
 * License along with this library;    if not,    write to the Free Software
 * Foundation, Inc., 51    Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 */

import java.io.IOException;
import jcontrol.comm.I2C;
import jcontrol.io.Console;

/**
 * Accesses the VM6101 light sensor using the SM (I<sup>2</sup>C) bus.
 * Refer to the VM6101 data sheet for details.
 *
 * @version 1.0
 * @author  witte, velitchkov, roebbenack
 */
public class VM6101 extends I2C {

    /** channel constant */
    public static final int CHANNEL_Y = 0;
    /** channel constant */
    public static final int CHANNEL_R = 1;
    /** channel constant */
    public static final int CHANNEL_G = 2;
    /** channel constant */
    public static final int CHANNEL_B = 3;

    /** VM6101 register */
    public static final int REG_REVISION           =  0;
    /** VM6101 register */
    public static final int REG_N_PIXEL            =  1;
    /** VM6101 register */
    public static final int REG_SYS_CTRL           =  2;
    /** VM6101 register */
    public static final int REG_Y_STATUS           =  4;
    /** VM6101 register */
    public static final int REG_R_STATUS           =  9;
    /** VM6101 register */
    public static final int REG_G_STATUS           = 14;
    /** VM6101 register */
    public static final int REG_B_STATUS           = 19;
    /** VM6101 register */
    public static final int REG_THRESHOLD_CTRL     = 24;
    
    /**
     * VM6101 specific illuminance constant.
     */
    public static final int ILLUMINANCE  = 2100000;
    
    /**
     * Creates a new instance of the VM6101 driver on the
     * specified I2C address.
     * 
     * @param address I2C address
     */
    public VM6101(int address) throws IOException {
        super(address);
        this.write( new byte[] { REG_THRESHOLD_CTRL, 0});
    }

    /**
     * Reads a single raw sample.
     * 
     * @param channel channel
     * @return raw sample
     * @throws IOException in case of I/O errors
     * @see #CHANNEL_Y
     * @see #CHANNEL_R
     * @see #CHANNEL_G
     * @see #CHANNEL_B
     */
    public int getRawSample( int channel) throws IOException {
        if ( (channel < CHANNEL_Y) || (channel > CHANNEL_B)) {
            throw new IllegalArgumentException( "invalid channel");
        }
        int basereg = REG_Y_STATUS + (channel*5);
        
        this.write( new byte[] { REG_SYS_CTRL, (byte)channel});
        for (;;) {
            byte[] status = new byte[1];
            this.read( new byte[] { (byte)basereg}, status, 0, 1);
            if ( (status[0] & 0x04) != 0) {
                // new data available
                break;
            }
        }

        byte[] value = new byte[4];
        this.read( new byte[] { (byte)(basereg+1)}, value, 0, 4);
        
        return (((int)value[0] & 0xff) << 24) |
                (((int)value[1] & 0xff) << 16) |
                (((int)value[2] & 0xff) <<  8) |
                (((int)value[3] & 0xff) <<  0);
    }
    
    /**
     * Gets the illuminance on the specified channel.
     * 
     * @param channel Channel
     * @throws IOException in case of I/O errors
     */
    public int getIlluminance(int channel) throws IOException {
        return ILLUMINANCE / getRawSample(channel);
    }
}
