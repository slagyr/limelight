package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.StyleAttribute;
import limelight.ui.painting.StaticImageFillStrategy;
import limelight.ui.painting.RepeatingImageFillStrategy;

public class FillStrategyAttributeTest extends TestCase
{
  private FillStrategyAttribute statik;
  private FillStrategyAttribute repeat;

  public void setUp() throws Exception
  {
    statik = new FillStrategyAttribute("static");
    repeat = new FillStrategyAttribute("repeat");
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
    assertEquals(true, statik.equals(new FillStrategyAttribute("static")));
    assertEquals(false, statik.equals(repeat));
    assertEquals(false, statik.equals(null));
  }
}
