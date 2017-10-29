(ns demo.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch-sync dispatch]]
            [demo.handlers]
             [demo.subs]))

(def ReactNative (js/require "react-native"))
(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))

;;[:> trystack {}]

(defn app-root []
  (dispatch [:get-books])
  (let [response (subscribe [:get-val :response])
        decoded (subscribe [:get-val :decoded])
        error (subscribe [:get-val :error])]
    (fn []
      [view {:style {:flex           1
                     :alignItems     "center"
                     :justifyContent "center"}}
       (when @error
         [text (str "error >>>>>>>>>>>>>>>>>>>>>>>>>>>> " @error)])
       (if @response
        [text (str "Respone ..........................." @response)]
        [text "NOT FOUND BRO"])
       (when @decoded
         [text (str "Decoded @@@@@@@@@@@@@@@@@@@@@@@@@@@" @decoded)])
       ] )))

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "main" #(r/reactify-component app-root)))
