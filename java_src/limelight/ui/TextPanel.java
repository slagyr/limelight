package limelight.ui;

import java.awt.*;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedString;
import java.util.LinkedList;
import limelight.Rectangle;

public class TextPanel extends Panel
{
  public static double widthPadding = 2.0; // The text measuerments aren't always quite right.  This helps.

  private String text;
  private BlockPanel panel;
  private double consumedHeight;
  private double consumedWidth;
  private LinkedList<TextLayout> lines;
  private int lastChecksum;
  private Graphics2D graphics;

  public TextPanel(BlockPanel panel, String text)
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

  public BlockPanel getPanel()
  {
    return panel;
  }

  //TODO MDM - Wow! This is inefficient.  Called in a loop below.Ê
  public Rectangle getBounds()
  {
    return panel.getRectangleInsidePadding();
  }

  public void paint(java.awt.Rectangle clip)
  {
    Point absoluteLocation = getAbsoluteLocation();
    graphics = (Graphics2D)getFrame().getGraphics().create(absoluteLocation.x + clip.x, absoluteLocation.y + clip.y, clip.width, clip.height);

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

  public void snapToSize()
  {
    int currentChecksum = panel.checksum();
    if(lastChecksum != currentChecksum)
    {
      lastChecksum = currentChecksum;
      buildLines();
      calculateDimentions();
    }
    setWidth((int)(consumedWidth + 0.5));
    setHeight((int)(consumedHeight + 0.5));
  }

  private Aligner createAligner()
  {
    return new Aligner(new Rectangle(0, 0, getWidth(), getHeight()), getStyle().getHorizontalAlignment(), getStyle().getVerticalAlignment());
  }

  private Style getStyle()
  {
    return panel.getBlock().getStyle();
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

  public void setGraphics(Graphics graphics)
  {
    this.graphics = (Graphics2D)graphics;
  }

  private Graphics2D getGraphics()
  {
    if(graphics == null)
      graphics = (Graphics2D)getFrame().getGraphics();
    return graphics;
  }
}
