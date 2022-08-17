package hello.ahd

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn
import scala.util.{Failure, Success, Try}

object HttpServerRoutingMinimal {

	def main(args: Array[String]): Unit = {

		Try(args(0), args(1).toInt) match {
			case Failure(_) => throw new IllegalArgumentException("usage: <cmd> <host> <port>")
			case Success((host, port)) =>

				implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "actor-system")
				implicit val ec: ExecutionContextExecutor = system.executionContext

				val route =
					path("hello") {
						get {
							complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Bonjour!</h1>"))
						}
					}

				val bindingFuture = Http().newServerAt(host, port).bind(route)

				println(s"Server now online. Please navigate to http://localhost:$port/hello\nPress RETURN to stop...")

				StdIn.readLine() // let it run until user presses return
				bindingFuture
					.flatMap(_.unbind()) // trigger unbinding from the port
					.onComplete(_ => system.terminate()) // and shutdown when done

		}
	}
}