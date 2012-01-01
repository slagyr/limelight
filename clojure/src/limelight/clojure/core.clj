;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.core)

(def *log* (java.util.logging.Logger/getLogger  "clojure"))

(defn log [& messages]
  (.info *log* (apply str messages)))

(defprotocol ResourceRoot
  (resource-path [this resource]))

(defn production [prop]
  (.getProxy (.getProduction (.getRoot @(.peer prop)))))

(defn- as-proxies [peer-props]
  (map (fn [child] (.getProxy child)) peer-props))

(defn child-props [parent]
  (as-proxies (.getChildPropPanels @(.peer parent))))

(defn add-props [parent & children]
  (let [peer-prop @(.peer parent)
        children (flatten children)]
    (doseq [child children]
      (if child
        (.add peer-prop @(.peer child))))
    children))

(defn parent-prop [prop]
  (.getProxy (.getParent @(.peer prop))))

(defn scene-prop [prop]
  (.getProxy (.getRoot @(.peer prop))))

(defn find-prop [prop id]
  (if-let [peer-result (.find @(.peer (scene-prop prop)) id)]
    (.getProxy peer-result)
    nil))

(defn find-props-named [root name]
  (as-proxies (.findByName @(.peer root) name)))

(defn prop-id [prop]
  (.getId @(.peer prop)))

(defn prop-name [prop]
  (.getName @(.peer prop)))

(defn prop-text [prop]
  (.getText @(.peer prop)))

(defn prop-text= [prop value]
  (.setText @(.peer prop) value))

(defn open-scene
  ([production scene-name] (.openScene (.peer production) scene-name {}))
  ([production scene-name stage-or-options]
    (if (string? stage-or-options)
      (.openScene (.peer production) scene-name stage-or-options {})
      (.openScene (.peer production) scene-name stage-or-options)))
  ([production scene-name stage-name options] (.openScene (.peer production) scene-name stage-name options)))