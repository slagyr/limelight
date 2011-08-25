//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.StyleValue;

public class SimpleIntegerValueTest extends TestCase
{
  private SimpleIntegerValue attr50;
  private SimpleIntegerValue attr100;
  private SimpleIntegerValue attr999;

  public void setUp() throws Exception
  {
    attr50 = new SimpleIntegerValue(50);
    attr100 = new SimpleIntegerValue(100);
    attr999 = new SimpleIntegerValue(999);
  }

  public void testCreation() throws Exception
  {
    assertEquals(true, (attr50 instanceof StyleValue));
    assertEquals(50, attr50.getValue());
  }

  public void testToString() throws Exception
  {
    assertEquals("50", attr50.toString());
    assertEquals("100", attr100.toString());
    assertEquals("999", attr999.toString());
  }

  public void testEquality() throws Exception
  {
    assertEquals(true, attr50.equals(attr50));
    assertEquals(true, attr50.equals(new SimpleIntegerValue(50)));
    assertEquals(false, attr50.equals(attr999));
  }
}
