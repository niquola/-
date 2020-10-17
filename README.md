# 禪

## 1. Reactive db

db is atom
there are **views** - (aka re-frame subs)
as a pure function of paths in db

```clj
(defn my-sub 
   {:z/paths 
     {:user [:user], 
      :data [:my :data]}
    :z/subs 
      {:osub #'other-sub}}
   [state]
   ......)
```

There are db transaction effects like:

:assoc-in :dissoc :update-in

While zen applies transaction  - 
it calculates which views may change and
re-evaluate them.

For example we do tx assoc :key and re-evaluate views 
with paths [:key ...]

# 2. render layer

render is dumb - i.e. no calculations in render


```clj
(def my-page 
 {z/views {:key #'view}
  z/render
  {:tag :div
   :text :view/key
   :on-click :view/key
   :child {:when :view/key :html :v/key}
   :if?
   :for-each?
   }
)
```
