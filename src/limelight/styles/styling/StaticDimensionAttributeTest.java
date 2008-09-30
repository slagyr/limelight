package limelight.styles.styling;

import limelight.styles.abstrstyling.StyleAttribute;
import junit.framework.TestCase;

public class StaticDimensionAttributeTest extends TestCase
{
  private StaticDimensionAttribute fifty;
  private StaticDimensionAttribute hundred;
  private StaticDimensionAttribute thousand;

  public void setUp() throws Exception
  {
    fifty = new StaticDimensionAttribute(50);
    hundred = new StaticDimensionAttribute(100);
    thousand = new StaticDimensionAttribute(1000);
  }

  public void testCreation() throws Exception
  {
    assertEquals(true, (fifty instanceof StyleAttribute));
    assertEquals(50, fifty.getValue());
  }

  public void testToString() throws Exception
  {
    assertEquals("50", fifty.toString());
    assertEquals("100", hundred.toString());
    assertEquals("1000", thousand.toString());
  }

  public void testEquality() throws Exception
  {
    assertEquals(true, fifty.equals(fifty));
    assertEquals(true, fifty.equals(new StaticDimensionAttribute(50)));
    assertEquals(false, fifty.equals(hundred));
    assertEquals(false, fifty.equals(null));
  }
}
