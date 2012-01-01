;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.stage)

(deftype Stage [peer theater]
  limelight.model.api.StageProxy
  (applyOptions [this options] nil))

(defn new-stage [theater name options]
  (let [stage (Stage. (atom nil) theater)
        peer (limelight.ui.model.FramedStage. name stage)]
    (swap! (.peer stage) (fn [_] peer))
    (.applyOptions peer (limelight.util.Opts. options))
    stage))



