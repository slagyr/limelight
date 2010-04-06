//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.StyleAttribute;

public class SimpleIntegerAttributeTest extends TestCase
{
  private SimpleIntegerAttribute attr50;
  private SimpleIntegerAttribute attr100;
  private SimpleIntegerAttribute attr999;

  public void setUp() throws Exception
  {
    attr50 = new SimpleIntegerAttribute(50);
    attr100 = new SimpleIntegerAttribute(100);
    attr999 = new SimpleIntegerAttribute(999);
  }

  public void testCreation() throws Exception
  {
    assertEquals(true, (attr50 instanceof StyleAttribute));
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
    assertEquals(true, attr50.equals(new SimpleIntegerAttribute(50)));
    assertEquals(false, attr50.equals(attr999));
  }
}
