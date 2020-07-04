package com.youtube.downloader.YTDownloader.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DownloadInfo {
  private String url;
  private String format;
}
