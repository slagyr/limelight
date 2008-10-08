require 'limelight/templates/templater_logger'

module Limelight
  module Templates

    # A class to create directories and file templates.  An instance of Templater must be provided with
    # a target_root and a source_root.  The target_root designates a root directory in which all directories and
    # files will be created.  The source_root designated a directory where all the file template can be found.
    #
    # A file template is a plain text file.  It may optionally contain token markers in the format !-TOKEN_NAME-!.
    # When a file template is installed by the templater, all the token margers will be replaced by tokens provided
    # in a hash.
    #
    class Templater

      # Return the default source_root for Limelight related file templates.
      #
      # $LIMELIGHT_LIB$/limelight/templates/sources
      #
      def self.source_dir
        return File.join(File.dirname(__FILE__), "sources")
      end

      # Carifies a path as relative or absolute.  Essentially if makes sure a path begins with a . if it's not
      # an absolute path.
      #
      #   Templater.clarity('some/path') -> './some/path'
      #   Templater.clarity('/root/path') -> '/root/path'
      #
      def self.clarify(path)
        return path if path[0..0] == '.'
        return path if path == File.expand_path(path)
        return File.join(".", path)
      end

      attr_reader :target_root, :source_root

      # See TemplaterLogger 
      #
      attr_accessor :logger

      # New instances Templater require a target_root.  The source_root may optionally be provided.  source_root
      # defaults to Templater.source_dir
      #
      # The logger is initializes as a TemplaterLogger
      #
      def initialize(target_root, source_root=Templater.source_dir)
        @logger = TemplaterLogger.new
        @target_root = Templater.clarify(target_root)
        @source_root = source_root
      end

      # Creates a deirectory.  If the specified directory's parent directory is missing, it will be created as will its
      # parent directory, and so on.
      #
      # After the following call,
      #
      #   templater.directory("dir1/dir2/dir3/dir4")
      #
      # The following directories will exist, inside the target_root, whether they existed prior to the call or not.
      #
      #   dir1
      #   dir1/dir2
      #   dir1/dir2/dir3
      #   dir1/dir2/dir3/dir4
      #
      def directory(path)
        full_path = File.join(@target_root, path)
        establish_directory(full_path)
      end

      # Creates the specified file from the specified file template.  The file will be created withint the target_root.
      # All parent diretories will be created if needed.  The source paramter should be a path pointing to a
      # file template in the source_root directory.
      #
      # Assume the the file <code>src/default.txt.template</code> exists in the source_root with the following content.
      #
      #   !-SCORES-! score and !-YEARS-! years ago, ...
      #
      # When the following command is executed,
      #
      #   templater.file('dir/foo.txt', 'src/default.txt.template', :SCORES => "Four", :YEARS => "seven")
      #
      # The file <code>dir/foo.txt</code> will exist in the target_root with the following content.
      #
      #   Four score and seven years ago, ...
      #
      def file(target, source, tokens = {})
        target_path = File.join(@target_root, target)
        source_source = File.join(@source_root, source)

        establish_directory(File.dirname(target_path))

        if File.exists?(target_path)
          @logger.file_already_exists(target_path)
        else
          @logger.creating_file(target_path)
          content = IO.read(source_source)
          content = replace_tokens(content, tokens)
          File.open(target_path, 'w') { |file| file.write content }
        end
      end

      private

      def establish_directory(full_path)
        return if File.exists?(full_path)
        parent_path = File.dirname(full_path)
        while (!File.exists?(parent_path))
          establish_directory(parent_path)
        end

        @logger.creating_directory(full_path)
        Dir.mkdir(full_path)
      end

      def replace_tokens(content, tokens)
        return content.gsub(/!-(\w+)-!/) do |value|
          token_name = value[2...-2]
          token = tokens[token_name] || tokens[token_name.to_sym] || "UNKNOWN TOKEN"
          token
        end
      end

    end

  end
end