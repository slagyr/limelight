//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.styles.Style;
import limelight.ui.Panel;
import limelight.ui.api.Prop;
import limelight.ui.api.PropablePanel;
import limelight.ui.api.Scene;
import limelight.util.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.*;
import java.util.List;

public class TextPanel extends BasePanel
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
  private List<StyledString> textChunks = new LinkedList<StyledString>();

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
    graphics.setColor(getTextColorFromStyle(getStyle()));
    float y = 0;
    if(lines == null)
      return;
    synchronized(this)
    {
      for(TextLayout textLayout : lines)
      {
        y += textLayout.getAscent();
        int x = getStyle().getCompiledHorizontalAlignment().getX((int)widthOf(textLayout), new Box(0, 0, getWidth(), getHeight()));
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
//    compiled = true;
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
      LinkedList<StyledText> styledParagraph = parser.parse(text);

      Font font = getFontFromStyle(getStyle());
      Font defaultFont = font;
      Color color = getTextColorFromStyle(getStyle());
      Color defaultColor = color;

      boolean lastUsedCustomFont = false;
      for (StyledText styledLine : styledParagraph)
      {
        String line = styledLine.getText();
        String tagName = styledLine.getStyle();

        if(!Util.equal(tagName,"default"))
        {
          Style tagStyle = getStyleFromTag(tagName);

          if(tagStyle != null)
          {
            font = getFontFromStyle(tagStyle);
            color = getTextColorFromStyle(tagStyle);
            lastUsedCustomFont = true;
          }
          else
          {
            // unrecognized style tag
            font = defaultFont;
            color = defaultColor;
          }
        }
        else if (lastUsedCustomFont)
        {
          font = defaultFont;
          color = defaultColor;
          lastUsedCustomFont = false;
        }
        addTextChunk(font, line, color);
      }
      closeParagraph();
      addLines();
    }
  }

  private Color getTextColorFromStyle(Style tagStyle)
  {
    return tagStyle.getCompiledTextColor().getColor();
  }

  private Font getFontFromStyle(Style style)
  {
    return new Font(style.getCompiledFontFace().getValue(), style.getCompiledFontStyle().toInt(), style.getCompiledFontSize().getValue());
  }

  private Style getStyleFromTag(String tagName)
  {
    Prop prop = ((PropablePanel) getPanel()).getProp();
    Scene scene = prop.getScene();
    Map styles = scene.getStyles();
    Style tagStyle = (Style) styles.get(tagName);
    return tagStyle;
  }

  private void closeParagraph()
  {
    Font font = getFontFromStyle(getStyle());
    textChunks.add(new StyledString(font, "\n", new Color(0, 0, 0, 0)));
  }

  private void addTextChunk(Font font, String chunk, Color color)
  {
    if(chunk.length() == 0)
    {
      chunk = " ";
    }
    textChunks.add(new StyledString(font, chunk, color));
  }

  private synchronized void addLines()
  {
    AttributedString aText = prepareAttributedString();
    AttributedCharacterIterator styledTextIterator = aText.getIterator();

    List<Integer> newlineLocations = getNewlineLocations(styledTextIterator);

    LineBreakMeasurer lbm = new LineBreakMeasurer(styledTextIterator, getRenderContext());
    boolean moreCharactersExist = true;
    int currentNewline = 0;
    int end = styledTextIterator.getEndIndex();
    while (lbm.getPosition() < end && moreCharactersExist)
    {
      boolean shouldEndLine = false;

      while(!shouldEndLine)
      {
        float width1 = (float) consumableArea.width;
        TextLayout layout = lbm.nextLayout(width1, newlineLocations.get(currentNewline) + 1, false);

        if (layout != null)
        {
          lines.add(layout);
        }
        else
        {
          shouldEndLine = true;
        }

        if (lbm.getPosition() == newlineLocations.get(currentNewline) + 1)
        {
          currentNewline += 1;
          shouldEndLine = true;
        }
        
        if (lbm.getPosition() == styledTextIterator.getEndIndex())
        {
          shouldEndLine = true;
          moreCharactersExist = false;
        }
      }
    }
  }

  private List<Integer> getNewlineLocations(AttributedCharacterIterator styledTextIterator)
  {
    List<Integer> newlineLocations = new ArrayList<Integer>();
    for (char c = styledTextIterator.first(); c != AttributedCharacterIterator.DONE; c = styledTextIterator.next())
    {
      if (c == '\n')
      {
        newlineLocations.add(styledTextIterator.getIndex());
      }
    }
    return newlineLocations;
  }

  private AttributedString prepareAttributedString()
  {
    StringBuffer buf = new StringBuffer();
    List<Integer> fontIndexes = new ArrayList<Integer>();
    List<Font> fonts = new ArrayList<Font>();
    List<Color> colors = new ArrayList<Color>();

    int i = 0;
    for (StyledString textChunk : textChunks)
    {
      buf.append(textChunk.text);
      fontIndexes.add(i);
      fonts.add(textChunk.font);
      colors.add(textChunk.color);
      i = i + textChunk.text.length();
    }

    AttributedString aText = new AttributedString(buf.toString());
    for (int fontIndex = 0; fontIndex < fonts.size(); fontIndex++)
    {
      int startIndex = fontIndexes.get(fontIndex);
      int endIndex;
      if(fontIndex + 1 == fonts.size())
        endIndex = buf.length();
      else
        endIndex = fontIndexes.get(fontIndex + 1);
      aText.addAttribute(TextAttribute.FONT, fonts.get(fontIndex), startIndex, endIndex);
      aText.addAttribute(TextAttribute.FOREGROUND, colors.get(fontIndex), startIndex, endIndex);
    }
    return aText;
  }

  public List<StyledString> getTextChunks()
  {
    return textChunks;
  }

  public FontRenderContext getRenderContext()
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

  protected class StyledString
  {
    protected String text;
    protected Font font;
    protected Color color;

    private StyledString(Font font, String text, Color color)
    {
      this.font = font;
      this.text = text;
      this.color = color;
    }

    public int getCharacterCount()
    {
      return text.length();
    }

    public String toString()
    {
      return text + "(font: " + font + ", color: " + color + ")";
    }
  }
}
