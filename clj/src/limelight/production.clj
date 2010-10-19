(ns limelight.production)

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
  (let [production (Production. peer (atom nil) (limelight.casting-director.CastingDirector. (.getResourceLoader peer)))]
    (swap! (.theater production) (fn [old] (limelight.theater.Theater. (.getTheater peer) production)))
    production))

