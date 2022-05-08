(ns page.db
  (:require
   [taoensso.timbre :as timbre :refer [info error]]
   [clojure.java.io :as io]
   [datahike.api :as d]
   [page.schema :refer [schema]]))

;; backends: in-memory, file-based, LevelDB, PostgreSQL

(def cfg {:store {:backend :file
                  :path "db"}
          :keep-history? false})

(defn create! []
  ;;(if (d/database-exists? cfg)
  (info "creating datahike db..")
  (d/delete-database cfg)
  (d/create-database cfg)
  (def conn (d/connect cfg))
  (info "creating schema..")
  (d/transact conn schema))

(defn connect! []
  ;;(if (d/database-exists? cfg)
  (if (-> cfg :store :path io/file (.exists))
    (do (info "connecting to datahike db..")
        (def conn (d/connect cfg)))
    (create!)))

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
  (add-page "daslu" "demo3"
            '[user/vegalite
              {:box :sm
               :spec {:$schema "https://vega.github.io/schema/vega-lite/v5.json"
                      :data {:values [{:from :a :to :b :cor 0.6}
                                      {:from :a :to :c :cor 0.2}
                                      {:from :a :to :d :cor -0.1}
                                      {:from :a :to :e :cor 0.0}
                                      {:from :b :to :c :cor 0.3}
                                      {:from :b :to :d :cor 0.7}
                                      {:from :b :to :e :cor 0.0}
                                      {:from :c :to :d :cor 0.4}
                                      {:from :c :to :e :cor 0.8}
                                      {:from :d :to :e :cor -0.2}]}
                      :mark "rect"
                      :width 600
                      :height 400
                      :encoding {:x {:field "to"
                                     :type "ordinal"}
                                 :y {:field "from"
                                     :type "ordinal"}
                                 :color {;:value "blue"
                                         :field "cor"
                                         :type "quantitative"}}
                      :config {:view {:stroke "transparent"}}}}])

  )

(comment
  (q find-user "daslu")
  (q load-page "daslu" "demo1"))

(connect!)
