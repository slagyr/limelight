#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

module Limelight

  class Main

    def self.run(args)
      Java::limelight.CmdLineMain.main(args)
    end

  end


end
