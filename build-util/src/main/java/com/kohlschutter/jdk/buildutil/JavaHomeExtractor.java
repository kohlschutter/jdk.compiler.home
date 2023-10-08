package com.kohlschutter.jdk.buildutil;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Collections;

/**
 * Extracts classes from a Java home directory, and copies them to a directory to be used with
 * standalone-home.
 * 
 * @author Christian Kohlschütter
 */
public class JavaHomeExtractor {
  public static void main(String[] args) throws Exception {
    if (args.length != 2) {
      throw new IllegalArgumentException("Usage: <path-to-java-home> <path-to-target/classes>");
    }
    String javaHome = args[0];
    if (javaHome == null || javaHome.isEmpty()) {
      System.err.println("WARNING: path to java-home not specified; skipping.");
      return;
    }
    File targetClasses = new File(args[1]);
    if (!targetClasses.isDirectory()) {
      throw new IllegalStateException("Not a directory: " + targetClasses);
    }

    FileSystem fs = FileSystems.newFileSystem(URI.create("jrt:/"), Collections.singletonMap(
        "java.home", javaHome));

    Files.walk(fs.getPath("/")).forEach((p) -> {
      if (!Files.isRegularFile(p)) {
        if (Files.isDirectory(p)) {
          return;
        }
        System.err.println("WARNING: Unexpected entry " + p);
        return;
      }

      try {
        copyToTarget(p, targetClasses, null);
      } catch (IOException t) {
        throw new RuntimeException(t);
      }
    });

    Path ctSym = Path.of(javaHome).resolve(Path.of("lib", "ct.sym"));
    if (Files.exists(ctSym)) {
      copyToTarget(ctSym, targetClasses, "/lib/ct.sym");
    }
  }

  private static void copyToTarget(Path p, File targetClasses, String targetPath)
      throws IOException {
    String fullPath = p.toString();
    if (targetPath == null) {
      if (!fullPath.startsWith("/modules/")) {
        System.err.println("WARNING: Skipping entry " + fullPath);
        return;
      }
      targetPath = fullPath;
    } else if (!targetPath.startsWith("/")) {
      System.err.println("WARNING: Bad targetPath " + targetPath);
    }
    File targetFile = new File(targetClasses, "com/kohlschutter/jdk/home" + targetPath);
    targetFile.getParentFile().mkdirs();

    Path target = targetFile.toPath();
    System.out.println("Copying " + p + "\n" + "to path " + target);

    Files.copy(p, target, LinkOption.NOFOLLOW_LINKS);
  }
}