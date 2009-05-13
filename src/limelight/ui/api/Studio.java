//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.api;

//TODO Move me our of ui package, and the Mock.
// Move the while api pacage our of the ui pacakge.
public interface Studio
{
  void open(String production);
  boolean should_allow_shutdown();
}
