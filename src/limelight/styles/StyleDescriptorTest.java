//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles;

import junit.framework.TestCase;

public class StyleDescriptorTest extends TestCase
{
  public void setUp() throws Exception
  {

  }

  public void testConstruction() throws Exception
  {
    StyleDescriptor descriptor = new StyleDescriptor(1, "NAME", "Blah");

    assertEquals(1, descriptor.index);
    assertEquals("NAME", descriptor.name);
    assertEquals("Blah", descriptor.defaultValue);
  }
}
