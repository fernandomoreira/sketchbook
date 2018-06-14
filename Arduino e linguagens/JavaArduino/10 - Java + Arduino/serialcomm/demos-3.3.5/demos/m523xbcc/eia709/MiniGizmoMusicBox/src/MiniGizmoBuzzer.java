/*
 * MiniGizmoBuzzer.java
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

import jcontrol.demos.eia709.common.Eia709NetworkVariableFreqHz;
import jcontrol.demos.eia709.common.Eia709NetworkVariableSwitch;
import jcontrol.io.SoundDevice;
import jcontrol.lang.Deadline;

/**
 * Universal buzzer class to control the buzzer of for a MiniGizmo board connected
 * via EIA-709. This class implements JControl's SoundDevice Interface, so it may
 * be used in conjunction with the MusicPlayer class.
 *
 * @version  1.0
 */
public class MiniGizmoBuzzer implements SoundDevice {

    private Eia709NetworkVariableSwitch m_nvoSwitch;
    private Eia709NetworkVariableFreqHz m_nvoFreq;

    /**
     * Constructs a new Buzzer using the default PWM channel.
     */
    public MiniGizmoBuzzer(Eia709NetworkVariableSwitch nvoSwitch,
                                       Eia709NetworkVariableFreqHz nvoFreq) {
        this.m_nvoSwitch = nvoSwitch;
        this.m_nvoFreq = nvoFreq;
    }

    /** Turns on the Buzzer with the specified frequency.
     * The noise will last until invoking {@link #off() off}.
     * @param frequency to use (range 250 ... 32767 Hz)
     */
    public void on(int frequency){
        try {
            m_nvoFreq.setFrequency (frequency);
            m_nvoSwitch.setState( Eia709NetworkVariableSwitch.STATE_ON, 100.0f);
        } catch(IOException e) {
        }
    }

    /** Turns off the Buzzer.
     */
    public void off(){
        try {
            m_nvoSwitch.setState( Eia709NetworkVariableSwitch.STATE_OFF, 100.0f);
        } catch(IOException e) {}
    }

    /** Turns on the Buzzer with the specified frequency and duration.
     * @param frequency to use (range 250 ... 32767 Hz)
     * @param duration for the noise (range 1 ... 32767 ms)
     */
    public synchronized void on(int frequency,int duration){
        try {
            m_nvoFreq.setFrequency (frequency);
        } catch(IOException e) {}
        synchronized(new Deadline(duration)){
            try {
                m_nvoSwitch.setState( Eia709NetworkVariableSwitch.STATE_ON, 100.0f);
            } catch(IOException e) {}
        }
        try {
            m_nvoSwitch.setState( Eia709NetworkVariableSwitch.STATE_OFF, 100.0f);
        } catch(IOException e) {}
    }
}
