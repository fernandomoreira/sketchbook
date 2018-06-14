package jcontrol.demos.eia709.common;
/*
 * Eia709NetworkVariableTemp.java
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
 * <p>This network variable implements the SNVT_freq_hz type. The variable stores a frequency
 * between 0hz and 6553.5hz in 0.1hz steps. The value can be obtained
 * by the <code>getFrequency()</code> method and changed by the
 * <code>setFrequency(int freq)</code> method.</p>
 * 
 * <p>The network variable stores the frequency in a single unsigned 16-bit word.
 * The word value represents the frequency in 1/10 hz, e.g. 440hz are represented
 * as 4400.</p>
 * 
 * @author Lorenz Witte
 */
public class Eia709NetworkVariableFreqHz extends Eia709NetworkVariable {

    /**
     * Minimum temperature.
     */
    public static final int MIN_FREQ = 0;
    
    /**
     * Maximum temperature.
     */
    public static final int MAX_FREQ = 65535;
    
    /**
     * Network variable value size in bytes.
     */
    public static final int SIZE = 2;

    /**
     * SNVT number.
     */
    public static final int TYPE_SNVT_FREQ_HZ = 76;
    
    /**
     * Creates a new SNVT_temp network variable.
     * 
     * @param node node the network variable should be registered to
     * @param identifier identifier
     * @param modifier modifier
     */
    public Eia709NetworkVariableFreqHz(Eia709Node node, String identifier, int modifier) {
        super(node, identifier, TYPE_SNVT_FREQ_HZ, SIZE, modifier);
    }

    /**
     * Creates a new SNVT_temp network variable.
     * 
     * @param node node the network variable should be registered to
     * @param identifier identifier
     * @param modifier modifier
     * @param selfDescription self description
     */
    public Eia709NetworkVariableFreqHz(Eia709Node node, String identifier, int modifier, String selfDescription) {
        super(node, identifier, TYPE_SNVT_FREQ_HZ, SIZE, modifier, selfDescription);
    }

    /**
     * Returns the stored frequency.
     * 
     * @return frequency in 1/10 hz
     */
    public int getFrequency() {
        byte[] value = getValue();
        int freq = (int)value[0] << 8;
        freq |= (int)value[1] & 0xff;
        return freq;
    }

    /**
     * Sets the frequency.
     * 
     * @param temp frequency in 1/10 hz
     * @throws IOException in case of transfer errors
     */
    public void setFrequency( int freq) throws IOException {
        if ( (freq < MIN_FREQ) || (freq > MAX_FREQ)) {
            throw new IllegalArgumentException( "value must be between 0 and 65535!");
        }
        byte[] value = new byte[ SIZE];
        value[0] = (byte)(freq >> 8);
        value[1] = (byte)(freq >> 0);
        setValue( value);
    }
}
