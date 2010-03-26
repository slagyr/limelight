//- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.styles.RichStyle;
import limelight.styles.Style;
import limelight.styles.StyleDescriptor;
import limelight.styles.StyleObserver;
import limelight.styles.abstrstyling.StyleAttribute;
import limelight.ui.Panel;
import limelight.ui.api.Prop;
import limelight.ui.api.PropablePanel;
import limelight.ui.api.Scene;
import limelight.ui.text.StyledText;
import limelight.ui.text.StyledTextParser;
import limelight.util.Box;
import limelight.util.Util;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.*;
import java.util.List;

public class TextPanel extends BasePanel implements StyleObserver
{
  private String text;
  private PropablePanel panel;
  private double consumedHeight;
  private double consumedWidth;
  private Graphics2D graphics;
  private boolean textChanged;
  public static FontRenderContext staticFontRenderingContext;
  private Box consumableArea;

  private LinkedList<TextLayout> lines;
  private List<StyledText> textChunks;

  //TODO MDM panel is not really needed here.  It's the same as parent.
  public TextPanel(PropablePanel panel, String text)
  {
    this.panel = panel;
    this.text = text;
    markAsNeedingLayout();
  }

  public String getText()
  {
    return text;
  }

  public void setText(String text)
  {
    boolean differentText = !Util.equal(text, this.text);
    if(!needsLayout() && differentText)
      markAsNeedingLayout();
    this.text = text;
    if(differentText)
    {
      markAsNeedingLayout();
      propagateSizeChangeUp(getParent());
      getParent().markAsNeedingLayout();
    }
  }

  public Panel getPanel()
  {
    return panel;
  }

  public void paintOn(Graphics2D graphics)
  {
    graphics.setColor(getStyle().getCompiledTextColor().getColor());
    float y = 0;
    if(lines == null)
      return;
    synchronized(this)
    {
      for(TextLayout textLayout : lines)
      {
        y += textLayout.getAscent();
        int x = getStyle().getCompiledHorizontalAlignment().getX((int) widthOf(textLayout), new Box(0, 0, getWidth(), getHeight()));
        textLayout.draw(graphics, x, y);
        y += textLayout.getDescent() + textLayout.getLeading();
      }
    }
  }

  public Style getStyle()
  {
    return panel.getStyle();
  }

  public void doLayout()
  {
    TextPanelLayout.instance.doLayout(this);
  }

  public Layout getDefaultLayout()
  {
    return TextPanelLayout.instance;
  }

  public void consumableAreaChanged()
  {
    markAsNeedingLayout();
  }

  public void compile()
  {
    buildLines();
    calculateDimensions();
    flushChanges();
    snapToSize();
    markAsDirty();
  }

  public void snapToSize()
  {
    setSize((int) (consumedWidth + 0.5), (int) (consumedHeight + 0.5));
  }

  public synchronized void buildLines()
  {
    consumableArea = panel.getChildConsumableArea();
    lines = new LinkedList<TextLayout>();

    if(text != null && text.length() > 0)
    {
      StyledTextParser parser = new StyledTextParser();
      textChunks = parser.parse(text);

      // TODO MDM Need a better way to get the styles
      Prop prop = ((PropablePanel) getPanel()).getProp();
      Scene scene = prop.getScene();
      Map<String, Style> styleMap = scene.getStyles();
      for(StyledText styledText : textChunks)
        styledText.applyStyles(styleMap, (RichStyle) getStyle(), this);
      // TODO MDM StyleObservers may cause a memory leak.  Styles keep track of panels that are no longer used?
      // TODO MDM Need to add sandbox for testing the dynaming updating of test style changes

      addLines();
    }
  }

  private Font getFontFromStyle(Style style)
  {
    String fontFace = style.getCompiledFontFace().getValue();
    int fontStyle = style.getCompiledFontStyle().toInt();
    int fontSize = style.getCompiledFontSize().getValue();
    return new Font(fontFace, fontStyle, fontSize);
  }

  protected Style getStyleFromTag(String tagName)
  {
    Prop prop = ((PropablePanel) getPanel()).getProp();
    Scene scene = prop.getScene();
    Map<String, Style> styles = scene.getStyles();
    return styles.get(tagName);
  }

  private void closeParagraph()
  {
    StyledText text1 = new StyledText("\n");
    text1.getStyle().addExtension((RichStyle) getStyle());
    textChunks.add(text1);
  }

  private synchronized void addLines()
  {
    AttributedString aText = prepareAttributedString();
    AttributedCharacterIterator styledTextIterator = aText.getIterator();

    List<Integer> newlineLocations = getNewlineLocations(styledTextIterator);
    LineBreakMeasurer lbm = new LineBreakMeasurer(styledTextIterator, getRenderContext());

    float width = (float) consumableArea.width;

    TextLayout layout;
    int startOfNextLayout;

    int currentLine = 0;
    int endIndex = styledTextIterator.getEndIndex();

    do
    {
      if(currentLine < newlineLocations.size())
        startOfNextLayout = newlineLocations.get(currentLine) + 1;
      else
        startOfNextLayout = endIndex + 1;

      layout = lbm.nextLayout(width, startOfNextLayout, false);

      lines.add(layout);

      if(lbm.getPosition() == startOfNextLayout)
        currentLine += 1;
    }
    while(layout != null && lbm.getPosition() < endIndex);
  }

  private List<Integer> getNewlineLocations(AttributedCharacterIterator styledTextIterator)
  {
    List<Integer> newlineLocations = new ArrayList<Integer>();
    for(char c = styledTextIterator.first(); c != AttributedCharacterIterator.DONE; c = styledTextIterator.next())
    {
      if(c == '\n')
        newlineLocations.add(styledTextIterator.getIndex());
    }
    return newlineLocations;
  }

  private AttributedString prepareAttributedString()
  {
    List<StyledText> textChunks = getTextChunks();

    StringBuffer buf = new StringBuffer();

    for(StyledText textChunk : textChunks)
    {
      buf.append(textChunk.getText());
    }

    AttributedString attributedString = new AttributedString(buf.toString());

    int startIndex = 0;
    int endIndex = 0;

    for(StyledText textChunk : textChunks)
    {
      if(textChunk == textChunks.get(textChunks.size() - 1))
        endIndex = buf.length();
      else
        endIndex = startIndex + textChunk.getText().length();

      Style style = textChunk.getStyle();
      attributedString.addAttribute(TextAttribute.FONT, getFontFromStyle(style), startIndex, endIndex);
      attributedString.addAttribute(TextAttribute.FOREGROUND, style.getCompiledTextColor().getColor(), startIndex, endIndex);

      startIndex += textChunk.getText().length();
    }

    return attributedString;
  }

  public List<StyledText> getTextChunks()
  {
    return textChunks;
  }

  public static FontRenderContext getRenderContext()
  {
    if(staticFontRenderingContext == null)
    {
      AffineTransform affineTransform = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getDefaultTransform();
      staticFontRenderingContext = new FontRenderContext(affineTransform, true, false);
    }
    return staticFontRenderingContext;
  }

  private void calculateDimensions()
  {
    consumedHeight = 0;
    consumedWidth = 0;
    synchronized(this)
    {
      for(TextLayout layout : lines)
      {
        consumedHeight += (layout.getAscent() + layout.getDescent() + layout.getLeading());
        double lineWidth = widthOf(layout);
        if(lineWidth > consumedWidth)
          consumedWidth = lineWidth;
      }
    }
  }

  private double widthOf(TextLayout layout)
  {
    return layout.getBounds().getWidth() + layout.getBounds().getX();
  }

  public void setGraphics(Graphics graphics)
  {
    this.graphics = (Graphics2D) graphics;
  }

  public boolean textChanged()
  {
    return textChanged;
  }

  public void flushChanges()
  {
    textChanged = false;
  }

  public Box getChildConsumableArea()
  {
    return null;
  }

  public Box getBoxInsidePadding()
  {
    return null;
  }

  public Graphics2D getGraphics()
  {
    if(graphics != null)
      return graphics;
    else
      return panel.getGraphics();
  }

  public String toString()
  {
    return "Text: <" + getText() + ">";
  }

  public boolean canBeBuffered()
  {
    return false;
  }

  public List<TextLayout> getLines()
  {
    return lines;
  }

  public void styleChanged(StyleDescriptor descriptor, StyleAttribute value)
  {
    markAsNeedingLayout();
    getParent().markAsNeedingLayout();
    propagateSizeChangeDown();
    propagateSizeChangeUp(getParent().getParent());
  }
}
