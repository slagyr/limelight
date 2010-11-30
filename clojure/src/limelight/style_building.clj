(ns limelight.style-building)


(declare *styles*)

(defn find-or-create [name]
  (or (@*styles* name)
      (limelight.styles.RichStyle.)))

(defn write-to-map [name style]
  (swap! *styles* (fn [styles] (assoc styles name style))))

(defn style [name attributes]
  (let [name (clojure.core/name name)
        style (find-or-create name)]
    (limelight.util.Options/apply style (limelight.util.OptionsMap. attributes))
    (write-to-map name style)))

(defn build-styles [styles input]
   (binding [*ns* (the-ns 'limelight.style-building)
             *styles* (atom styles)]
    (load-string (str "[" input "]"))
    @*styles*))
