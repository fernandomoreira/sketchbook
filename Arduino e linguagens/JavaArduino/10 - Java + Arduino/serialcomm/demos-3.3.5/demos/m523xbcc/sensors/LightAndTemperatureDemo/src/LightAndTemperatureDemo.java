/*
 * LightAndTemperatureDemo.java
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
import java.lang.Thread;
import jcontrol.lang.ThreadExt;

import jcontrol.bus.i2c.TMP75;
import jcontrol.bus.i2c.VM6101;
import jcontrol.comm.RS232;

/**
 * <p>
 * This application demonstrates how to read the sensors (TMP175 and VM6101) and to
 * send the values to the serial interface, using a simple ASCII-based "protocol".
 *
 * The application uses 19200bps, 8N1 and will send continuesly the following
 * strings via the serial port:
 *   AT#Temp:<TempValue>
 *   AT#Light:<LightValue>
 * Notes:
 * - <TempValue> and <LightValue> will be replaced by the real values from the
 *   sensor measurement.
 * - The temperature will be given in centigrades (10C = 100 centigrades).
 * - The light value will be given in Lux.
 * </p>
 */
public class LightAndTemperatureDemo extends Thread {

  /** I2C address of the TMP175 module. */
  public static final int TMP175_I2C_ADDRESS = 0x92;

  /** I2C address of the VM6101 module. */
  public static final int VM6101_I2C_ADDRESS = 0x20;

  /** Poll sensors in interval of 50ms */
  private static final int POLL_INTERVAL = 200;

  /** Baud rate used for serial communication */
  private static final int BAUD_RATE = 19200;

  /** String constants used for the simple ASII-based protocol */
  private static final String AT_PREFIX = "AT#";
  private static final String AT_TEMPERATURE = "Temp";
  private static final String AT_LIGHT = "Light";

  /** Local variables */
  private TMP75 m_tmp175;
  private VM6101 m_vm6101;
  private RS232 m_rs232;

  private int m_temperature;
  private int m_light;


  /**
   * Initialize Sensors
   */
  public void initSensors() {
        do {
            m_tmp175 = new TMP75(TMP175_I2C_ADDRESS);
            if (m_tmp175 != null) {
                try {
                    m_temperature = m_tmp175.getTemp();
                } catch (IOException e) {
                    m_tmp175 = null;
                }
            }
        } while (m_tmp175 == null);
        do {
            try {
                m_vm6101 = new VM6101(VM6101_I2C_ADDRESS);
                if (m_vm6101 != null) {
                    m_light = m_vm6101.getIlluminance(VM6101.CHANNEL_Y);
                } else {
                    m_vm6101 = null;
                }
            } catch (IOException e) {
                m_vm6101 = null;
            }
        } while (m_vm6101 == null);
  }

  /**
   * Initialize Serial Interface
   */
  public void initRS232() {
    try {
      m_rs232 = new RS232(BAUD_RATE);
    } catch(IOException e) {}
  }

  /**
   * Read values from sensors into local variables m_tmp175 and m_vm6101
   */
  private void readSensorData() {
    // poll temperature sensor
    if (m_tmp175 != null) {
      try {
        m_temperature = m_tmp175.getTemp();
      } catch (IOException e) {}
    }

    // poll light sensor
    if ( m_vm6101 != null ) {
      try {
        m_light = m_vm6101.getIlluminance(VM6101.CHANNEL_Y);
        // check if value overflows
        if (m_light > 32000) {
          m_light = 32767;
        }
      } catch (IOException e) {}
    }
  }

  /**
   * Send values from sensors to serial interface
   */
  private void sendData() {
    // Check if serial interface object exists
    if (m_rs232 != null) {
      // consume incoming data
      consumeIncomingData();

      // send temperature data to serial interface
      m_rs232.print(AT_PREFIX);
      m_rs232.print(AT_TEMPERATURE);
      m_rs232.print(":");
      m_rs232.println(String.valueOf(m_temperature));

      // send light data to serial interface
      m_rs232.print(AT_PREFIX);
      m_rs232.print(AT_LIGHT);
      m_rs232.print(":");
      m_rs232.println(String.valueOf(m_light));
    }
  }

  /**
   * Consumes characters coming from serial interface
   * (all characters are ignored)
   */
  private void consumeIncomingData() {
    try {
      for(;;) {
        if (m_rs232.available() > 0) {
          m_rs232.read();
        } else {
          break;
        }
        try {
          ThreadExt.sleep(1);
        } catch (InterruptedException e) {
        }
      }
    } catch(IOException e) {}
  }

  /**
   * Run method of the polling thread
   */
  public void run() {
    for(;;) {
      readSensorData();
      sendData();
      try {
        ThreadExt.sleep(POLL_INTERVAL);
      } catch(InterruptedException e) {}
    }
  }

  /**
   * Main method of the application
   * This method is started first. It constructs the polling thread,
   * initializes the sensors and serial interface and starts the
   * polling thread.
   */
  public static void main( String[] args) {
    LightAndTemperatureDemo thread = new LightAndTemperatureDemo();
    thread.initSensors();
    thread.initRS232();
    thread.start();
  }

}
