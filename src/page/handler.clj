(ns page.handler
  (:require
   [taoensso.timbre :refer [trace warn debug info error]]
   [clojure.core.async :refer [go <! <!!]]
   [clojure.edn]
   [ring.util.response :as res]
   [ring.util.request :refer  [body-string]]
   [modular.webserver.middleware.api :refer [wrap-api-handler]]
   [modular.webserver.handler.registry :refer [add-ring-handler]]
   [page.db :as db]))

(defn page-get-handler
  [{:keys [query-params] :as req}]
  (warn "page-get-handler: " req)
  (let [{:keys [user page]} query-params]
    (-> (db/load-page user page)
        res/response)))

(defn page-set-handler
  [{:keys [query-params] :as req}]
  (warn "page-set-handler: " req)
  (let [s (body-string req)
        body (clojure.edn/read-string s)
        {:keys [user page content]} body]
    (warn "string:" s "hiccup: " body)
    (db/add-page user page content)
    (res/response {:message "src sent."})))

(add-ring-handler :page/get (wrap-api-handler page-get-handler))
(add-ring-handler :page/set (wrap-api-handler page-set-handler))
