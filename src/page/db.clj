(ns page.db
  (:require
   [taoensso.timbre :as timbre :refer [info error]]
   [clojure.java.io :as io]
   [datahike.api :as d]
   [page.schema :refer [schema]]))

(def cfg {:store {:backend :file ;; backends: in-memory, file-based, LevelDB, PostgreSQL
                  :path "db"}
          :keep-history? false})

(defn create! [seed-fn]
  ;;(if (d/database-exists? cfg)
  (info "creating datahike db..")
  (d/delete-database cfg)
  (d/create-database cfg)
  (def conn (d/connect cfg))
  (info "creating schema..")
  (d/transact conn schema)
  (seed-fn))

(defn connect! [seed-fn]
  ;;(if (d/database-exists? cfg)
  (if (-> cfg :store :path io/file (.exists))
    (do (info "connecting to datahike db..")
        (def conn (d/connect cfg)))
    (create! seed-fn)))

(defn transact [t]
  (d/transact conn t))

(defn q [w & args]
  (if args
    (apply d/q w @conn args)
    (d/q w @conn)))

(def find-user
  '[:find (pull ?id [*]) .
    :in $ ?user-name
    :where
    [?id :user/name ?user-name]])

(def load-page
  '[:find (pull ?page-id [:page/content]) .
    :in $ ?user-name ?page-name
    :where
    [?user-id :user/name ?user-name]
    [?page-id :page/user ?user-id]
    [?page-id :page/name ?page-name]])

(defn add-user [name password]
  (transact [{:user/name name
              :user/password password}]))

(defn add-page [user-name name content]
  (transact [{:page/user [:user/name user-name]
              :page/name name
              :page/content (pr-str content)}]))

(comment
  (add-user "daslu" "mysecretPassword!!1")
  (add-page "daslu" "demo1" [:p "hello world"])

  (q find-user "daslu")
  (q load-page "daslu" "demo1")

;  
  )
