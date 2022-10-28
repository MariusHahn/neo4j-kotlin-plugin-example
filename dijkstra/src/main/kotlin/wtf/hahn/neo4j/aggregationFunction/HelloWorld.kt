package wtf.hahn.neo4j.aggregationFunction

import org.neo4j.procedure.Name
import org.neo4j.procedure.UserFunction

class HelloWorld {

    @UserFunction
    fun helloWord(@Name("name") name : String = "world"): String {
        return "Hello $name"
    }
}