package controllers

import scala.concurrent.ExecutionContext.Implicits.global

import org.joda.time.DateTimeZone
import play.api.mvc._
import services.{SunService, WeatherService}

class Application
  extends Controller {
  val sunService = new SunService
  val weatherService = new WeatherService

  def index: Action[AnyContent] = Action.async {
    val lusakaLatitude: Double = -1.2920659
    val lusakaLongitude: Double = 36.8219462
    val lusakaTimezoneName = "Africa/Lusaka"

    for {
      sunInfo <- sunService.getSunInfo(lusakaLatitude, lusakaLongitude, DateTimeZone.forID(lusakaTimezoneName))
      temperature <- weatherService.getTemperature(lusakaLatitude, lusakaLatitude)
    } yield {
      Ok(views.html.index(sunInfo, temperature))
    }
  }
}
