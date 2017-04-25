package autowire

import java.io.Writer

import utest._
import utest.framework._
import utest.framework.ExecutionContext.RunNow
//import upickle.Js
//import upickle.default._
import acyclic.file

import scala.concurrent.Future

object Test extends TestSuite {

  val tests = TestSuite {
    'example {

      trait MyApi {
        val subApiString: SubApi[String]
      }

      trait SubApi[T] {
        def fooFighter(s: T): String // does not compile
//        def fooFighterString(s:String): String // compiles fine
      }

      // server-side implementation, and router
      object MyApiImpl extends MyApi {

        override val subApiString: SubApi[String] = null
      }

      trait MyReader[T] {}

      implicit val mr: MyReader[String] = ???
      implicit val mi: MyReader[Int] = ???

      implicit val msw: MyWriter[Seq[String]] = ???
      implicit val mwsw: MyWriter[String] = ???

      trait MyWriter[T] {}

      // we serialize into String
      trait MyServer extends autowire.Server[String, MyReader, MyWriter] {
        def write[Result: MyWriter](r: Result): String = ???

        def read[Result: MyReader](p: String): Result = ???

      }

      object MyServerObj extends MyServer{

        val routes: PartialFunction[Core.Request[String], Future[String]] = MyServerObj.route[MyApi](MyApiImpl)
      }


    }

  }
}

