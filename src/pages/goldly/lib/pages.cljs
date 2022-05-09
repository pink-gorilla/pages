(ns pages
  (:require
   [rf]
   [user :refer [notify info]]))

;; this ns is to be used by users to create pages
;; [pages/page-link "user1" "demo1"]

(defn goto-page [user page]
  (rf/dispatch [:bidi/goto :user-page
                :user user :page page
                :query-params {}]))

(defn a-page [{:keys [user page]} & body]
  [:a {:on-click #(goto-page user page)}
   body])

(defn link [user page]
  [:p.m-2.border.bg-blue-500.w-32
   [:a {:on-click #(goto-page user page)}
    (str "! " user "/" page)]])

(defn show-dialog [dialog-fn size]
  (rf/dispatch [:modal/open dialog-fn size]))

(defn notify-success [message]
  (info "notify success: " message)
  (notify :info [:p message]))

(defn notify-error [message]
  (info "notify error: " message)
  (notify :error message 0)) ; error will not go away with time