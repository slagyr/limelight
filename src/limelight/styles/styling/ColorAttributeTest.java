package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.StyleAttribute;

import java.awt.*;

public class ColorAttributeTest extends TestCase
{
  private ColorAttribute blue;
  private ColorAttribute red;
  private ColorAttribute hex;

  public void setUp() throws Exception
  {
    blue = new ColorAttribute(Color.blue);
    red = new ColorAttribute(Color.red);
    hex = new ColorAttribute(new Color(12, 34, 56, 78));
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
    assertEquals(true, blue.equals(new ColorAttribute(Color.blue)));
    assertEquals(false, blue.equals(red));
    assertEquals(false, blue.equals(null));
  }
}
