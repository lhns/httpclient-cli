package de.lhns.httpclient

import caseapp.Parser
import caseapp.core.Error.MalformedValue
import caseapp.core.app.CaseApp
import caseapp.core.argparser.{ArgParser, SimpleArgParser}
import cats.data.EitherT
import cats.effect._
import cats.syntax.all._
import org.http4s.curl.CurlApp
import org.http4s.{Header, Headers, Method, Request, Uri}
import org.typelevel.ci.CIString

case class Options(
                    method: Method,
                    url: Uri,
                    header: Seq[Header.Raw]
                  )

object Options {
  private implicit val methodParser: ArgParser[Method] = SimpleArgParser.from("http method")(
    Method.fromString(_)
      .leftMap(e => MalformedValue("http method", e.message))
  )

  private implicit val uriParser: ArgParser[Uri] = SimpleArgParser.from("url")(
    Uri.fromString(_)
      .leftMap(e => MalformedValue("url", e.message))
  )

  private implicit val headerParser: ArgParser[Header.Raw] = SimpleArgParser.from("http header")(string =>
    string.split("\\s*:\\s*", 2).toSeq match {
      case Seq(name, value) => Right(Header.Raw(CIString(name), value))
      case _ => Left(MalformedValue("http header", "must match pattern `name:value`"))
    }
  )

  implicit val parser: Parser[Options] = {
    import caseapp._
    Parser[Options]
  }
}

object Main extends CurlApp {
  override def run(args: List[String]): IO[ExitCode] =
    (for {
      (options, _) <- EitherT(IO(CaseApp.parse[Options](args)))
        .leftMap(_.message)
      _ <- EitherT.right[String](curlClient.run(Request[IO](
        method = options.method,
        uri = options.url,
        headers = Headers(options.header.map[Header.ToRaw](e => e): _*)
      )).use { response =>
        response.body
          .through(
            if (response.status.isSuccess)
              fs2.io.stdout
            else
              fs2.io.stderr
          )
          .compile
          .drain
      })
    } yield ())
      .fold(_ => ExitCode.Error, _ => ExitCode.Success)
}
