package limelight;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.font.*;
import java.text.*;
import java.io.*;
import java.util.*;

public class Panel extends JPanel
{
	private Block block;
	private BufferedImage buffer;

	public Panel(Block owner)
	{
		this.block = owner;
		setOpaque(false);
		setDoubleBuffered(false);
		setLayout(new BlockLayout(this));
	}

	public Block getBlock()
	{
		return block;
	}

	int paints = 0;
	boolean badName = false;

	public void paint(Graphics graphics)
	{
		if(buffer == null || shouldRepaintBuffer(graphics))
		{
			buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D bufferGraphics = (Graphics2D)buffer.getGraphics();

			paintComponent(bufferGraphics);
			paintBorder(bufferGraphics);
		}

		Composite	originalComposite = ((Graphics2D)graphics).getComposite();
		if(block.getStyle().getTransparency() != null)
		{
			float transparency = 1f - (Integer.parseInt(block.getStyle().getTransparency()) / 100.0f);
			Composite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency);
			((Graphics2D)graphics).setComposite(alphaComposite);
		}
		Rectangle clip = new Rectangle(graphics.getClipBounds());
		graphics.drawImage(buffer, clip.x, clip.y, clip.x + clip.width, clip.y + clip.height, clip.x, clip.y, clip.x + clip.width, clip.y + clip.height, null);
		((Graphics2D)graphics).setComposite(originalComposite);
		super.paintChildren(graphics);
	}

	private boolean shouldRepaintBuffer(Graphics graphics)
	{
		Rectangle clip = new Rectangle(graphics.getClipBounds());
		Rectangle bounds = new Rectangle(getBounds());
		return bounds.width == clip.width && bounds.height == clip.height;
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
		String[] paragraphs = block.getText().split("\n");
		for (String paragraph : paragraphs)
		{
			if (paragraph.length() != 0)
			{
				AttributedString aText = new AttributedString(paragraph);
				aText.addAttribute(TextAttribute.FONT, createFont());
				LineBreakMeasurer lbm = new LineBreakMeasurer(aText.getIterator(), newGraphics.getFontRenderContext());
				while (lbm.getPosition() < paragraph.length())
				{
					TextLayout layout = lbm.nextLayout((float) usableArea.getWidth());
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
		Rectangle r = getRectangleInsideBorders();
		if(block.getStyle().getBackgroundColor() != null)
		{
			graphics.setColor(Colors.resolve(block.getStyle().getBackgroundColor()));
			graphics.fill(r);
		}
		if(block.getStyle().getBackgroundImage() != null)
		{
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
