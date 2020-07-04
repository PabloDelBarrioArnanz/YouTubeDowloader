package com.youtube.downloader.YTDownloader.service;

import com.github.kiulian.downloader.model.YoutubeVideo;
import com.github.kiulian.downloader.model.formats.Format;

import java.util.function.Function;
import java.util.stream.Stream;

public enum FormatSelector {

  AUDIO("AUDIO", youtubeVideo -> youtubeVideo.audioFormats().get(0)),
  VIDEO("VIDEO", youtubeVideo -> youtubeVideo.videoFormats().get(0)),
  AUDIO_VIDEO("AUDIO_VIDEO", youtubeVideo -> youtubeVideo.videoWithAudioFormats().get(0));

  Function<YoutubeVideo, Format> getFormat;
  private String format;

  FormatSelector(String format, Function<YoutubeVideo, Format> getFormat) {
    this.format = format;
    this.getFormat = getFormat;
  }

  public static Function<YoutubeVideo, Format> getExtractorFormatFunction(String format) {
    return Stream.of(FormatSelector.values())
      .filter(formatType -> formatType.format.equals(format))
      .findAny()
      .map(formatType -> formatType.getFormat)
      .orElse(AUDIO.getFormat);
  }
}
