package model

import play.api.libs.json.{JsPath, Json, OFormat, Reads}
import play.api.libs.functional.syntax._

case class Activity(name: String,  description: Option[String] = None)

object Activity {
  implicit val activityFormat: OFormat[Activity] = Json.format[Activity]

  implicit val read: Reads[Activity] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "description").readNullable[String]
    )(Activity.apply _)
}
