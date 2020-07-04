package com.youtube.downloader.YTDownloader.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static com.youtube.downloader.YTDownloader.service.util.ThrowingFunction.unchecked;

public class Util {

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
    headers.add(HttpHeaders.CONTENT_TYPE, "audio/mpeg");
    headers.add("name", fileName);
    return headers;
  }
}
