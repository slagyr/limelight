//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.api;

import limelight.ui.Panel;
import limelight.ResourceLoader;

import java.awt.event.*;

public interface Prop
{ 
  Panel getPanel();
  String getName();
  Scene getScene();
  ResourceLoader getLoader();
}
