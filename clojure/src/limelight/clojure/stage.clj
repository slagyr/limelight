;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.stage
  (:use
    [limelight.clojure.core]))

(deftype Stage [_peer _theater]

  limelight.model.api.StageProxy
  (applyOptions [this options] nil)

  limelight.clojure.core.TheaterSource
  (theater [this] (.getProxy (.getTheater @_peer)))

  limelight.clojure.core.ProductionSource
  (production [this] (production _theater))

  limelight.clojure.core.SceneSource
  (scene [this] (if-let [peer-scene (.getScene @_peer)] (.getProxy peer-scene) nil))

  limelight.clojure.core.StageSource
  (stage [this] this)

  clojure.lang.Named
  (getName [this] (.getName @_peer))
  )

(defn new-stage [theater name options]
  (let [stage (Stage. (atom nil) theater)
        peer (limelight.ui.model.FramedStage. name stage)]
    (swap! (._peer stage) (fn [_] peer))
    (.applyOptions peer (limelight.util.Opts. options))
    stage))



