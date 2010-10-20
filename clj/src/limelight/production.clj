(ns limelight.production
  (:import [limelight.casting-director CastingDirector]
           [limelight.theater Theater]))

(deftype Production [peer theater casting-director]
  limelight.model.api.ProductionProxy
  (callMethod [this name args] nil)
  (getCastingDirector [this] nil)
  (getTheater [this] nil)
  (illuminate [this] nil)
  (loadLibraries [this] nil)
  (loadStages [this] nil)
  (loadScene [this scene-path options] nil)
  (loadStyles [this scene] nil))

(defn new-production [peer]
  (let [casting-director (CastingDirector. (.getResourceLoader peer))
        production (Production. peer (atom nil) casting-director)]
    (swap! (.theater production) (fn [old] (Theater. (.getTheater peer) production)))
    production))

