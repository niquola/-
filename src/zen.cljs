(?ns zen)

(defonce app-db   (atom {::myview 1}))
(defonce el-reg   (atom {}))
(defonce view-reg (atom {}))

(defn subscribe [view cb])

(defn by-id [id]
  (js/document.getElementById id))

(defn mk-el [tag id]
  (let [el (js/document.createElement tag)]
    (aset el "id" id)
    el))

(defn eval-handler [h ev]
  (h {:ev ev}))

(defn eval-view [view]
  (let [paths (:z/paths (meta view))
        db @app-db
        inp (->> paths
                 (reduce (fn [acc [k pth]] (assoc acc k (get-in db pth))) {}))
        res (view inp)]
    (println "view" res)
    res))

(defn do-render [render & [node]]
  (let [id "tmp"
        tag (or (:< render) "div")
        el (mk-el tag id)
        reg (atom {})
        model (when-let [v (:view render)]
                (eval-view v))]
    (println "model" model)
    (when-let [v (:view render)]
      (subscribe v (fn [x] "Changed")))
    (when-let [txt (:text render)]
      (if (keyword? txt)
        (do
          ;; todo subscribe
          (when-let [val (get model txt)]
            (aset el "textContent" val)))
        (aset el "textContent" txt)))
    (when-let [cls (:. render)]
      (if (keyword? cls)
        (do
          (println "subs for " cls)
          (when-let [val (get model cls)]
            (.add (.. el -classList) val)))
        (.add (.. el -classList) cls)))
    (doseq [[evn h] (:on render)]
      (swap! reg assoc-in [:ev evn]
             (.addEventListener el (name evn) (fn [ev] (eval-handler h ev)))))
    (swap! el-reg assoc id reg)
    (when node
      (.appendChild node el))))

;; example
(defn do-click
  {:z/path {:v ::myview}}
  [{v :v}]
  (println "Do click")
  {:tx [assoc ::myview (inc (or v 0))]})

(defn my-view
  {:z/paths {:v [::myview]}}
  [{v :v}]
  {:text (str "Clicked " v)
   :active-class "active"
   :on-click #'do-click})

(defn do-remove [& _ ])


(defn tooltip-view [])

(def tooltip {:z/view #'tooltip-view})

(def my-render
  {:< #'my-view :. :active-class :on {:click #'do-click} :_ :text
   :> {:icon {:< :img :src :avatar}
       :title {:hide :hidden? :_ :text}
       :remove {:when :removable?
                :< :a :on {:click #'do-remove} :_ "Remove"}
       :tooltip {:< #'tooltip}}})


(do-render my-render (by-id "app"))

