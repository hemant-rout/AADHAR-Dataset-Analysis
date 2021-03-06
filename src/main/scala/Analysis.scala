import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

object Analysis {

  val conf = new SparkConf().setAppName("Aadhar Data Analysis").setMaster("local")
  val sc = new SparkContext(conf)
  val sqlContext=new SQLContext(sc)

  import sqlContext. implicits._

  def main(args: Array[String]): Unit = {

    // Reading the data and created a Dataframe
    val data = sc.textFile("InputData/InputDataWithoutHeader")

    val datamap = data.map(rec => rec.split(","))

    val dataRDD = datamap.map(rec => Data(rec(0), rec(1), rec(2), rec(3), rec(4), rec(5), rec(6), rec(7).toInt,
                                          rec(8).toInt, rec(9).toInt, rec(10).toInt, rec(11).toInt))

    val dataDF = dataRDD.toDF()

    dataDF.registerTempTable("data")

    // Reviewing the data
    // sqlContext.sql("SELECT * FROM data LIMIT 10").show()

    // Getting the total and gender wise count of records
    /*sqlContext.sql("SELECT COUNT(*) FROM data WHERE Gender!='F' AND Gender!='M'").show()
    sqlContext.sql("SELECT COUNT(*) FROM data WHERE Gender!='M'").show()
    sqlContext.sql("SELECT COUNT(*) FROM data WHERE Gender!='F' AND Gender!='M'").show()*/

    // Count the number of identities(Aadhaar) generated by each Enrollment Agency and Get Top 3 agencies
    /*sqlContext
      .sql("SELECT EnrolmentAgency, SUM(AadharGenerated) AS TotalAadharCount FROM data GROUP BY EnrolmentAgency " +
        "ORDER BY TotalAadharCount DESC " +
        "LIMIT 3")
      .collect()
      .foreach(println)*/

    // Top 10 districts with maximum identities generated for both Male and Female
    /*sqlContext
      .sql("SELECT District, SUM(AadharGenerated) AS TotalAadharCount " +
           "FROM data " +
           "GROUP BY District " +
           "ORDER BY TotalAadharCount " +
           "LIMIT 10")
        .collect()
        .foreach(println)*/

    // Top 3 State With maximum identities generated for both Male and Female
      /*sqlContext
          .sql("SELECT State, COUNT(*) AS num" +
              " FROM (SELECT * FROM data WHERE Gender NOT IN ('T')) AS Temp " +
            "GROUP BY State " +
            "Order By num DESC " +
            "LIMIT 3")
          .take(10)
          .foreach(println)*/

    // Top 3 States With maximum identities generated for Female
    /*sqlContext
      .sql("SELECT State, COUNT(*) AS num" +
        " FROM (SELECT * FROM data WHERE Gender = 'F') AS Temp " +
        "GROUP BY State " +
        "Order By num " +
        "LIMIT 3")
      .collect()
      .foreach(println)*/



  }

  case class Data(Registrar: String, EnrolmentAgency: String, State: String, District: String, SubDistrict: String,
                  PinCode: String, Gender: String, Age: Int, AadharGenerated: Int, EnrolmentRejected: Int, EmailProvided: Int,
                  MobileNumProvided: Int)
}

