package com.youtube.downloader.YTDownloader.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DownloadInfo {
  private String url;
  private String path;
  private String format;
}
