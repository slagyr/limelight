//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.model.api;

import limelight.util.ResourceLoader;

import java.util.Map;

public interface PropProxy
{
  ResourceLoader getLoader();

  void applyOptions(Map<String, Object> options);
}
