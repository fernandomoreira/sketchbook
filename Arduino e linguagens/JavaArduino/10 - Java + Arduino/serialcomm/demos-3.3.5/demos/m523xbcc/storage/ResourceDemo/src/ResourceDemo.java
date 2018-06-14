/*
 * ResourceDemo.java
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
import jcontrol.io.Resource;
import jcontrol.io.Console;

/**
 * <p>Demonstrate the usage of archive resources. In specially this
 * demo prints all entries of the application archive to the console.
 * Following by printing the content of the resource 'README.TXT' to
 * the console.</p>
 *
 * @author Rst, roebbenack
 */
public class ResourceDemo {

    public static void main(String[] args) {

        try {
            // open resource storage
            Resource in = new Resource(Resource.FLASHACCESS);

            // dump flash resource
            while (in != null) {
                Resource bak = in;
                Console.out.println(">> Directory of Flash-Archive:");
                while (in != null) {
                    Console.out.println(in.getName().concat(", ").concat(
                            String.valueOf(in.length())).concat(" bytes"));
                    in = in.next();
                }
                in = bak.nextArchive();
            }

            // dump built-in rom resource
            in = new Resource(Resource.ROMACCESS);
            Console.out.println(">> Directory of ROM-Archive:");
            while (in != null) {
                Console.out.println(in.getName().concat(", ").concat(
                        String.valueOf(in.length())).concat(" bytes"));
                in = in.next();
            }
        } catch (IOException e) {
            Console.out.println("==catched==");
        }

        Resource in;
        try {
            in = new Resource("README.TXT");
        } catch (IOException e) {
            Console.out.println("==openerror==");
            return;
        }

        Console.out.print(">> Dumping File: \"".concat(in.getName()).concat(":"));
        Console.out.println("\", ".concat(String.valueOf(in.length())).concat(
                " bytes"));
        try {
            for (;;) {
                String buffer = in.readLine();
                Console.out.println(buffer);
            }
        } catch (IOException e) {
            Console.out.println("==EOF==");
        }

    }
}