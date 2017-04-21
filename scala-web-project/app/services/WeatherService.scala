package services

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import play.api.libs.ws.WS
import play.api.Play.current

class WeatherService {
  def getTemperature(latitude: Double, longitude: Double): Future[Double] = {
    val weatherServiceResponse = WS.url(s"http://api.openweathermap.org/data/2.5/weather?lat=$latitude" +
      s"&lon=$longitude&APPID=&units=metric").get()

    weatherServiceResponse.map({ response =>
      val weatherJson = response.json
      val temperature = (weatherJson \ "main" \ "temp").as[Double]
      temperature
    })
  }
}
