require "limelight/package"

module Limelight
  module PackageServer
    
    def self.included(base)
      base.extend ClassMethods 
    end 
    
    module ClassMethods
      def serve_limelight_packages(options = {})
        options[:production_path] ||= "#{RAILS_ROOT}/lib/productions"
        class_inheritable_accessor :options 
        self.options = options 
        
        include InstanceMethods
        before_filter :download_package, :only => [:download]
      end
    end
    
    module InstanceMethods
      def download_package
        package = Package.find_by_name(params[:package], options[:production_path])
        
        if package.contents.nil?
          render :file => "#{RAILS_ROOT}/public/404.html", :status => 404
        else
          send_data(package.contents, :type => "application/x-limelight", :filename => package.file_name)
        end
      end
    end
    
  end
end