//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.StyleAttribute;
import limelight.ui.painting.StaticImageFillStrategy;
import limelight.ui.painting.RepeatingImageFillStrategy;

public class SimpleFillStrategyAttributeTest extends TestCase
{
  private SimpleFillStrategyAttribute statik;
  private SimpleFillStrategyAttribute repeat;

  public void setUp() throws Exception
  {
    statik = new SimpleFillStrategyAttribute("static");
    repeat = new SimpleFillStrategyAttribute("repeat");
  }

  public void testCreation() throws Exception
  {
    assertEquals(true, (statik instanceof StyleAttribute));
    assertEquals("static", statik.getName());
    assertEquals(StaticImageFillStrategy.class, statik.getStrategy().getClass());

    assertEquals("repeat", repeat.getName());
    assertEquals(RepeatingImageFillStrategy.class, repeat.getStrategy().getClass());
  }

  public void testToString() throws Exception
  {
    assertEquals("static", statik.toString());
    assertEquals("repeat", repeat.toString());
  }

  public void testEquality() throws Exception
  {
    assertEquals(true, statik.equals(statik));
    assertEquals(true, statik.equals(new SimpleFillStrategyAttribute("static")));
    assertEquals(false, statik.equals(repeat));
    assertEquals(false, statik.equals(null));
  }
}
