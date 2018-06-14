/*
 * MiniGizmoMusicBox.java
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
import jcontrol.demos.eia709.common.Eia709NetworkVariableFreqHz;
import jcontrol.demos.eia709.common.Eia709NetworkVariableSwitch;
import jcontrol.io.Console;
import jcontrol.io.Resource;
import jcontrol.io.SoundDevice;
import jcontrol.lang.ThreadExt;

/**
 * The MiniGizmoMusicBox is meant to be used in conjunction with the MGDemo running
 * on a Echelon MiniGizmo connected to the M5235BCC device via EIA-709. The demo
 * will play the StarWars hymn on the MiniGizmo's buzzer.
 *
 * @author Lorenz Witte
 */
public class MiniGizmoMusicBox implements Eia709Controllet, Runnable {

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
    public static final byte[] PROGRAM_NAME = { 'M', 'U', 'S', 'I', 'C', 'B', 'O', 'X'};

    /**
     * The Starwars tune!
     */
    public static final String SONG = "StarWars.imy";

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
    public Eia709NetworkVariableSwitch nvoBuzzerSwitch;

    /**
     * The network variable for changing the buzzer frequency.
     */
    public Eia709NetworkVariableFreqHz nvoBuzzerFrequency;

    /**
     * MiniGizmo
     */
    public SoundDevice m_buzzer;

    /**
     * The EIA-709 node.
     */
    private Eia709Node m_node;

    /**
     * Constructor.
     */
    public MiniGizmoMusicBox() {
    }

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

        m_buzzer = new MiniGizmoBuzzer( nvoBuzzerSwitch, nvoBuzzerFrequency);
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
    }

    /**
     * Implements the controllet's mainloop which continuously polls the CEA 709 Daughter
     * Cards buttons. On button press or release, the MiniGizmo's buzzer is turned on/off
     * accordingly.
     * The mainloop is run in its own thread.
     */
    public void run() {

        // sets flag to indicate the thread is running
        Console.out.println( "let's play!");
        try {
            Resource song = new Resource(SONG);
            MelodyPlayer player = new MelodyPlayer( m_buzzer, song);
            (new Thread(player)).start(); // start playing the song
                try {
                    while ((player!=null) && (player.isPlaying())) {
                        ThreadExt.sleep(10); // wait for terminate playing
                    }
                } catch (InterruptedException e) {
                }
            } catch (IOException e) {
                Console.out.println( "Can not load song file: ".concat(SONG));
            }
    }

    /**
     * Starts the MiniGizmoMusicBox demo.
     */
    public static void main(String[] args) {

        // create node
        Eia709Node node = new Eia709Node( UNIQUE_NODE_ID);

        // supply node address
        node.setNodeAddress( DOMAIN_NAME, SRC_SUBNET, SRC_NODE);

        // create and install controllet
        node.setControllet( new MiniGizmoMusicBox());

        // start node
        node.start();
    }
}
