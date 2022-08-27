package config

import com.google.inject.Inject
import play.api.Configuration

import javax.inject.Singleton

@Singleton
class AppConfig @Inject() (configuration: Configuration) {

  lazy val keyspace = configuration.getOptional[String]("cassandra.keyspace").getOrElse("smis")
  lazy val hosts = configuration.getOptional[String]("cassandra.hosts").getOrElse("localhost")
  lazy val port = configuration.getOptional[Int]("cassandra.port").getOrElse(9042)
  lazy val preparedStatementCacheSize = configuration.getOptional[Int]("cassandra.preparedStatementCacheSize")
    .getOrElse(100)
  lazy val localDataCenter = configuration.getOptional[String]("cassandra.data-center").getOrElse("DC1")
}

