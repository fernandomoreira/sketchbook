package jcontrol.demos.eia709.common;
/*
 * Eia709NetworkVariableSwitch.java
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
import jcontrol.comm.eia709.Eia709NetworkVariable;
import jcontrol.comm.eia709.Eia709Node;

/**
 * <p>This class represents an <b>SNVT_switch</b> EIA-709 network variable. It
 * contains two values that can be changed individually or at the same time:
 * light intensity ("value") in percentage and the state (on or off).</p>
 * 
 * <p>The network variable has the following binary layout:</p>
 * <table>
 * <th><td>offset</td><td>type</td><td>description</td></th>
 * <tr><td>value</td>0</td><td>unsigned byte</td><td>intensity in 0.5 percent steps (between 0 and 200)</td></tr>
 * <tr><td>state</td>1</td><td>signed byte</td><td>state (0: off, 1: on, all others: invalid)</td></tr>
 * </table>
 * 
 * @author Lorenz Witte
 */
public class Eia709NetworkVariableSwitch extends Eia709NetworkVariable {

    /**
     * Minimum intensity: 0%
     */
    public static final float MIN_INTENSITY    = 0f;
    
    /**
     * Maximum intensity: 100%
     */
    public static final float MAX_INTENSITY    = 100f;
    
    /**
     * Switch state "off".
     */
    public static final int STATE_OFF          = 0;
    
    /**
     * Switch state "on".
     */
    public static final int STATE_ON           = 1;

    /**
     * Switch state "invalid".
     */
    public static final int STATE_INVALID      = -1;

    private static final int INTENSITY_OFFSET  = 0;
    private static final int STATE_OFFSET      = 1;

    /**
     * Network variable value size in bytes.
     */
    public static final int SIZE        = 2;

    /**
     * Creates a new SNVT_switch network variable.
     * 
     * @param node node the network variable should be registered to
     * @param identifier identifier
     * @param modifier modifier
     */
    public Eia709NetworkVariableSwitch(Eia709Node node, String identifier, int modifier) {
        super(node, identifier, Eia709NetworkVariable.TYPE_SNVT_SWITCH, SIZE, modifier);
    }

    /**
     * Creates a new SNVT_switch network variable.
     * 
     * @param node node the network variable should be registered to
     * @param identifier identifier
     * @param modifier modifier
     * @param selfDescription self description
     */
    public Eia709NetworkVariableSwitch(Eia709Node node, String identifier, int modifier, String selfDescription) {
        super(node, identifier, Eia709NetworkVariable.TYPE_SNVT_SWITCH, SIZE, modifier, selfDescription);
    }

    /**
     * Returns the current intensity.
     * 
     * @return intensity in percent
     */
    public float getIntensity() {
        byte[] value = getValue();
        return (float)((int)value[INTENSITY_OFFSET] & 0xff) / 2;
    }

    /**
     * Returns the current state.
     * 
     * @return STATE_ON, STATE_OFF, STATE_INVALID
     * @see #STATE_ON
     * @see #STATE_OFF
     * @see #STATE_INVALID
     */
    public int getState() {
        byte[] value = getValue();
        switch (value[STATE_OFFSET]) {
        case STATE_OFF:
            return STATE_OFF;
        case STATE_ON:
            return STATE_ON;
        default:
            return STATE_INVALID;
        }
    }

    /**
     * Sets the state without changing the intensity.
     * 
     * @param state new state (STATE_ON or STATE_OFF)
     * 
     * @throws IOException in case of transfer errors
     * @see #STATE_ON
     * @see #STATE_OFF
     */
    public void setState( int state) throws IOException {
        if ( (state != STATE_OFF) && (state != STATE_ON)) {
            throw new IllegalArgumentException( "state must be STATE_ON or STATE_OFF");
        }
        byte[] value = getValue();
        value[STATE_OFFSET] = (byte)state;
        setValue( value);
    }

    /**
     * Sets state and intensity.
     * 
     * @param state new state (STATE_ON or STATE_OFF)
     * @param intensity new intensity in percent
     * 
     * @throws IOException in case of transfer errors
     * @see #STATE_ON
     * @see #STATE_OFF
     */
    public void setState( int state, float intensity) throws IOException {
        if ( (state != STATE_OFF) && (state != STATE_ON)) {
            throw new IllegalArgumentException( "state must be STATE_ON or STATE_OFF");
        } else if ( (intensity < MIN_INTENSITY) || (intensity > MAX_INTENSITY)) {
            throw new IllegalArgumentException( "intensity must be between 0% and 100%");
        }
        byte[] value = new byte[2];
        value[STATE_OFFSET] = (byte)state;
        value[INTENSITY_OFFSET] = (byte)(int)(intensity*2);
        setValue( value);
    }
}
