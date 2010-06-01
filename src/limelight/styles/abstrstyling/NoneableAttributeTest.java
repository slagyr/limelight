//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.abstrstyling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.StyleValue;
import limelight.styles.values.SimpleIntegerValue;

public class NoneableAttributeTest extends TestCase
{
  private NoneableValue<SimpleIntegerValue> none;
  private NoneableValue<SimpleIntegerValue> fifty;

  public void setUp() throws Exception
  {
    none = new NoneableValue<SimpleIntegerValue>(null);
    fifty = new NoneableValue<SimpleIntegerValue>(new SimpleIntegerValue(50));
  }

  public void testCreation() throws Exception
  {
    assertEquals(true, (none instanceof StyleValue));
    assertEquals(true, none.isNone());
    assertEquals(50, fifty.getAttribute().getValue());
  }

  public void testToString() throws Exception
  {
    assertEquals("none", none.toString());
    assertEquals("50", fifty.toString());
  }

  public void testEquality() throws Exception
  {
    assertEquals(true, none.equals(none));
    assertEquals(true, none.equals(new NoneableValue<SimpleIntegerValue>(null)));
    assertEquals(false, none.equals(fifty));
    assertEquals(true, fifty.equals(fifty));
    assertEquals(true, fifty.equals(new NoneableValue<SimpleIntegerValue>(new SimpleIntegerValue(50))));
    assertEquals(false, none.equals(null));
  }
}
