(ns demo.effects
  (:require [re-frame.core :refer [reg-event-db reg-event-fx reg-fx dispatch subscribe] :as rf]
            [demo.db :as db :refer [app-db]]
            [demo.utils :as utils]))


;; Our ngrok: "http://3ff333e6.ngrok.io/books"
;; Test dummy data: "https://reqres.in/api/users/1"

(reg-fx
 :get-books
 (fn [params]
   (utils/request (str "https://reqres.in/api/users/1"))))
