package com.youtube.downloader.YTDownloader.model;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DownloadInfo {

  private Set<VideoInfo> videoInfoList;
}
