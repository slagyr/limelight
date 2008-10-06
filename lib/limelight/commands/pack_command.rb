#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/commands/command'

module Limelight
  module Commands

    # Compresses the specified Production into a single .llp file.
    #
    #   jruby -S limelight pack <production_name>
    #
    class PackCommand < Command

      install_as "pack"

      def self.description
        return "Pack the designated limelight production into a .llp file."
      end

      def run(args)
        packer = Limelight::Util::Packer.new
        packer.pack(args.shift)
      end
    end

  end


end
