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
        return "Pack a limelight production into a .llp file."
      end

      protected ###########################################

      def parameter_description
        return "<production_path>"
      end

      def parse_remainder(args)
        @production_path = args.shift
        raise "Missing production path" if @production_path.nil?
      end

      def process
        packer = Limelight::Util::Packer.new
        packer.pack(@production_path)
      end

    end

  end


end
