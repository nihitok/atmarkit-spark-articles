name := "Spark Sample Project" // プロジェクトの名前

version := "1.0" // プロジェクトのバージョン

scalaVersion := "2.11.8" // 使用するscalaのバージョン(執筆時点で最新)

// 依存ライブラリは下記に記述します
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.0.0",
  "org.apache.spark" %% "spark-mllib" % "2.0.0",
  "org.apache.spark" %% "spark-sql" % "2.0.0",
  "com.databricks" %% "spark-csv" % "1.4.0"
)
