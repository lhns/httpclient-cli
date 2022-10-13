package de.lhns.httpclient

import caseapp.cats.IOCaseApp
import caseapp.core.RemainingArgs
import caseapp.core.app.CaseApp
import caseapp.core.argparser.{ArgParser, SimpleArgParser}
import cats.effect._
import cats.syntax.all._
import org.http4s.{Method, Request}
import org.http4s.curl.CurlApp
import cats.syntax.either._

case class Options(method: String,
                  )

object Options {
  //implicit val customArgParser: ArgParser[Method] =
  //  SimpleArgParser.from[String]("http method")(Method.fromString(_).leftMap(e =>))
}

object Main extends IOCaseApp[Options] with CurlApp {
  override def run(options: Options, remainingArgs: RemainingArgs): IO[ExitCode] = {
    println(options.toString).as(ExitCode.Success)
  }
  /*override def run(args: List[String]): IO[ExitCode] = {
    CaseApp.parse[Options](args)
    for {
      responses <- curlClient.stream(Request[IO](

      ))
      _ <- responses.traverse_(r => IO.println(r.joke))
    } yield ()
  }*/
}
