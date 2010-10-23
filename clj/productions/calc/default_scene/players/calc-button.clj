(on-mouse-clicked
  (fn [e]
    (let [prop (.getRecipient e)
          lcd (.find (.getRoot prop) "lcd")
          button-text (.getText prop)]
      (cond
        (= "c" button-text)
          (.setText lcd "()")
        (= "enter" button-text)
          (.setText lcd (str (load-string (str (.getText lcd)))))
        :else
          (let [current-text (.getText lcd)
                middle-text (.substring current-text 1 (- (.length current-text) 1))]
            (.setText lcd (str "(" middle-text (.getText prop) ")")))))))

