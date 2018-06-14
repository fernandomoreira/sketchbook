package jcontrol.demos.eia709.lampdemo;
/*
 * LampDemo.java
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

/**
 * <p>The <code>LampDemo</code> can be used in conjunction with the <code>SwitchDemo</code> 
 * to simulate a lamp/switch communication over EIA-709. The demo defines an incoming switch
 * network variable (selector 1000), which can be used to turn on/off the lamp, and
 * an outgoing network variable (selector 1001) to report the current state.</p>
 * <p>Changes of the lamp's state will be displayed on the default console
 * (RS232).</p>
 * <p>The lamp can be accessed in the <code>"JCNTRL"</code> domain as node <code>1</code> 
 * in subnet <code>20</code>.</p>
 * 
 * @author Lorenz Witte
 */
public class LampDemo implements Eia709Controllet, NetworkVariableListener {

  /** 
   * node this program runs on
   */
  private Eia709Node m_node;

  /**
   * Toggling this variable causes the lamp to be turned
   * on/off.
   */
  private Eia709NetworkVariableSwitch m_nviSwitch;

  /**
   * This network variable reports the lamp state.
   */
  private Eia709NetworkVariableSwitch m_nvoState;

  /** Supplies static binding information. */
  public void bind() {
    // set nviSwitch selector
    m_nviSwitch.setSelector( 1000);

    // set nvoState selector
    m_nvoState.setSelector( 1001);

    // set address for nvoState (domain broadcast, subnet: 20)
    Eia709Address addr = Eia709Address.createAddressType0( m_node, 20);
    m_nvoState.setAddress(addr);
  }

  /** Creates network variables */
  public void configure(Eia709Node node) {
    m_node = node;

    // create nviSwitch and attach listener
    m_nviSwitch = new Eia709NetworkVariableSwitch( m_node, "nviSwitch", 
        Eia709NetworkVariable.MOD_DIR_INCOMING);
    m_nviSwitch.setListener( this);

    // create nvoState
    m_nvoState = new Eia709NetworkVariableSwitch( m_node, "nvoState", 
        Eia709NetworkVariable.MOD_DIR_OUTGOING | Eia709NetworkVariable.MOD_SYNC);
  }

  /** Returns program name (8 bytes). */
  public byte[] getProgramName() {
    return new byte[] { 'L', 'A','M', 'P', 'D', 'E','M', 'O'};
  }

  /** Starts the program. */
  public void start() {}

  /** Stops the program. */
  public void stop() {}

  /** Is called when the switch is toggled. */
  public void networkVariableChanged(NetworkVariableEvent event) {
    try {
        Console.out.println("setState to: "+m_nviSwitch.getState());
        m_nvoState.setState( m_nviSwitch.getState(), m_nviSwitch.getIntensity());
    } catch (IOException e) {
      Console.out.println( "something odd happened!");
    }
  }

  public static void main(String[] args) {
    // create node
    Eia709Node node = new Eia709Node( new byte[] { 'N', 'O', 'D', 'E', 'I', 'D'});

    // set node address (domain: "JCNTRL", subnet: 20, node: 1)
    node.setNodeAddress( new byte[] { 'J', 'C', 'N', 'T', 'R', 'L'}, 20, 1);

    // install controllet
    node.setControllet( new LampDemo());

    // start node
    node.start();
  }
}