(ns limelight.prop)

(deftype Prop [peer]
  limelight.model.api.PropProxy
  (applyOptions [this options] nil))

(defn new-prop [options]
  (let [prop (Prop. (atom nil))
        peer (limelight.ui.model.PropPanel. prop)]
    (swap! (.peer prop) (fn [_] peer))
    (.addOptions peer options)
    prop))