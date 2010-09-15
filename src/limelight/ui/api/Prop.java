//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.api;

import limelight.ui.Panel;
import limelight.ResourceLoader;

import java.awt.event.*;
import java.util.Map;

public interface Prop
{
  ResourceLoader getLoader();

  void illuminate(Map<String, Object> options);
}
