package limelight;

import limelight.ui.*;

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
  private int lastChecksum;
  private Graphics2D graphics;

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
    this.text = text;
  }

  public Panel getPanel()
  {
    return panel;
  }

  public limelight.ui.Rectangle getBounds()
  {
    return panel.getRectangleInsidePadding();
  }

  public Dimension getPreferredSize()
  {
    graphics = (Graphics2D)getGraphics();
    compile();
    return new Dimension((int)(consumedWidth + 0.5), (int)(consumedHeight + 0.5));
  }

  public void paint(Graphics aGraphics)
	{
    graphics = (Graphics2D)aGraphics;
    compile();

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
    return panel.getBlock().getStyle();
  }

  private void compile()
	{
    int currentChecksum = panel.checksum();
    if(lastChecksum != currentChecksum)
    {
      lastChecksum = currentChecksum;
      buildLines();
      calculateDimentions();
    }
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
          LineBreakMeasurer lbm = new LineBreakMeasurer(aText.getIterator(), graphics.getFontRenderContext());
          while (lbm.getPosition() < paragraph.length())
          {
            TextLayout layout = lbm.nextLayout((float) getBounds().getWidth());
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

  public Graphics getGraphics()
  {
    if(graphics != null)
      return graphics;
    return super.getGraphics();
  }
}
