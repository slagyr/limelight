//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.styles.attributes;

import org.junit.Before;
import org.junit.Test;

public class FontFaceAttributeTest extends AbstractStyleAttributeTestBase
{
  @Before
  public void setUp() throws Exception
  {
    attribute = new FontFaceAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Font Face", attribute.getName());
    assertEquals("string", attribute.getCompiler().type);
    assertEquals("Arial", attribute.getDefaultValue().toString());
  }

  @Test
  public void shouldApplyChanges() throws Exception
  {
    checkFontChange();
  }
}
