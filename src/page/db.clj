(ns page.db
  (:require
   [taoensso.timbre :as timbre :refer [info error]]
   [clojure.java.io :as io]
   [datahike.api :as d]
   [page.schema :refer [schema]]))

; backends: in-memory, file-based, LevelDB, PostgreSQL

(def cfg {:store {:backend :file
                  :path "data/datahike-db"}
          :keep-history? false})

(defn create! []
  ;(if (d/database-exists? cfg)
  (info "creating datahike db..")
  (d/delete-database cfg)
  (d/create-database cfg)
  (def conn (d/connect cfg))
  (info "creating schema..")
  (d/transact conn schema))

(defn connect! []
  ;(if (d/database-exists? cfg)
  (let [db-filename (get-in cfg [:store :path])]
    (if (.exists (io/file db-filename))
      (do (info "connecting to datahike db..")
          (def conn (d/connect cfg)))
      (create!))))


(defn transact [t]
  (d/transact conn t))

(defn q [w & args]
  (if args
    (apply d/q w @conn args)
    (d/q w @conn)))