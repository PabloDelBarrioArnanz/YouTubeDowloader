package com.youtube.downloader.YTDownloader.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.youtube.downloader.YTDownloader.util.Peek.peek;
import static com.youtube.downloader.YTDownloader.util.ThrowingConsumer.uncheckedConsumer;
import static com.youtube.downloader.YTDownloader.util.ThrowingFunction.unchecked;

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
    headers.add(HttpHeaders.CONTENT_TYPE, "application/zip");
    headers.add("name", fileName);
    return headers;
  }

  public static Resource zipVideos(List<File> videos) {

  }

  public static void zipVideos2(List<File> videos) {

    FileOutputStream fos = new FileOutputStream(zipFileName);
    ZipOutputStream zos = new ZipOutputStream(fos);

    Optional.of("ENJOY_IT.zip")
      .map(unchecked(FileOutputStream::new))
      .map(ZipOutputStream::new)
      .map(zipOutputStream -> Optional.of(zipOutputStream)
        .map(zip -> videos.stream()
          .map(video -> Optional.of(video)
            .map(File::getName)
            .map(ZipEntry::new)
            .map(peek(uncheckedConsumer(zip::putNextEntry)))
            .map(zipEntry -> unchecked(Files.readAllBytes(Paths.get(video.getPath())))))
        )
      )


    try {
      File firstFile = new File(filePaths[0]);
      String zipFileName = firstFile.getName().concat(".zip");

      FileOutputStream fos = new FileOutputStream(zipFileName);
      ZipOutputStream zos = new ZipOutputStream(fos);

      for (String aFile : filePaths) {
        zos.putNextEntry(new ZipEntry(new File(aFile).getName()));

        byte[] bytes = Files.readAllBytes(Paths.get(aFile));
        zos.write(bytes, 0, bytes.length);
        zos.closeEntry();
      }

      zos.close();

    } catch (FileNotFoundException ex) {
      System.err.println("A file does not exist: " + ex);
    } catch (IOException ex) {
      System.err.println("I/O error: " + ex);
    }
  }
}
