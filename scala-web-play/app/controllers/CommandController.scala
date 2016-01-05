package controllers

import java.io.File

import scala.sys.process.Process
import scala.sys.process._

import play.api.mvc.{Action, Controller}

class CommandController extends Controller {

  def vulnerable1(value:String) = Action {
    val pb = Process(value)
    pb.run()
    Ok("Result:\n"+"done")
  }

  def vulnerable2(value:String) = Action {
    val pb = Process(value)
    val result = pb.!
    Ok("Result:\n"+result)
  }

  def vulnerable3(value:String) = Action {
    val pb = Process(value)
    val result = pb.!!
    Ok("Result:\n"+result)
  }

  def vulnerable4(value:String) = Action {
    val result = value.!
    Ok("Result:\n"+result)
  }

  def vulnerable5(value:String) = Action {
    val result = value.!!
    Ok("Result:\n"+result)
  }

  def vulnerable6(value:String) = Action {
    val result = Process(value).lines.mkString("\n")
    Ok("Result:\n"+result)
  }

  def extendedVulnerable1(filename:String) = Action {
    val result = "find src -name "+filename+" -exec grep null {} ;"  #|  "xargs test -z"  #&&  "echo null-free"  #||  "echo null detected"  !!;
    Ok("Result:\n"+result)
  }

  def various(value:String) = Action {
    Process(Seq("ls",value)).run()

    Process(value,Seq("-la","/test")).run()
    Process("ls",Seq(value,"/test")).run()
    Process("ls",Seq("-la",value)).run()
    Process("ls",Seq("-la","/test")).run() //Safe

    Process(value,new File("."),("extra","1234")).run()
    Process("ls",new File(value),("extra","1234")).run()
    Process("ls",new File("."),(value,"1234")).run()
    Process("ls",new File("."),("extra",value)).run()
    Process("ls",new File("."),("extra","1234")).run() //Safe **

    Process(Seq(value),new File("."),("extra","1234")).run()
    Process(Seq("ls"),new File(value),("extra","1234")).run()
    Process(Seq("ls"),new File("."),(value,"1234")).run()
    Process(Seq("ls"),new File("."),("extra",value)).run()
    Process(Seq("ls"),new File("."),("extra","1234")).run() //Safe **

    Process(Seq(value),None,("extra","1234")).run()
    Process(Seq("ls"),None,(value,"1234")).run()
    Process(Seq("ls"),None,("extra",value)).run()
    Process(Seq("ls"),None,("extra","1234")).run() //Safe **

    Process(value,1)
    Process("ls",1) //Safe
    Process("ls",value.toInt) //Safe

    Seq("ls",value).!
    Seq("ls",value).!!

    Ok("Result:\nok")
  }

  def safe1() = Action {
    val pb = Process("ls")
    val result = pb.!
    Ok("Result:\n"+result)
  }

  def extendedSafe1(filename:String) = Action {
    val result = "find src -name *.txt -exec grep null {} ;"  #|  "xargs test -z"  #&&  "echo null-free"  #||  "echo null detected"  !!;
    Ok("Result:\n"+result)
  }
}
