package actors

import ErrorHandler.StudentException
import akka.NotUsed
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.event.LoggingReceive
import akka.persistence.cassandra.query.scaladsl.CassandraReadJournal
import akka.persistence.query.{EventEnvelope, Offset, PersistenceQuery}
import akka.persistence.typed.PersistenceId
import akka.stream.Materializer
import akka.stream.scaladsl.Source
import constants.EventsTags
import dto.State

import java.util.UUID
import scala.annotation.tailrec
import scala.concurrent.ExecutionContext

object StudentManager {

  val ID = "sm_id_" + UUID.randomUUID().toString
  val NAME = "student_manager"
}

class StudentManager(
  val id: String
) (implicit  ec: ExecutionContext)
  extends Actor with ActorLogging {

  import StudentActor._

  private var state = State()

  val queries = PersistenceQuery(context.system).readJournalFor[CassandraReadJournal](CassandraReadJournal.Identifier)
  val students: Source[EventEnvelope, NotUsed] = queries.eventsByTag(EventsTags.STUDENT_REGISTERED_EVENT, Offset.noOffset)
  implicit val mat = Materializer(context)

  students.runForeach { env =>
    env.event match {
      case StudentRegisteredEvent(student) if(!state.contains(student.applicationNumber)) => {
        state = state.updateState(student.applicationNumber)
      }
      case _ => println(s"Unknown event $env")
    }}



  override def receive: Receive =
    LoggingReceive {

      case RegisterStudentCommand(student) =>
          val persistenceId = createUniqueApplicationNumber
          getStudentActor(persistenceId) forward RegisterStudentCommand(student.copy(applicationNumber = Some(persistenceId)))

      case _ =>  sender() ! StudentRegistrationFailed(StudentException("Invalid Command."))
    }

//  def onEvent: Receive = {
//    case StudentRegisteredEvent(student) =>
//      StudentsInSystem +: student.applicationNumber.getOrElse("")
//  }

  private def getStudentActor(appNumber: String): ActorRef = {

    val name = s"regStudent_$appNumber"
    context.child(name) match {
      case Some(actorRef) => actorRef
      case None => context.actorOf(Props(new StudentActor(PersistenceId.ofUniqueId(appNumber).id)), name)
    }
  }

  private def createUniqueApplicationNumber: String = {

    @tailrec
    def loop(state: State, acc: String): String = {
      if(!state.contains(Some(acc))) acc
      else loop(state, UUID.randomUUID().toString)
    }

    loop(state, UUID.randomUUID().toString)
  }
}
