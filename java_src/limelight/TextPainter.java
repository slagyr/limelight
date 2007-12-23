package limelight;

import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.font.TextAttribute;
import java.awt.font.LineBreakMeasurer;
import java.util.LinkedList;
import java.text.AttributedString;

public class TextPainter extends Painter
{
  public TextPainter(Panel panel)
  {
    super(panel);
  }

  public void paint(Graphics2D graphics)
	{  
    Style style = getStyle();
    String text = panel.getBlock().getText();
    if(text != null && text.length() > 0)
		{
			Rectangle usableArea = panel.getRectangleInsidePadding();
			Graphics2D newGraphics = (Graphics2D)graphics.create(usableArea.x, usableArea.y, usableArea.width, usableArea.height);
			Aligner aligner = new Aligner(new Rectangle(newGraphics.getClipBounds()), style.getHorizontalAlignment(), style.getVerticalAlignment());

			newGraphics.setColor(Colors.resolve(style.getTextColor()));

			LinkedList<TextLayout> lines = buildTextLines(newGraphics, usableArea, aligner);

			int y = aligner.startingY();// - (int)((TextLayout)lines.get(0)).getDescent();;
			for (TextLayout textLayout : lines)
			{
				y += textLayout.getAscent() + textLayout.getDescent() + textLayout.getLeading();
				textLayout.draw(newGraphics, aligner.startingX(textLayout.getBounds().getWidth()), y);
			}
		}
	}

	private LinkedList<TextLayout> buildTextLines(Graphics2D newGraphics, Rectangle usableArea, Aligner aligner)
	{
		LinkedList<TextLayout> lines = new LinkedList<TextLayout>();
		String[] paragraphs = panel.getBlock().getText().split("\n");
		for (String paragraph : paragraphs)
		{
			if (paragraph.length() != 0)
			{
				AttributedString aText = new AttributedString(paragraph);
				aText.addAttribute(TextAttribute.FONT, panel.createFont());
				LineBreakMeasurer lbm = new LineBreakMeasurer(aText.getIterator(), newGraphics.getFontRenderContext());
				while (lbm.getPosition() < paragraph.length())
				{
					TextLayout layout = lbm.nextLayout((float) usableArea.getWidth());
					lines.add(layout);
					aligner.addConsumedHeight(layout.getAscent() + layout.getDescent() + layout.getLeading());
				}
			}
			else
				lines.add(new TextLayout(" ", panel.createFont(), newGraphics.getFontRenderContext()));
		}
		return lines;
	}
}
