(defn to-digits [x]
  (map read-string (clojure.string/split x #"")))

(defn the-same [digits]
  (some #(= (nth % 0) (nth % 1)) (partition 2 1 digits)))

(defn password? [x]
  (let [digits (to-digits (.toString x))]
  (and (the-same digits) (apply <= digits))))

(count (filter password? (range 353096 843213)))
