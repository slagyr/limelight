//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.abstrstyling;

import limelight.styles.compiling.RealStyleAttributeCompilerFactory;
import limelight.styles.abstrstyling.StyleCompiler;

public interface StyleAttributeCompilerFactory
{
  RealStyleAttributeCompilerFactory compilerFactory = null;

  StyleCompiler compiler(String type, String name);

}
