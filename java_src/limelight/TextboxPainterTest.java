package limelight;

import junit.framework.TestCase;

import javax.swing.*;
import java.awt.*;

public class TextboxPainterTest extends TestCase
{
  private Panel panel;
  private TextboxPainter painter;

  public void setUp() throws Exception
  {
    panel = new Panel(new MockBlock());
    painter = new TextboxPainter(panel);
    panel.getPainters().add(painter);
  }

  public void testThatATextboxIsAddedToThePanel() throws Exception
  {
    assertEquals(1, panel.getComponents().length);
    assertEquals(JTextField.class, panel.getComponents()[0].getClass());
  }

  public void testItsPanelIsSterilized() throws Exception
  {
    assertTrue(panel.isSterilized());
  }

  public void testThatTheLayoutIsSet() throws Exception
  {
    LayoutManager layout = panel.getLayout();
    assertTrue(layout.getClass() == InputLayout.class);
  }
}
