//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.attributes;

import limelight.styles.compiling.DimensionAttributeCompiler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RightBorderColorAttributeTest extends Assert
{
  private RightBorderColorAttribute attribute;

  @Before
  public void setUp() throws Exception
  {
    attribute = new RightBorderColorAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Right Border Color", attribute.getName());
    assertEquals("color", attribute.getCompiler().type);
    assertEquals("#000000ff", attribute.getDefaultValue().toString());
  }
}
