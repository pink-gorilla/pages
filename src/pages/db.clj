(ns pages.db
  (:require
   [taoensso.timbre :as timbre :refer [info error]]
   [clojure.java.io :as io]
   [datahike.api :as d]
   [pages.schema :refer [schema]]))

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

;; USER

(defn add-user [name password]
  (transact [{:user/name name
              :user/password password}]))

(def q-users
  '[:find [(pull ?user-id [:user/name]) ...]
    :in $
    :where
    [?user-id :user/name] ; get all user-id
    ])

(defn users []
  (->> (q q-users)
       (map :user/name)))

(def find-user
  '[:find (pull ?id [*]) .
    :in $ ?user-name
    :where
    [?id :user/name ?user-name]])

;; PAGES

(def q-user-page-list
  '[:find [(pull ?page-id [:page/name]) ...]
    :in $ ?user-name
    :where
    [?user-id :user/name ?user-name] ; get user-id
    [?page-id :page/user ?user-id] ; get all pages for the user
    ])

(defn user-pages [user]
  (->> (q q-user-page-list user)
       (map :page/name)))


(defn q-page-id [user-name page-name]
  (let [pq '[:find ?page-id .
            :in $ ?page-name-full
            :where
            [?page-id :page/name-full ?page-name-full]]
        name-full (str user-name "/" page-name)]
   (q pq name-full)
  ))



(defn set-page [user-name page-name content]
  (let [name-full (str user-name "/" page-name)
        page-id (q-page-id user-name page-name)]
    (if page-id 
      (do (info "updating page " name-full "id: " page-id)
          (transact [{:db/id page-id
                      :page/content (pr-str content)}]))
      (do (info "adding new page: " name-full)
         (transact [{:page/user [:user/name user-name]
                     :page/name-full name-full
                     :page/name page-name
                     :page/content (pr-str content)}])))))

(def load-page
  '[:find (pull ?page-id [:page/content]) .
    :in $ ?user-name ?page-name
    :where
    [?user-id :user/name ?user-name]
    [?page-id :page/user ?user-id]
    [?page-id :page/name ?page-name]])




(comment


  (users)

  (q find-user "seed")
  (q find-user "woldemord")

  (q load-page "seed" "index")

  (user-pages "seed")

  (add-user "daslu" "mysecretPassword!!1")
  (q find-user "daslu")
  (set-page "daslu" "demo1" [:p.bg-green-500.m-5 "hello world 1"])
  (set-page "daslu" "demo2" '[:p.bg-red-500.m-5 [user/customer {:first "daniel" :last "slutzky"}]])
  (set-page "daslu" "demo3" '[:p.bg-red-500.m-5 [user/users {}]])
  (set-page "daslu" "index" '[:p.bg-red-500.m-5 [user/user-page "daslu"]])





  (q-page-id "daslu" "demo1")
  (q-page-id "daslu" "demo0000")

  (q load-page "daslu" "demo1")
  (user-pages "daslu")

  (user-pages "demo")

;  
  )
