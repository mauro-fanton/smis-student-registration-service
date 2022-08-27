package query.persistence

import java.time.LocalDate


case class ApplicationDetails(
  application_number: String,
  first_name: String,
  surname: String,
  dob: LocalDate,
  house_number: Int,
  street_name: String,
  city: String,
  county: Option[String] = None,
  post_code: String,
  primary_guardian_name: String,
  secondary_guardian_name: Option[String] = None,
  primary_telephone_num: String,
  secondary_telephone_num: Option[String] = None
//  activities: Option[List[Activity]] = None
)

