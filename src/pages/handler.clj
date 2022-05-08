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

(defn set-page [user password page content]
  (let [r-user (db/q db/find-user user)]
    (if r-user
       (let [correct-password (:user/password r-user)]
        (if (= password correct-password)
           (db/set-page user page content)
           {:error "wrong password!"}))
      (do 
        (db/add-user user password)
        (db/set-page user page content)))))


(defn page-publish-handler
  [{:keys [query-params] :as req}]
  ;(warn "page-publish-handler: " req)
  (let [user (get query-params "user")
        page (get query-params "page")
        password (get query-params "password")
        _ (info "publishing :user " user "page: " page "password: " password)
        body (body-string req)
        content (edn/read-string body)]
    (warn "body: " body)
    (warn "content: " content)
    (try 
      (let [r (set-page user password page content)]
        (info "add result: " r)
       r)
      (catch Exception ex
        (warn "exception in publish: " ex)
        ;(res/response {:headers {"Content-Type" "application/edn"}
        ;               :body (pr-str {:error (pr-str ex)})})
        {:error (pr-str ex)}
        )
      
    )))






(add-ring-handler :pages/users (wrap-api-handler users-handler))
(add-ring-handler :pages/user-pages (wrap-api-handler user-pages-handler))
(add-ring-handler :pages/page-publish (wrap-api-handler page-publish-handler))
(add-ring-handler :pages/page-get (wrap-api-handler page-get-handler))

