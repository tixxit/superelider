Superelider
===========

This is a slightly more general version of the @elidable annotation, along with
Scala compiler plugin that actually performs the removal. The main difference
is that the annotation, `@net.tixxit.superelider.elidable`, takes 2 arguments:

 - *level*: The level is just an integer. If the elision level specified during
   compile time is greater than this number, then calls to the method will be
   removed.

 - *label*: The label let's you group your elidable methods into functional
   units (eg. "logger"). You can create a hierarchy of labels by separating
   them with a `:` (eg. "logger:something").

You specify your elision levels using the compiler option 
`-P:superelider:elide-below:[label1:label2:]level`, where the labels are
replaced with some hierarchy of labels you've denoted, and level is an integer
specify the label.

Superelider will always use the first available level specified in a hierarchy.
So, if a method is annotated with 
`@net.tixxit.superelider.elidable(100, "a:b:c")`, then it will search for a
set level in, in order, "a:b:c", "a:b", "a", "" ("" is the top level, set with
`-P:superelider:elide-below:###`). This provides you better control, as you
can remove most logging statements by doing something like,
`-P:superelider:elide-below:logger:5000`, then adding some back in by doing,
`-P:superelider:elide-below:logger:my.package:1000`.

Simple Example
==============

Create a file, Hello.scala:

    import net.tixxit.superelider.elidable

    object Hello {
      @elidable(200, "hello:hi") def sayHi() { println("Hello, world!") }
      @elidable(200, "hello:bye") def sayBye() { println("Goodbye, world!") }
      @elidable(250) def doOtherStuff() { println("Doing stuff...") }

      def main(args: Array[String]) {
        sayHi()
	doOtherStuff()
        sayBye()
      }
    }

You can now try compiling it with various options and running it:

    -P:superelider:elide-below:150
    -P:superelider:elide-below:hello:300
    -P:superelider:elide-below:hello:300 -P:superelider:elide-below:hello:hi:0

sbt
===

You can use this from sbt by adding the repository `http://repo.tixxit.net/`,
add `"net.tixxit" %% "superelider-compiler-plugin" % "0.1"` as a compiler
plugin and `"net.tixxit" %% "superelider-annotation" % "0.1"` as a library
dependency and you should be good to go. Here is an example partial build.sbt:

    resolvers += "Tom's Repo" at "http://repo.tixxit.net/"
    
    addCompilerPlugin("net.tixxit" %% "superelider-compiler-plugin" % "0.1")
    
    libraryDependencies += "net.tixxit" %% "superelider-annotation" % "0.1"


Note: The plugin and library are currently only built against 2.9.1


Building/Using
==============

You can build it using `sbt package`. The annotation library and the compiler
plugin can be used separately or together. For now, just plunk the jar files
into `lib/` in your project (if you're using sbt), and add the scalacOptions
`"-Xplugin:lib/superelider-compiler-plugin_2.9.1-0.1.jar"` to get the compiler
to use the plugin. Don't forget to copy over the annotation library jar as
well.

TODO
====

I still need to create some tests (and figure out how to test whether the some
calls were remove or not). Aside from that, the plugin should be fairly 
usable, as it is quite simple.

