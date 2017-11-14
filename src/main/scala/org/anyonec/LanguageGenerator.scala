package org.anyonec

import org.anyonec.ast.RootNode


class LanguageGenerator extends ClassLoader {

  def generate(language: String, sourceCode: String, filename: String): (Array[Byte], RootNode) = {
    //val parser =  Class.forName("org.anyonec.languages."+language+"Parser").newInstance.asInstanceOf[{ def parse(language: String, expression: String): RootNode  }]
    val parser = new AnyoneParser
    parser.loadLanguage(language);
    val rootNode = parser.parse(language, sourceCode)
    (rootNode.generateByteCode(filename), rootNode)
  }
}
