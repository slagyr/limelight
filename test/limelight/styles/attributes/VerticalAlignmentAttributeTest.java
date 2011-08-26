//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.styles.attributes;

import org.junit.Before;
import org.junit.Test;

public class VerticalAlignmentAttributeTest extends AbstractStyleAttributeTestBase
{
  @Before
  public void setUp() throws Exception
  {
    attribute = new VerticalAlignmentAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Vertical Alignment", attribute.getName());
    assertEquals("vertical alignment", attribute.getCompiler().type);
    assertEquals("top", attribute.getDefaultValue().toString());
  }

  @Test
  public void shouldStyleChanged() throws Exception
  {
    checkAlignmentChange();
  }
}
