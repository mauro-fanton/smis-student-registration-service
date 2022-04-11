package projections


import EventHandlers.SMISProjectionHandler
import actors.TaggedEvent
import akka.actor.typed.ActorSystem
import akka.persistence.cassandra.query.scaladsl.CassandraReadJournal
import akka.persistence.query.Offset
import akka.projection.ProjectionId
import akka.projection.cassandra.scaladsl.CassandraProjection
import akka.projection.eventsourced.EventEnvelope
import akka.projection.eventsourced.scaladsl.EventSourcedProvider
import akka.projection.scaladsl.{AtLeastOnceProjection, SourceProvider}

object StudentProjection {

  val ProjectionName: String = "smis"

  def sourceProvider(system: ActorSystem[_], tag: String): SourceProvider[Offset, EventEnvelope[TaggedEvent]] =
    EventSourcedProvider
      .eventsByTag[TaggedEvent](
        system = system,
        readJournalPluginId = CassandraReadJournal.Identifier,
        tag = tag)

  def projection(system: ActorSystem[_], tag: String): AtLeastOnceProjection[Offset, EventEnvelope[TaggedEvent]] =
    CassandraProjection
      .atLeastOnce(
        projectionId = ProjectionId(StudentProjection.ProjectionName, tag),
        sourceProvider(system, tag),
        handler = () => new SMISProjectionHandler(system))

}
