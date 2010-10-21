(ns limelight.production
  (:use [limelight.casting-director]
        [limelight.theater]
        [limelight.building :only (build-stages)]
        [limelight.scene :only (new-scene)])
  (:import [limelight.casting-director CastingDirector]
           [limelight.theater Theater]))

(deftype Production [peer theater casting-director]
  limelight.model.api.ProductionProxy
  (callMethod [this name args] nil)
  (getCastingDirector [this] casting-director)
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
      scene))
  
  (loadStyles [this scene] nil))

(defn new-production [peer]
  (let [casting-director (CastingDirector. (.getResourceLoader peer))
        production (Production. peer (atom nil) casting-director)]
    (swap! (.theater production) (fn [old] (Theater. (.getTheater peer) production)))
    (.setProxy peer production)
    production))
