#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

module Limelight

  # Most exeception thrown by Limelight Ruby code will be of this type.
  #
  class LimelightException < Exception

    def initialize(thing)
      case thing
      when String
        super(thing)
      when Exception
        super("#{thing.class}: #{thing.message}")
        set_backtrace(thing.backtrace)
      else
        raise "Can't build LimelightException out of #{thing}"
      end
    end

  end

end