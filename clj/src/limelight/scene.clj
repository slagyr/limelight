(ns limelight.scene)

(deftype Scene [peer]
  limelight.model.api.SceneProxy
  (getPeer [this] @peer))

(defn new-scene [options]
  (let [scene (Scene. (atom nil)) peer (limelight.ui.model.ScenePanel. scene)]
    (swap! (.peer scene) (fn [_] peer))
    (.addOptions peer options)
    scene))