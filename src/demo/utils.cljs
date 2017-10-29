(ns demo.utils
  (:require [re-frame.core :refer [dispatch]]
            [cognitect.transit :as transit]))

(defn http-get
  ([url on-success on-error]
   (http-get url nil on-success on-error))
  ([url valid-response? on-success on-error]
   (-> (.fetch js/window url (clj->js {:method  "GET"
                                       :headers {"Cache-Control" "no-cache"}}))
       (.then (fn [response]
                (let [ok?  (.-ok response)
                      ok?' (if valid-response?
                             (and ok? (valid-response? response))
                             ok?)]
                  [(.-_bodyText response) ok?'])))
       (.then (fn [[response ok?]]
                (cond
                  ok? (let [json (.parse js/JSON response)
                            obj (js->clj json :keywordize-keys true)]
                        (on-success obj))

                  (and on-error (str "FROM THEN in then" (not ok?)))
                  (on-error (str "FROM THEN in else" response))

                  :else false)))
       (.catch (or (fn [error]
                     (dispatch [:set-key-val :error (str "      FROM catch        \n" error)]))
                   on-error)))))

(def reader (transit/reader :json))

(defn request
  [url]
  ;; [uri {:keys [request-method on-success on-error retry? retries params]
  ;;       :or {retries default-retries
  ;;            request-method :get}
  ;;       :as opts}]
  ;;(assert (#{:get :post} request-method) (str "invalid request method " request-method))
  ;; (let [uri* (str (client-conf-api)
  ;;                 (to-uri uri)
  ;;                 (when (= request-method :get)
  ;;                   (str "?" (query-string params))))]
  ;;   ;;(log/info "fetch:" uri*)
  ;;   )
  (-> url
      (js/fetch (clj->js {:method "GET"
                          :headers {"Accept" "application/transit+json"}}))
      (.then (fn [response]
               (dispatch [:set-key-val :response (str "got response" response)])
               (.json response)))
      (.then (fn [json-response]
               (dispatch [:set-key-val :decoded
                          (str json-response " ------------------------------------------ "
                               (js->clj json-response :keywordize-keys true))])))
      (.catch (fn [err]
                (dispatch [:set-key-val :error (str "Oops, an error occurred:" err)])))))
