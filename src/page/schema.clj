(ns page.schema)


(def aftership
  [{:db/ident :tracking/id
    :db/unique :db.unique/identity  ; db.unique/identity or :db.unique/value 
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :tracking/invoice
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one}
   {:db/ident :tracking/invoice-number
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :tracking/email
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :tracking/slug
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :tracking/number
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :tracking/tag
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :tracking/date
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :tracking/weight
    :db/valueType :db.type/double
    :db/cardinality :db.cardinality/one}])