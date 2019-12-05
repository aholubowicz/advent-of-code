(defn to-digits [x]
  (map read-string (clojure.string/split x #"")))

(defn the-same [digits]
  (some #{2} (map count (partition-by identity digits))))

(defn password? [x]
  (let [digits (to-digits (.toString x))]
  (and (the-same digits) (apply <= digits))))

(count (filter password? (range 353096 843213)))
