(ns demo.navtest
  (:require [reagent.core :as r :refer [atom]]
            [demo.navigation :refer [enhanced-stack-navigator stack-screen NavigationActionsMap]]))

(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))


(defn t1 [props]
  (let [{:keys [screenProps navigation]} props
        {:keys [navigate  goBack]} navigation]
    [view {:style {:flex           1
                   :alignItems     "center"
                   :justifyContent "center"}}
     [touchable-highlight {:style {:background-color "#999"
                                   :padding 10 :border-radius 5}
                           :on-press #(navigate "T2")}
      [text {:style {:color "white"
                     :text-align "center" :font-weight "bold"}}
       "Go to T2"]]]))

(defn t2 [props]
  (let [{:keys [screenProps navigation]} props
        {:keys [navigate  goBack]} navigation]
    [view {:style {:flex           1
                   :alignItems     "center"
                   :justifyContent "center"}}
     [touchable-highlight {:style {:background-color "#999"
                                   :padding 10 :border-radius 5}
                           :on-press #(navigate "T3")}
      [text {:style {:color "white"
                     :text-align "center" :font-weight "bold"}}
       "Go to T3"]]]))

(defn t3 [props]
  (let [{:keys [screenProps navigation]} props
        {:keys [navigate  goBack]} navigation]
    [view {:style {:flex           1
                   :alignItems     "center"
                   :justifyContent "center"}}
     [touchable-highlight {:style {:background-color "#999"
                                   :padding 10 :border-radius 5}
                           :on-press #(navigate "T4")}
      [text {:style {:color "white"
                     :text-align "center" :font-weight "bold"}}
       "Go to T4"]]]))

(defn t4 [props]
  (let [{:keys [screenProps navigation]} props
        {:keys [navigate  goBack dispatch]} navigation
        navz (NavigationActionsMap "Navigation/NAVIGATE")
        resetz (NavigationActionsMap "Navigation/RESET")
        navFunc (.navigate navigation-actions (clj->js {:routeName "T1"}))
        resetFunc (.reset navigation-actions (clj->js {:index 0
                                                       :actions [navFunc]}))]
      [view {:style {:flex           1
                   :alignItems     "center"
                   :justifyContent "center"}}
     [touchable-highlight {:style {:background-color "#999"
                                   :padding 10 :border-radius 5}
                           :on-press #_(goBack "T1")
                           #(dispatch #_resetFunc
                                      (resetz
                                       (clj->js {:index 0
                                                 :actions
                                                 [(navz (clj->js {:routeName "T1"}))]})))}
      [text {:style {:color "white"
                     :text-align "center" :font-weight "bold"}}
       "GoBack to T1"]]]))

(def trystack
  (enhanced-stack-navigator {:T1 {:screen (stack-screen t1 {:title "T1 "})}
                             :T2 {:screen (stack-screen t2 {:title "T2"})}
                             :T3 {:screen (stack-screen t3 {:title "T3"})}
                             :T4 {:screen (stack-screen t4 {:title "T4"})}}
                            {}))
