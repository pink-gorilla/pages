(ns pages.handler
  (:require
   [clojure.edn :as edn]
   [taoensso.timbre :refer [trace debug info warn error]]
   [ring.util.response :as res]
   [ring.util.request :refer  [body-string]]
   [modular.webserver.middleware.api :refer [wrap-api-handler]]
   [modular.webserver.handler.registry :refer [add-ring-handler]]
   [pages.db :as db]))

(defn users-handler
  [_]
  (warn "users handler: ")
    (-> (db/users)
        res/response))

(defn user-pages-handler
  [{:keys [query-params] :as req}]
  (warn "user-pages-handler: " req)
  (let [{:keys [user]} query-params]
    (-> (db/user-pages user)
        res/response)))

(defn page-get-handler
  [{:keys [query-params] :as req}]
  (warn "page-get-handler: " req)
  (let [user (get query-params "user")
        page (get query-params "page")]
    (info "getting page: " user "/" page)
    (let [p (db/load-page user page)]
      (info "page: " p)
      ;(res/response {:body p})
      p
      )
))

(defn page-publish-handler
  [{:keys [query-params] :as req}]
  (warn "page-publish-handler: " req)
  (let [s (body-string req)
        body (edn/read-string s)
        {:keys [user password page content]} body]
    (warn "string:" s "hiccup: " body)
    (warn "password: " password)
    (db/add-page user page content)
    (res/response {:message "src sent."})))






(add-ring-handler :pages/users (wrap-api-handler users-handler))
(add-ring-handler :pages/user-pages (wrap-api-handler user-pages-handler))
(add-ring-handler :pages/page-publish (wrap-api-handler page-publish-handler))
(add-ring-handler :pages/page-get (wrap-api-handler page-get-handler))

