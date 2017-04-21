object GreetingPrintService {
  def printGreeting(name: String): Unit = {
    println(GreetingService.getGreeting(name))
  }
}
