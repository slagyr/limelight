(ns limelight.production
  (:use [limelight.common]
        [limelight.theater]
        [limelight.stage-building :only (build-stages)]
        [limelight.prop-building :only (build-props)]
        [limelight.style-building :only (build-styles)]
        [limelight.scene :only (new-scene)])
  (:import [limelight.theater Theater]))

(deftype Production [peer theater]

  limelight.model.api.ProductionProxy
  (callMethod [this name args] nil)
  (getTheater [this] @theater)
  (illuminate [this] nil)
  (loadLibraries [this] nil)

  (loadStages [this]
    (build-stages @theater
                  (.readText (.getResourceLoader peer) "stages.clj")))

  (loadScene [this scene-path options]
    (let [full-scene-path (.pathTo (.getResourceLoader peer) scene-path)
          scene-name (limelight.io.FileUtil/filename scene-path)
          _ (.put options "path" full-scene-path)
          _ (.put options "name" scene-name)
          scene (new-scene options)]
      (.setProduction @(.peer scene) peer)
      (build-props scene (.readText (.getResourceLoader @(.peer scene)) "props.clj"))
      scene))
  
  (loadStyles [this scene]
    (let [new-styles (build-styles {} (.readText (.getResourceLoader @(.peer scene)) "styles.clj"))
          scene-styles (.getStylesStore @(.peer scene))]
      (doall
        (map (fn [[name value]] (.put scene-styles name value))
             new-styles))))

  ResourceRoot
  (resource-path [this resource]
    (.pathTo (.getResourceLoader (.peer this)) resource)))

(defn new-production [peer]
  (let [production (Production. peer (atom nil))]
    (swap! (.theater production) (fn [old] (Theater. (.getTheater peer) production)))
    (.setProxy peer production)
    production))
