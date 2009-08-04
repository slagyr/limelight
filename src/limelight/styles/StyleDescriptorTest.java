//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles;

import junit.framework.TestCase;
import limelight.styles.compiling.IntegerAttributeCompiler;
import limelight.styles.styling.SimpleIntegerAttribute;

public class StyleDescriptorTest extends TestCase
{
  public void setUp() throws Exception
  {

  }

  public void testConstruction() throws Exception
  {
    StyleDescriptor descriptor = new StyleDescriptor(1, "NAME", new IntegerAttributeCompiler(), new SimpleIntegerAttribute(50));

    assertEquals(1, descriptor.index);
    assertEquals("NAME", descriptor.name);
    assertEquals(IntegerAttributeCompiler.class, descriptor.compiler.getClass());
    assertEquals(50, ((SimpleIntegerAttribute)descriptor.defaultValue).getValue());
  }
}
