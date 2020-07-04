
package com.youtube.downloader.YTDownloader.service;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.youtube.downloader.YTDownloader.model.DownloadInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Optional;
import java.util.function.UnaryOperator;

import static com.youtube.downloader.YTDownloader.service.FormatSelector.getExtractorFormatFunction;
import static com.youtube.downloader.YTDownloader.service.util.ThrowingFunction.unchecked;
import static com.youtube.downloader.YTDownloader.util.Util.getFileAsResource;

@Slf4j
@Component
public class DownloaderService {

  private static final String ID_EXTRACTOR = ".*v=";
  public UnaryOperator<String> extractIdFromURL = url -> url.replaceAll(ID_EXTRACTOR, Strings.EMPTY);
  private static final String path = System.getProperty("user.dir");

  public Resource download(DownloadInfo downloadInfo) {
    YoutubeDownloader youtubeDownloader = new YoutubeDownloader();
    return Optional.of(downloadInfo)
      .map(DownloadInfo::getUrl)
      .map(extractIdFromURL)
      .map(unchecked(youtubeDownloader::getVideo))
      .flatMap(video -> Optional.of(video)
        .map(vid -> getExtractorFormatFunction(downloadInfo.getFormat()))
        .map(formatExtractor -> formatExtractor.apply(video))
        .map(unchecked(format -> video.download(format, new File("R:/test")))))
      .map(video -> getFileAsResource(video.getPath()))
      .orElseThrow(RuntimeException::new);
  }
}
