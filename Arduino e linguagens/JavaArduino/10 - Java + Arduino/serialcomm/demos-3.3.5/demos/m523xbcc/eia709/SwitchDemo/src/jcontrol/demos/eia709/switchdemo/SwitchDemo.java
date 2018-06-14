package jcontrol.demos.eia709.switchdemo;
/*
 * SwitchControllet.java
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
import jcontrol.comm.eia709.Eia709Controllet;
import jcontrol.comm.eia709.Eia709NetworkVariable;
import jcontrol.comm.eia709.Eia709Node;
import jcontrol.comm.nv.NetworkVariableEvent;
import jcontrol.comm.nv.NetworkVariableListener;
import jcontrol.demos.eia709.common.Eia709NetworkVariableSwitch;
import jcontrol.io.Console;
import jcontrol.lang.ThreadExt;

/**
 * <p>The <code>SwitchDemo</code> is the counterpart of the <code>LampDemo</code>. Together 
 * they simulate a lamp/switch communication over EIA-709. The demo defines an outgoing SNVT_Switch
 * network variable (selector 1000), which can be used to turn on/off the lamp, and
 * an incoming SNVT_Switch network variable (selector 1001) to receive status information
 * from the lamp.</p>
 * <p>The demo toggles the lamp once per second and displays its state to the default console
 * (RS232).</p>
 * <p>The switch tries to access the lamp in the <code>"JCNTRL"</code> domain as node <code>1</code> 
 * in subnet <code>20</code>.</p>
 * 
 * @author Lorenz Witte
 */
public class SwitchDemo implements Eia709Controllet, NetworkVariableListener, Runnable {

    /** 
     * node this program runs on
     */
    private Eia709Node m_node;
    
    /**
     * Toggling this variable causes the lamp to be turned
     * on/off.
     */
    private Eia709NetworkVariableSwitch m_nvoSwitch;

    /**
     * This network variable reports the lamp state.
     */
    private Eia709NetworkVariableSwitch m_nviState;
    
    private boolean m_running = false;
    
    private Thread m_thread;
    
    /** Supplies static binding information. */
    public void bind() {
        // set nv - selector
        m_nvoSwitch.setSelector( 1000 );
        
        // set nv2 - selector
        m_nviState.setSelector( 1001 );
        
        // set address for nvoSwitch (domain broadcast, subnet: 20)
        Eia709Address addr = Eia709Address.createAddressType0( m_node, 20);
        m_nvoSwitch.setAddress(addr);
    }

    /** Creates network variables */
    public void configure(Eia709Node node) {
        m_node = node;

        // create nvoSwitch
        m_nvoSwitch = new Eia709NetworkVariableSwitch( m_node, "nvoSwitch", 
                Eia709NetworkVariable.MOD_DIR_OUTGOING | Eia709NetworkVariable.MOD_SYNC,
                "This network variable acts as a lamp switch.");

        // create nviState and attach listener
        m_nviState = new Eia709NetworkVariableSwitch( m_node, "nviState", 
                Eia709NetworkVariable.MOD_DIR_INCOMING | Eia709NetworkVariable.MOD_SYNC,
                "This network variable acts as lamp status");
        
        m_nviState.setListener( this);
    }

    /** Returns program name (8 bytes). */
    public byte[] getProgramName() {
        return new byte[] { 'S', 'W', 'I', 'T', 'C', 'H', '0', '0'};
    }

    /** Starts the program. */
    public void start() {
        m_thread = new Thread( this);
        m_running = true;
        m_thread.start();
    }

    /** Stops the program. */
    public void stop() {
        m_running = false;
        m_thread.interrupt();
    }

    /** Main function. */
    public void run() {
        while ( m_running) {
            try { 
                ThreadExt.sleep(1000); 
                m_nvoSwitch.setState( Eia709NetworkVariableSwitch.STATE_ON, 100.0F);
                Console.out.println( "SwitchControllet: switch on");

                ThreadExt.sleep(1000); 
                m_nvoSwitch.setState( Eia709NetworkVariableSwitch.STATE_OFF);
                Console.out.println( "SwitchControllet: switch off");
            } catch (InterruptedException e) {
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** Is called when the light state is changed. */
    public void networkVariableChanged(NetworkVariableEvent event) {
        if ( (event.getType() & NetworkVariableEvent.VALUE_CHANGED) != 0) {
            Console.out.println( "SwitchControllet: light state changed");
        }
    }

    public static void main(String[] args) {
        
        // create node
        Eia709Node node = new Eia709Node( new byte[] {'N', 'O', 'D', 'E', '0', '2'} );
        
        // create node address
        node.setNodeAddress( new byte[] { 'J', 'C', 'N', 'T', 'R', 'L' }, 20, 2);
        
        // install controllet
        node.setControllet( new SwitchDemo());

        // start node
        node.start();
    }
    
}
