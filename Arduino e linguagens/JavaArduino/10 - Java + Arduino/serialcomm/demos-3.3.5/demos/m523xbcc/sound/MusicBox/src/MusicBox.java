/*
 * SimpleSound.java
 * Copyright (C) 2000-2007 DOMOLOGIC Home Automation GmbH
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 */
import java.io.IOException;

import jcontrol.lang.ThreadExt;
import jcontrol.io.Resource;
import jcontrol.io.Console;

/**
 * <p>Demonstrates how to play melodies in iMelody format by the on board buzzer.
 * This demo will play the StarWars hymn.</p>
 *
 * @author telkamp
 * @date 17.05.07 09:21
 */
public class MusicBox {

    public static final String SONG = "StarWars.imy";

    /**
     * standard constructor
     */
    public MusicBox() {

        try {
            Resource song = new Resource(SONG);
            MelodyPlayer player = new MelodyPlayer(song);
            (new Thread(player)).start(); // start playing the song
                try {
                    while ((player!=null) && (player.isPlaying())) {
                        ThreadExt.sleep(10); // wait for terminate playing
                    }
                } catch (InterruptedException e) {}
            } catch (IOException e) {
                //Console.out.println( "Can not load song file: ".concat(SONG));
            }
    }

    /**
     * main method. Program execution starts here.
     */
    public static void main(String[] args) {
        new MusicBox();
    }
}
