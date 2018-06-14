/*
 * BCCServer.java
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
package jcontrol.demos.eia709.bccserver;

import java.io.IOException;

import jcontrol.bus.i2c.*;
import jcontrol.comm.eia709.Eia709Address;
import jcontrol.comm.eia709.Eia709Constants;
import jcontrol.comm.eia709.Eia709Controllet;
import jcontrol.comm.eia709.Eia709NetworkVariable;
import jcontrol.comm.eia709.Eia709Node;
import jcontrol.comm.nv.NetworkVariableEvent;
import jcontrol.comm.nv.NetworkVariableListener;
import jcontrol.demos.eia709.common.Eia709NetworkVariableFreqF;
import jcontrol.demos.eia709.common.Eia709NetworkVariableSwitch;
import jcontrol.demos.eia709.common.Eia709NetworkVariableTemp;
import jcontrol.io.Buzzer;
import jcontrol.io.Console;
import jcontrol.io.DataOutputStream;
import jcontrol.lang.ThreadExt;

/**
 * <p>The BCCServer exposes the functions of the M523XBCC KITs EIA-709 extension board
 * to an EIA-709 network. Supported components are:</p>
 * <ul>
 * <li>the TMP175 temperature sensor,</li>
 * <li>the VM6101 ambient light sensor,</li>
 * <li>the PCA9555 GPIO module providing 8 pushbuttons and 8 LEDs,</li>
 * <li>and an integrated buzzer.</li>
 * </ul>
 * <p>Each component is accessible via a network variable, some can be
 * polled by external sources, some are automatically propagated. The
 * following table contains all NVs:</p>
 * <table>
 * <th><td>selector</td><td>data type</td><td>direction</td><td>propagation</td><td>function</td></th>
 * <td><td>nvi_LED[i]</td><td>10+i</td><td>SNVT_switch</td><td>in</td><td>---</td><td>sets state of LED i (0 <= i <= 7)</td></tr>
 * <td><td>nvo_Switch[i]</td><td>20+i</td><td>SNVT_switch</td><td>out</td><td>sync</td><td>supplies state of switch i (0 <= i <= 7)</td></tr>
 * <tr><td>nvi_Buzzer</td><td>30</td><td>SNVT_freq_f</td><td>in</td><td>---</td><td>sets the buzzer to the given frequency (0 turns off the buzzer)</td></th>
 * <tr><td>nvo_Temperature</td><td>31</td><td>SNVT_temp</td><td>out</td><td>polled</td><td>provides the temperature from the TMP175 sensor</td></tr>
 * <td><td>nvo_LightSensor</td><td>32</td><td>special*</td><td>out</td><td>polled</td><td>provides light sensity from the VM6101 sensor</td></tr>
 * </table><br/>
 * * The light sensor data is transmitted as 4 16-bit values containing the intensity of the different
 * light channels Y,R,G and B.
 * 
 * <p>In the default configuration, the BCCServer runs in the domain <code>JCNTRL</code> and uses
 * node address <code>10</code> and subnet <code>1</code>. The outgoing network variables
 * will provide their values via broadcast in subnet <code>2</code>.</p>
 * <p>The node address configuration can be adjusted via constants
 * <code>DOMAIN_NAME</code>, <code>SRC_SUBNET</code>, and <code>SRC_NODE</code>. The peer
 * address of the network variables can be modified using constants
 * <code>DST_ADDRESS_TYPE</code>, <code>DST_SUBNET</code>, <code>DST_NODE</code>, and
 * <code>DST_GROUP</code>.</p>
 * 
 * @see jcontrol.demos.eia709.common.Eia709NetworkVariableSwitch
 * @see jcontrol.demos.eia709.common.Eia709NetworkVariableTemp
 * @see jcontrol.demos.eia709.common.Eia709NetworkVariableFreqF
 * 
 * @author Lorenz Witte
 */
public class BCCServer implements Eia709Controllet, NetworkVariableListener, Runnable {

    /**
     * Program name: "BCCSERVE".
     */
    private static final byte[] PROGRAM_NAME = { 'B', 'C', 'C', 'S', 'E', 'R', 'V', 'E'};

    /**
     * If enabled, pressing the pushbuttons will update the incoming network variables
     * for the LEDs and the buzzer locally.
     */
    public static final boolean SIMULATION_MODE       = true;
    
    // number of LEDs and switches
    public static final int NUMBER_OF_LEDS            = 8;
    public static final int NUMBER_OF_SWITCHES        = 8;

    ///////////////////////////////////////////////////////////////////////////////
    // I2C addresses                                                             //
    ///////////////////////////////////////////////////////////////////////////////

    /**
     * I2C address of the TMP175 module.
     */
    public static final int TMP175_I2C_ADDRESS         = 0x92;
    /**
     * I2C address of the VM6101 module.
     */
    public static final int VM6101_I2C_ADDRESS        = 0x20;
    /**
     * I2C address of the PCA9555 module.
     */
    public static final int PCA9555_I2C_ADDRESS       = 0x40;

    ///////////////////////////////////////////////////////////////////////////////
    // NV selectors                                                              //
    ///////////////////////////////////////////////////////////////////////////////
    /**
     * LED selector base. Actual selector of LED <i>i</i> is
     * <code>LED_SELECTOR_BASE + i</code>.
     */
    public static final int LED_SELECTOR_BASE         = 10;
    /**
     * Switch selector base. Actual selector of switch <i>i</i> is
     * <code>SWITCH_SELECTOR_BASE + i</code>.
     */
    public static final int SWITCH_SELECTOR_BASE      = 20;
    /**
     * Selector of NV "nvi_Buzzer".
     */
    public static final int BUZZER_SELECTOR           = 30;
    /**
     * Selector of NV "nvo_Temperature".
     */
    public static final int TEMPERATURE_SELECTOR      = 31;
    /**
     * Selector of NV "nvi_Light".
     */
    public static final int LIGHT_SENSOR_SELECTOR     = 32;

    ///////////////////////////////////////////////////////////////////////////////
    // node specific settings                                                    //
    ///////////////////////////////////////////////////////////////////////////////

    /**
     * Unique node ID (neuron ID) of 6 bytes.
     */
    private static final byte[] UNIQUE_NODE_ID        = { 'D', 'O', 'M', 'O', '0', '1'};
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
    private static final int SRC_SUBNET               = 1;
    /**
     * This node's "node" address (i.e. srcNode).
     * 
     * @see jcontrol.comm.eia709.Eia709Constants#MIN_NODE_ADDRESS
     * @see jcontrol.comm.eia709.Eia709Constants#MAX_NODE_ADDRESS
     */
    private static final int SRC_NODE                 = 10;

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
    private static final int DST_ADDRESS_TYPE         = Eia709Constants.ADDRESS_TYPE_0;  // broadcast
    /**
     * NV dstSubnet.
     * 
     * @see jcontrol.comm.eia709.Eia709Constants#MIN_SUBNET_ADDRESS
     * @see jcontrol.comm.eia709.Eia709Constants#MAX_SUBNET_ADDRESS
     */
    private static final int DST_SUBNET               = 2;
    /**
     * NV dstNode.
     * 
     * @see jcontrol.comm.eia709.Eia709Constants#MIN_NODE_ADDRESS
     * @see jcontrol.comm.eia709.Eia709Constants#MAX_NODE_ADDRESS
     */
    private static final int DST_NODE                 = 0;
    /**
     * NV dstGroup.
     * 
     * @see jcontrol.comm.eia709.Eia709Constants#MIN_GROUP_ID
     * @see jcontrol.comm.eia709.Eia709Constants#MAX_GROUP_ID
     */
    private static final int DST_GROUP                = 0;
    
    /**
     * The node the server runs on.
     */
    protected Eia709Node m_node;

    /**
     * Outgoing network variables containing switch states.
     */
    protected Eia709NetworkVariableSwitch m_nvoSwitches[] = new Eia709NetworkVariableSwitch[NUMBER_OF_SWITCHES];

    /**
     * Incoming network variables containing lamp states.
     */
    protected Eia709NetworkVariableSwitch m_nviLed[] = new Eia709NetworkVariableSwitch[NUMBER_OF_LEDS];

    /**
     * Incoming network variable containing the Buzzer frequency.
     */
    protected Eia709NetworkVariableFreqF m_nviBuzzer;

    /**
     * Outgoing network variable containing the TMP175's temperature.
     */
    protected Eia709NetworkVariableTemp m_nvoTemperature;
    
    /**
     * Outgoing network variable containing the VM6106's light information.
     */
    protected Eia709NetworkVariable m_nvoLightSensor;

    /**
     * Array containing the current LED states.
     */
    private boolean m_ledStates[] = new boolean[NUMBER_OF_LEDS];

    /**
     * Array containing the current switch states.
     */
    private boolean m_switchStates[] = new boolean[NUMBER_OF_SWITCHES];

    /**
     * Internal flag indicating the state of the thread.
     */
    private boolean m_running = false;
    
    /**
     * Internal thread polling pushbuttons, temperature and ambient light.
     */
    private Thread m_thread;

    /**
     * PCA9555 instance.
     */
    private PCA9555 m_pca9555 = null;

    /**
     * TMP75 instance.
     */
    private TMP75 m_tmp75 = null;
    
    /**
     * VM6101 instance.
     */
    private VM6101 m_vm6101 = null;
    
    /**
     * Buzzer instance.
     */
    private Buzzer m_buzzer = null;

    /**
     * Constructor.
     */
    public BCCServer() {
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
            m_nviLed[i].setSelector( LED_SELECTOR_BASE+i);
        }
        for ( int i = 0; i < NUMBER_OF_SWITCHES; i++) {
            m_nvoSwitches[i].setSelector( SWITCH_SELECTOR_BASE+i);
            m_nvoSwitches[i].setAddress( address);
        }
        
        m_nviBuzzer.setSelector( BUZZER_SELECTOR);
        m_nvoTemperature.setSelector( TEMPERATURE_SELECTOR);
        m_nvoTemperature.setAddress( address);
        m_nvoLightSensor.setSelector( LIGHT_SENSOR_SELECTOR);
        m_nvoLightSensor.setAddress( address);
    }

    /**
     * Creates the required network variables.
     */
    public void configure(Eia709Node node) {
        m_node = node;
        for ( int i = 0; i < NUMBER_OF_LEDS; i++) {
            // create lamp network variable...
            m_nviLed[i] = new Eia709NetworkVariableSwitch( node, 
                    "nvi_LED[".concat( Integer.toString( i)).concat("]"), 
                    Eia709NetworkVariable.MOD_DIR_INCOMING);
            // ... and attach listener.
            m_nviLed[i].setListener( this);
        }
        for ( int i = 0; i < NUMBER_OF_SWITCHES; i++) {
            // create switch network variable
            m_nvoSwitches[i] = new Eia709NetworkVariableSwitch( node,
                    "nvo_Switch[".concat( Integer.toString( i)).concat("]"), 
                    Eia709NetworkVariable.MOD_DIR_OUTGOING | 
                    Eia709NetworkVariable.MOD_SYNC |
                    Eia709NetworkVariable.MOD_SERVICE_UNACKD);
        }

        // create buzzer network variable...
        m_nviBuzzer = new Eia709NetworkVariableFreqF( node, "nvi_Buzzer", 
                Eia709NetworkVariable.MOD_DIR_INCOMING);
        // ... and attach listener.
        m_nviBuzzer.setListener( this);
        
        // create temperature network variable
        m_nvoTemperature = new Eia709NetworkVariableTemp( node, "nvo_Temperature",
                Eia709NetworkVariable.MOD_DIR_OUTGOING | 
                Eia709NetworkVariable.MOD_POLLED);
        
        // create light sensor network variable
        m_nvoLightSensor = new Eia709NetworkVariable( node, "nvo_Light",
                0,
                8,
                Eia709NetworkVariable.MOD_DIR_OUTGOING | 
                Eia709NetworkVariable.MOD_SYNC |
                Eia709NetworkVariable.MOD_SERVICE_UNACKD);
    }

    /**
     * Returns the program name of 8 bytes.
     * 
     * @return program name ("BCCSERVE")
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
        
        m_buzzer = new Buzzer();
        
        try {
            m_pca9555 = new PCA9555( PCA9555_I2C_ADDRESS);
            for ( int i = 0; i < 8; i++) {
                m_pca9555.setMode( i*2, PCA9555.INPUT);
                m_pca9555.setMode( i*2+1, PCA9555.OUTPUT);
            }
        } catch (IOException e) {
            Console.out.println( "failed to setup GPIOs");
            m_pca9555 = null;
        }
        
        m_tmp75 = new TMP75( TMP175_I2C_ADDRESS);
        
        try {
            m_vm6101 = new VM6101( VM6101_I2C_ADDRESS);
        } catch (IOException e) {
            m_vm6101 = null;
        }

        m_thread = new Thread( this);
        m_running = true;
        m_thread.setName( "BCCServer");
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

    /**
     * Handles network variable events. On demand, the buzzer frequency will be set,
     * or LEDs will be turned of/off
     */
    public void networkVariableChanged(NetworkVariableEvent event) {
        // check, if a value has actually changed
        if ( (event.getType() & NetworkVariableEvent.VALUE_CHANGED) != 0) {
            // an actual value has been modified
            Eia709NetworkVariable nv = (Eia709NetworkVariable)event.getNetworkVariable();
//            Console.out.println( "network variable \"".concat(nv.getIdString()).concat( "\" changed."));
            if ( nv == m_nviBuzzer) {
                // buzzer frequency changed
                int frequency = (int)((Eia709NetworkVariableFreqF)nv).getFrequency();

                Console.out.println( "buzzer frequency: ".concat( Integer.toString(frequency)));
                if ( frequency > 0) {
                    m_buzzer.on(frequency);
                } else {
                    m_buzzer.off();
                }
            } else {
                // check if a LED status changed
                for ( int i = 0; i < NUMBER_OF_LEDS; i++) {
                    if ( nv == m_nviLed[i]) {
                        // lamp toggled
                        if ( ((Eia709NetworkVariableSwitch)nv).getState() == Eia709NetworkVariableSwitch.STATE_ON) {
                            m_ledStates[i] = true;
                            if ( m_pca9555 != null) {
                                try {
                                    m_pca9555.setState( i*2+1, false);
                                } catch (IOException e) {
                                }
                            }
                        } else {
                            m_ledStates[i] = false;
                            if ( m_pca9555 != null) {
                                try {
                                    m_pca9555.setState( i*2+1, true);
                                } catch (IOException e) {
                                }
                            }
                        }

//                        Console.out.print( "led state: ");
//                        for ( int j = 0; j < NUMBER_OF_LEDS; j++) {
//                            Console.out.print( m_ledStates[j]?"o":"-");
//                        }
//                        Console.out.println();
                    }
                }
            }
        }
    }

    /**
     * Background thread which continuously polls pushbuttons, temperature, and ambient
     * light to update the corresponding network variables.
     */
    public void run() {
        while (m_running) {
            try {
                // poll temperature sensor
                if ( m_tmp75 != null) {
                    try {
                        int temp = m_tmp75.getTemp();
                        Console.out.println( "temperature: ".concat(Integer.toString(temp/10)).
                                    concat(".").concat(Integer.toString(temp%10)).concat( "\u00b0C"));
                        m_nvoTemperature.setTemperature( temp);
                    } catch (IOException e) {
                    }
                }
                
                // poll light sensor
                if ( m_vm6101 != null ) {
                try {
                    int red         = m_vm6101.getIlluminance(VM6101.CHANNEL_R);
                    int green         = m_vm6101.getIlluminance(VM6101.CHANNEL_G);
                    int blue         = m_vm6101.getIlluminance(VM6101.CHANNEL_B);
                    int luminance     = m_vm6101.getIlluminance(VM6101.CHANNEL_Y);
                    DataOutputStream out = m_nvoLightSensor.getDataOutputStream();
                    out.writeShort((short)red);
                    out.writeShort((short)green);
                    out.writeShort((short)blue);
                    out.writeShort((short)luminance);
                    m_nvoLightSensor.propagate();
                } catch (IOException e) {} 
                }
                
                // poll switches
                if ( m_pca9555 != null) {
                    try {
                        for ( int i = 0; i < NUMBER_OF_SWITCHES; i++) {
                            boolean state = !m_pca9555.getState(i*2);
                            if ( !m_switchStates[i] & state) {
                                Console.out.println( "switch ".concat(Integer.toString(i)).concat(" pressed"));
                                m_switchStates[i] = true;
                                m_nvoSwitches[i].setState( Eia709NetworkVariableSwitch.STATE_ON);
                                if ( SIMULATION_MODE) {
                                    m_nviLed[i].setState( Eia709NetworkVariableSwitch.STATE_ON);
                                    m_nviBuzzer.setFreqency( 440f);
                                }
                            } else if ( m_switchStates[i] & !state) {
                                Console.out.println( "switch ".concat(Integer.toString(i)).concat(" released"));
                                m_switchStates[i] = false;
                                m_nvoSwitches[i].setState( Eia709NetworkVariableSwitch.STATE_OFF);
                                if ( SIMULATION_MODE) {
                                    m_nviLed[i].setState( Eia709NetworkVariableSwitch.STATE_OFF);
                                    m_nviBuzzer.setFreqency( 0f);
                                }
                            }
                        }
                    } catch (IOException e) {
                        Console.out.println( "IOException!");
                    }
                }
                
                ThreadExt.sleep(20);
            } catch (InterruptedException e) {
                Console.out.println( "InterruptedException!");
            }
        }
    }
    
    public static void main( String[] args) {
        // create node
        Eia709Node node = new Eia709Node( UNIQUE_NODE_ID);

        // supply node address
        node.setNodeAddress( DOMAIN_NAME, SRC_SUBNET, SRC_NODE);
        
        // create and install controllet
        node.setControllet( new BCCServer());

        // start node
        node.start();
    }
}
