package jcontrol.demos.eia709.clients;
/*
 * TempSensorDemo.java
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
import jcontrol.comm.nv.NetworkVariableEvent;
import jcontrol.comm.nv.NetworkVariableListener;
import jcontrol.demos.eia709.common.Eia709NetworkVariableFreqF;
import jcontrol.demos.eia709.common.Eia709NetworkVariableTemp;
import jcontrol.io.Console;
import jcontrol.lang.ThreadExt;

/**
 * <p>The <code>TempSensorDemo</code> remotely reads the temperature from a M523XBCCKIT's CEA 709
 * Daughter Card. The remote node must be running the <code>BCCServer</code> application.</p>
 * <p>The communication is established through 1 network variables of type SNVT_Temp.</p>
 * <p><code>TempSensorDemo</code> and <code>BCCServer</code> run together out of the box. If
 * any configuration changes are made to either of them, the other application must be
 * updated as well. This includes selector or address modifications.</p>
 * 
 * @author Lorenz Witte, Thomas Roebbenack
 */
public class TempSensorDemo implements Runnable, Eia709Controllet, NetworkVariableListener {
    
    /**
     * Program name "TEMPDEMO".
     */
    private static final byte[] PROGRAM_NAME = { 'T', 'E', 'M', 'P', 'D', 'E', 'M', 'O'};

    ///////////////////////////////////////////////////////////////////////////////
    // NV selectors                                                              //
    ///////////////////////////////////////////////////////////////////////////////

    /**
     * Selector of NV "nvi_Buzzer".
     */
    public static final int BUZZER_SELECTOR           = 30;
    
    /**
     * Selector of NV "nvo_Temperature".
     */
    public static final int TEMPERATURE_SELECTOR      = 31;
    
    
    ///////////////////////////////////////////////////////////////////////////////
    // node specific settings                                                    //
    ///////////////////////////////////////////////////////////////////////////////

    /**
     * Unique node ID (neuron ID) of 6 bytes.
     */
    private static final byte[] UNIQUE_NODE_ID        = { 'D', 'O', 'M', 'O', '0', '3'};
    
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
    private static final int SRC_NODE                 = 2;

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
     * Incoming network variables containing temperature state.
     */
    protected Eia709NetworkVariableTemp m_nviTemp;

    /**
     * Outgoing network variables containing temperature state.
     */
    protected Eia709NetworkVariableFreqF m_nvoBuzzer;
    
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
        m_running = true;
        while (m_running) {
            
            try { ThreadExt.sleep(1000); } catch (InterruptedException e) {}
            
            try {
                m_nviTemp.poll();
            } catch (IOException e) {
                Console.out.println("IO-ERROR");
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
        m_nviTemp.setSelector( TEMPERATURE_SELECTOR );
        m_nviTemp.setAddress(address);
        m_nvoBuzzer.setSelector( BUZZER_SELECTOR );
        m_nvoBuzzer.setAddress(address);
    }

    /**
     * Creates the required network variables.
     */
    public void configure(Eia709Node node) {
        m_node = node;
        // create temperature network variable
        m_nviTemp = new Eia709NetworkVariableTemp( node, 
                    "nvi_Temp", 
                    Eia709NetworkVariable.MOD_DIR_INCOMING );
        // set temperature listener to get events
        m_nviTemp.setListener(this);
        // create buzzer network variable
        m_nvoBuzzer = new Eia709NetworkVariableFreqF( node, 
                "nvo_Buzzer", 
                Eia709NetworkVariable.MOD_DIR_OUTGOING |
                Eia709NetworkVariable.MOD_SERVICE_UNACKD |
                Eia709NetworkVariable.MOD_SYNC);
        
    }

    /**
     * Returns the program name of 8 bytes.
     * 
     * @return program name ("SENSDEMO")
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
        m_thread.setName( "SensorDemo");
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
        node.setControllet( new TempSensorDemo() );

        Console.out.println( "starting node");
        node.start();
    }

    public void networkVariableChanged(NetworkVariableEvent event) {
        // check, if a value has actually changed
        if ( (event.getType() & NetworkVariableEvent.VALUE_CHANGED) != 0) {
            // an actual value has been modified
            Eia709NetworkVariable nv = (Eia709NetworkVariable)event.getNetworkVariable();
            // debug
            Console.out.println( "network variable \"".concat(nv.getIdString()).concat( "\" changed."));
            // check for temperature-network-variable
            if ( nv == m_nviTemp) {
                // get changed temperature
                float temp = m_nviTemp.getTemperature();
                // dump value onto console
                Console.out.println("temperature = "+temp+" �C");
                // beep if temperature is bigger than 30 degree celcius
                if ( temp > 30.0F ) {
                    try {
                        for (int i=0; i<3; i++) {
                            m_nvoBuzzer.setFreqency(5000F);
                            try { ThreadExt.sleep(200);    } catch (InterruptedException e) {}
                            m_nvoBuzzer.setFreqency(00F);
                            try { ThreadExt.sleep(200);    } catch (InterruptedException e) {}
                        }                        
                    } catch (IOException e) {}
                }
            }
        }        
    }

}
