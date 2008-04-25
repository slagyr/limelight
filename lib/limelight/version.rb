module Limelight
  module VERSION #:nodoc:
    unless defined? MAJOR
      MAJOR  = 0
      MINOR  = 0
      TINY   = 1

      STRING = [MAJOR, MINOR, TINY].join('.')
      TAG    = "REL_" + [MAJOR, MINOR, TINY].join('_')

      NAME   = "Limelight"
      URL    = "http://limelight.8thlight.com"  
    
      DESCRIPTION = "#{NAME}-#{STRING} - Limelight\n#{URL}"
    end
  end
end