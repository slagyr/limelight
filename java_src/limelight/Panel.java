package limelight;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.*;
import java.text.*;
import java.io.*;
import java.util.*;

public class Panel extends JPanel
{
	private Block block;
	private BlockLayout layout;

	public Panel(Block owner)
	{
		this.block = owner;
		setOpaque(false);
		layout = new BlockLayout(this);
		setLayout(layout);
	}

	public Block getBlock()
	{
		return block;
	}

	public void paint(Graphics graphics)
	{
		super.paint(graphics);
	}

	protected void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		paintComponent((Graphics2D)graphics);
	}

	protected void paintBorder(Graphics graphics)
	{
		super.paintBorder(graphics);
		paintBorder((Graphics2D)graphics);
	}

	public void paintComponent(Graphics2D graphics)
	{
		paintBackground(graphics);
		paintText(graphics);
	}

	private void paintText(Graphics2D graphics)
	{
		if(block.getText() != null)
		{
			Rectangle usableArea = block.getRectangleInsidePadding();
			Graphics2D newGraphics = (Graphics2D)graphics.create(usableArea.x, usableArea.y, usableArea.width, usableArea.height);
			Aligner aligner = new Aligner(new Rectangle(newGraphics.getClipBounds()), block.getHorizontalAlignment(), block.getVerticalAlignment());

			newGraphics.setColor(block.getTextColor());

			LinkedList<TextLayout> lines = buildTextLines(newGraphics, usableArea, aligner);

			int y = aligner.startingY();// - (int)((TextLayout)lines.get(0)).getDescent();;
			for(Iterator iterator = lines.iterator(); iterator.hasNext();)
			{
				TextLayout textLayout = (TextLayout) iterator.next();
				y += textLayout.getAscent() + textLayout.getDescent() + textLayout.getLeading();
				textLayout.draw(newGraphics, aligner.startingX(textLayout.getBounds().getWidth()), y);
			}
		}
	}

	private LinkedList<TextLayout> buildTextLines(Graphics2D newGraphics, Rectangle usableArea, Aligner aligner)
	{
		LinkedList<TextLayout> lines = new LinkedList<TextLayout>();
		String[] paragraphs = block.getText().split("\n");
		for(int i = 0; i < paragraphs.length; i++)
		{
			String paragraph = paragraphs[i];
			if(paragraph.length() != 0)
			{
				AttributedString aText = new AttributedString(paragraph);
				aText.addAttribute(TextAttribute.FONT, createFont());
				LineBreakMeasurer lbm = new LineBreakMeasurer(aText.getIterator(), newGraphics.getFontRenderContext());
				while(lbm.getPosition() < paragraph.length())
				{
					TextLayout layout = lbm.nextLayout((float)usableArea.getWidth());
					lines.add(layout);
					aligner.addConsumedHeight(layout.getAscent() + layout.getDescent() + layout.getLeading());
				}
			}
			else
				lines.add(new TextLayout("", createFont(), newGraphics.getFontRenderContext()));
		}
		return lines;
	}

	public void paintBackground(Graphics2D graphics)
	{
		if(block.getBackgroundImage() != null)
		{
			Rectangle r = block.getRectangleInsideBorders();
			try
			{
				Image image = ImageIO.read(new File(block.getBackgroundImage()));
				Graphics2D borderedGraphics = (Graphics2D)graphics.create(r.x, r.y, r.width, r.height); 
				ImageFillStrategies.get(block.getBackgroundImageFillStrategy()).fill(borderedGraphics, image);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void paintBorder(Graphics2D graphics)
	{
		Pen pen = new Pen(graphics);

		Rectangle r = block.getRectangleInsideMargins();
		r.shave(block.getTopBorderWidth() / 2, block.getRightBorderWidth() / 2 + 1, block.getBottomBorderWidth() / 2 + 1, block.getLeftBorderWidth() / 2);

		if(block.getTopBorderWidth() > 0)
			pen.withColor(block.getTopBorderColor()).withStroke(block.getTopBorderWidth()).drawLine(r.left(), r.top(),r. right(), r.top());
		if(block.getRightBorderWidth() > 0)
			pen.withColor(block.getRightBorderColor()).withStroke(block.getRightBorderWidth()).drawLine(r.right(), r.top(), r.right(), r.bottom());
		if(block.getBottomBorderWidth() > 0)
			pen.withColor(block.getBottomBorderColor()).withStroke(block.getBottomBorderWidth()).drawLine(r.right(), r.bottom(), r.left(), r.bottom());
		if(block.getLeftBorderWidth() > 0)
			pen.withColor(block.getLeftBorderColor()).withStroke(block.getLeftBorderWidth()).drawLine(r.left(), r.bottom(), r.left(), r.top());

//		int middle = panel.getHeight() / 2;
//		int center = panel.getWidth() / 2;
//		pen.withColor(Color.cyan).withStroke(5).drawLine(left - 100, middle, right + 100, middle);
//		pen.withColor(Color.cyan).withStroke(5).drawLine(center, top - 100, center, bottom + 100);
	}

	public Font createFont()
	{
		int style = 0;
		if(block.getFontStyle() != null && block.getFontStyle().indexOf("bold") != -1)
			style |= Font.BOLD;
		if(block.getFontStyle() != null && block.getFontStyle().indexOf("italic") != -1)
			style |= Font.ITALIC;

		String face = block.getFontFace() == null ? "Arial" : block.getFontFace();
		int size = block.getFontSize() <= 0 ? 10 : block.getFontSize();

		return new Font(face, style, size);
	}
}
