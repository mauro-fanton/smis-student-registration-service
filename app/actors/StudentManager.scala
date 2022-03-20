package actors

import ErrorHandler.StudentException
import actors.SupervisorActor.createSupervisorWithOnStopStrategy
import akka.NotUsed
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.event.LoggingReceive
import akka.persistence.cassandra.query.scaladsl.CassandraReadJournal
import akka.persistence.query.{EventEnvelope, Offset, PersistenceQuery}
import akka.stream.Materializer
import akka.stream.scaladsl.Source
import constants.EventsTags
import dto.State

import java.util.UUID
import scala.annotation.tailrec
import scala.concurrent.ExecutionContext

object StudentManager {
  val ID = "sm_id_" + UUID.randomUUID().toString
  val Name = "student_manager"
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
      case StudentRegisteredEvent(student) if(!state.contains(student.applicationNumber)) =>
        state = state.updateState(student.applicationNumber)
      case _ => println(s"Unknown event $env")
    }}


  override def preStart() = {
    log.info("Starting actor: {}", StudentManager.Name)
  }

  override def receive: Receive =
    LoggingReceive {
      case RegisterStudentCommand(student) =>
          log.info("Handling RegisterStudentCommand command,")
          getStudentActor forward RegisterStudentCommand(
            student.copy(applicationNumber = Some(createUniqueApplicationNumber))
          )
      case _ =>  {
        log.error("Invalid Command")
        sender() ! StudentRegistrationFailed(StudentException("Invalid Command."))
      }
    }

//  def onEvent: Receive = {
//    case StudentRegisteredEvent(student) =>
//      StudentsInSystem +: student.applicationNumber.getOrElse("")
//  }

  private def getStudentActor: ActorRef = {
    log.info("Getting Actor: {}", StudentActor.Name)

    context.child(StudentActor.Name) match {
      case Some(actorRef) => actorRef
      case None =>
        val childProps = Props(new StudentActor(StudentActor.Id))
        context.actorOf(createSupervisorWithOnStopStrategy(childProps), StudentActor.Name)
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
