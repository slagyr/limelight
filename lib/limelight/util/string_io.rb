module Limelight
  module Util

    class StringIO < String

      def puts(*args)
        args.each do |arg|
          self << arg << "\n"
        end
      end

      def flush
        
      end

    end

  end
end