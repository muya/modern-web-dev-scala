package controllers

import scala.concurrent.ExecutionContext.Implicits.global

import model.SunInfo
import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTime, DateTimeZone}
import play.api.Play.current
import play.api.libs.ws.WS
import play.api.mvc._

class Application
  extends Controller {
  def index: Action[AnyContent] = Action.async {
    val lusakaLatitude = "-1.2920659"
    val lusakaLongitude = "36.8219462"
    val lusakaTimezoneName = "Africa/Lusaka"

    val sunInfoResponse = WS.url(s"http://api.sunrise-sunset.org/json?lat=$lusakaLatitude&lng=$lusakaLongitude" +
      s"&formatted=0").get()

    val weatherInfoResponse = WS.url(s"http://api.openweathermap.org/data/2.5/weather?lat=$lusakaLatitude" +
      s"&lon=$lusakaLongitude&APPID=24f54a5e19332bd52bf3a31c83a8c61d&units=metric").get()

    for {
      sunInfoData <- sunInfoResponse
      weatherInfoData <- weatherInfoResponse
    } yield {
      val sunInfoResponseJson = sunInfoData.json

      val sunriseTimeStr = (sunInfoResponseJson \ "results" \ "sunrise").as[String]
      val sunriseTime = DateTime.parse(sunriseTimeStr)
      val sunsetTimeStr = (sunInfoResponseJson \ "results" \ "sunset").as[String]
      val sunsetTime = DateTime.parse(sunsetTimeStr)

      val lusakaTimezoneFormatter = DateTimeFormat.forPattern("HH:mm:ss")
        .withZone(DateTimeZone.forID(lusakaTimezoneName))

      val sunInfo = SunInfo(lusakaTimezoneFormatter.print(sunriseTime),
        lusakaTimezoneFormatter.print(sunsetTime), lusakaTimezoneName)

      val weatherInfoJson = weatherInfoData.json
      val temperature = (weatherInfoJson \ "main" \ "temp").as[Double]

      Ok(views.html.index(sunInfo, temperature))
    }
  }
}
