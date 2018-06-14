/*
 * SimpleSound.java
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
import jcontrol.io.Buzzer;
import jcontrol.lang.ThreadExt;

/**
 * <p>Demonstrate how to programming the on board buzzer. In specially
 * this demo infinite makes 3 noisy beeps, waits a moment and start again.</p>
 *
 * @author RSt, roebbenack
 * @date 08.10.04 17:21
 */
public class SimpleSound {

    /**
     * standard constructor
     */
    public SimpleSound() {

        try {
            Buzzer b = new Buzzer();

            while ( true ) {

                for (int j = 0; j < 4; j++) {
                    // beep with 880Hz
                    // NOTE: frequency not working on this hardware (M523XBCC)!
                    b.on(880);
                    // wait for 150ms
                    ThreadExt.sleep(150);
                    b.off();
                    // wait for 50ms
                    ThreadExt.sleep(50);
                }

                // wait for 1000ms
                ThreadExt.sleep(1000);

            }

        } catch (InterruptedException e) {}
    }

    /**
     * main method. Program execution starts here.
     */
    public static void main(String[] args) {
        new SimpleSound();
    }
}
