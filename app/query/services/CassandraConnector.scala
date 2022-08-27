package query.services

import com.datastax.oss.driver.api.core.CqlSession
import config.AppConfig
import io.getquill.{CassandraAsyncContext, SnakeCase}
import play.api.Logging

import java.net.InetSocketAddress
import javax.inject.{Inject, Singleton}

abstract class CassandraConnector
  extends Logging {

  logger.info("#########")
//  println(s"HOST => ${appConfig.hosts}")
//  println(s"PORT => ${appConfig.port}")
//  println(s"localDataCenter => ${appConfig.localDataCenter}")
  logger.info("#########")
  val dbSession = CqlSession.builder()
    .addContactPoint(new InetSocketAddress("localhost", 9042))
    .withLocalDatacenter("DC1").build();

  val connection = new CassandraAsyncContext(SnakeCase, "smis" )
}
