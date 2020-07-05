
package com.youtube.downloader.YTDownloader.service;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.model.YoutubeVideo;
import com.youtube.downloader.YTDownloader.model.DownloadInfo;
import com.youtube.downloader.YTDownloader.model.VideoInfo;
import com.youtube.downloader.YTDownloader.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Optional;
import java.util.Random;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static com.youtube.downloader.YTDownloader.service.FormatSelector.getExtractorFormatFunction;
import static com.youtube.downloader.YTDownloader.util.ThrowingFunction.unchecked;

@Slf4j
@Component
public class DownloaderService {

  private static final String ID_EXTRACTOR = ".*v=";
  public UnaryOperator<String> extractIdFromURL = url -> url.replaceAll(ID_EXTRACTOR, Strings.EMPTY);
  private static final String path = System.getProperty("user.dir");

  public Resource download(DownloadInfo downloadInfoList) {
    return generateId.apply(downloadInfoList).getVideoInfo()
      .parallelStream()
      .map(downloadInfo -> Optional.of(downloadInfo)
        .map(this::searchVideo)
        .map(video -> download(video, downloadInfoList.getId(), downloadInfo.getFormat())))
      .flatMap(Optional::stream)
      .collect(Collectors.collectingAndThen(Collectors.toList(), Util::zipVideos));
  }


  private UnaryOperator<DownloadInfo> generateId = downloadInfo -> downloadInfo.id(new Random().nextLong());

  private YoutubeVideo searchVideo(VideoInfo videoInfo) {
    YoutubeDownloader youtubeDownloader = new YoutubeDownloader();
    return Optional.of(videoInfo)
      .map(VideoInfo::getUrl)
      .map(extractIdFromURL)
      .map(unchecked(youtubeDownloader::getVideo))
      .orElseThrow(RuntimeException::new);
  }

  private File download(YoutubeVideo ytVideo, Long id, String selectFormat) {
    return Optional.of(ytVideo)
      .map(vid -> getExtractorFormatFunction(selectFormat))
      .map(formatExtractor -> formatExtractor.apply(ytVideo))
      .map(unchecked(format -> ytVideo.download(format, new File("R:/test/" + id))))
      .orElseThrow(RuntimeException::new);
  }
}
