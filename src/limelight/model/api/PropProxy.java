//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.model.api;

import limelight.ui.model.Prop;
import java.util.Map;

public interface PropProxy
{
  void applyOptions(Map<String, Object> options);

  Prop getPeer();
}
