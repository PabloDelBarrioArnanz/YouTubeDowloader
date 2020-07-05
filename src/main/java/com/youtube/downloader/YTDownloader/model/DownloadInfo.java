package com.youtube.downloader.YTDownloader.model;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DownloadInfo {
  private Long id;
  private Set<VideoInfo> videoInfo;

  public DownloadInfo id(Long id) {
    this.id = id;
    return this;
  }
}
