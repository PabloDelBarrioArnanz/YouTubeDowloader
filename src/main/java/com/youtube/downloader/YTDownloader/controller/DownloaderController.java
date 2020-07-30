package com.youtube.downloader.YTDownloader.controller;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.youtube.downloader.YTDownloader.model.VideoInfo;
import com.youtube.downloader.YTDownloader.service.DownloaderService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.util.Supplier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Route("")
@PageTitle("Youtube Downloader")
@AllArgsConstructor
public class DownloaderController extends VerticalLayout {

  private DownloaderService downloaderService;

  public DownloaderController() {
    HorizontalLayout horizontalLayout = new HorizontalLayout();
    HorizontalLayout horizontalLayout2 = new HorizontalLayout();
    VerticalLayout verticalLayout = new VerticalLayout();
    List<VideoInfo> downloadInfoList = new ArrayList<>();
    ListDataProvider<VideoInfo> downloadInfoProvider = new ListDataProvider<>(downloadInfoList);

    Grid<VideoInfo> urlsToDownload = createTable(downloadInfoProvider);

    TextField urlField = new TextField("Insert YouTube URL");
    urlField.setWidth("400px");
    ComboBox<String> urlFormat = new ComboBox<>("Select format", Arrays.asList("AUDIO", "VIDEO"));
    urlFormat.setWidth("110px");
    urlFormat.setValue("AUDIO");

    Button addUrlButton = new Button("Add URL");
    addUrlButton.addClickListener(event -> {
      downloadInfoList.add(new VideoInfo(urlField.getValue(), urlFormat.getValue()));
      urlsToDownload.getDataProvider().refreshAll();
      urlField.setValue(Strings.EMPTY);
    });
    Button startDownloadButton = new Button("Start Download!");
    startDownloadButton.addClickListener(event -> {
      Anchor anchor = new Anchor(downloaderService.download(downloadInfoList), "ENJOY_IT.zip");
      anchor.add(new Button(new Icon(VaadinIcon.DOWNLOAD_ALT)));
      horizontalLayout2.add(anchor);
    });
    Button removeUrlButton = new Button("Remove selected URLs");
    removeUrlButton.addClickListener(event -> {
      downloadInfoList.removeAll(urlsToDownload.getSelectedItems());
      urlsToDownload.getDataProvider().refreshAll();
    });

    verticalLayout.add(createTitle.get(), urlsToDownload);
    horizontalLayout.add(urlField, urlFormat);
    horizontalLayout2.add(addUrlButton, removeUrlButton, startDownloadButton);
    add(verticalLayout, horizontalLayout, horizontalLayout2);
  }

  private static Supplier<Label> createTitle = () ->
    Optional.of("Welcome to YouTube Downloader!")
      .map(Label::new)
      .flatMap(label -> Optional.of(label)
        .map(Component::getElement)
        .map(Element::getStyle)
        .map(style -> style.set("fontWeight", "bold"))
        .map(style -> style.set("font-size", "35px"))
        .map(style -> style.set("font-color", "LightBlue"))
        .map(style -> label))
      .orElseThrow(RuntimeException::new);

  private Grid<VideoInfo> createTable(ListDataProvider<VideoInfo> downloadInfoProvider) {
    Grid<VideoInfo> urlsToDownload = new Grid<>();
    urlsToDownload.setHeight("300px");
    urlsToDownload.setWidth("600px");
    urlsToDownload.addColumn(VideoInfo::getUrl)
      .setHeader("URL")
      .setSortable(true);
    urlsToDownload.addColumn(VideoInfo::getFormat)
      .setHeader("Format")
      .setFlexGrow(0)
      .setSortable(true);
    urlsToDownload.setDataProvider(downloadInfoProvider);
    urlsToDownload.setSelectionMode(Grid.SelectionMode.MULTI);
    return urlsToDownload;
  }
}
