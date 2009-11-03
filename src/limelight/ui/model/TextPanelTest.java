//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import junit.framework.TestCase;
import limelight.styles.Style;
import limelight.styles.FlatStyle;
import limelight.ui.api.MockScene;

import javax.swing.*;
import java.awt.font.TextLayout;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;

public class TextPanelTest extends TestCase
{
  private TextPanel panel;
  private Style style;
  private JFrame frame;
  private MockPropablePanel parent;
  private RootPanel root;

  public void setUp() throws Exception
  {
    TextPanel.widthPadding = 0;
    parent = new MockPropablePanel();
    parent.setLocation(0, 0);
    parent.setSize(100, 100);
    style = parent.getProp().getStyle();
    panel = new TextPanel(parent, "Some Text");
    parent.add(panel);
    root = new RootPanel(new MockPropFrame());
    root.setPanel(parent);

    style.setTextColor("black");
    
    panel.setRenderContext(new FontRenderContext(new AffineTransform(), true, true));
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
    assertEquals(true, panel.getWidth() >= 98 && panel.getWidth() <= 99);
    assertEquals(69, panel.getHeight());
  }

  public void testPreferredSizeWithBigFontSize() throws Exception
  {
    useFrame();
    style.setFontSize("40");
    panel.doLayout();
    assertEquals(80, panel.getWidth());
    assertEquals(138, panel.getHeight());
  }

  public void testDimensionsWhenLastLineIsLongest() throws Exception
  {
    useFrame();
    panel.setText("1\n2\n3\nlongest");
    panel.doLayout();
    assertEquals(true, panel.getWidth() >= 39 && panel.getWidth() <= 41);
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
    assertTrue(panel.needsLayout());

    panel.resetLayout();
    panel.setText("Something");
    assertFalse(panel.needsLayout());

    panel.setText("Something Else");
    assertTrue(panel.needsLayout());

    panel.resetLayout();
    assertFalse(panel.needsLayout());
  }
  
  public void testLayoutFlushedChangedText() throws Exception
  {
    panel.resetLayout();
    assertEquals(false, panel.needsLayout());

    panel.setText("Something");
    assertEquals(true, panel.needsLayout());

    panel.doLayout();
    assertEquals(false, panel.needsLayout());
  }

  public void testCanBeBuffered() throws Exception
  {
    assertEquals(false, panel.canBeBuffered());
  }

  public void testBuildingLines() throws Exception
  {
    panel.setRenderContext(new FontRenderContext(new AffineTransform(), true, true));
    panel.setText("some text");
    panel.buildLines();

    List<TextLayout> lines = panel.getLines();

    assertEquals(1, lines.size());
    TextLayout layout = lines.get(0);
    assertEquals(9, layout.getCharacterCount());
    assertSubString("family=Arial", layout.toString());
    assertSubString("name=Arial", layout.toString());
    assertSubString("size=12", layout.toString());
  }

  public void testStylingAppliedToLine() throws Exception
  {
    parent.prop.scene = new MockScene();
    FlatStyle myStyle = new FlatStyle();
    ((MockScene)parent.prop.scene).styles.put("my_style", myStyle);
    myStyle.setFontFace("Helvetica");
    myStyle.setFontStyle("bold");
    myStyle.setFontSize("20");

    parent.setSize(200, 100);
    panel.setRenderContext(new FontRenderContext(new AffineTransform(), true, true));
    panel.setText("<my_style>some text</my_style>");
    panel.buildLines();


    List<TextLayout> lines = panel.getLines();

    TextLayout layout = lines.get(0);
    assertEquals(1, lines.size());
    assertEquals(9, layout.getCharacterCount());
    assertSubString("family=Helvetica", layout.toString());
    assertSubString("name=Helvetica", layout.toString());
    assertSubString("style=bold", layout.toString());
    assertSubString("size=20", layout.toString());
  }

  public void testMultipleStylesAppliedToLine() throws Exception
  {
    parent.prop.scene = new MockScene();
    FlatStyle myStyle = new FlatStyle();
    ((MockScene)parent.prop.scene).styles.put("my_style", myStyle);
    myStyle.setFontFace("Helvetica");
    myStyle.setFontStyle("bold");
    myStyle.setFontSize("20");

    FlatStyle myOtherStyle = new FlatStyle();
    ((MockScene)parent.prop.scene).styles.put("my_other_style", myOtherStyle);
    myOtherStyle.setFontFace("Cuneiform");
    myOtherStyle.setFontStyle("italic");
    myOtherStyle.setFontSize("19");

    parent.setSize(200, 100);
    panel.setRenderContext(new FontRenderContext(new AffineTransform(), true, true));
    panel.setText("<my_style>some </my_style><my_other_style>text</my_other_style>");
    panel.buildLines();

    List<TextLayout> lines = panel.getLines();

    TextLayout layout = lines.get(0);
    assertEquals(2, lines.size());
    assertEquals(5, layout.getCharacterCount());
    assertSubString("family=Helvetica", layout.toString());
    assertSubString("name=Helvetica", layout.toString());
    assertSubString("style=bold", layout.toString());
    assertSubString("size=20", layout.toString());

    TextLayout layout2 = lines.get(1);
    assertEquals(5, layout.getCharacterCount());
    assertSubString("family=Dialog", layout2.toString());
    assertSubString("name=Cuneiform", layout2.toString());
    assertSubString("style=italic", layout2.toString());
    assertSubString("size=19", layout2.toString());
  }

  private void assertSubString(String subString, String fullString)
  {
    int i = fullString.indexOf(subString);
    assertTrue(subString + " not found in " + fullString, i > -1);
  }

  public void testTagRegex() throws Exception
  {
    Matcher matcher = TextPanel.TAG_REGEX.matcher("<abc>123</abc>");
    assertEquals(true, matcher.find());
    assertEquals("<abc>123</abc>", matcher.group(0));
    assertEquals("abc", matcher.group(1));
    assertEquals("123", matcher.group(2));
  }

  public void testUnmatchedTagRegex() throws Exception
  {
    Matcher matcher = TextPanel.TAG_REGEX.matcher("<abc>123</def>");
    assertEquals(false, matcher.find());
  }

  public void testMultilineContentCapture() throws Exception
  {
    Matcher matcher = TextPanel.TAG_REGEX.matcher("<abc>123\n456</abc>");
    assertEquals(true, matcher.find());
    assertEquals("123\n456", matcher.group(2));
  }

  public void testChangingTestRequiresUpdates() throws Exception
  {
    parent.doLayout();
    assertFalse(panel.needsLayout());
    assertFalse(parent.needsLayout());

    panel.setText("New Text");

    assertEquals(true, panel.needsLayout());
    assertEquals(true, parent.needsLayout());        
  }

  public void testLayoutCausesDirtyRegion() throws Exception
  {
    panel.doLayout();

    ArrayList<Rectangle> list = new ArrayList<Rectangle>();
    root.getAndClearDirtyRegions(list);
    assertEquals(1, list.size());
    assertEquals(panel.getAbsoluteBounds(), list.get(0));
  }
  
  public void testResizesTextWhenSizeChanges() throws Exception
  {
    panel.setText("Some really long text so that there are multiple lines requiring layout when the size changes.");
    panel.doLayout();

    int originalHeight = panel.getHeight();

    parent.setSize(200, 200);
    panel.doLayout();

    int newHeight = panel.getHeight();

    assertEquals(true, 200 - panel.getWidth() < 100 );
    assertEquals(true, newHeight < originalHeight);
  }
  
  public void testParentSizeChangesAlwaysRequiresLayout() throws Exception
  {
    panel.resetLayout();
    assertEquals(false, panel.needsLayout());

    panel.consumableAreaChanged();

    assertEquals(true, panel.needsLayout());
  }
}


