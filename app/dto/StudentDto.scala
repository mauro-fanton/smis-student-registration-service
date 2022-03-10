package dto

import model.{Activity, Address}
import play.api.libs.json._

import java.util.Date

case class StudentDto
(
  id: Option[String] = None,
  applicationNumber: Option[String] = None,
  firstName: String = "",
  surname: String = "",
  dob: Date = null,
  address: Address = null,
  primaryGuardianName: String = "",
  secondaryGuardianName: Option[String] = None,
  primaryTelephoneNum: String = "",
  secondaryTelephoneNum: Option[String] = None,
  activities: Option[List[Activity]] = None)

object StudentDto {
  /**
   * Mapping to read/write a PostResource out as a JSON value.
   */
  implicit val addressFormat: Format[Address] = Json.format[Address]
  implicit val activityFormat: Format[Activity] = Json.format[Activity]
  implicit val studentFormat: Format[StudentDto] = Json.format[StudentDto]
}