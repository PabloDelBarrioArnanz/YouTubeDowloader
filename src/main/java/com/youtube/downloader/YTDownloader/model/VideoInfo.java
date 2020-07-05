package com.youtube.downloader.YTDownloader.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VideoInfo {
  private String url;
  private String format;
}
