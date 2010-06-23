package limelight.ui;

import limelight.ui.model.TextPanel;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;
import java.awt.font.FontRenderContext;

import static org.junit.Assert.assertEquals;

public class TextLayoutImplTest
{
  private static Font courier;
  private static FontRenderContext context;

  @BeforeClass
  public static void suiteSetUp()
  {
    courier = new Font("courier", Font.PLAIN, 12);
    context = TextPanel.getRenderContext();
  }
  
  @Test
  public void shouldMeasureText() throws Exception
  {
    assertEquals(21, new TextLayoutImpl("abc", courier, context).getWidth());
    assertEquals(42, new TextLayoutImpl("abc123", courier, context).getWidth());
    assertEquals(84, new TextLayoutImpl("abc123xyz789", courier, context).getWidth());
  }

  @Test
  public void shouldMeasureTrailingWhiteSpace() throws Exception
  {
    assertEquals(28, new TextLayoutImpl("abc ", courier, context).getWidth());
    assertEquals(28, new TextLayoutImpl("abc\t", courier, context).getWidth());
  }

  @Test
  public void shouldMeasurePreceedingWhiteSpace() throws Exception
  {
    assertEquals(28, new TextLayoutImpl(" abc", courier, context).getWidth());
    assertEquals(28, new TextLayoutImpl("\tabc", courier, context).getWidth());
  }

  @Test
  public void shouldMeasureEmptyString() throws Exception
  {
    assertEquals(0, new TextLayoutImpl("", courier, context).getWidth());     
  }

}
