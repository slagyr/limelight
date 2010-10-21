(ns limelight.stage)

(deftype Stage [peer theater]
  limelight.model.api.StageProxy
  (applyOptions [this options] nil))

(defn new-stage [theater name options]
  (let [stage (Stage. (atom nil) theater)
        peer (limelight.ui.model.FramedStage. name stage)]
    (swap! (.peer stage) (fn [_] peer))
    (.applyOptions peer (limelight.util.OptionsMap. options))
    stage))


