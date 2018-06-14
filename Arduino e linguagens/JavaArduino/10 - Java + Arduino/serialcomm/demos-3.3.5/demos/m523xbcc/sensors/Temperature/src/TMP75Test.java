/*
 * TMP75Test.java
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
import jcontrol.io.Console;
import jcontrol.lang.ThreadExt;
import jcontrol.bus.i2c.TMP75;

/**
 * <p>
 * Demonstrate the programming of an I2C device. In specially the TMP75
 * temperature sensor. This demo infinite read the temp-value from the
 * sensor and print it to the console.
 * </p>
 */
public class TMP75Test {

    /**
     * Application Constructor.
     */
    public TMP75Test() {
        // display startup message
        Console.out.println("TMP75 Test Program.");
        Console.out.println();
        // connect to device
        TMP75 tmp75;
        tmp75 = new TMP75(0x92);
        for (;;) {
            int temp = 0;
            for (;;) {
                try {
                    temp = tmp75.getTemp();//get temperature value
                } catch (IOException e) {
                    Console.out.println("Error: Couldn't read from I2C-Object!");
                }
                int whole = temp / 10;
                int parts = temp % 10;//one tenth Centigrade
                Console.out.println("Temperature = ".concat(Integer.toString(whole)).concat(".").concat(Integer.toString(parts)).concat("\u00b0C"));
                try {
                    ThreadExt.sleep(500);//do nothing for 500 msecs
                } catch (InterruptedException e) {
                }
            }
        }
    }

    /**
     * Main method. Program execution starts here.
     */
    public static void main(String[] args) {
        new TMP75Test();// start measuring
    }
}