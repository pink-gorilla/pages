(ns page.schema)

(def user
  [{:db/ident :user/name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/password
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}])

(def page
  [{:db/ident :page/user
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident :page/name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :page/content
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :page/count
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one}])

(def schema
  (vec (concat user page)))
