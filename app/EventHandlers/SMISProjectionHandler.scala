package EventHandlers

import actors.StudentActor.StudentRegisteredEvent
import actors.TaggedEvent
import akka.Done
import akka.actor.ActorSystem
import akka.projection.eventsourced.EventEnvelope
import akka.projection.scaladsl.Handler
import play.api.Logger
import query.repository.ApplicationRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


class SMISProjectionHandler @Inject()
(
  repo: ApplicationRepository
)(implicit val ec: ExecutionContext)
  extends Handler[EventEnvelope[TaggedEvent]]() {

  private val logger = Logger(this.getClass)

  override def process(envelope: EventEnvelope[TaggedEvent]): Future[Done] = {

    val processed = envelope.event match {
      case StudentRegisteredEvent(student) => Future.successful {
        repo.registerStudent(student)
          .onComplete(_ match {
                case Success(_) =>
                  logger.info(s"Successfully inserted application: ${student.applicationNumber} into projection")
                case Failure(e) =>
                  logger.error(s"Projection: Error inserting application: ${student.applicationNumber}", e)
              })

        Done
      }
      case _ => {
        logger.error("Unsupported event when saving projections")
        throw new UnsupportedOperationException("Unsupported event")
      }

    }
    processed.onComplete {
      case Success(a) => logger.info(s"Updated projection ${a.toString}")
      case Failure(e) => logger.error("Error Processing Event", e)
    }
    processed
  }
}
