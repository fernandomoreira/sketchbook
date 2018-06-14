package jcontrol.demos.eia709.common;
/*
 * Eia709NetworkVariableFreqF.java
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
import jcontrol.io.DataInputStream;
import jcontrol.io.DataOutputStream;

/**
 * <p>This network variable implements the SNVT_freq_f type. The variable stores a frequency
 * in Hertz.</p>
 * 
 * <p>The frequency is stored in the network variable as a single 4-byte float.</p>
 * 
 * @author Lorenz Witte
 */
public class Eia709NetworkVariableFreqF extends Eia709NetworkVariable {

    /**
     * Network variable value size in bytes.
     */
    public static final int SIZE = 4;

    /**
     * Creates a new SNVT_freq_f network variable.
     * 
     * @param node node the network variable should be registered to
     * @param identifier identifier
     * @param modifier modifier
     */
    public Eia709NetworkVariableFreqF(Eia709Node node, String identifier, int modifier) {
        super(node, identifier, Eia709NetworkVariable.TYPE_SNVT_FREQ_F, SIZE, modifier);
    }

    /**
     * Creates a new SNVT_freq_f network variable.
     * 
     * @param node node the network variable should be registered to
     * @param identifier identifier
     * @param modifier modifier
     * @param selfDescription self description
     */
    public Eia709NetworkVariableFreqF(Eia709Node node, String identifier, int modifier, String selfDescription) {
        super(node, identifier, Eia709NetworkVariable.TYPE_SNVT_FREQ_F, SIZE, modifier, selfDescription);
    }

    /**
     * Returns the stored frequency value.
     * 
     * @return frequency in Hz
     */
    public float getFrequency() {
        DataInputStream in = this.getDataInputStream();
        try {
            return in.readFloat();
        } catch (IOException e) {
            return 0f;
        }
    }

    /**
     * Sets the frequency.
     * 
     * @param freq new frequency in Hz
     * @throws IOException in case of transfer errors
     */
    public void setFreqency( float freq) throws IOException {
        DataOutputStream out = this.getDataOutputStream();
        out.writeFloat( freq);
        this.propagate();
    }
}
