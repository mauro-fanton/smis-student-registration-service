package controllers

import net.logstash.logback.marker.LogstashMarker
import play.api.{Logger, MarkerContext}
import play.api.http.{FileMimeTypes, HttpVerbs}
import play.api.i18n.{Langs, MessagesApi}
import play.api.mvc.{ActionBuilder, AnyContent, BaseController, BodyParser, ControllerComponents, DefaultActionBuilder, MessagesRequestHeader, PlayBodyParsers, PreferredMessagesProvider, Request, RequestHeader, Result, WrappedRequest}
import services.StudentService

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

trait StudentRequestHeader
  extends MessagesRequestHeader
    with PreferredMessagesProvider
class StudentRequest[A](request: Request[A], val messagesApi: MessagesApi)
  extends WrappedRequest(request)
    with StudentRequestHeader

/**
 * Provides an implicit marker that will show the request in all logger statements.
 */
trait RequestMarkerContext {
  import net.logstash.logback.marker.Markers

  private def marker(tuple: (String, Any)) = Markers.append(tuple._1, tuple._2)

  private implicit class RichLogstashMarker(marker1: LogstashMarker) {
    def &&(marker2: LogstashMarker): LogstashMarker = marker1.and(marker2)
  }

  implicit def requestHeaderToMarkerContext(implicit request: RequestHeader): MarkerContext = {
    MarkerContext {
      marker("id" -> request.id) && marker("host" -> request.host) && marker(
        "remoteAddress" -> request.remoteAddress)
    }
  }

}

/**
 * The action builder for the Post resource.
 *
 * This is the place to put logging, metrics, to augment
 * the request with contextual data, and manipulate the
 * result.
 */
class StudentActionBuilder @Inject()(messagesApi: MessagesApi,
                                     playBodyParsers: PlayBodyParsers)(
                                   implicit val executionContext: ExecutionContext)
  extends ActionBuilder[StudentRequest, AnyContent]
    with RequestMarkerContext
    with HttpVerbs {

  override val parser: BodyParser[AnyContent] = playBodyParsers.anyContent

  type RequestBlock[A] = StudentRequest[A] => Future[Result]

  private val logger = Logger(this.getClass)

  override def invokeBlock[A](request: Request[A],
                              block: RequestBlock[A]): Future[Result] = {
    // Convert to marker context and use request in block
    implicit val markerContext: MarkerContext = requestHeaderToMarkerContext(
      request)
    logger.trace(s"invokeBlock: ")

    val future = block(new StudentRequest(request, messagesApi))

    future.map { result =>
      request.method match {
        case GET | HEAD =>
          result.withHeaders("Cache-Control" -> s"max-age: 100")
        case other =>
          result
      }
    }
  }
}

case class StudentControllerComponents @Inject()
(
  studentActionBuilder: StudentActionBuilder,
  studentService: StudentService,
  actionBuilder: DefaultActionBuilder,
  parsers: PlayBodyParsers,
  messagesApi: MessagesApi,
  langs: Langs,
  fileMimeTypes: FileMimeTypes,
  executionContext: scala.concurrent.ExecutionContext
) extends ControllerComponents
/**
 * Exposes actions and handler to the PostController by wiring the injected state into the base class.
 */
class StudentBaseController @Inject()(pcc: StudentControllerComponents)
  extends BaseController
    with RequestMarkerContext {
  override protected def controllerComponents: ControllerComponents = pcc

  def StudentAction: StudentActionBuilder = pcc.studentActionBuilder

  def studentService: StudentService = pcc.studentService
}
