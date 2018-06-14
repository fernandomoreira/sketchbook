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
 * MERCHANTABILITY or FITNESS FOR A    PARTICULAR PURPOSE.Â  See the GNU
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
 * <p>This network variable implements the SNVT_temp type. The variable stores a temperature
 * between -274.0°C and 6279.5°C in 0.1°C steps. The value can be obtained
 * by the <code>getTemperature()</code> method and changed by the
 * <code>setTemperature(float temp)</code> method.</p>
 * 
 * <p>The network variable stores the temperature in a single signed 16-bit word.
 * The word value is the actual temperature multiplied by ten, e.g. -12.3°C are represented
 * as -123, 56°C are represented as 560.</p>
 * 
 * @author Lorenz Witte
 */
public class Eia709NetworkVariableTemp extends Eia709NetworkVariable {

    /**
     * Minimum temperature.
     */
    public static final int MIN_TEMPERATURE = -2740;
    
    /**
     * Maximum temperature.
     */
    public static final int MAX_TEMPERATURE = 62795;
    
    /**
     * Network variable value size in bytes.
     */
    public static final int SIZE = 2;

    /**
     * Creates a new SNVT_temp network variable.
     * 
     * @param node node the network variable should be registered to
     * @param identifier identifier
     * @param modifier modifier
     */
    public Eia709NetworkVariableTemp(Eia709Node node, String identifier, int modifier) {
        super(node, identifier, Eia709NetworkVariable.TYPE_SNVT_TEMP, SIZE, modifier);
    }

    /**
     * Creates a new SNVT_temp network variable.
     * 
     * @param node node the network variable should be registered to
     * @param identifier identifier
     * @param modifier modifier
     * @param selfDescription self description
     */
    public Eia709NetworkVariableTemp(Eia709Node node, String identifier, int modifier, String selfDescription) {
        super(node, identifier, Eia709NetworkVariable.TYPE_SNVT_TEMP, SIZE, modifier, selfDescription);
    }

    /**
     * Returns the stored temperature.
     * 
     * @return temperature in °C
     */
    public float getTemperature() {
        return (float)getTemperatureAsInt() / 10;
    }

    /**
     * Returns the stored temperature.
     * 
     * @return temperature in 0.1 °C steps (i.e. 56°C = 560)
     */
    public int getTemperatureAsInt() {
        byte[] value = getValue();
        int temp = (int)value[0] << 8;
        temp |= (int)value[1] & 0xff;
        return temp;
    }
    
    /**
     * Sets the temperature.
     * 
     * @param temp temperature in °C between -274.0°C and 6279.5°C
     * @throws IOException in case of transfer errors
     */
    public void setTemperature( float temp) throws IOException {
        setTemperature((int)(temp*10));
    }
    
    /**
     * Sets the temperature.
     * 
     * @param temp temperature in 0.1 °C steps (i.e. 56°C = 560).
     * @throws IOException in case of transfer errors
     */
    public void setTemperature( int temp) throws IOException {
        if ( (temp < MIN_TEMPERATURE) || (temp > MAX_TEMPERATURE)) {
            throw new IllegalArgumentException( "value must be between -274.0 and 6279.5 degrees!");
        }
        byte[] value = new byte[ SIZE];
        value[0] = (byte)(temp >> 8);
        value[1] = (byte)(temp >> 0);
        setValue( value);
    }
}
