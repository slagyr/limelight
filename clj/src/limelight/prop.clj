(ns limelight.prop)

(defprotocol PropHelp
  (children [this])
  (add-children [this children])
  (add [this child]))

(def prop-fns
  {:children (fn [this]
               (map (fn [child] (.getProxy child)) (.getChildPropPanels @(.peer this))))
   :add-children (fn [this children]
                    (doall (map (fn [child] (if (seq? child)
                                                 (add-children this child)
                                                 (add this child)))
                                children)))
   :add (fn [this child]
          (.add @(.peer this) @(.peer child))
          child)
   })

(deftype Prop [peer]
  limelight.model.api.PropProxy
  (applyOptions [this options] nil))

(extend Prop
  PropHelp
  prop-fns)

(defn new-prop [options]
  (let [prop (Prop. (atom nil))
        peer (limelight.ui.model.PropPanel. prop)]
    (swap! (.peer prop) (fn [_] peer))
    (.addOptions peer options)
    prop))
