package services

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import model.SunInfo
import org.joda.time.{DateTime, DateTimeZone}
import org.joda.time.format.DateTimeFormat
import play.api.libs.ws.WS
import play.api.Play.current

class SunService {
  def getSunInfo(latitude: Double, longitude: Double, timeZone: DateTimeZone): Future[SunInfo] = {
    val sunInfoResponse = WS.url(s"http://api.sunrise-sunset.org/json?lat=$latitude&lng=$longitude&formatted=0").get()

    sunInfoResponse.map({response =>
      val sunInfoResponseJson = response.json

      val sunriseTimeStr = (sunInfoResponseJson \ "results" \ "sunrise").as[String]
      val sunriseTime = DateTime.parse(sunriseTimeStr)
      val sunsetTimeStr = (sunInfoResponseJson \ "results" \ "sunset").as[String]
      val sunsetTime = DateTime.parse(sunsetTimeStr)

      val lusakaTimezoneFormatter = DateTimeFormat.forPattern("HH:mm:ss")
        .withZone(timeZone)

      val sunInfo = SunInfo(lusakaTimezoneFormatter.print(sunriseTime),
        lusakaTimezoneFormatter.print(sunsetTime), timeZone.toString)

      sunInfo
    })
  }
}
