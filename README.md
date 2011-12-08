Superelider
===========

This is a slightly more general version of the @elidable annotation, along with
Scala compiler plugin that actually performs the removal. The main difference
is that the annotation, `@net.tixxit.superelider.elidable`, takes 2 arguments:

 - *label*: The label let's you group your elidable methods into functional
   units (eg. "logger"). You can create a hierarchy of labels by separating
   them with a `:` (eg. "logger:something").

 - *level*: The level is just an integer. If the elision level specified during
   compile time is greater than this number, then calls to the method will be
   removed.

You specify your elision levels using the compiler option 
`-P:superelider:elide-below:[label1:label2:]level`, where the labels are
replaced with some hierarchy of labels you've denoted, and level is an integer
specify the label.

Superelider will always use the first available level specified in a hierarchy.
So, if a method is annotated with 
`@net.tixxit.superelider.elidable("a:b:c", 100)`, then it will search for a
set level in, in order, "a:b:c", "a:b", "a", "" ("" is the top level, set with
`-P:superelider:elide-below:###`). This provides you better control, as you
can remove most logging statements by doing something like,
`-P:superelider:elide-below:logger:5000`, then adding some back in by doing,
`-P:superelider:elide-below:logger:my.package:1000`.

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
