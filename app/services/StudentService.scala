package services

import actors.StudentActor.{RegisterStudentCommand, StudentRegistrationFailed, StudentRegistrationSuccess}
import actors.StudentManager
import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import errors.NotFoundException
import model.{Address, Student}
import play.api.{Logger, MarkerContext}

import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import scala.concurrent.{Await, ExecutionContext, Future}

class StudentService @Inject()(system: ActorSystem)
(implicit ec: ExecutionContext) {

  val logger = Logger(this.getClass())

  val address1 = Address(46, "Park Avenue", "Newcastle", None, "NE4 7WE")
  val address2 = Address(2, "Eat End", "Newcastle", None, "NE12 7AB")
  val dateFormat = new SimpleDateFormat("dd-MM-yyyy")
  val students = List(
    Student(id = Some("123"), firstName = "Mauro", surname = "Fanton", dob = dateFormat.parse("02-05-2019"),
      address = address1, primaryGuardianName = "Anna", primaryTelephoneNum = "89676"),
    Student(id = Some("4"), firstName = "Jon", surname = "McAnthony", dob = dateFormat.parse("09-12-2018"),
      address = address2, primaryGuardianName = "Angela", primaryTelephoneNum = "56456")
  )

  implicit val timeout = Timeout(15, TimeUnit.SECONDS)
  lazy val studentManager = Await.result(system.actorSelection("user/" + StudentManager.Name).resolveOne(), timeout.duration)


  def lookUp(id: String)(
    implicit mc: MarkerContext): Future[Option[Student]] = {

    Future {
      students.find(s => s.id == Some(id)) match {
        case None => throw NotFoundException(s"Student with id: $id not found")
        case other => other
      }
    }
  }

  def getAll(implicit mc: MarkerContext): Future[Iterable[Student]] = {
    Future { students }
  }

  def addStudent(studentInfo: Student): Future[Option[Student]] = {
    Future {
      val newStudents = studentInfo :: students
      Option(studentInfo)
    }
  }

  def addStudentDto(student: Student): Future[Option[Student]] = {
    studentManager ask RegisterStudentCommand(student) map {
      case StudentRegistrationSuccess(student) => Some(student)
      case StudentRegistrationFailed(cause) =>
        logger.error(s"Error occurred while registering student: ${cause.getMessage}")
        None
    }
  }

//  def addStudentDto1(student: Student): Future[Option[Student]] = {
//    persistentStudent ? RegisterStudentCommand(student) map {
//      case StudentRegistrationSuccess(student) => Some(student)
//      case StudentRegistrationFailed(cause) =>
//        logger.error(s"Error occurred while registering student: ${cause.getMessage}")
//        None
//    }
//  }
}

