//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.styles.Style;
import limelight.ui.Panel;
import limelight.ui.api.Prop;
import limelight.ui.api.PropablePanel;
import limelight.ui.api.Scene;
import limelight.util.*;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedString;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextPanel extends BasePanel
{
  public static final Pattern TAG_REGEX = Pattern.compile("<(\\w+)>(.*)</(\\1)>", Pattern.MULTILINE | Pattern.DOTALL);
  public static double widthPadding = 0; // The text measuerments aren't always quite right.  This helps.
  //TODO widthPadding might not be needed any more... Was not calculating width the same way in two places.

  private String text;
  private PropablePanel panel;
  private double consumedHeight;
  private double consumedWidth;
  private LinkedList<TextLayout> lines;
  private Graphics2D graphics;
  private boolean textChanged;
  private boolean compiled;
  private FontRenderContext renderContext;
  public static FontRenderContext staticFontRenderingContext;

  //TODO MDM panel is not really needed here.  It's the same as parent.
  public TextPanel(PropablePanel panel, String text)
  {
    this.panel = panel;
    this.text = text;
    setNeedsLayout();
  }

  public String getText()
  {
    return text;
  }

  public void setText(String text)
  {
    boolean differentText = !Util.equal(text, this.text);
    if(!textChanged && differentText)
      textChanged = true;
    this.text = text;
    if(differentText)
    {
      setNeedsLayout();
      propogateSizeChange(getParent());
      getParent().setNeedsLayout();
    }
  }

  public Panel getPanel()
  {
    return panel;
  }

  public void paintOn(Graphics2D graphics)
  {
    Aligner aligner = createAligner();
    graphics.setColor(getStyle().getCompiledTextColor().getColor());
    float y = 0;
    if(lines == null)
      return;
    synchronized(this)
    {
      for(TextLayout textLayout : lines)
      {
        y += textLayout.getAscent();
        textLayout.draw(graphics, aligner.startingX(widthOf(textLayout)), y);
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
    super.doLayout();
    if(!compiled || textChanged())
    {
      try
      {
        buildLines();
        calculateDimentions();
        compiled = true;
      }
      catch(Exception e)
      {
        //okay
      }
    }
    snapToSize();
    markAsDirty();
  }

  public void snapToSize()
  {
    setSize((int) (consumedWidth + 0.5), (int) (consumedHeight + 0.5));
  }

  private Aligner createAligner()
  {
    return new Aligner(new Box(0, 0, getWidth(), getHeight()), getStyle().getCompiledHorizontalAlignment().getAlignment(), getStyle().getCompiledVerticalAlignment().getAlignment());
  }

  public void buildLines()
  {
    synchronized(this)
    {
      lines = new LinkedList<TextLayout>();
      if(text != null && text.length() > 0)
      {
        String[] paragraphs = text.split("\n");
        Style style = getStyle();
        Font font = new Font(style.getCompiledFontFace().getValue(), style.getCompiledFontStyle().toInt(), style.getCompiledFontSize().getValue());
        for(String paragraph : paragraphs)
        {
          Matcher matcher = TAG_REGEX.matcher(paragraph);
          if(matcher.find())
          {
            paragraph = matcher.group(2);
            String tagName = matcher.group(1);
            Prop prop = ((PropablePanel) getPanel()).getProp();
            Scene scene = prop.getScene();
            Map styles = scene.getStyles();
            Style tagStyle = (Style) styles.get(tagName);
            if(tagStyle != null)
              font = new Font(tagStyle.getCompiledFontFace().getValue(), tagStyle.getCompiledFontStyle().toInt(), tagStyle.getCompiledFontSize().getValue());
            else
              System.out.println("no style for tag: " + tagName);
          }

          if(paragraph.length() != 0)
          {
            AttributedString aText = new AttributedString(paragraph);
            aText.addAttribute(TextAttribute.FONT, font);
            LineBreakMeasurer lbm = new LineBreakMeasurer(aText.getIterator(), getRenderContext());
            while(lbm.getPosition() < paragraph.length())
            {
              float width1 = (float) panel.getChildConsumableArea().width;
              TextLayout layout = lbm.nextLayout(width1);
              lines.add(layout);
            }
          }
          else
          {
            lines.add(new TextLayout(" ", font, getRenderContext()));
          }
        }
      }
    }
  }

  private FontRenderContext getRenderContext()
  {
    if(renderContext == null)
    {
      if(staticFontRenderingContext != null)
        renderContext = staticFontRenderingContext;
      else
        renderContext = getRoot().getGraphics().getFontRenderContext();
    }
    return renderContext;
  }

  private void calculateDimentions()
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
    return layout.getBounds().getWidth() + layout.getBounds().getX() + widthPadding;
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

  public void setRenderContext(FontRenderContext renderContext)
  {
    this.renderContext = renderContext;
  }
}
