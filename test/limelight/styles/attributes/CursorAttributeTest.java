//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.styles.attributes;

import org.junit.Before;
import org.junit.Test;

public class CursorAttributeTest extends AbstractStyleAttributeTestBase
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
