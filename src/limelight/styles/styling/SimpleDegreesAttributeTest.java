//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.StyleAttribute;

public class SimpleDegreesAttributeTest extends TestCase
{

  public void testCreation() throws Exception
  {
    SimpleDegreesAttribute attr = new SimpleDegreesAttribute(180);
    assertEquals(true, attr instanceof StyleAttribute);
    assertEquals(180, attr.getDegrees());
  }
}
