//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.StyleValue;

public class SimpleOnOffValueTest extends TestCase
{
  private SimpleOnOffValue on;
  private SimpleOnOffValue off;

  public void setUp() throws Exception
  {
    on = new SimpleOnOffValue(true);
    off = new SimpleOnOffValue(false);
  }

  public void testCreation() throws Exception
  {
    assertEquals(true, (on instanceof StyleValue));
    assertEquals(true, on.isOn());
    assertEquals(false, off.isOn());
    assertEquals(true, off.isOff());
    assertEquals(false, on.isOff());
  }

  public void testToString() throws Exception
  {
    assertEquals("on", on.toString());
    assertEquals("off", off.toString());
  }

  public void testEquality() throws Exception
  {
    assertEquals(true, on.equals(on));
    assertEquals(true, on.equals(new SimpleOnOffValue(true)));
    assertEquals(false, on.equals(off));
    assertEquals(false, on.equals(null));
  }
}
