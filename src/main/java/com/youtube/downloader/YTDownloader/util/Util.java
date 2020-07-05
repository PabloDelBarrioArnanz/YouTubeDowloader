package com.youtube.downloader.YTDownloader.util;

import org.apache.logging.log4j.util.Strings;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.youtube.downloader.YTDownloader.util.Peek.peek;
import static com.youtube.downloader.YTDownloader.util.ThrowingConsumer.uncheckedConsumer;
import static com.youtube.downloader.YTDownloader.util.ThrowingFunction.unchecked;

@Component
public class Util {

  public static final String fileType = "application/zip";
  public static final String headerKey = "name";
  private static final String downloadPath = "/home/rootroot/Downloads/ENJOY_IT";

  public static Resource getFileAsResource(String filePath) {
    return Optional.of(filePath)
      .map(Paths::get)
      .map(Path::toUri)
      .map(unchecked(UrlResource::new))
      .filter(resource -> resource.exists() && resource.isReadable())
      .orElseThrow(RuntimeException::new);
  }

  public static HttpHeaders prepareHeader(String fileName) {
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, fileType);
    headers.add(headerKey, fileName.replaceAll("\\d[0-9]*", Strings.EMPTY));
    return headers;
  }

  public static Resource zipVideos(Set<File> videos) {
    String zipName = zipNameGenerator.get();
    return Optional.of(zipName)
      .map(unchecked(FileOutputStream::new))
      .map(ZipOutputStream::new)
      .flatMap(zipOutputStream -> Optional.of(zipOutputStream)
        .map(peek(zip -> videos.forEach(video -> Optional.of(video)
          .map(File::getName)
          .map(ZipEntry::new)
          .map(peek(uncheckedConsumer(zip::putNextEntry)))
          .map(zipEntry -> video.getPath())
          .map(Paths::get)
          .map(unchecked(Files::readAllBytes))
          .map(peek(uncheckedConsumer(bytes -> zipOutputStream.write(bytes, 0, bytes.length))))
          .map(peek(uncheckedConsumer(bytes -> zipOutputStream.closeEntry()))))))
        .map(peek(uncheckedConsumer(zipOutput -> zipOutputStream.close()))))
      .map(zipOutputStream -> getFileAsResource(zipName))
      .orElseThrow(RuntimeException::new);
  }

  private final static Supplier<String> zipNameGenerator = () -> downloadPath.concat("_" + System.nanoTime() + ".zip");
}
