//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.painting;

import limelight.util.Box;

import java.awt.*;

public class StaticImageFillStrategy implements ImageFillStrategy
{
	public void fill(Graphics2D graphics, Image image)
	{
		Box area = new Box(graphics.getClipBounds());
		Graphics innerGraphics = graphics.create(area.x, area.y, area.width, area.height);
		innerGraphics.drawImage(image, 0, 0, null);
	}
}
