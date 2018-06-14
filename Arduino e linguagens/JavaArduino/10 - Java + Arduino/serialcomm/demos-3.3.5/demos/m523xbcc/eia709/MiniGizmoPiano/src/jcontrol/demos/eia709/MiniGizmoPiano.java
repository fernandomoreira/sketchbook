package jcontrol.demos.eia709;
/*
 * MiniGizmoPiano.java
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

import jcontrol.bus.i2c.PCA9555;
import jcontrol.comm.eia709.Eia709Address;
import jcontrol.comm.eia709.Eia709Controllet;
import jcontrol.comm.eia709.Eia709NetworkVariable;
import jcontrol.comm.eia709.Eia709Node;
import jcontrol.demos.eia709.common.Eia709NetworkVariableFreqHz;
import jcontrol.demos.eia709.common.Eia709NetworkVariableSwitch;
import jcontrol.io.Console;

/**
 * The MiniGizmoPiano is meant to be used in conjunction with the MGDemo running
 * on a Echelon MiniGizmo connected to the M5235BCC device via EIA-709. With this
 * demo it is possible to control the MiniGizmo's buzzer by pressing the buttons
 * on the CEA 709 Daughter Card. The tones played are meant to resemble the C major
 * scale, but unfortunately the MiniGizmo's buzzer doesn't live up to it.
 * 
 * @author Lorenz Witte
 */
public class MiniGizmoPiano implements Eia709Controllet, Runnable {

    /**
     * Number of switches connected to the PCA9555.
     */
    public static final int NUMBER_OF_SWITCHES        = 8;

    /**
     * I2C address of the PCA9555 module.
     */
    public static final int PCA9555_I2C_ADDRESS       = 0x40;

    /**
     * Unique node ID (neuron ID) of 6 bytes.
     */
    private static final byte[] UNIQUE_NODE_ID        = { 'D', 'O', 'M', 'O', '0', '2'};

    /**
     * Domain name of 0,1,3 or 6 bytes. The MiniGizmo uses the domain "ISI".
     */
    private static final byte[] DOMAIN_NAME           = { 'I', 'S', 'I'};

    /**
     * This node's "subnet" address (i.e. srcSubnet).
     * 
     * @see jcontrol.comm.eia709.Eia709Constants#MIN_SUBNET_ADDRESS
     * @see jcontrol.comm.eia709.Eia709Constants#MAX_SUBNET_ADDRESS
     */
    private static final int SRC_SUBNET               = 117;

    /**
     * This node's "node" address (i.e. srcNode).
     * 
     * @see jcontrol.comm.eia709.Eia709Constants#MIN_NODE_ADDRESS
     * @see jcontrol.comm.eia709.Eia709Constants#MAX_NODE_ADDRESS
     */
    private static final int SRC_NODE                 = 119;

    /**
     * The name of this program.
     */
    public static final byte[] PROGRAM_NAME = { 'M', 'G', '_', 'P', 'I', 'A', 'N', 'O'};

    /**
     * Selector of the buzzer switch NV.
     */
    public static final int NVO_BUZZER_SWITCH_SELECTOR = 16360;

    /**
     * Selector of the buzzer frequency NV.
     */
    public static final int NVO_BUZZER_FREQUENCY_SELECTOR = 16361;
    
    /**
     * The network variable for turning the buzzer on and off.
     */
    private Eia709NetworkVariableSwitch nvoBuzzerSwitch;

    /**
     * The network variable for changing the buzzer frequency.
     */
    private Eia709NetworkVariableFreqHz nvoBuzzerFrequency;

    /**
     * The EIA-709 node.
     */
    private Eia709Node m_node;

    /**
     * Flag that indicates wether the controllet is running or not.
     */
    private boolean m_running = false;
    
    /**
     * Supplies the necessary binding information. Here we will use domain broadcast
     * to communicate with the Minigizmo.
     */
    public void bind() {
        // domain broadcast
        Eia709Address gizmoAddress = Eia709Address.createAddressType1( m_node, 128);
        
        nvoBuzzerSwitch.setAddress( gizmoAddress);
        nvoBuzzerSwitch.setSelector( NVO_BUZZER_SWITCH_SELECTOR);
        nvoBuzzerFrequency.setAddress( gizmoAddress);
        nvoBuzzerFrequency.setSelector( NVO_BUZZER_FREQUENCY_SELECTOR);
    }

    /**
     * Creates the required network variables.
     */
    public void configure(Eia709Node node) {

        m_node = node;
        
        // the switch variable
        nvoBuzzerSwitch = new Eia709NetworkVariableSwitch(node, "nvoBuzzerSwitch",
                Eia709NetworkVariable.MOD_DIR_OUTGOING | Eia709NetworkVariable.MOD_SERVICE_UNACKD);

        // the switch variable
        nvoBuzzerFrequency = new Eia709NetworkVariableFreqHz(node, "nvoBuzzerFrequency",
                Eia709NetworkVariable.MOD_DIR_OUTGOING | Eia709NetworkVariable.MOD_SERVICE_UNACKD);
    }

    /**
     * Returns the EIA-709 program name.
     * @return program name
     */
    public byte[] getProgramName() {
        return PROGRAM_NAME;
    }

    /**
     * Runs the controllet. This method is called by the node, do not call directly!
     */
    public void start() {
        new Thread( this).start();
    }

    /**
     * Stops the controllet. This method is called by the node, do not call directly!
     *
     */
    public void stop() {
        m_running = false;
    }

    /**
     * Implements the controllet's mainloop which continuously polls the CEA 709 Daughter 
     * Cards buttons. On button press or release, the MiniGizmo's buzzer is turned on/off
     * accordingly.
     * The mainloop is run in its own thread.
     */
    public void run() {
        
        // sets flag to indicate the thread is running
        m_running = true;

        // configure the PCA9555's ports connected to the buttons
        // as input ports
        PCA9555 pca9555 = null;
        try {
            pca9555 = new PCA9555( PCA9555_I2C_ADDRESS);
            for ( int i = 0; i < 8; i++) {
                pca9555.setMode( i*2, PCA9555.INPUT);
            }
            
        } catch (IOException e) {
            Console.out.println( "failed to setup GPIOs");
            return;
        }

        // stores the current state of the switches
        boolean switchStates[] = new boolean[NUMBER_OF_SWITCHES];
        
        // the frequencies associated with the buttons
        final int frequencies[] = new int[] { 2640, 2970, 3300, 3520, 3960, 4400, 4950, 5280};
        
        while (m_running) {
            try {
                for ( int i = 0; i < NUMBER_OF_SWITCHES; i++) {
                    // poll switch
                    boolean state = !pca9555.getState(i*2);
                    if ( !switchStates[i] & state) {
                        // switch was pressed
                        Console.out.println( "switch ".concat(Integer.toString(i)).concat(" pressed"));
                        switchStates[i] = true;

                        // adjust frequency
                        nvoBuzzerFrequency.setFrequency( frequencies[i]);
                        // enable buzzer
                        nvoBuzzerSwitch.setState( Eia709NetworkVariableSwitch.STATE_ON, 100.0f);
                    } else if ( switchStates[i] & !state) {
                        // switch was released
                        Console.out.println( "switch ".concat(Integer.toString(i)).concat(" released"));
                        switchStates[i] = false;

                        // disable buzzer
                        nvoBuzzerSwitch.setState( Eia709NetworkVariableSwitch.STATE_OFF, 100.0f);
                    }
                }
            } catch (IOException e) {
                Console.out.println( "uh, IOException!");
            }
        }
    }
    
    /**
     * Starts the MiniGizmoPiano demo.
     */
    public static void main(String[] args) {
    
        // create node
        Eia709Node node = new Eia709Node( UNIQUE_NODE_ID);

        // supply node address
        node.setNodeAddress( DOMAIN_NAME, SRC_SUBNET, SRC_NODE);
        
        // create and install controllet
        node.setControllet( new MiniGizmoPiano());

        Console.out.println( "Welcome to the MiniGizmoPiano");
        Console.out.println( "Press the buttons to play a tune");

        // start node
        node.start();
    }
}
