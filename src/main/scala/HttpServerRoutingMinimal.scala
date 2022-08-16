package hello.ahd

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._

import scala.concurrent.ExecutionContextExecutor
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

				val binding = Http().newServerAt(host, port).bind(route)

				binding.onComplete {
					case Success(binding) =>
						val address = binding.localAddress
						system.log.info(
							"Server running at {}:{}",
							address.getHostString,
							address.getPort
						)
					case Failure(ex) =>
						system.log.error("Failed to bind server, terminating system", ex)
						system.terminate()
				}

				sys.addShutdownHook {
					println(" Au revoir!")
				}
		}
	}
}