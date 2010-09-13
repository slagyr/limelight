#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module VERSION #:nodoc:
    unless defined? MAJOR
      MAJOR  = 0
      MINOR  = 5
      TINY   = 11

      STRING = [MAJOR, MINOR, TINY].join('.')
      TAG    = "REL_" + [MAJOR, MINOR, TINY].join('_')

      NAME   = "Limelight"
      URL    = "http://limelight.8thlight.com"  
    
      DESCRIPTION = "#{NAME}-#{STRING} - Limelight\n#{URL}"
    end
  end
end