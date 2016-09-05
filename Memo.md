


### 学習データの生成

##### 2次元の場合

```
ruby -e 'puts "x,y";Range.new(0,5).step(0.05).each {|i| puts "#{i},#{Math.sin(i)+Random.rand(-0.3..0.3)}"}' > src/main/resources/data.csv
```

##### 3次元の場合

```
ruby -e 'r= ->{Random.rand(-0.3..0.3)};puts "x,y,z";Range.new(0,5).step(0.05).each {|i| puts "#{i},#{Math.sin(i)+r.call},#{Math.sin(i)+r.call}"}' > src/main/resources/data.csv
```

### 学習データのplot

```
gnuplot -e 'set datafile separator ","; set title "sin(x)+ε"; set key autotitle columnhead; plot "src/main/resources/data.csv"'
```

### 結果のplot

```
gnuplot -e 'set datafile separator ","; set title "sin(x)+ε"; set key autotitle columnhead; plot "src/main/resources/data.csv", "tmp.csv/part-r-000-babecf59-fc0a-4633-a6ef-e956aad9b557.csv", sin(x)'
```
