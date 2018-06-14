/*
 * I2CPortScan.java
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
import jcontrol.lang.ThreadExt;
import jcontrol.io.Console;
import jcontrol.comm.I2C;

/**
 * <p>The I2CPortScan tests each address between 0..254 for connected devices.
 * If found one - the address of device is print to the console.</p>
 *
 * @author roebbenack
 */
public class I2CPortScan extends I2C {

    public I2CPortScan(int address)    {
        super(address);
    }

    /**
     * searches for existing devices
     */
    public static void main(String[] args) {

        int    i;
        int    count =    0;

        // print startup message
        Console.out.println("Scanning I2C Port");
        Console.out.println("=================");
        Console.out.println();

        // scan    complete I2C bus
        for    (i = 0;    i <    255; i+=2) {
            try    {
                I2CTestDevice test = new I2CTestDevice(i);
                test.test();
                count++;
                Console.out.println("Device found at address 0x".concat(Integer.toHexString(i)));
                ThreadExt.sleep(500);
            } catch    (InterruptedException e) {
            } catch(IOException    e){
            }
        }

        // print shutdown message
        Console.out.println();
        Console.out.println("Scan finished with address 0x".concat(Integer.toHexString(i-1)));
        Console.out.println(Integer.toString(count).concat(" devices found."));

        // finish in endless loop
        for(;;)    {}
    }

}
