package query.repository



import com.datastax.oss.driver.api.core.CqlSession
import config.AppConfig
import io.getquill.{CassandraAsyncContext, SnakeCase}
import model.Student
import play.api.Logging
import query.persistence.ApplicationDetails
import query.services.CassandraConnector

import java.net.InetSocketAddress
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext


class ApplicationRepository @Inject() (conf: AppConfig)(implicit val ec: ExecutionContext)
//  extends CassandraConnect
{
  val dbSession = CqlSession.builder()
    .addContactPoint(new InetSocketAddress(conf.hosts, conf.port))
    .withLocalDatacenter("DC1").build();

  val connection = new CassandraAsyncContext(SnakeCase, "smis")
 import connection._

  def registerStudent(student: Student) = {

    val applicationNumber = student.applicationNumber.fold[String](
      throw new IllegalStateException("Application Number not defined."))(s => s)

    val addQuery = quote {
      query[ApplicationDetails].insert(
        _.application_number -> lift(applicationNumber),
        _.dob -> lift(student.dob),
        _.city -> lift(student.address.city),
        _.county -> lift(student.address.county),
        _.surname -> lift(student.surname),
        _.first_name -> lift(student.firstName),
        _.house_number -> lift(student.address.houseNumber),
        _.post_code -> lift(student.address.postCode),
        _.primary_guardian_name -> lift(student.primaryGuardianName),
        _.secondary_guardian_name -> lift(student.secondaryGuardianName),
        _.primary_telephone_num -> lift(student.primaryTelephoneNum),
        _.street_name -> lift(student.address.streetName)
      )
    }
    connection.run(addQuery)
  }

}
