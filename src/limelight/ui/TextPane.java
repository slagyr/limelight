//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui;

import limelight.styles.Style;
import limelight.util.Util;
import limelight.util.Colors;
import limelight.util.FontFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.font.TextAttribute;
import java.awt.font.LineBreakMeasurer;
import java.util.LinkedList;
import java.text.AttributedString;

public class TextPane extends JPanel
{
  public static double widthPadding = 2.0; // The text measuerments aren't always quite right.  This helps.

  private String text;
  private Panel panel;
  private double consumedHeight;
  private double consumedWidth;
  private LinkedList<TextLayout> lines;
  private Graphics2D graphics;
  private boolean textChanged;
  private boolean compiled;
  private Rectangle bounds;

  public TextPane(Panel panel, String text)
  {
    this.panel = panel;
    this.text = text;
  }

  public String getText()
  {
    return text;
  }

  public void setText(String text)
  {
    if(!textChanged && !Util.equal(text, this.text))
      textChanged = true;
    this.text = text;
  }

  public Panel getPanel()
  {
    return panel;
  }

  public limelight.ui.Rectangle getBounds()
  {
    return bounds;
  }

  public void doLayout()
  {
    if(!compiled || textChanged() || fontChanged())
    {
      bounds = panel.getRectangleInsidePadding();
      buildLines();
      calculateDimentions();
      setSize((int)(consumedWidth + 0.5), (int)(consumedHeight + 0.5));
      compiled = true;
    }
  }

  public Dimension getPreferredSize()
  {
    doLayout();
    return getSize();
  }

  public void paint(Graphics aGraphics)
	{
    graphics = (Graphics2D)aGraphics;

    Aligner aligner = createAligner();
    graphics.setColor(Colors.resolve(getStyle().getTextColor()));
    float y = 0;
    for (TextLayout textLayout : lines)
    {
      y += textLayout.getAscent();
      textLayout.draw(graphics, aligner.startingX(textLayout.getBounds().getWidth()), y);
      y += textLayout.getDescent() + textLayout.getLeading();
    }
	}

  private Aligner createAligner()
  {
    return new Aligner(new limelight.ui.Rectangle(0, 0, getWidth(), getHeight()), getStyle().getHorizontalAlignment(), getStyle().getVerticalAlignment());
  }

  private Style getStyle()
  {
    return panel.getStyle();
  }

  private void buildLines()
  {
    lines = new LinkedList<TextLayout>();
    if(text != null && text.length() > 0)
    {
      String[] paragraphs = text.split("\n");
      Font font = FontFactory.instance.createFont(getStyle());
      for (String paragraph : paragraphs)
      {
        if(paragraph.length() != 0)
        {
          AttributedString aText = new AttributedString(paragraph);
          aText.addAttribute(TextAttribute.FONT, font);
          LineBreakMeasurer lbm = new LineBreakMeasurer(aText.getIterator(), getGraphics().getFontRenderContext());
          while (lbm.getPosition() < paragraph.length())
          {
            TextLayout layout = lbm.nextLayout((float) (getBounds().getWidth()));
            lines.add(layout);
          }
        }
        else
          lines.add(new TextLayout(" ", font, getGraphics().getFontRenderContext()));
      }
    }
  }

  private void calculateDimentions()
  {
    consumedHeight = 0;
    consumedWidth = 0;
    for (TextLayout layout : lines)
    {
      consumedHeight += (layout.getAscent() + layout.getDescent() + layout.getLeading());
      double lineWidth = layout.getBounds().getWidth() + widthPadding;
      if(lineWidth > bounds.width)
        lineWidth = bounds.width;
      if(lineWidth > consumedWidth)
        consumedWidth = lineWidth;
    }
  }

  public Graphics2D getGraphics()
  {
    if(graphics != null)
      return graphics;
    return (Graphics2D)super.getGraphics();
  }

  public boolean textChanged()
  {
    return textChanged;
  }

  public void flushChanges()
  {
    textChanged = false;
  }

  private boolean fontChanged()
  {
    Style style = getStyle();
    return style.changed() && (style.changed(Style.FONT_FACE) || style.changed(Style.FONT_SIZE) || style.changed(Style.FONT_STYLE));
  }
}
