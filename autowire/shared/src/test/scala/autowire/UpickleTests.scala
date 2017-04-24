package autowire

import utest._
import utest.framework._
import utest.framework.ExecutionContext.RunNow
import upickle.Js
import upickle.default._
import acyclic.file

import scala.concurrent.Future

object Test extends TestSuite {

  val tests = TestSuite {
    'example {
      import upickle._

      // shared API interface
      trait MyApi {
        def doThing(i: Int, s: String): Seq[String]
//        val subApiString:SubApi[String]
      }

      trait SubApi[T]{
        def fooFighter(s:T):String
      }

      // server-side implementation, and router
      object MyApiImpl extends MyApi {
        def doThing(integerInput: Int, stringInput: String) = Seq.fill(integerInput)(stringInput)

//        override val subApiString: SubApi[String] =  null
      }

      object MyServer
          extends autowire.Server[String,
                                  upickle.default.Reader,
                                  upickle.default.Writer] {
        def write[Result: Writer](r: Result) = upickle.default.write(r)

        def read[Result: Reader](p: String) = upickle.default.read[Result](p)
//        def read[Result: Reader](p: String) = ???

//        case class Request[PickleType](path : Seq[String], args: Map[String, PickleType])
        val routes:PartialFunction[Core.Request[String], Future[String]] = MyServer.route[MyApi](MyApiImpl)
      }

//      // client-side implementation, and call-site
//      object MyClient
//          extends autowire.Client[String,
//                                  upickle.default.Reader,
//                                  upickle.default.Writer] {
//        def write[Result: Writer](r: Result) = upickle.default.write(r)
//
//        def read[Result: Reader](p: String) = upickle.default.read[Result](p)
//
//        override def doCall(req: Request) = {
//          println(req)
//          MyServer.routes.apply(req)
//        }
//      }
//
//      MyClient[MyApi].doThing(3, "lol").call().foreach(println)
    }

  }
}

