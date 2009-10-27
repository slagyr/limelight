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
import java.text.AttributedString;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
  private Box consumableArea;

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
    calculateDimentions();
    compiled = true;
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
      String[] paragraphs = text.split("\n");
      Style style = getStyle();
      Font font = new Font(style.getCompiledFontFace().getValue(), style.getCompiledFontStyle().toInt(), style.getCompiledFontSize().getValue());
      Font defaultFont = font;
      for(String paragraph : paragraphs)
      {
        StyledTextParser parser = new StyledTextParser();
        LinkedList<StyledText> styledParagraph = parser.parse(paragraph);

        for (StyledText styledLine : styledParagraph)
        {
          String line = styledLine.getText();
          String tagName = styledLine.getStyle();

          if(!Util.equal(tagName,"default"))
          {
            Prop prop = ((PropablePanel) getPanel()).getProp();
            Scene scene = prop.getScene();
            Map styles = scene.getStyles();
            Style tagStyle = (Style) styles.get(tagName);
            if(tagStyle != null)
              font = new Font(tagStyle.getCompiledFontFace().getValue(), tagStyle.getCompiledFontStyle().toInt(), tagStyle.getCompiledFontSize().getValue());
            else
              System.out.println("no style for tag: " + tagName);
          }
          addLine(font, line);
        }

        font = defaultFont;
      }
    }
  }

  private void addLine(Font font, String line)
  {
    if(line.length() != 0)
    {
      AttributedString aText = new AttributedString(line);
      aText.addAttribute(TextAttribute.FONT, font);
      LineBreakMeasurer lbm = new LineBreakMeasurer(aText.getIterator(), getRenderContext());
      while(lbm.getPosition() < line.length())
      {
        float width1 = (float) consumableArea.width;
        TextLayout layout = lbm.nextLayout(width1);
        lines.add(layout);
      }
    }
    else
    {
      lines.add(new TextLayout(" ", font, getRenderContext()));
    }
  }

//  private FontRenderContext getRenderContext()
//  {
//    if(renderContext == null)
//    {
//      if(staticFontRenderingContext != null)
//        renderContext = staticFontRenderingContext;
//      else
//      {
//        Graphics2D graphics = getRoot().getGraphics();
//        renderContext = graphics.getFontRenderContext();
//      }
//    }
//    return renderContext;
//  }


  public FontRenderContext getRenderContext()
  {
    if(staticFontRenderingContext == null)
    {
      AffineTransform affineTransform = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getDefaultTransform();
      staticFontRenderingContext = new FontRenderContext(affineTransform, true, false);  
    }
    return staticFontRenderingContext;
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
