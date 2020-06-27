package com.youtube.downloader.YTDownloader;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.model.YoutubeVideo;
import com.github.kiulian.downloader.model.formats.AudioVideoFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.util.List;

import static java.util.concurrent.CompletableFuture.runAsync;

@Controller
public class TestController {

  @GetMapping("test")
  public ResponseEntity<HttpStatus> test() {
    System.out.println("test..........");
    runAsync(this::download);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  public void download() {
    try {
      YoutubeDownloader downloader = new YoutubeDownloader();
      YoutubeVideo video = downloader.getVideo("dYs1y7ZhEb8");
      List<AudioVideoFormat> videoWithAudioFormats = video.videoWithAudioFormats().ge;
      videoWithAudioFormats.forEach(it -> {
        System.out.println(it.videoQuality() + " : " + it.url());
      });
      video.download(videoWithAudioFormats.get(0), new File("/home/rootroot/Downloads"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
