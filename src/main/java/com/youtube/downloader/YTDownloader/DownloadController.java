package com.youtube.downloader.YTDownloader;

import com.youtube.downloader.YTDownloader.model.DownloadInfo;
import com.youtube.downloader.YTDownloader.model.VideoInfo;
import com.youtube.downloader.YTDownloader.service.DownloaderService;
import com.youtube.downloader.YTDownloader.util.Util;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;
import java.util.Set;


@Slf4j
@Controller
@AllArgsConstructor
public class DownloadController {

  private final DownloaderService downloaderService;

  @GetMapping("/main")
  public String mainView() {
    return "MainView";
  }

  @PostMapping("/download")
  public ResponseEntity<Resource> download(@RequestBody DownloadInfo downloadInfo) {
    return Optional.of(downloadInfo)
      .map(downloaderService::download)
      .flatMap(resource -> Optional.of(resource)
        .map(Resource::getFilename)
        .map(Util::prepareHeader)
        .map(headers -> new ResponseEntity<>(resource, headers, HttpStatus.OK)))
      .orElseThrow(RuntimeException::new);
  }
}
