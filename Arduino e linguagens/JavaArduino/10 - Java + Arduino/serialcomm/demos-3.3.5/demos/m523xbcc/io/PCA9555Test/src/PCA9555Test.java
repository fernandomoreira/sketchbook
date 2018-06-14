/*
 * PCA9555Test.java
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

import jcontrol.bus.i2c.PCA9555;
import jcontrol.io.Buzzer;
import jcontrol.io.Console;
import jcontrol.lang.ThreadExt;

/**
 * <p>
 * Demonstrate the programming of an I2C device. In specially the PCA9555
 * multi i/o expander. This demo infinite switch on every connected led in
 * row for a short time and also give a noisy beep if the switch near
 * lightning led is pressed.
 * </p>
 */
public class PCA9555Test {

    /**
     * Application Constructor.
     */
    public PCA9555Test() {
        // display startup message
        Console.out.println("PCA9555 Test Program.");
        Console.out.println();
        // connect to device
        PCA9555 pca;
        try {
            pca = new PCA9555(0x40);

            try {
                Console.out.println( "configure GPIOs");
                for ( int i = 0; i < 8; i++) {
                    pca.setMode( i*2, PCA9555.INPUT); // all even values are keys
                    pca.setMode( i*2+1, PCA9555.OUTPUT); // all odd values are leds
                }
            } catch (IOException e) {
                Console.out.println( "failed to setup GPIOs");
                return;
            }

            Buzzer b = new Buzzer();

            Console.out.println( "running knight rider animation");
            Console.out.println( "(press a key to get a sound while the light is at key-position)");
            for (;;) {
                for (;;) {
                    try {
                        for (int i=1; i<16; i+=2) {
                            pca.setState(i, false); // switch on led: low-active!
                            if ( !pca.getState(i-1) ) { b.on(5000, 20); } // if key pressed at current light position -> beep
                            try { ThreadExt.sleep(50); } catch (InterruptedException e) {}
                            pca.setState(i, true); // switch off led: high-active!
                        }
                        for (int i=15; i>0; i-=2) {
                            pca.setState(i, false); // switch on led: low-active!
                            if ( !pca.getState(i-1) ) { b.on(5000, 20); } // if key pressed at current light position -> beep
                            try { ThreadExt.sleep(50); } catch (InterruptedException e) {}
                            pca.setState(i, true); // switch off led: high-active!
                        }
                    } catch (IOException e) {
                        Console.out.println("Error: Couldn't read from I2C-Object!");
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
        new PCA9555Test();// start measuring
    }
}