import slick.jdbc.SQLiteProfile.api._
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Main extends App {

  case class Record(id: Int, datetime: LocalDateTime)

  implicit val localDateTimeColumnType = MappedColumnType.base[LocalDateTime, String](
    { dt => dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) },
    { str => LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) }
  )

  class TestdataTable(tag: Tag) extends Table[Record](tag, "testdata") {
    def id = column[Int]("id")

    def datetime = column[LocalDateTime]("datetime")

    def * = (id, datetime) <> (Record.tupled, Record.unapply)
  }

  val testdata = TableQuery[TestdataTable]

  val db = Database.forURL(
    url = "jdbc:sqlite:../test.db",
    driver = "org.sqlite.JDBC"
  )

  val start = System.nanoTime()

  val action = testdata.result
  val records = Await.result(db.run(action), Duration.Inf)

  val end = System.nanoTime()
  val elapsedSec = (end - start) / 1e9
  println(s"Read ${records.size} records in $elapsedSec sec")

  records.headOption.foreach { first =>
    println(s"First record => id: ${first.id}, datetime: ${first.datetime}")
  }

  db.close()
}
