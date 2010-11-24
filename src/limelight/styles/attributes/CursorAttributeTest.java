//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.attributes;

import org.junit.Before;
import org.junit.Test;

public class CursorAttributeTest extends AbstractStyleAttributeTest
{
  @Before
  public void setUp() throws Exception
  {
    attribute = new CursorAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Cursor", attribute.getName());
    assertEquals("cursor", attribute.getCompiler().type);
    assertEquals("default", attribute.getDefaultValue().toString());
  }
}
