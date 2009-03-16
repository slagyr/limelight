//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.ResourceLoader;
import limelight.util.NanoTimer;
import limelight.util.Debug;
import limelight.caching.SimpleCache;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageCache extends SimpleCache<String, Image>
{
  private final ResourceLoader loader;

  public ImageCache(ResourceLoader loader)
  {
    this.loader = loader;
  }

  public Image getImage(String imagePath) throws IOException
  {
    Image image = retrieve(imagePath);
    if(image == null)
      image = loadImage(imagePath);
    return image;
  }

  private BufferedImage loadImage(String imagePath) throws IOException
  {
    String imageFilename = loader.pathTo(imagePath);
//Debug debug = new Debug();
    BufferedImage image = ImageIO.read(new File(imageFilename));
//debug.log("loaded image " + imageFilename);
    cache(imagePath, image);
    return image;
  }
}
