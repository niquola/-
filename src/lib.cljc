(ns lib)


(def db-atom (atom {:title "Title"}))

(defn dispatch [handler & args]
  (let [db @db-atom
        {{a :assoc} :tx :as fx} (apply handler db args)]
    (println a)
    (if a
      (assoc db (first a) (second a))
      db)))

(defn eval-sub [sub & args]
  (let [db @db-atom]
    (apply sub db args)))

(comment

  (defn my-handler [db args]
    {:tx {:assoc [:key (:title db)]}})

  (defn my-sub [db]
    (get db :key))

  (dispatch my-handler {:args 1})

  @db-atom

  (eval-sub my-sub)




  )
