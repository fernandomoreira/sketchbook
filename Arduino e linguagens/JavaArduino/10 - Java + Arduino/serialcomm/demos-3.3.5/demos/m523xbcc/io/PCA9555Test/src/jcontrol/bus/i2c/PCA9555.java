package jcontrol.bus.i2c;
/*
 * PCA9555.java
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
 * Accesses the PCA9555 Multi-I/O-Expander using the SM (I<sup>2</sup>C) bus.
 * Refer to the PCA9555 data sheet for details.
 * 
 * @version 1.0
 * @author  telkamp
 * @jcontrol.devices lib
 */
public class PCA9555 extends I2C {

    /**
     * Represents the GPIO state "HIGH".
     * Used for input and output channels.
     * @see #setState(int, boolean)
     * @see #getState(int)
     */
    public static final boolean HIGH = true;


    /** 
     * Represents the GPIO state "LOW"
     * Used for input and output channels.
     * @see #setState(int, boolean)
     * @see #getState(int)
     */
    public static final boolean LOW = false;


    /**
     * Represents a GPIO input mode
     * @see #setMode(int, int)
     * @see #getMode(int)
     */
    public static final int INPUT = 0x04;


    /**
     * Represents a GPIO input mode with integrated weak pullup resistor.
     * @see #setMode(int, int)
     * @see #getMode(int)
     */
    public static final int PULLUP = 0x04;


    /**
     * Represents a GPIO Push/Pull output mode.
     * The GPIO's output state is undefined.
     * @see #setMode(int, int)
     * @see #getMode(int)
     */
    public static final int OUTPUT = 0x06;


    /**
     * Represents a GPIO Push/Pull output mode.
     * The GPIO's output state is undefined.
     * @see #setMode(int, int)
     * @see #getMode(int)
     */
    public static final int PUSHPULL = 0x06;


    /**
     * Internal Registers of the PCA9555 Device
     */
    private static final int INPUT_PORT_0    = 0x00;
    private static final int INPUT_PORT_1    = 0x01;
    private static final int OUTPUT_PORT_0   = 0x02;
    private static final int OUTPUT_PORT_1   = 0x03;
    //private static final int POLARITY_PORT_0 = 0x04;
    //private static final int POLARITY_PORT_1 = 0x05;
    private static final int CONFIG_PORT_0   = 0x06;
    private static final int CONFIG_PORT_1   = 0x07;

    /**
     * The set mask defines a bitmask to set a dedicated bit 
     * given by a channel (0..15)
     */
    private static final byte[] SET_MASK = {0x01, 0x02, 0x04, 0x08, 
                                            0x10, 0x20, 0x40, (byte)0x80,
                                            0x01, 0x02, 0x04, 0x08, 
                                            0x10, 0x20, 0x40, (byte)0x80};
    /**
     * The clear mask defines a bitmask to reset a dedicated bit
     * given by a channel (0..15)
     */
    private static final byte[] CLR_MASK = {(byte)0xFE, (byte)0xFD, (byte)0xFB, (byte)0xF7, 
                                            (byte)0xEF, (byte)0xDF, (byte)0xBF, 0x7F,
                                            (byte)0xFE, (byte)0xFD, (byte)0xFB, (byte)0xF7, 
                                            (byte)0xEF, (byte)0xDF, (byte)0xBF, 0x7F};

    /**
     * Mirrored configuration options
     */
    private byte m_config_port_0;
    private byte m_config_port_1;
    private byte m_output_port_0;
    private byte m_output_port_1;

    public PCA9555(int address) throws IOException {
        // Call constructor of parent I2C class
        super(address);
        // Configure all ports to input mode
        m_config_port_0 = (byte) 0xFF;
        m_config_port_1 = (byte) 0xFF;
        write(new byte[] {CONFIG_PORT_0, m_config_port_0, m_config_port_1}, 0, 3);
        // Set all output ports to 1 as defined by the reset sequence
        m_output_port_0 = (byte) 0xFF;
        m_output_port_1 = (byte) 0xFF;
        write(new byte[] {OUTPUT_PORT_0, m_output_port_0, m_output_port_1}, 0, 3);
    }


    /** Sets a single IO-Pin.
     * The GPIO channel has to be chosen from the tables above.
     * The channel has to be in output mode.
     * @param channel logical identification number of the pin
     * @param state decides, if the Pin is to be turned <code>ON</code> or <code>OFF</code>
     * @see #HIGH
     * @see #LOW
     * @see #setMode(int, int)
     */
    public void setState(int channel, boolean state) throws IOException {
        if ((channel >=0) && (channel < 8)) {
            if (state) {
                m_output_port_0 |= SET_MASK[channel];
            } else {
                m_output_port_0 &= CLR_MASK[channel];
            }
            write(new byte[] {OUTPUT_PORT_0, m_output_port_0}, 0, 2);
        } else if (channel < 16) {
            if (state) {
                m_output_port_1 |= SET_MASK[channel];
            } else {
                m_output_port_1 &= CLR_MASK[channel];
            }
            write(new byte[] {OUTPUT_PORT_1, m_output_port_1}, 0, 2);
        } else {
            throw (new IOException());
        }
    };


    /** Gets the state of a single IO-Pin.
     * The GPIO channel has to be chosen from the tables above.
     * @param channel logical identification number of the pin
     * @return the level of the selected pin, equals <code>HIGH</code> or <code>LOW</code>.
     * @see #HIGH
     * @see #LOW
     */
    public boolean getState(int channel) throws IOException {
        byte[] result = new byte[1];
        if ((channel >=0) && (channel < 8)) {
            read(new byte[] {INPUT_PORT_0}, result, 0, 1);
            return ((result[0] & SET_MASK[channel]) != 0);
        } else if (channel < 16) {
            read(new byte[] {INPUT_PORT_1}, result, 0, 1);
            return ((result[0] & SET_MASK[channel]) != 0);
        } else {
            throw (new IOException());
        }
    };


    /** Sets the mode of the given pin.
     * The GPIO channel has to be chosen from the tables above.
     * <code>mode</code> is either an input mode identifier or a combination of output mode
     * identifier and a pin level representation.
     * @param channel logical identification number of the pin
     * @param mode a constant or combination of constants, defined in this class
     * @see #getMode(int)
     * @see #INPUT
     * @see #OUTPUT
     */
    public void setMode(int channel, int mode) throws IOException {
        if ((channel >=0) && (channel < 8)) {
            if (mode == INPUT) {
                m_config_port_0 |= SET_MASK[channel];
            } else {
                m_config_port_0 &= CLR_MASK[channel];
            }
            write(new byte[] {CONFIG_PORT_0, m_config_port_0}, 0, 2);
        } else if (channel < 16) {
            if (mode == INPUT) {
                m_config_port_1 |= SET_MASK[channel];
            } else {
                m_config_port_1 &= CLR_MASK[channel];
            }
            write(new byte[] {CONFIG_PORT_1, m_config_port_1}, 0, 2);
        } else {
            throw (new IOException());
        }
    };


    /** Gets the mode of the given pin.
     * The GPIO channel has to be chosen from the tables above.
     * Returned <code>mode</code> is a combination of input or output mode
     * identifier and a pin level representation.
     * @param channel logical identification number of the pin
     * @return the current mode
     * @see #setMode(int channel, int mode)
     * @see #INPUT
     * @see #OUTPUT
     */
    public int getMode(int channel) throws IOException {
        //byte[] result = new byte[1];
        if ((channel >=0) && (channel < 8)) {
            if ((m_config_port_0 & SET_MASK[channel]) != 0)
                return INPUT;
            else
                return OUTPUT;
        } else if (channel < 16) {
            if ((m_config_port_1 & SET_MASK[channel]) != 0)
                return INPUT;
            else
                return OUTPUT;
        } else {
            throw (new IOException());
        }
    };


    /**
     * Sets the mode of the entire port to INPUT or OUTPUT.
     */
    public void setPortMode(int portNr, int mode) throws IOException{
        // A HIGH state indicates the corresponding pin as an input.
        if((portNr<0) || (portNr>1)){
            throw new IOException();
        }
        byte[] message = {(byte) 0x06, (byte) 0xFF};
        if(portNr == 1){
            message[0] = (byte) 0x07;
        }
        if(mode==PUSHPULL){
            message[1] = (byte) 0x00;
        }
        this.write(message, 0, message.length);
    }
    
    /**
     * Sets the specified port to the given bit mask.
     * This methode only takes effect, when the corresponding
     * port is set to OUTPUT mode.
     */
    public void setPort(int portNr, byte bitMask) throws IOException{
        if((portNr<0) || (portNr>1)){
            throw new IOException();
        }
        byte[] message = {(byte) 0x02, bitMask};
        if(portNr == 1){
            message[0] = (byte) 0x03;
        }
        this.write(message, 0, message.length);
    }

    /**
     * <b>NOTE: not supported</b>
     * 
     * Sets the specified I/O to the given state.
     * This methode only takes effect, when the corresponding
     * I/O is set to OUTPUT mode.
     */
    public void setIO(int ioNr, boolean state){
    }
    
    /**
     * Reads the current state of the I/O pins of the specified port.
     */
    public byte readPort(int portNr) throws IOException{
        byte[] result = {(byte) 0x00};
        if((portNr<0) || (portNr>1)){
            throw new IOException();
        }
        byte[] register = {(byte) 0x00};
        if(portNr == 1){
            register[0] = (byte) 0x01;
        }
        read(register, result, 0, 1);
        return result[0];
    }
    
    /**
     * <b>NOTE: not supported</b>
     * 
     * @return always false 
     */
    public boolean getIO(int ioNr){
        boolean result = LOW;
        return result;
    }
    
    /**
     * for test purposes, do not use
     */
    public static void main( String[] args) {
        try {
            PCA9555 pca9555 = new PCA9555(0x40);
            for ( int i = 0; i < 16; i++) {
                pca9555.setMode( i, PCA9555.INPUT);
            }
            for (;;) {
                Console.out.print( "state: ");
                for ( int i = 0; i < 16; i++) {
                    Console.out.print( pca9555.getState(i)?"-":"o");
                }
                Console.out.println();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}
