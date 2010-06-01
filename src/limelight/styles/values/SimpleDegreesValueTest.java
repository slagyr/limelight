//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.StyleValue;

public class SimpleDegreesValueTest extends TestCase
{

  public void testCreation() throws Exception
  {
    SimpleDegreesValue attr = new SimpleDegreesValue(180);
    assertEquals(true, attr instanceof StyleValue);
    assertEquals(180, attr.getDegrees());
  }
}
