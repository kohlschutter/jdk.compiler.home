# Standalone jdk.compiler.home

## What

This repository builds artifacts for the [standalone
jdk.compiler](https://github.com/kohlschutter/jdk.compiler.standalone),
specifically contents of arbitrary java.home directories, specifically the
contents of lib/modules and lib/ct.sym, which are required by the standalone
compiler.

## Why

This allows a very simple packaging of all compiler resources as simple Maven
artifacts.

## How

By default, the require build utilities are built, and a rather empty main jar
is created:

    mvn clean install

Run the following commands to build a jar containing all required contents from
a given java.home directory:

    mvn clean install -Dclassifier=custom -Djavahome.dir=/Library/Java/JavaVirtualMachines/jdk-11.0.20.1+1/Contents/Home

(adjust the path accordingly, and change the value for "classifier" as well)

To release "jdk11" and "jdk21" artifacts (for `jdk.compiler.standalone`), use the following:

    mvn clean install -Djdk11.home=/path/to/java11home -Djdk21.home=/path/to/java21home

## Caveats

When working in Eclipse, close the project "standalone-home", otherwise
workspace resolution may not find the right jar.

## When (Changelog)

### _(2023-10-13)_ jdk.compiler.home 1.1.0

- Provide both `jdk11` and `jdk21` artifacts
- Add GraalVM native-image support

### _(2023-10-10)_ jdk.compiler.home 1.0.0

- Initial release

## Who

This repository has been packaged by [Christian Kohlschütter](https://kohlschuetter.github.io/blog/).

The code itself carries the original license, GNU General Public License
version 2 only, subject to the "Classpath" exception as provided in
he LICENSE file that accompanies the original code.
