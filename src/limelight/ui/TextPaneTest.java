//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui;

import junit.framework.TestCase;

import javax.swing.*;
import java.awt.*;

import limelight.styles.Style;
import limelight.ui.TextPane;

public class TextPaneTest extends TestCase
{
  private TextPane pane;
  private limelight.ui.Rectangle bounds;
  private MockProp prop;
  private Style style;
  private JFrame frame;
  private MockPanel panel;

  public void setUp() throws Exception
  {
    TextPane.widthPadding = 0;
    bounds = new limelight.ui.Rectangle(0, 0, 100, 100);
    panel = new MockPanel();
    panel.rectangleInsidePadding = bounds;
    style = panel.getStyle();
    pane = new TextPane(panel, "Some Text");
    style.setTextColor("black");
  }

  public void tearDown()
  {
    if(frame != null)
      frame.setVisible(false);
  }

  public void testConstructor() throws Exception
  {
    assertEquals(panel, pane.getPanel());
    assertEquals("Some Text", pane.getText());
  }

  public void testPreferredSize() throws Exception
  {
    useFrame();
    pane.doLayout();
    Dimension size = pane.getPreferredSize();
    assertEquals(59, size.width);
    assertEquals(14, size.height);
  }

  public void testPreferredSizeWithMoreText() throws Exception
  {
    useFrame();
    pane.setText("Once upon a time, there was a developer working on a tool called Limelight.");
    pane.doLayout();
    Dimension size = pane.getPreferredSize();
    assertEquals(99, size.width);
    assertEquals(69, size.height);
  }

  public void testPreferredSizeWithBigFontSize() throws Exception
  {
    useFrame();
    style.setFontSize("40");  
    pane.doLayout();
    Dimension size = pane.getPreferredSize();
    assertEquals(79, size.width);
    assertEquals(138, size.height);
  }

  public void testDimnsionsWhenLastLineIsLongest() throws Exception
  {
    useFrame();
    pane.setText("1\n2\n3\nlongest");
    pane.doLayout();
    Dimension size = pane.getPreferredSize();
    assertEquals(41, size.width);
    assertEquals(55, size.height);
  }

  private void useFrame()
  {
    frame = new JFrame();
    frame.add(pane);
    frame.setVisible(true);
  }

  public void testTextChanged() throws Exception
  {
    assertFalse(pane.textChanged());

    pane.setText("Something");
    assertTrue(pane.textChanged());

    pane.flushChanges();
    pane.setText("Something");
    assertFalse(pane.textChanged());

    pane.setText("Something Else");
    assertTrue(pane.textChanged());

    pane.flushChanges();
    assertFalse(pane.textChanged());
  }
}
