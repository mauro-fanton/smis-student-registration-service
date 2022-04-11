package EventHandlers

import actors.StudentActor.StudentRegisteredEvent
import actors.TaggedEvent
import akka.Done
import akka.actor.typed.ActorSystem
import akka.projection.eventsourced.EventEnvelope
import akka.projection.scaladsl.Handler
import play.api.Logger

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success

class SMISProjectionHandler(system: ActorSystem[_])
  extends Handler[EventEnvelope[TaggedEvent]]() {

  private implicit val ec: ExecutionContext = system.executionContext
  private val logger = Logger(this.getClass)

  override def process(envelope: EventEnvelope[TaggedEvent]): Future[Done] = {

    val processed = envelope.event match {
      case StudentRegisteredEvent(student) =>
        logger.info(s"############################ $student")
        Future.successful(Done) // skip
    }
    processed.onComplete {
      case Success(a) => logger.info(s"Update projecttuion ${a.toString}")
      case _          => ()
    }
    processed
  }
}
