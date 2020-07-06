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
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static com.youtube.downloader.YTDownloader.service.FormatSelector.getExtractorFormatFunction;
import static com.youtube.downloader.YTDownloader.util.Peek.peek;
import static com.youtube.downloader.YTDownloader.util.ThrowingFunction.unchecked;

@Slf4j
@Component
public class DownloaderService {

  private static final String ID_EXTRACTOR = ".*v=";
  private static final String downloadPath = "/home/rootroot/Downloads/";

  public Resource download(DownloadInfo downloadInfo) {
    return downloadInfo.getVideoInfoList()
      .parallelStream()
      .map(peek(video -> log.info(video.toString())))
      .map(videoInfo -> Optional.of(videoInfo)
        .map(this::searchVideo)
        .map(video -> download(video, videoInfo.getFormat())))
      .flatMap(Optional::stream)
      .collect(Collectors.collectingAndThen(Collectors.toSet(), Util::zipVideos));
  }

  private YoutubeVideo searchVideo(VideoInfo videoInfo) {
    YoutubeDownloader youtubeDownloader = new YoutubeDownloader();
    return Optional.of(videoInfo)
      .map(VideoInfo::getUrl)
      .map(extractIdFromURL)
      .map(unchecked(youtubeDownloader::getVideo))
      .orElseThrow(RuntimeException::new);
  }

  public final UnaryOperator<String> extractIdFromURL = url -> url.replaceAll(ID_EXTRACTOR, Strings.EMPTY);

  private File download(YoutubeVideo ytVideo, String selectFormat) {
    return Optional.of(ytVideo)
      .map(vid -> getExtractorFormatFunction(selectFormat))
      .map(formatExtractor -> formatExtractor.apply(ytVideo))
      .map(unchecked(format -> ytVideo.download(format, new File(downloadPath))))
      .orElseThrow(RuntimeException::new);
  }
}
