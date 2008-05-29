module DownloadButton

  def mouse_clicked(e)
    url = scene.find("url_field").text
    downloader = Java::limelight.Context.instance.downloader
    begin
      file = downloader.download(url)
      scene.open_production(file.absolute_path)
    rescue Exception => e
      scene.stage.alert(e.to_s)
    end
  end

end