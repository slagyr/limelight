//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.styles.RichStyle;
import limelight.styles.Style;
import limelight.styles.StyleObserver;
import limelight.ui.api.MockProp;
import limelight.ui.api.MockScene;
import limelight.ui.text.StyledText;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.font.TextLayout;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class TextPanelTest
{
  private TextPanel panel;
  private Style style;
  private Frame frame;
  private MockPropablePanel parent;
  private ScenePanel root;
  private String defaultFontFace;
  private String defaultFontSize;
  private String defaultFontStyle;
  private Color defaultTextColor;

  @Before
  public void setUp() throws Exception
  {
    parent = new MockPropablePanel();
    parent.setLocation(0, 0);
    parent.setSize(100, 100);
    style = parent.getStyle();
    panel = new TextPanel(parent, "Some Text");
    parent.add(panel);
    root = new ScenePanel(new MockProp());
    root.setFrame(new MockPropFrame());
    root.add(parent);
    style.setTextColor("green");
    parent.prop.scene = new MockScene();

    defaultFontFace = style.getFontFace();
    defaultFontSize = style.getFontSize();
    defaultFontStyle = style.getFontStyle();
    defaultTextColor = style.getCompiledTextColor().getColor();
  }

  @After
  public void tearDown()
  {
    if(frame != null)
      frame.setVisible(false);
  }

  @Test
  public void testConstructor() throws Exception
  {
    assertEquals(parent, panel.getPanel());
    assertEquals("Some Text", panel.getText());
  }

  @Test
  public void preferredSize() throws Exception
  {
    useFrame();
    panel.doLayout();
    assertEquals(59, panel.getWidth());
    assertEquals(14, panel.getHeight());
  }

  @Test
  public void preferredSizeWithMoreText() throws Exception
  {
    useFrame();
    panel.setText("Once upon a time, there was a developer working on a tool called Limelight.", parent);
    panel.doLayout();
    assertEquals(true, panel.getWidth() >= 98 && panel.getWidth() <= 99);
    assertEquals(69, panel.getHeight());
  }

  @Test
  public void preferredSizeWithBigFontSize() throws Exception
  {
    useFrame();
    style.setFontSize("40");
    panel.doLayout();
    assertEquals(80, panel.getWidth());
    assertEquals(138, panel.getHeight());
  }

  @Test
  public void dimensionsWhenLastLineIsLongest() throws Exception
  {
    useFrame();
    panel.setText("1\n2\n3\nlongest", parent);
    panel.doLayout();
    assertEquals(true, panel.getWidth() >= 39 && panel.getWidth() <= 41);
    assertEquals(55, panel.getHeight());
  }

  private void useFrame()
  {
    frame = new Frame();
    frame.setVisible(true);
    panel.setGraphics(frame.getGraphics());
  }

  @Test
  public void textChanged() throws Exception
  {
    assertEquals(false, panel.textChanged());

    panel.setText("Something", parent);
    assertEquals(true, panel.needsLayout());

    panel.resetLayout();
    panel.setText("Something", parent);
    assertEquals(false, panel.needsLayout());

    panel.setText("Something Else", parent);
    assertEquals(true, panel.needsLayout());

    panel.resetLayout();
    assertEquals(false, panel.needsLayout());
  }
  
  @Test
  public void layoutFlushedChangedText() throws Exception
  {
    panel.resetLayout();
    assertEquals(false, panel.needsLayout());

    panel.setText("Something", parent);
    assertEquals(true, panel.needsLayout());

    panel.doLayout();
    assertEquals(false, panel.needsLayout());
  }

  @Test
  public void canBeBuffered() throws Exception
  {
    assertEquals(false, panel.canBeBuffered());
  }

  @Test
  public void buildingLines() throws Exception
  {
    panel.setText("some text", parent);
    panel.buildLines();

    List<TextLayout> lines = panel.getLines();

    assertEquals(1, lines.size());
    TextLayout layout = lines.get(0);
    assertEquals(9, layout.getCharacterCount());
    assertSubString("family=" + defaultFontFace, layout.toString());
    assertSubString("name=" + defaultFontFace, layout.toString());
    assertSubString("size=" + defaultFontSize, layout.toString());
  }

  @Test
  public void stylingAppliedToLine() throws Exception
  {
    createStyles();

    parent.setSize(200, 100);
    panel.setText("<my_style>some text</my_style>", parent);
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

  @Test
  public void observerAddedForLineStyling() throws Exception
  {
    panel.setText("some text", parent);
    panel.buildLines();

    Style style = panel.getTextChunks().get(0).getStyle();
    assertEquals(true, style.hasObserver(panel));
  }

  @Test
  public void multipleStylesAppliedToLine() throws Exception
  {
    createStyles();

    parent.setSize(200, 100);
    panel.setText("<my_style>some </my_style><my_other_style>text</my_other_style>", parent);
    panel.buildLines();

    List<StyledText> chunks = panel.getTextChunks();

    StyledText layout = chunks.get(0);
    assertEquals(5, layout.getText().length());
    assertSubString("family=Helvetica", layout.toString());
    assertSubString("name=Helvetica", layout.toString());
    assertSubString("style=bold", layout.toString());
    assertSubString("size=20", layout.toString());

    StyledText layout2 = chunks.get(1);
    assertEquals(5, layout.getText().length());
    assertSubString("family=Dialog", layout2.toString());
    assertSubString("name=Cuneiform", layout2.toString());
    assertSubString("style=italic", layout2.toString());
    assertSubString("size=19", layout2.toString());
  }

  private void createStyles()
  {
    RichStyle myStyle = new RichStyle();
    root.getStylesStore().put("my_style", myStyle);
    myStyle.setFontFace("Helvetica");
    myStyle.setFontStyle("bold");
    myStyle.setFontSize("20");
    myStyle.setTextColor("red");

    RichStyle myOtherStyle = new RichStyle();
    root.getStylesStore().put("my_other_style", myOtherStyle);
    myOtherStyle.setFontFace("Cuneiform");
    myOtherStyle.setFontStyle("italic");
    myOtherStyle.setFontSize("19");
    myOtherStyle.setTextColor("blue");

    RichStyle sizeOnlyStyle = new RichStyle();
    root.getStylesStore().put("size_only_style", sizeOnlyStyle);
    sizeOnlyStyle.setFontSize("25");
  }

  @Test
  public void interlacedTextAndStyledText()
  {
    createStyles();
    parent.setSize(200, 100);
    panel.setText("This is <my_other_style>some </my_other_style> fantastic <my_style>text</my_style>", parent);
    panel.buildLines();

    List<StyledText> chunks = panel.getTextChunks();
    assertEquals(4, chunks.size());

    StyledText interlacedLayout = chunks.get(2);
    assertNoSubString("name=Cuneiform", interlacedLayout.toString());
    assertNoSubString("size=19", interlacedLayout.toString());
  }

  @Test
  public void unrecognizedInterlacedStyle()
  {
    createStyles();
    parent.setSize(200, 100);
    panel.setText("This is <my_other_style>some </my_other_style><bogus_style>fantastic</bogus_style><my_style>text</my_style>", parent);
    panel.buildLines();

    List<StyledText> chunks = panel.getTextChunks();
    assertEquals(4, chunks.size());

    StyledText interlacedLayout = chunks.get(2);
    assertNoSubString("name=Cuneiform", interlacedLayout.toString());
    assertNoSubString("size=19", interlacedLayout.toString());
  }

  @Test
  public void styledTextOnSameLine()
  {
    createStyles();
    parent.setSize(200, 100);
    panel.setText("This <my_other_style>some </my_other_style> text", parent);
    panel.buildLines();

    List<TextLayout> lines = panel.getLines();
    assertEquals(1, lines.size());

    String onlyLine = lines.get(0).toString();
    assertSubString("name=Cuneiform", onlyLine);
    assertSubString("size=19", onlyLine);
    assertSubString("style=italic", onlyLine);
    assertSubString("name=" + defaultFontFace, onlyLine);
    assertSubString("size=" + defaultFontSize, onlyLine);
    assertSubString("style=" + defaultFontStyle, onlyLine);
  }

  @Test
  public void styledInheritsFromDefault()
  {
    createStyles();
    parent.setSize(200, 100);
    panel.setText("<size_only_style>This some text</size_only_style>", parent);
    panel.buildLines();

    List<TextLayout> lines = panel.getLines();
    assertEquals(1, lines.size());

    String onlyLine = lines.get(0).toString();
    assertSubString("name=" + defaultFontFace, onlyLine);
    assertSubString("size=" + "25", onlyLine);
    assertSubString("style=" + defaultFontStyle, onlyLine);

    StyledText first = panel.getTextChunks().get(0);
    assertEquals(defaultTextColor, first.getColor());
  }

  @Test
  public void styledAcrossLineBreak()
  {
    createStyles();
    parent.setSize(200, 100);
    panel.setText("This <my_other_style>some\n more</my_other_style> text", parent);

    panel.buildLines();

    List<TextLayout> lines = panel.getLines();
    assertEquals(2, lines.size());

    TextLayout first = lines.get(0);
    TextLayout second = lines.get(1);
    assertSubString("name=Cuneiform", first.toString());
    assertSubString("name=" + defaultFontFace, first.toString());
    assertSubString("name=Cuneiform", second.toString());
    assertSubString("name=" + defaultFontFace, second.toString());
  }

  @Test
  public void textColor() throws Exception
  {
    createStyles();
    panel.setText("text <my_other_style>here</my_other_style> man", parent);
    panel.buildLines();

    StyledText first = panel.getTextChunks().get(0);
    assertEquals(defaultTextColor, first.getColor());

    StyledText second = panel.getTextChunks().get(1);
    assertEquals(new Color(0x0000FF), second.getColor());

    StyledText third = panel.getTextChunks().get(2);
    assertEquals(defaultTextColor, third.getColor());
  }

  @Test
  public void textChunksOverwrittenOnCompile() throws Exception
  {
    panel.setText("Here is some original text.", parent);
    panel.buildLines();

    int originalChunks = panel.getTextChunks().size();

    panel.buildLines();

    assertEquals(originalChunks, panel.getTextChunks().size());
  }

  private void assertSubString(String subString, String fullString)
  {
    int i = fullString.indexOf(subString);
    assertEquals(subString + " not found in " + fullString, true, i > -1);
  }

  private void assertNoSubString(String subString, String fullString)
  {
    int i = fullString.indexOf(subString);
    assertEquals(subString + " found in " + fullString, false, i > -1);
  }

  @Test
  public void changingTestRequiresUpdates() throws Exception
  {
    parent.doLayout();
    assertEquals(false, panel.needsLayout());
    assertEquals(false, parent.needsLayout());

    panel.setText("New Text", parent);

    assertEquals(true, panel.needsLayout());
    assertEquals(true, parent.needsLayout());        
  }

  @Test
  public void layoutCausesDirtyRegion() throws Exception
  {
    panel.doLayout();

    ArrayList<Rectangle> list = new ArrayList<Rectangle>();
    root.getAndClearDirtyRegions(list);
    assertEquals(1, list.size());
    assertEquals(panel.getAbsoluteBounds(), list.get(0));
  }
  
  @Test
  public void resizesTextWhenSizeChanges() throws Exception
  {
    panel.setText("Some really long text so that there are multiple lines requiring layout when the size changes.", parent);
    panel.doLayout();

    int originalHeight = panel.getHeight();
    parent.setSize(400, 200);
    panel.doLayout();

    int newHeight = panel.getHeight();

    assertEquals(true, 200 - panel.getWidth() < 100 );
    assertEquals(true, newHeight < originalHeight);
  }
  
  @Test
  public void parentSizeChangesAlwaysRequiresLayout() throws Exception
  {
    panel.resetLayout();
    assertEquals(false, panel.needsLayout());

    panel.consumableAreaChanged();

    assertEquals(true, panel.needsLayout());
  }
  
  @Test
  public void teardownStyledTextBeforeDiscarding() throws Exception
  {
    panel.setText("Original Text", parent);
    panel.doLayout();
    List<StyleObserver> observers = panel.getStyle().getObservers();
    assertEquals(1, observers.size());
    StyleObserver observer = observers.get(0);

    panel.doLayout();
    List<StyleObserver> newObservers = panel.getStyle().getObservers();
    assertEquals(1, newObservers.size());
    StyleObserver newObserver = newObservers.get(0);
    assertNotSame(newObserver, observer);
  }
}


