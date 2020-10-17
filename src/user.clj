(ns user
  (:require
   [shadow.cljs.devtools.api :as shadow]
   [shadow.cljs.devtools.config :as shadow.config]
   [shadow.cljs.devtools.server :as shadow.server]
   [clojure.java.io :as io]))

(defn delete-recursively [f]
  (when (.isDirectory f)
    (doseq [c (.listFiles f)]
      (delete-recursively c)))
  (io/delete-file f))

(shadow.server/load-config)

(defn restart-shadow-clean []
  (shadow.server/stop!)
  (try (-> (shadow.config/get-build :app)
           (get-in [:dev :output-dir])
           (io/file)
           (delete-recursively))
       (delete-recursively (io/file ".shadow-cljs"))
       (catch Exception _))
  (shadow.server/start!)
  (shadow/watch :app))


(defn restart-fw []
  (restart-shadow-clean))

(defn restart-fw-clean []
  (restart-shadow-clean))

(defn cljs-repl []
  (shadow/repl :app))

(comment
  (restart-fw)

  (restart-fw-clean)

  (cljs-repl)
  )

