//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.updates;

import limelight.ui.model.Update;

public interface Updates
{
  Update layoutAndPaintUpdate = new LayoutAndPaintUpdate(4);
  Update scrollChangedUpdate = new ScrollChangedUpdate(3);
  Update paintUpdate = new PaintUpdate(2);
  Update shallowPaintUpdate = new ShallowPaintUpdate(1);
}
