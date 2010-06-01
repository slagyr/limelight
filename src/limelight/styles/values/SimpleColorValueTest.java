//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.StyleValue;

import java.awt.*;

public class SimpleColorValueTest extends TestCase
{
  private SimpleColorValue blue;
  private SimpleColorValue red;
  private SimpleColorValue hex;

  public void setUp() throws Exception
  {
    blue = new SimpleColorValue(Color.blue);
    red = new SimpleColorValue(Color.red);
    hex = new SimpleColorValue(new Color(12, 34, 56, 78));
  }

  public void testCreation() throws Exception
  {
    assertEquals(true, blue instanceof StyleValue);
    assertEquals(Color.blue, blue.getColor());
  }
  
  public void testToString() throws Exception
  {
    assertEquals("#0000ffff", blue.toString());
    assertEquals("#ff0000ff", red.toString());
    assertEquals("#0c22384e", hex.toString());
  }

  public void testEquality() throws Exception
  {
    assertEquals(true, blue.equals(blue));
    assertEquals(true, blue.equals(new SimpleColorValue(Color.blue)));
    assertEquals(false, blue.equals(red));
    assertEquals(false, blue.equals(null));
  }
}
