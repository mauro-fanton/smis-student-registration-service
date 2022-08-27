package controllers


import model.{Activity, Address, Student}
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.libs.ws._
import play.api.test._

import java.text.SimpleDateFormat
import java.time.LocalDate

class StudentRegistrationControllerTests extends PlaySpecification {

  val application: Application = GuiceApplicationBuilder().build()
  val ws = application.injector.instanceOf[WSClient]

  "Application" should {
    "be reachable" in new WithServer {

      val response = await(ws.url(s"http://localhost:${port}/smis/student/4").get())

      response.status must equalTo(OK)
    }
  }

  "Application" should {
    "return not found" in new WithServer {

      val response = await(ws.url(s"http://localhost:${port}/smis/student").get())

      response.status must equalTo(NOT_FOUND)
    }
  }

  "Application" should {
    "add a student" in new WithServer {

      val activities = Option(List(Activity("Coding Club"), Activity("Science Club")))
      val address = Address(houseNumber = 78, streetName = "Long Street", city = "Leeds", postCode = "NE23 7RT")
      val data1: Student = Student(
        id = Some("1"),
        applicationNumber = Some("123"),
        firstName = "Marc",
        surname = "Smith",
        dob = LocalDate.parse("05-07-2020"),
        primaryGuardianName = "Laura J0nson",
        primaryTelephoneNum = "07862367465",
        activities = activities,
        address = address)

      val response = await(ws.url(s"http://localhost:${port}/smis/register/student").post(Json.toJson(data1)))

      response.status must equalTo(OK)
    }
  }

}
