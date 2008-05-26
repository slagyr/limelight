# -----
# Copyright 2008 8th Light Inc.
# Limelight and all included source files are distributed under terms of the GNU LGPL.
# -----

module Copyrights

  ENDL = "\n"

  class Copyright

    attr_reader :file_ext, :text_filename, :comment_prefix

    def glob(dir)
      return Dir.glob(File.join(dir, "**", "*.#{file_ext}"))  
    end

    def text
      if @text.nil?
        @text = IO.read(File.join(File.expand_path(File.dirname(__FILE__)), text_filename)).strip       
      end
      return @text
    end

  end

  class Ruby < Copyright

    def initialize
      @file_ext = "rb"
      @text_filename = "ruby.txt"
      @comment_prefix = "#-"
    end

  end

  class Java < Copyright

    def initialize
      @file_ext = "java"
      @text_filename = "java.txt"
      @comment_prefix = "//-"
    end

  end

  class SourceFile

    attr_reader :copyright

    def initialize(filename, comment_prefix)
      @filename = filename
      @comment_prefix = comment_prefix
      @lines = IO.readlines(filename, ENDL)
      find_copyright
    end

    def has_copyright?
      @copyright.length > 0
    end

    def find_copyright
      @copyright = ''
      @lines.each do |line|
        if line[0...@comment_prefix.length] == @comment_prefix
          @copyright << line << ENDL
        else
          break
        end
      end
      @copyright.strip!
    end

    def add_copyright(text)

    end

    def save!
      
    end
  end


  def self.add(lang_name, dir)
    lang = eval("#{lang_name}.new")
    lang.glob(dir).each do |filename|
      source_file = SourceFile.new(filename, lang.comment_prefix)
      if source_file.has_copyright?
        if source_file.copyright != lang.text
          puts "invalid copyright: #{filename}"
#          source_file.remove_copyright
#          source_file.add_copyright(lang.text)
#          source_file.save!
        else
          puts "already has valid copyright: #{filename}"
        end
      else
        puts "missing copyright: #{filename}"
#        source_file.add_copyright(lang.text)
#        source_file.save!
      end
    end
  end

end