/*
 * VoleBigImageMenuExample.java
 * Copyright (C) 2000-2007 DOMOLOGIC Home Automation GmbH
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 */

import jcontrol.ui.vole.Frame;
import jcontrol.ui.vole.menu.BigImageMenu;

/**
 * <p>This example demonstrates how to use the
 * component BigImageMenu within the GUI framework
 * JControl/Vole.</p>
 *
 * <p>(C) DOMOLOGIC Home Automation GmbH 2003-2005</p>
 */
public class VoleBigImageMenuExample extends Frame {

  /**
   * Create and show a BigImageMenu
   */
  public VoleBigImageMenuExample() {
    // lights on!
    jcontrol.io.Backlight.setBrightness(255);

    // create a big BigImageMenu
    BigImageMenu menu = new BigImageMenu(0, 0, 128, 64);

    // add some menu items
    menu.addMenuItem("menu1.jcif");
    menu.addMenuItem("menu2.jcif");
    menu.addMenuItem("menu3.jcif");

    // show the menu
    setMenu(menu);
    show();
  }

  /**
   * Instantiate the VoleBigImageMenuExample
   */
  public static void main(String[] args) {
    new VoleBigImageMenuExample();
  }
}
