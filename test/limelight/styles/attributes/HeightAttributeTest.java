//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.attributes;

import org.junit.Before;
import org.junit.Test;

public class HeightAttributeTest extends AbstractStyleAttributeTestBase
{
  @Before
  public void setUp() throws Exception
  {
    attribute = new HeightAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Height", attribute.getName());
    assertEquals("dimension", attribute.getCompiler().type);
    assertEquals("auto", attribute.getDefaultValue().toString());
  }
  
  @Test
  public void shouldStyleChanged() throws Exception
  {
    checkDimensionChange();
  }
}
