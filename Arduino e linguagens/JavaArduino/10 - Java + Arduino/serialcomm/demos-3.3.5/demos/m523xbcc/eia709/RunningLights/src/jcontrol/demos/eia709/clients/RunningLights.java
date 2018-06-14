package jcontrol.demos.eia709.clients;
/*
 * RunningLights.java
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
import jcontrol.comm.eia709.Eia709Address;
import jcontrol.comm.eia709.Eia709Constants;
import jcontrol.comm.eia709.Eia709Controllet;
import jcontrol.comm.eia709.Eia709NetworkVariable;
import jcontrol.comm.eia709.Eia709Node;
import jcontrol.demos.eia709.common.Eia709NetworkVariableSwitch;
import jcontrol.io.Console;
import jcontrol.lang.ThreadExt;

/**
 * <p>The <code>RunningLights</code> demo remotely programs the lights of a M523XBCCKIT's CEA 709
 * Daughter Card. The remote node must be running the 
 * <code>BCCServer</code> application.</p>
 * <p>The communication is established through 8 network variables of type SNVT_Switch
 * representing one LED each.</p>
 * <p><code>RunningLights</code> and <code>BCCServer</code> run together out of the box. If
 * any configuration changes are made to either of them, the other application must be
 * updated as well. This includes selector or address modifications.</p>
 * 
 * @author Lorenz Witte
 */
public class RunningLights implements Runnable, Eia709Controllet {
    
    public static final int NUMBER_OF_LEDS            = 8;

    /**
     * Program name "RUNLIGHT".
     */
    private static final byte[] PROGRAM_NAME = { 'R', 'U', 'N', 'L', 'I', 'G', 'H', 'T'};

    ///////////////////////////////////////////////////////////////////////////////
    // NV selectors                                                              //
    ///////////////////////////////////////////////////////////////////////////////
    /**
     * LED selector base. Actual selector of LED <i>i</i> is
     * <code>LED_SELECTOR_BASE + i</code>.
     */
    public static final int LED_SELECTOR_BASE         = 10;

    ///////////////////////////////////////////////////////////////////////////////
    // node specific settings                                                    //
    ///////////////////////////////////////////////////////////////////////////////

    /**
     * Unique node ID (neuron ID) of 6 bytes.
     */
    private static final byte[] UNIQUE_NODE_ID        = { 'D', 'O', 'M', 'O', '0', '2'};
    /**
     * Domain name of 0,1,3 or 6 bytes.
     */
    private static final byte[] DOMAIN_NAME           = { 'J', 'C', 'N', 'T', 'R', 'L'};
    /**
     * This node's "subnet" address (i.e. srcSubnet).
     * 
     * @see jcontrol.comm.eia709.Eia709Constants#MIN_SUBNET_ADDRESS
     * @see jcontrol.comm.eia709.Eia709Constants#MAX_SUBNET_ADDRESS
     */
    private static final int SRC_SUBNET               = 2;
    /**
     * This node's "node" address (i.e. srcNode).
     * 
     * @see jcontrol.comm.eia709.Eia709Constants#MIN_NODE_ADDRESS
     * @see jcontrol.comm.eia709.Eia709Constants#MAX_NODE_ADDRESS
     */
    private static final int SRC_NODE                 = 1;

    ///////////////////////////////////////////////////////////////////////////////
    // nv peer address settings                                                  //
    ///////////////////////////////////////////////////////////////////////////////

    /**
     * NV address type (<code>ADDRESS_TYPE_0</code>, <code>ADDRESS_TYPE_1</code>,
     * or <code>ADDRESS_TYPE_2A</code>.
     * 
     * @see jcontrol.comm.eia709.Eia709Constants#ADDRESS_TYPE_0
     * @see jcontrol.comm.eia709.Eia709Constants#ADDRESS_TYPE_1
     * @see jcontrol.comm.eia709.Eia709Constants#ADDRESS_TYPE_2A
     */
    private static final int DST_ADDRESS_TYPE         = Eia709Constants.ADDRESS_TYPE_2A;
    /**
     * NV dstSubnet.
     * 
     * @see jcontrol.comm.eia709.Eia709Constants#MIN_SUBNET_ADDRESS
     * @see jcontrol.comm.eia709.Eia709Constants#MAX_SUBNET_ADDRESS
     */
    private static final int DST_SUBNET               = 1;
    /**
     * NV dstNode.
     * 
     * @see jcontrol.comm.eia709.Eia709Constants#MIN_NODE_ADDRESS
     * @see jcontrol.comm.eia709.Eia709Constants#MAX_NODE_ADDRESS
     */
    private static final int DST_NODE                 = 10;
    /**
     * NV dstGroup.
     * 
     * @see jcontrol.comm.eia709.Eia709Constants#MIN_GROUP_ID
     * @see jcontrol.comm.eia709.Eia709Constants#MAX_GROUP_ID
     */
    private static final int DST_GROUP                = 0;

    /**
     * The node the application runs on.
     */
    protected Eia709Node m_node;

    /**
     * Incoming network variables containing lamp states.
     */
    protected Eia709NetworkVariableSwitch m_nvoLed[] = new Eia709NetworkVariableSwitch[NUMBER_OF_LEDS];

    /**
     * Internal flag indicating the state of the thread.
     */
    private boolean m_running = false;
    
    /**
     * Internal thread polling pushbuttons, temperature and ambient light.
     */
    private Thread m_thread;

    /**
     * Contains the control logic.
     */
    public void run() {
        while (m_running) {
            try {
                // turn LEDs on
                for ( int i = 0; i < 8; i++) {
                    try {
                        Console.out.println( "turning on lamp ".concat( Integer.toString(i)));
                        m_nvoLed[i].setState( Eia709NetworkVariableSwitch.STATE_ON, 100.0f);
                    } catch (IOException e) {
                    }
                    ThreadExt.sleep( 150);
                }
                // turn LEDs off
                for ( int i = 0; i < 8; i++) {
                    try {
                        Console.out.println( "turning off lamp ".concat( Integer.toString(i)));
                        m_nvoLed[i].setState( Eia709NetworkVariableSwitch.STATE_OFF);
                    } catch (IOException e) {
                    }
                    ThreadExt.sleep( 150);
                }
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * Supplies static binding information: NV selectors and peer addresses.
     */
    public void bind() {
        // create dest address for all variables
        Eia709Address address;
        switch (DST_ADDRESS_TYPE) {
        case Eia709Constants.ADDRESS_TYPE_0:
            address = Eia709Address.createAddressType0( m_node, DST_SUBNET);
            break;
        case Eia709Constants.ADDRESS_TYPE_1:
            address = Eia709Address.createAddressType1( m_node, DST_GROUP);
            break;
        case Eia709Constants.ADDRESS_TYPE_2A:
            address = Eia709Address.createAddressType2a( m_node, DST_SUBNET, DST_NODE);
            break;
        default:
            throw new IllegalArgumentException( "illegal address type");
        }
        
        // supply selectors & addresses
        for ( int i = 0; i < NUMBER_OF_LEDS; i++) {
            m_nvoLed[i].setSelector( LED_SELECTOR_BASE+i);
            m_nvoLed[i].setAddress(address);
        }

    }

    /**
     * Creates the required network variables.
     */
    public void configure(Eia709Node node) {
        m_node = node;
        for ( int i = 0; i < NUMBER_OF_LEDS; i++) {
            // create lamp network variable
            m_nvoLed[i] = new Eia709NetworkVariableSwitch( node, 
                    "nvo_LED[".concat( Integer.toString( i)).concat("]"), 
                    Eia709NetworkVariable.MOD_DIR_OUTGOING |
                    Eia709NetworkVariable.MOD_SERVICE_ACKD |
                    Eia709NetworkVariable.MOD_SYNC);
        }
    }

    /**
     * Returns the program name of 8 bytes.
     * 
     * @return program name ("RUNLIGHT")
     */
    public byte[] getProgramName() {
        return PROGRAM_NAME;
    }

    /**
     * Starts the controllet.<br/>
     * <b>Note:</code> This is called internally by the node, don't call this method
     * directly.
     */
    public void start() {
        m_thread = new Thread( this);
        m_running = true;
        m_thread.setName( "RunningLights");
        m_thread.start();
    }

    /**
     * Stops the controllet.<br/>
     * <b>Note:</code> This is called internally by the node, don't call this method
     * directly.
     */
    public void stop() {
        m_running = false;
        m_thread.interrupt();
    }

    public static void main(String[] args) {
        Console.out.println( "creating node");
        Eia709Node node = new Eia709Node( UNIQUE_NODE_ID);

        // supply node address
        Console.out.println( "configure node address");
        node.setNodeAddress( DOMAIN_NAME, SRC_SUBNET, SRC_NODE);
        
        Console.out.println( "creating controllet");
        node.setControllet( new RunningLights());

        Console.out.println( "starting node");
        node.start();
    }

}
