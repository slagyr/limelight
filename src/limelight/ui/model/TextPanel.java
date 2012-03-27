//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.Log;
import limelight.styles.RichStyle;
import limelight.styles.ScreenableStyle;
import limelight.styles.StyleAttribute;
import limelight.styles.StyleObserver;
import limelight.styles.abstrstyling.StyleValue;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TextPanel extends PanelBase implements StyleObserver, TextAccessor
{
  private String text;
  private Prop panel;
  private double consumedHeight;
  private double consumedWidth;
  private Graphics2D graphics;
  private boolean textChanged;
  public static FontRenderContext staticFontRenderingContext;
  private Box consumableArea;

  private LinkedList<TextLayout> lines;
  private List<StyledText> textChunks;

  //TODO MDM panel is not really needed here.  It's the same as parent.
  public TextPanel(Prop panel)
  {
    this.panel = panel;
  }

  public String getText()
  {
    return text;
  }

  public void setText(String text, Prop panel)
  {
    boolean differentText = !Util.equal(text, this.text);
//    if(!needsLayout() && differentText)
//      markAsNeedingLayout();
    this.text = text;
    if(differentText)
    {
      markAsNeedingLayout();
      doPropagateSizeChangeUp(getParent());
      getParent().markAsNeedingLayout();
    }
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

  public ScreenableStyle getStyle()
  {
    return panel.getStyle();
  }

  public Layout getDefaultLayout()
  {
    return TextPanelLayout.instance;
  }

  public void compile()
  {
    cleanup();
    buildLines();
    calculateDimensions();
    flushChanges();
    snapToSize();
    markAsDirty();
  }

  private void cleanup()
  {
    if(textChunks == null)
      return;

    for(StyledText chunk : textChunks)
      chunk.teardownStyles();

    textChunks = null;
  }

  public void snapToSize()
  {
    setSize((int) (consumedWidth + 0.5), (int) (consumedHeight + 0.5));
  }

  public synchronized void buildLines()
  {
    consumableArea = panel.getChildConsumableBounds();
    lines = new LinkedList<TextLayout>();

    if(text != null && text.length() > 0)
    {
      StyledTextParser parser = new StyledTextParser();
      textChunks = parser.parse(text);

      Map<String, RichStyle> styleMap = getRoot().getStyles();
      for(StyledText styledText : textChunks)
        styledText.setupStyles(styleMap, getStyle(), this);
      // TODO MDM StyleObservers may cause a memory leak.  Styles keep track of panels that are no longer used?

      addLines();
    }
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

    StringBuilder buf = new StringBuilder();

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

      attributedString.addAttribute(TextAttribute.FONT, textChunk.getFont(), startIndex, endIndex);
      attributedString.addAttribute(TextAttribute.FOREGROUND, textChunk.getColor(), startIndex, endIndex);

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

  public Graphics2D getGraphics()
  {
    if(graphics != null)
      return graphics;
    else
      return panel.getGraphics();
  }

  public String toString()
  {
    return "TextPanel: <" + getText() + ">";
  }

  public boolean canBeBuffered()
  {
    return false;
  }

  public List<TextLayout> getLines()
  {
    return lines;
  }

  public void styleChanged(StyleAttribute attribute, StyleValue value)
  {
    markAsNeedingLayout();
    getParent().markAsNeedingLayout();
    doPropagateSizeChangeUp(getParent().getParent());
  }
}
