package projections


import EventHandlers.SMISProjectionHandler
import actors.TaggedEvent
import projections.StudentProjection.ProjectionName

import javax.inject.Singleton
//import akka.actor.ActorSystem
//import akka.actor.ActorSystem
import akka.actor.typed.ActorSystem
import akka.persistence.cassandra.query.scaladsl.CassandraReadJournal
import akka.persistence.query.Offset
import akka.projection.ProjectionId
import akka.projection.cassandra.scaladsl.CassandraProjection
import akka.projection.eventsourced.EventEnvelope
import akka.projection.eventsourced.scaladsl.EventSourcedProvider
import akka.projection.scaladsl.{AtLeastOnceProjection, SourceProvider}
import constants.EventsTags
import query.repository.ApplicationRepository

import javax.inject.Inject
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global

class StudentProjection @Inject()
(
  handler: SMISProjectionHandler
)(implicit val ex: ExecutionContext ){

  def sourceProvider(system: ActorSystem[_], tag: String): SourceProvider[Offset, EventEnvelope[TaggedEvent]] =
    EventSourcedProvider
      .eventsByTag[TaggedEvent](
        system = system,
        readJournalPluginId = CassandraReadJournal.Identifier,
        tag = EventsTags.STUDENT_REGISTERED_EVENT)

  def projection(system: ActorSystem[_], tag: String): AtLeastOnceProjection[Offset, EventEnvelope[TaggedEvent]] =
    CassandraProjection
      .atLeastOnce(
        projectionId = ProjectionId(ProjectionName, EventsTags.STUDENT_REGISTERED_EVENT),
        sourceProvider(system, tag),
        handler = () => handler)

}

object StudentProjection {
  val ProjectionName: String = "smis"
}
