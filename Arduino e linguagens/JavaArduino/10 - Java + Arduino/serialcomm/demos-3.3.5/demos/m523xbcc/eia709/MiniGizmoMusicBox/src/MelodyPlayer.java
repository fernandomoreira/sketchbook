import java.io.IOException;

import jcontrol.io.Buzzer;
import jcontrol.io.SoundDevice;
import jcontrol.io.Resource;
import jcontrol.lang.Deadline;
import jcontrol.lang.DeadlineMissException;

/**
 *  This class <code>MelodyPlayer</code> is used to play a set of tones using a buzzer.<br>
 *  It uses the iMelody format, specified by Infrared Data Association (IrDA).
 *  <p>
 *  &lt;melody&gt; ::= {&lt;silence&gt;|&lt;note&gt;|&lt;repeat&gt;}+<br>
 *  &lt;silence&gt; ::= &lt;rest&gt;&lt;duration&gt;[&lt;duration-specifier&gt;]<br>
 *  &lt;rest&gt; ::= "r"<br>
 *  &lt;repeat&gt; ::= "("{&lt;silence&gt;|&lt;note&gt;}+"@"&lt;repeat-count&gt;")"<br>
 *  &lt;repeat-count&gt; ::= "0"|"1"|"2"|...      (0 is repeat forever)<br>
 *  &lt;note&gt; ::= [&lt;octave-prefix&gt;]&lt;basic-ess-iss-note&gt;&lt;duration&gt;[&lt;duration-specifier&gt;]<br>
 *  &lt;duration&gt; ::= "0"|"1"|"2"|"3"|"4"|"5"<br>
 *  &lt;duration-specifier&gt; ::= "."|":"|";"<br>
 *  &lt;octave-prefix&gt; ::= "*0"|"*1"|"*2"...<br>
 *  &lt;basic-ess-iss-note&gt; ::= &lt;basic-note&gt;|&lt;ess-note&gt;|&lt;iss-note&gt;<br>
 *  &lt;basic-note&gt; ::= "c"|"d"|"e"|"f"|"g"|"a"|"b"<br>
 *  &lt;ess-note&gt; ::= "&d"|"&e"|"&g"|"&a"|"&b"    (flat m_notes)<br>
 *  &lt;iss-note&gt; ::= "#c"|"#d"|"#f"|"#g"|"#a"    (sharp m_notes)
 *  <p>
 *  Note: No &lt;repeat&gt; block within &lt;repeat&gt; block!<br>
 *  <p>
 *  Duration:<br>
 *  0  - Full-note<br>
 *  1  - 1/2-note<br>
 *  2  - 1/4-note<br>
 *  3  - 1/8-note<br>
 *  4  - 1/16-note<br>
 *  5  - 1/32-note<br>
 * <p>
 * Duration Specifier:<br>
 *  none - No special duration<br>
 *   .   - Dotted note<br>
 *   :   - Double dotted note<br>
 *   ;   - 2/3 m_notesLength<br>
 *
 * @author Marcus Timmermann
 */

public class MelodyPlayer implements Runnable {

    private static final byte NO = (byte) 0x0;
    private static final byte IS = (byte) 0x1;
    private static final byte ES = (byte) 0x2;

    /**
     * Normal mode
     * @see #setStyle(int)
     */
    public static final byte NORMAL = (byte) 0x0;
    /**
     * Continuous mode
     * @see #setStyle(int)
     */
    public static final byte CONT   = (byte) 0x1;
    /**
     * Staccato mode
     * @see #setStyle(int)
     */
    public static final byte STACC  = (byte) 0x2;

    private byte[] m_notes = null;
    private Resource m_resource = null;
    private int m_notesLength;
    private int m_noteLength;
    private int m_position = 0;
    private int m_songStyle = NORMAL;
    private SoundDevice m_buzzer = null;
    private boolean m_playing = true;
    private boolean m_running = false;

    /**
     * Set the melody string.
     * @param melody the complete melody string to play
     */
    public void setNotes(String melody) {
        m_notes = melody.getBytes();
        m_resource = null;
    }

    /**
     * Set the beat.
     * @param beat beats per minute
     */
    public void setBeat(int beat) {
        m_noteLength = (30000/beat)<<3;
    }

    /**
     * Set the song style.
     *
     * @param songStyle one of {@link #NORMAL}, {@link #STACC} or {@link #CONT}
     */
    public void setStyle(int songStyle) {
        if (songStyle == NORMAL || songStyle == CONT || songStyle == STACC)
          m_songStyle = songStyle;
        else
          m_songStyle = NORMAL;
    }

    private int index = 0;

    /**
     * Stops the music playing.
     */
    public void stop() {
        m_playing = false;
    }

    public boolean isPlaying() {
        return m_running;
    }

    /**
     * Read the header of the iMelody file
     */
    private void readHeader() throws IOException {
        int rowIndex = 0;
        boolean done = false;
        boolean beatDefined = false;
        int[] header = new int[7];
        int c;
        while (!done) {
            c = m_resource.read();
            m_position++;
            while (c!='\n' && c!='\r' && !done) {
                if (rowIndex<7) {
                    header[rowIndex]=c;
                    if (!beatDefined && rowIndex==4) {
                        if (header[0] == 'B' &&
                            header[1] == 'E' &&
                            header[2] == 'A' &&
                            header[3] == 'T' &&
                            header[4] == ':') {
                            beatDefined = true;
                            int beat = 0;
                            c = m_resource.read();
                            m_position++;
                            while (c!='\n' && c!='\r') {
                                beat = beat*10+(c&0xff-48);
                                c = m_resource.read();
                                m_position++;
                            }
                            if (c=='\r') {
                                c = m_resource.read();
                                m_position++;
                            }
                            setBeat(beat);
                            rowIndex = 0;
                        }
                    }
                    if (rowIndex==6) {
                        if (header[0] == 'M' &&
                            header[1] == 'E' &&
                            header[2] == 'L' &&
                            header[3] == 'O' &&
                            header[4] == 'D' &&
                            header[5] == 'Y' &&
                            header[6] == ':') {
                            done = true;
                        }
                    }
                }
                if (!done) {
                    c = m_resource.read();
                    rowIndex++;
                    m_position++;
                }

            }
            if (c=='\r') {
                c = m_resource.read();
                m_position++;
            }
            rowIndex = 0;
        }
        if (!beatDefined) setBeat(120);
    }

    /**
     * Seek to specified position in resource file
     */
    private void seek(int offset) throws IOException {
        index+=offset;
        if((index<0)||(index>=m_notesLength)) throw new IOException();
        if(m_resource!=null) m_resource.seek(offset,false);
    }

    /**
     * Gets the next char from resource file
     */
    private int get() throws IOException {
        if((index>=m_notesLength) || (!m_playing)) throw new IOException();
        if (m_notes!=null) {
            return (char)m_notes[index++];
        } else if(m_resource!=null){
            index++;
            int c = m_resource.read();
            if (c!='\n' && c!='\r') {
                return c;
            }
            else throw new IOException();
        }
        throw new IOException();
    }

    /**
     * Play the specified melody.
     */
    public void run() {
        if (m_notes==null && m_resource==null) {
            return;
        }
        if (m_notes!=null && m_notes.length<2) {
            return;
        }
        m_running = true;
        int repeatStart = -1;
        int repeatCount = -1;
        int octave = 4;
        int freq   = 0;
        byte prefix = NO;
        int duration = 0;
        int pause = 0;
        boolean action = false;

        Deadline dl = new Deadline(25);
        synchronized(dl) {
            try {
                if (m_resource!=null) m_resource.seek(m_position, true);
            loop:   for (int i=0;i<m_notesLength&&m_playing;i++) {
                        switch (get()) {
                            case 'r': // silence
                                freq = 0;
                                action = true;
                                break;
                            case '*': // octave prefix
                                octave = get()-'0';
                                break;
                            case '&': prefix = ES;
                                break;
                            case '#': prefix = IS;
                                break;
                            case 'a': freq=(prefix==IS)?932:((prefix==ES)?831:880); action = true; break;
                            case 'b': freq=(prefix==IS)?1047:((prefix==ES)?932:988);action = true; break;
                            case 'c': freq=(prefix==IS)?554:((prefix==ES)?494:523); action = true; break;
                            case 'd': freq=(prefix==IS)?622:((prefix==ES)?554:587); action = true; break;
                            case 'e': freq=(prefix==IS)?698:((prefix==ES)?622:659); action = true; break;
                            case 'f': freq=(prefix==IS)?740:((prefix==ES)?659:698); action = true; break;
                            case 'g': freq=(prefix==IS)?831:((prefix==ES)?740:784); action = true; break;
                            case '(': repeatStart = index; break;
                            case ')': repeatStart = -1;
                                repeatCount = -1;
                                break;
                            case '@': if (repeatCount==-1) {
                                    repeatCount = get()-'0';
                                    if (repeatCount==0) {
                                        // repeat forever
                                        repeatCount=-1;
                                        seek(repeatStart-index);
                                    }
                                } else {
                                    repeatCount--;
                                }
                                if (repeatCount>0) {
                                    seek(repeatStart-index);
                                }
                                break;
                        }
                        if (action) {
                            action = false;
                            prefix = NO;
                            // duration
                            int c = get();
                            if (c>='0' && c<='5') {
                                duration = m_noteLength / (1<<(c - '0'));
                            } else break loop;
                            try {
                                switch (get()) {
                                    case '.': duration = duration*3/2; break;
                                    case ':': duration = duration*9/8; break;
                                    case ';': duration = duration*2/3; break;
                                    default : seek(-1);
                                }
                            } catch (IOException e) {
                                seek(-1);
                            }
                            if(freq==0){
                                pause+=duration;
                                duration=0;
                            }
                            if (pause>0) {
                                try {
                                    dl.append(pause);
                                } catch (DeadlineMissException e) {}
                                m_buzzer.off();
                                pause=0;
                            }
                            if(duration>0){
                                switch(m_songStyle){
                                    case NORMAL:
                                        pause=duration>>4;
                                        break;
                                    case STACC:
                                        pause=duration-(m_noteLength>>6);
                                        if(pause>=duration) pause=duration-1;
                                }
                                int shift = octave-4;
                                if (shift>0) freq<<=shift;
                                else if(shift<0) freq>>=-shift;
                                try {
                                    dl.append(duration-pause);
                                } catch (DeadlineMissException e) {}
                                m_buzzer.on(freq);
                            }
                            octave = 4;
                        }
                    }
                } catch (java.io.IOException e) {
                }
                try {
                   dl.check();
                } catch (DeadlineMissException e) {
                }
        }

        m_buzzer.off();
        m_running = false;
    }

    /**
     * Creates a new MelodyPlayer object using a string and the default system buzzer.
     *
     * @param melody  the complete melody string to play
     */
    public MelodyPlayer (String melody) {
        m_buzzer = new Buzzer();
        m_notes = melody.getBytes();
        m_notesLength = m_notes.length;
        setBeat(120);
    }

    /**
     * Creates a new MelodyPlayer object using a resource and the default system buzzer.
     * <P>
     * This should be the prefered way to play MelodyPlayer sounds as the note data will be read
     * directly from the resource.
     *
     * @param m_resource  a Resource specifying an MelodyPlayer (<code>.imy</code>) file.
     */
    public MelodyPlayer (Resource m_resource) {
        this(new Buzzer(), m_resource);
    }

    /**
     * Creates a new MelodyPlayer object using a resource and a spcific m_buzzer device.
     * <P>
     * This should be the prefered way to play MelodyPlayer sounds as the note data will be read
     * directly from the resource.
     * @param m_buzzer the {@link SoundDevice} tu use
     * @param m_resource  a Resource specifying an MelodyPlayer (<code>.imy</code>) file.
     */
    public MelodyPlayer (SoundDevice m_buzzer, Resource m_resource) {
        this.m_buzzer = m_buzzer; // only Buzzer supported yet (interface speedup)
        this.m_resource = m_resource;
        m_notes = null;
        m_position = 0;
        try {
            m_resource.seek(m_position, true);
            readHeader();
            m_notesLength = m_resource.length()-m_position;
        } catch (IOException e) {
        }

    }

}