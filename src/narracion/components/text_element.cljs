(ns narracion.components.text-element
  (:require [narracion.views.video.state :as state]
            [re-frame.core :as re-frame]
            [reagent.core :as r]
            [reagent.dom :as rdom]
            [goog.string :as gs]))

(defn text-area
  [_]
  (r/create-class
    {:component-did-mount
     (fn [this]
       (let [e (rdom/dom-node this)]
         (.focus e)))
     :reagent-render
     (fn [{:keys [on-blur value on-change]}]
       [:textarea
        {:on-blur on-blur
         :on-change on-change
         :value value}])}))

(defn text-element
  [{:keys [mode timestamp text]}]
  [:div.text-element
   [:div.timestamp
    (gs/format "%.2fs" timestamp)]
   [:div.text
    (if (= mode :insert)
      [text-area
       {:on-change
        (fn [evt]
          (->> (.. evt -target -value)
               (vector ::state/update-item timestamp)
               (re-frame/dispatch)))
        :value text}]
      [:div text])]])
