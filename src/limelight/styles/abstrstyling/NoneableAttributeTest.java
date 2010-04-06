//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.abstrstyling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.StyleAttribute;
import limelight.styles.styling.SimpleIntegerAttribute;

public class NoneableAttributeTest extends TestCase
{
  private NoneableAttribute<SimpleIntegerAttribute> none;
  private NoneableAttribute<SimpleIntegerAttribute> fifty;

  public void setUp() throws Exception
  {
    none = new NoneableAttribute<SimpleIntegerAttribute>(null);
    fifty = new NoneableAttribute<SimpleIntegerAttribute>(new SimpleIntegerAttribute(50));
  }

  public void testCreation() throws Exception
  {
    assertEquals(true, (none instanceof StyleAttribute));
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
    assertEquals(true, none.equals(new NoneableAttribute<SimpleIntegerAttribute>(null)));
    assertEquals(false, none.equals(fifty));
    assertEquals(true, fifty.equals(fifty));
    assertEquals(true, fifty.equals(new NoneableAttribute<SimpleIntegerAttribute>(new SimpleIntegerAttribute(50))));
    assertEquals(false, none.equals(null));
  }
}
