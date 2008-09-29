package limelight.styles;

import limelight.styles.styling.RealStyleAttributeCompilerFactory;

public interface StyleAttributeCompilerFactory
{
  RealStyleAttributeCompilerFactory compilerFactory = null;

  StyleAttributeCompiler compiler(String name);
}
