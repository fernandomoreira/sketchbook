/*
 * I2CTestDevice.java
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
import jcontrol.comm.I2C;

/**
 * Helper-Class for I2CPortScan. It's creates an I2C-Object for the overgiven address
 * and provides a #test() - method to check if a sensor exists.
 *
 * @author roebbenack
 */
public class I2CTestDevice extends I2C {

    /**
     * Create a new Test-I2C-Object with specific address.
     */
    public I2CTestDevice(int address) {
        super(address);
    }

    /**
     * Try to communicate with the I2C sensor. If it fails, an IOException is thrown.
     *
     * @throws IOException
     *                 if no sensor exists
     */
    public void test() throws IOException {
        read();
    }
}
