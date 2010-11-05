(ns limelight.common)

(defprotocol ResourceRoot
  (resource-path [this resource]))

(defn production [scene]
  (.getProxy (.getProduction @(.peer scene))))

(defn child-props [parent]
  (map (fn [child] (.getProxy child)) (.getChildPropPanels @(.peer parent))))

(defn add-props [parent & children]
  (let [peer-prop @(.peer parent)
        children (flatten children)]
    (doseq [child children]
      (if child
        (.add peer-prop @(.peer child))))
    children))

