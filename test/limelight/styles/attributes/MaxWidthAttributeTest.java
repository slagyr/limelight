//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.styles.attributes;

import limelight.styles.compiling.DimensionAttributeCompiler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MaxWidthAttributeTest extends Assert
{
  private MaxWidthAttribute attribute;

  @Before
  public void setUp() throws Exception
  {
    attribute = new MaxWidthAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Max Width", attribute.getName());
    assertEquals("noneable simple dimension", attribute.getCompiler().type);
    assertEquals("none", attribute.getDefaultValue().toString());
  }
}
