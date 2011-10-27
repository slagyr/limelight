//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.styles.attributes;

import org.junit.Before;
import org.junit.Test;

public class TransparencyAttributeTest extends AbstractStyleAttributeTestBase
{
  @Before
  public void setUp() throws Exception
  {
    attribute = new TransparencyAttribute();
  }

  @Test
  public void shouldCreation() throws Exception
  {
    assertEquals("Transparency", attribute.getName());
    assertEquals("percentage", attribute.getCompiler().type);
    assertEquals("0%", attribute.getDefaultValue().toString());
  }

  @Test
  public void shouldApplyChanges() throws Exception
  {
    setUpPanel();

    attribute.applyChange(panel, null);

    assertNotNull(cache.retrieve(panel));
    assertEquals(true, panel.markedAsDirty);
  }
}
