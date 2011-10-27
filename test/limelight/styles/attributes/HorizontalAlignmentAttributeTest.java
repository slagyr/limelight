//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.styles.attributes;

import org.junit.Before;
import org.junit.Test;

public class HorizontalAlignmentAttributeTest extends AbstractStyleAttributeTestBase
{
  @Before
  public void setUp() throws Exception
  {
    attribute = new HorizontalAlignmentAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Horizontal Alignment", attribute.getName());
    assertEquals("horizontal alignment", attribute.getCompiler().type);
    assertEquals("left", attribute.getDefaultValue().toString());
  }

  @Test
  public void shouldStyleChanged() throws Exception
  {
    checkAlignmentChange();
  }

}
