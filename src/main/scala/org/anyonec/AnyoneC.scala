package org.anyonec

import java.io.FileOutputStream

import org.anyonec.ast.RootNode

object AnyoneC {
  def main(args: Array[String]) {
    if (args.length < 1) {
      println("Usage: ArnoldC [-language Arnold|Bruce] [-run|-declaim] [FileToSourceCode]")
      return
    }
    val filename = getFilNameFromArgs(args)
    val language = getLanguageFromArgs(args)
    val sourceCode = scala.io.Source.fromFile(filename).mkString
    val generator  = new LanguageGenerator
     val classFilename = if (filename.contains('.')) {
      filename.replaceAll("\\.[^.]*$", "")
    }
    else {
      filename
    }
    val (bytecode, root) = generator.generate(language, sourceCode, classFilename)

    val out = new FileOutputStream(classFilename + ".class")
    out.write(bytecode)
    out.close()

    processOption(args, classFilename, root)

  }

  def getFilNameFromArgs(args:Array[String]):String = args.length match {
    case 3 => args(2)
    case 4 => args(3)
    case _ => throw new RuntimeException("WHAT THE FUCK DID I DO WRONG!")
  }

  def getLanguageFromArgs(args:Array[String]):String = args.length match {
    case 3 => args(0)
    case 4 => args(1)
    case _ => throw new RuntimeException("WHAT THE FUCK DID I DO WRONG!")
  }
  def getCommandFromArgs(args:Array[String]):String = args.length match {
    case 3 => ""
    case 4 => args(2)
    case _ => throw new RuntimeException("WHAT THE FUCK DID I DO WRONG!")
  }

  def processOption(args:Array[String], argFunc: => String, root: RootNode): Unit =
    getCommandFromArgs(args) match {

      case "-run" => Executor.execute(argFunc)
      case "-declaim" => Declaimer.declaim(root, argFunc, getLanguageFromArgs(args))
      case _ =>
    }

}
