/*
 * VM6101Test.java
 * Copyright (C) 2000-2007 DOMOLOGIC Home Automation GmbH
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 */

import java.io.IOException;

import jcontrol.bus.i2c.VM6101;
import jcontrol.io.Console;
import jcontrol.lang.ThreadExt;

/**
 * <p>
 * Demonstrate the programming of an I2C device. In specially the VM6101
 * ambient light sensor. This demo infinite read the red, green, blue und
 * luminance value from the sensor and print it to the console.
 * </p>
 */
public class VM6101Test {

    /**
     * Application Constructor.
     */
    public VM6101Test() {
        // display startup message
        Console.out.println("VM6101 Test Program.");
        Console.out.println();
        // connect to device
        VM6101 vm6101;
        try {
            vm6101 = new VM6101(0x20);
            for (;;) {
                int red, green, blue, luminance;
                for (;;) {
                    try {
                        red       = vm6101.getIlluminance( VM6101.CHANNEL_R );
                        Console.out.println("red       = "+red);
                        green     = vm6101.getIlluminance( VM6101.CHANNEL_G );
                        Console.out.println("green     = "+green);
                        blue      = vm6101.getIlluminance( VM6101.CHANNEL_B );
                        Console.out.println("blue      = "+blue);
                        luminance = vm6101.getIlluminance( VM6101.CHANNEL_Y );
                        Console.out.println("luminance = "+luminance);
                    } catch (IOException e) {
                        Console.out.println("Error: Couldn't read from I2C-Object!");
                    }
                    Console.out.println();
                    try {
                        ThreadExt.sleep(500);//do nothing for 500 msecs
                    } catch (InterruptedException e) {
                    }
                }
            }
        } catch (IOException e1) {
            Console.out.println("Error: IOException while creating VM6101-Object!");
        }
    }

    /**
     * Main method. Program execution starts here.
     */
    public static void main(String[] args) {
        new VM6101Test();// start measuring
    }
}