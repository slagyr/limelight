//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.StyleAttribute;

import java.awt.*;

public class SimpleColorAttributeTest extends TestCase
{
  private SimpleColorAttribute blue;
  private SimpleColorAttribute red;
  private SimpleColorAttribute hex;

  public void setUp() throws Exception
  {
    blue = new SimpleColorAttribute(Color.blue);
    red = new SimpleColorAttribute(Color.red);
    hex = new SimpleColorAttribute(new Color(12, 34, 56, 78));
  }

  public void testCreation() throws Exception
  {
    assertEquals(true, blue instanceof StyleAttribute);
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
    assertEquals(true, blue.equals(new SimpleColorAttribute(Color.blue)));
    assertEquals(false, blue.equals(red));
    assertEquals(false, blue.equals(null));
  }
}
