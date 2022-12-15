(ns laba1.core
  (:require [promesa.core :as p]
            [clojure.core.async
             :as a
             :refer [>! <! >!! <!! go chan close! go-loop]]))

(defn vec-to-chan [vec]
  (let [c (chan)
        v (conj vec "end")]
    (go (doseq [x v]
          (>! c x))

        (close! c))
    c))

(defn main[c]
  (<!!
    (go-loop [Result [] Temp [] Count 0 i 0]
      (if-let [x (<! c)]
        (if (= i 0)
          (recur Result [] x (inc i))
          (do
            (if (= Count 0)
              (recur (conj Result Temp) [] x (inc i))
              (recur Result (conj Temp x) (dec Count) (inc i))
              ))
          )
        Result)
      )))

(def ch (vec-to-chan [3 4 0 2 1 2 2 4 5]))
(println "Result" (main ch))
