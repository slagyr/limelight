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

	public Panel(Block owner)
	{
		this.block = owner;
		setOpaque(false);
		setLayout(new BlockLayout(this));
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
			Rectangle usableArea = getRectangleInsidePadding();
			Graphics2D newGraphics = (Graphics2D)graphics.create(usableArea.x, usableArea.y, usableArea.width, usableArea.height);
			Aligner aligner = new Aligner(new Rectangle(newGraphics.getClipBounds()), block.getStyle().getHorizontalAlignment(), block.getStyle().getVerticalAlignment());

			newGraphics.setColor(Colors.resolve(block.getStyle().getTextColor()));

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
				lines.add(new TextLayout(" ", createFont(), newGraphics.getFontRenderContext()));
		}
		return lines;
	}

	public void paintBackground(Graphics2D graphics)
	{
		if(block.getStyle().getBackgroundImage() != null)
		{
			Rectangle r = getRectangleInsideBorders();
			try
			{
				Image image = ImageIO.read(new File(block.getStyle().getBackgroundImage()));
				Graphics2D borderedGraphics = (Graphics2D)graphics.create(r.x, r.y, r.width, r.height); 
				ImageFillStrategies.get(block.getStyle().getBackgroundImageFillStrategy()).fill(borderedGraphics, image);
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

		Rectangle r = getRectangleInsideMargins();
		r.shave(resolveInt(block.getStyle().getTopBorderWidth()) / 2, resolveInt(block.getStyle().getRightBorderWidth()) / 2 + 1, resolveInt(block.getStyle().getBottomBorderWidth()) / 2 + 1, resolveInt(block.getStyle().getLeftBorderWidth()) / 2);

		if(resolveInt(block.getStyle().getTopBorderWidth()) > 0)
			pen.withColor(Colors.resolve(block.getStyle().getTopBorderColor())).withStroke(resolveInt(block.getStyle().getTopBorderWidth())).drawLine(r.left(), r.top(),r. right(), r.top());
		if(resolveInt(block.getStyle().getRightBorderWidth()) > 0)
			pen.withColor(Colors.resolve(block.getStyle().getRightBorderColor())).withStroke(resolveInt(block.getStyle().getRightBorderWidth())).drawLine(r.right(), r.top(), r.right(), r.bottom());
		if(resolveInt(block.getStyle().getBottomBorderWidth()) > 0)
			pen.withColor(Colors.resolve(block.getStyle().getBottomBorderColor())).withStroke(resolveInt(block.getStyle().getBottomBorderWidth())).drawLine(r.right(), r.bottom(), r.left(), r.bottom());
		if(resolveInt(block.getStyle().getLeftBorderWidth()) > 0)
			pen.withColor(Colors.resolve(block.getStyle().getLeftBorderColor())).withStroke(resolveInt(block.getStyle().getLeftBorderWidth())).drawLine(r.left(), r.bottom(), r.left(), r.top());
	}

	private int resolveInt(String value)
	{
		try
		{
			return Integer.parseInt(value);
		}
		catch(NumberFormatException e)
		{
			return 0;
		}
	}

	public Font createFont()
	{
		int style = 0;
		if(block.getStyle().getFontStyle() != null && block.getStyle().getFontStyle().indexOf("bold") != -1)
			style |= Font.BOLD;
		if(block.getStyle().getFontStyle() != null && block.getStyle().getFontStyle().indexOf("italic") != -1)
			style |= Font.ITALIC;

		String face = block.getStyle().getFontFace() == null ? "Arial" : block.getStyle().getFontFace();
		int size = resolveInt(block.getStyle().getFontSize()) <= 0 ? 10 : resolveInt(block.getStyle().getFontSize());

		return new Font(face, style, size);
	}

	public void snapToDesiredSize()
	{
		Rectangle r = ((Panel)getParent()).getRectangleInsidePadding();
		int width = translateDimension(block.getStyle().getWidth(), r.width);
		int height = translateDimension(block.getStyle().getHeight(), r.height);
		setSize(width, height);
	}

	private int translateDimension(String sizeString, int maxSize)
	{
		if(sizeString.endsWith("%"))
		{
			double percentage = Double.parseDouble(sizeString.substring(0, sizeString.length() - 1));
			return (int)((percentage * 0.01) * (double)maxSize);
		}
		else
		{
			return Integer.parseInt(sizeString);
		}
	}

	public Rectangle getRectangle()
	{
		return new Rectangle(0, 0, getWidth(), getHeight());
	}

	public Rectangle getRectangleInsideMargins()
	{
		Rectangle r = getRectangle();
		r.shave(resolveInt(block.getStyle().getTopMargin()), resolveInt(block.getStyle().getRightMargin()), resolveInt(block.getStyle().getBottomMargin()), resolveInt(block.getStyle().getLeftMargin()));
		return r;
	}

	public Rectangle getRectangleInsideBorders()
	{
		Rectangle r = getRectangleInsideMargins();
		r.shave(resolveInt(block.getStyle().getTopBorderWidth()), resolveInt(block.getStyle().getRightBorderWidth()), resolveInt(block.getStyle().getBottomBorderWidth()), resolveInt(block.getStyle().getLeftBorderWidth()));
		return r;
	}

	public Rectangle getRectangleInsidePadding()
	{
		Rectangle r = getRectangleInsideBorders();
		r.shave(resolveInt(block.getStyle().getTopPadding()), resolveInt(block.getStyle().getRightPadding()), resolveInt(block.getStyle().getBottomPadding()), resolveInt(block.getStyle().getLeftPadding()));
		return r;
	}


}
