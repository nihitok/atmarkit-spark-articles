import org.apache.spark.sql.SparkSession                                                                                   
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.ml.feature._
import org.apache.spark.ml.regression.LinearRegression

object SparkExampleApp {

  def main(args: Array[String]) {
    // SparkSessionオブジェクトの作成
    val spark = SparkSession
      .builder()
      .appName("Spark Sample App")                                                                                                    
      .getOrCreate()

    import spark.implicits._ // データフレームを扱うためのおまじない

    // この後に処理を書いていきます。

    // AWSのAccessKeyの設定
    spark.sparkContext.hadoopConfiguration.set("fs.s3n.awsAccessKeyId", "*********")
    spark.sparkContext.hadoopConfiguration.set("fs.s3n.awsSecretAccessKey", "*********")

    // csvからデータを読み込みデータフレームの作成
    val filePath = args(0)
    val df = spark.sqlContext.read
        .format("com.databricks.spark.csv") // データのフォーマットを指定します。
        .option("header", "true") // カラム名をデータフレームのスキーマに反映します。
        .option("inferSchema", "true") // 型の自動変換も行います。
        .load(filePath)

    // Pipelineの各要素を作成

    // 1. 素性のベクトルを生成します
    val assembler = new VectorAssembler()
      .setInputCols(Array("x")) // 説明変数を指定します。
      .setOutputCol("features")

    // 2. 素性のベクトルを多項式にします
    val polynomialExpansion = new PolynomialExpansion()
      .setInputCol(assembler.getOutputCol) // featuresの文字列です。
      .setOutputCol("polyFeatures")
      .setDegree(4)

    // 3. 線形回帰の予測器を指定します
    val linearRegression = new LinearRegression()
      .setLabelCol("y") // 目的変数です。
      .setFeaturesCol(polynomialExpansion.getOutputCol) // polyFeaturesの文字列です。
      .setMaxIter(100) // 繰り返しが100回
      .setRegParam(0.0) // 正則化パラメータ

    // 1~3を元にパイプラインオブジェクトを作ります
    val pipeline = new Pipeline()
      .setStages(Array(assembler, polynomialExpansion, linearRegression))


    val Array(trainingData, testData) = df.randomSplit(Array(0.7, 0.3))
    val model = pipeline.fit(trainingData) // 学習データを指定します。

    // csvに保存
    val outputFilePath = args(1)
    model.transform(testData) // テストデータを指定します
      .select("x", "prediction")
      .write
      .format("com.databricks.spark.csv") // データのフォーマットを指定します。
      .option("header", "false") // ヘッダーにカラム名をつけるか
      .save(outputFilePath) // ファイルの保存先です。

  }
}
