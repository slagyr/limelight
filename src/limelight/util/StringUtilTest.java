//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.util;

import junit.framework.TestCase;

public class StringUtilTest extends TestCase
{

  public void testJoin() throws Exception
  {
    String result = StringUtil.join(new String[]{"one", "two", "three"}, ",");
    assertEquals("one,two,three", result);

    result = StringUtil.join(new String[]{}, ",");
    assertEquals("", result);

    result = StringUtil.join(new String[]{"one"}, ",");
    assertEquals("one", result);
  }
  
}
