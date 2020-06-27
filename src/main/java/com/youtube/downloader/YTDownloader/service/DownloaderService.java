
package com.youtube.downloader.YTDownloader.service;

import com.youtube.downloader.YTDownloader.model.DownloadInfo;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.util.function.UnaryOperator;

@Component
public class DownloaderService {

  private static final String ID_EXTRACTOR = "(?<=v=).*$";
  public UnaryOperator<String> extractIdFromURL = url -> url.replaceAll(ID_EXTRACTOR, Strings.EMPTY);

  public void download(DownloadInfo downloadInfo) {

  }

}
