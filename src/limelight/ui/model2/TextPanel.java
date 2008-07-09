package limelight.ui.model2;

import limelight.util.Colors;
import limelight.util.Util;
import limelight.util.Box;
import limelight.util.Aligner;
import limelight.ui.api.*;
import limelight.ui.Panel;
import limelight.styles.Style;

import java.awt.*;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedString;
import java.util.LinkedList;

public class TextPanel extends BasePanel
{
  public static double widthPadding = 2.0; // The text measuerments aren't always quite right.  This helps.

  private String text;
  private PropablePanel panel;
  private double consumedHeight;
  private double consumedWidth;
  private LinkedList<TextLayout> lines;
  private Graphics2D graphics;
  private boolean textChanged;
  private boolean compiled;

  //TODO MDM panel is not really needed here.  It's the same as parent.
  public TextPanel(PropablePanel panel, String text)
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

  public void paintOn(Graphics2D graphics)
  {   
    Aligner aligner = createAligner();
    graphics.setColor(Colors.resolve(getStyle().getTextColor()));
    float y = 0;
    if(lines == null)
      return;
    for (TextLayout textLayout : lines)
    {
      y += textLayout.getAscent();
      textLayout.draw(graphics, aligner.startingX(textLayout.getBounds().getWidth()), y);
      y += textLayout.getDescent() + textLayout.getLeading();
    }
  }

  public Style getStyle()
  {
    return panel.getStyle();
  }

  public void doLayout()
  {
    if(!compiled || textChanged())
    {
      buildLines();
      calculateDimentions();
      compiled = true;
    }
    snapToSize();
  }

  public void snapToSize()
  {
    setSize((int)(consumedWidth + 0.5), (int)(consumedHeight + 0.5));
  }

  private Aligner createAligner()
  {
    return new Aligner(new Box(0, 0, getWidth(), getHeight()), getStyle().getHorizontalAlignment(), getStyle().getVerticalAlignment());
  }

  private void buildLines()
  {
    lines = new LinkedList<TextLayout>();
    if(text != null && text.length() > 0)
    {
      String[] paragraphs = text.split("\n");
      Font font = limelight.alt_ui.FontFactory.instance.createFont(getStyle());
      for (String paragraph : paragraphs)
      {
        if(paragraph.length() != 0)
        {
          AttributedString aText = new AttributedString(paragraph);
          aText.addAttribute(TextAttribute.FONT, font);
          LineBreakMeasurer lbm = new LineBreakMeasurer(aText.getIterator(), getGraphics().getFontRenderContext());
          while (lbm.getPosition() < paragraph.length())
          {
            //TODO MDM - Wow! This is inefficient. The getChildConsumableArea has to be calculated every time!
            float width1 = (float) panel.getChildConsumableArea().width;
            TextLayout layout = lbm.nextLayout(width1);
            lines.add(layout);
          }
        }
        else
          lines.add(new TextLayout(" ", font, graphics.getFontRenderContext()));
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
      if(lineWidth > consumedWidth)
        consumedWidth = lineWidth;
    }
  }

  public void setGraphics(Graphics graphics)
  {
    this.graphics = (Graphics2D)graphics;
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
}
