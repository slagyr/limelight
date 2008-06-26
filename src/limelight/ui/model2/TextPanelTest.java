package limelight.ui.model2;

import junit.framework.TestCase;
import limelight.util.Box;
import limelight.styles.Style;

import javax.swing.*;

public class TextPanelTest extends TestCase
{
  private TextPanel panel;
  private Box bounds;
  private Style style;
  private JFrame frame;
  private MockPropablePanel parent;

  public void setUp() throws Exception
  {
    TextPanel.widthPadding = 0;
    bounds = new Box(0, 0, 100, 100);
    parent = new MockPropablePanel();
    parent.setLocation(0, 0);
    parent.setSize(100, 100);
    style = parent.getProp().getStyle();
    panel = new TextPanel(parent, "Some Text");
    style.setTextColor("black");
  }

  public void tearDown()
  {
    if(frame != null)
      frame.setVisible(false);
  }

  public void testConstructor() throws Exception
  {
    assertEquals(parent, panel.getPanel());
    assertEquals("Some Text", panel.getText());
  }

  public void testPreferredSize() throws Exception
  {
    useFrame();
    panel.doLayout();
    assertEquals(59, panel.getWidth());
    assertEquals(14, panel.getHeight());
  }

  public void testPreferredSizeWithMoreText() throws Exception
  {
    useFrame();
    panel.setText("Once upon a time, there was a developer working on a tool called Limelight.");
    panel.doLayout();
    assertEquals(99, panel.getWidth());
    assertEquals(69, panel.getHeight());
  }

  public void testPreferredSizeWithBigFontSize() throws Exception
  {
    useFrame();
    style.setFontSize("40");
    panel.doLayout();
    assertEquals(79, panel.getWidth());
    assertEquals(138, panel.getHeight());
  }

  public void testDimnsionsWhenLastLineIsLongest() throws Exception
  {
    useFrame();
    panel.setText("1\n2\n3\nlongest");
    panel.doLayout();
    assertEquals(41, panel.getWidth());
    assertEquals(55, panel.getHeight());
  }

  private void useFrame()
  {
    frame = new JFrame();
    frame.setVisible(true);
    panel.setGraphics(frame.getGraphics());
  }

  public void testTextChanged() throws Exception
  {
    assertFalse(panel.textChanged());

    panel.setText("Something");
    assertTrue(panel.textChanged());

    panel.flushChanges();
    panel.setText("Something");
    assertFalse(panel.textChanged());

    panel.setText("Something Else");
    assertTrue(panel.textChanged());

    panel.flushChanges();
    assertFalse(panel.textChanged());
  }
}
