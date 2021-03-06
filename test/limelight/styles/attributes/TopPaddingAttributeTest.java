//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.styles.attributes;

import org.junit.Before;
import org.junit.Test;

public class TopPaddingAttributeTest extends AbstractStyleAttributeTestBase
{
  @Before
  public void setUp() throws Exception
  {
    attribute = new TopPaddingAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Top Padding", attribute.getName());
    assertEquals("pixels", attribute.getCompiler().type);
    assertEquals("0", attribute.getDefaultValue().toString());
  }

  @Test
  public void shouldStyleChanged() throws Exception
  {
    checkInsetChange();
  }
}
