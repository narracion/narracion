(ns narracion.views.video.state
  (:require [narracion.components.video :as video]
            [re-frame.core :as re-frame]))

;; ## Interceptors

(def normal-mode-only
  "Cancels handling of an event unless in normal mode."
  (re-frame/->interceptor
    :id     :normal-mode-only
    :before (fn [context]
              (if (= :normal (-> context :coeffects :db :mode))
                context
                (assoc context :queue [])))))

(def ^:private interceptors
  [normal-mode-only
   (re-frame/path :video)])

;; ## Events

;; ### Current Status

(re-frame/reg-event-db
  ::player-ready
  interceptors
  (fn [db [_ player]]
    (assoc db :player player :current-time 0.0)))

(re-frame/reg-event-db
  ::set-current-time
  (fn [db [_ time-seconds]]
    (assoc-in db [:video :current-time] time-seconds)))

(re-frame/reg-event-db
  ::insert-mode
  [normal-mode-only]
  (fn [db _]
    (-> db
        (update :entries (fnil conj [])
                {:timestamp (-> db :video :current-time)
                 :text ""})
        (assoc :mode :insert))))

(re-frame/reg-event-db
  ::normal-mode
  (fn [db _]
    (assoc db :mode :normal)))

(re-frame/reg-event-db
  ::update-item
  (fn [db [_ timestamp new-text]]
    (update db :entries
            #(map
               (fn [e]
                 (if (= (:timestamp e) timestamp)
                   (assoc e :text new-text)
                   e))
               %))))

;; ### Interactions

(re-frame/reg-event-fx
  ::toggle-play
  interceptors
  (fn [{{:keys [player]} :db} _]
    (when player
      {::toggle-play {:player player}})))

(re-frame/reg-event-fx
  ::skip-video
  interceptors
  (fn [{{:keys [current-time player]} :db} [_ base-delta]]
    (when player
      (let [delta (if (video/paused? player)
                    (/ base-delta 2)
                    base-delta)]
        {::set-video-time
         {:player player
          :time-seconds (+ (or current-time 0) delta)}}))))

;; ## Effects

(re-frame/reg-fx
  ::toggle-play
  (fn [{:keys [player]}]
    (video/toggle-play player)))

(re-frame/reg-fx
  ::set-video-time
  (fn [{:keys [player time-seconds]}]
    (video/set-current-time player time-seconds)))

;; ## Subscriptions

(re-frame/reg-sub
  ::current-time
  (fn [db _]
    (or (get-in db [:video :current-time]) 0.0)))

(re-frame/reg-sub
  ::mode
  (fn [db _]
    (:mode db)))

(re-frame/reg-sub
  ::current-entry
  (fn [db _]
    (let [time-seconds (-> db :video :current-time)]
      (->> db
           (:entries)
           (sort-by :timestamp)
           (take-while #(<= (:timestamp %) time-seconds))
           (last)))))
