package org.anyonec

import java.io.FileNotFoundException

import org.parboiled.errors.ParsingException

class NonExistingLanguageTest extends RemoteLanguageTest {

  it should "detect faulty variable names" in {
    val code =
      "IT'S SHOWTIME\n" +
        "HEY CHRISTMAS TREE 1VAR\n" +
        "YOU SET US UP 123\n" +
        "YOU HAVE BEEN TERMINATED\n"
    intercept[FileNotFoundException] {
      getOutput(code)
    }
  }
}
