//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.attributes;

import org.junit.Before;
import org.junit.Test;

public class FontSizeAttributeTest extends AbstractStyleAttributeTestBase
{
  @Before
  public void setUp() throws Exception
  {
    attribute = new FontSizeAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Font Size", attribute.getName());
    assertEquals("integer", attribute.getCompiler().type);
    assertEquals("12", attribute.getDefaultValue().toString());
  }

  @Test
  public void shouldApplyChanges() throws Exception
  {
    checkFontChange();
  }
}
