package limelight.ui;

import junit.framework.TestCase;

public class StyleDescriptorTest extends TestCase
{
  public void setUp() throws Exception
  {

  }

  public void testConstruction() throws Exception
  {
    StyleDescriptor descriptor = new StyleDescriptor(1, "Blah");

    assertEquals(1, descriptor.index);
    assertEquals("Blah", descriptor.defaultValue);
  }
}
