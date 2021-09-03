package com.example.MangaFinder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.LineSignatureValidator;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.flex.container.FlexContainer;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.objectmapper.ModelObjectMapper;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class Controller {
    @Autowired
    @Qualifier("lineMessagingClient")
    private LineMessagingClient lineMessagingClient;

    @Autowired
    @Qualifier("lineSignatureValidator")
    private LineSignatureValidator lineSignatureValidator;

    private MangaResponse mangaResponse = null;

    @RequestMapping(value = "/webhook", method = RequestMethod.POST)
    public ResponseEntity<String> callback(
            @RequestHeader("X-Line-Signature") String xLineSignature,
            @RequestBody String eventsPayload)
    {
        try{
            if(!lineSignatureValidator.validateSignature(eventsPayload.getBytes(), xLineSignature)) {
                throw new RuntimeException("Invalid Signature Validation");
            }

            ObjectMapper objectMapper = ModelObjectMapper.createNewObjectMapper();
            LineEventsModel lineEventsModel = objectMapper.readValue(eventsPayload, LineEventsModel.class);

            lineEventsModel.getEvents().forEach((event)->{
                if(event instanceof MessageEvent) {
                    MessageEvent messageEvent = (MessageEvent) event;

                    handlePrivateChats(messageEvent);
                }
            });

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private void handlePrivateChats(MessageEvent event) {
        TextMessageContent textMessageContent = (TextMessageContent) event.getMessage();
        String msg = "";

        if(textMessageContent.getText().equalsIgnoreCase("help")) {
            msg = "Command List:\n- help\n- search\n- show popular manga\n- -q Title\n- -g Genre";
            replyText(event.getReplyToken(), msg);
        } else if(textMessageContent.getText().equalsIgnoreCase("search")) {
            replyFlexMessage(event.getReplyToken());
        } else if(textMessageContent.getText().equalsIgnoreCase("search by name")) {
            msg = "Manga apa yang ingin anda cari? (Contoh: -q Oregairu)";
            replyText(event.getReplyToken(), msg);
        } else if(textMessageContent.getText().equalsIgnoreCase("search by genre")) {
            msg = "Genre (Contoh: -g Action):\n- Action\n- Adventure\n- Comedy\n- Drama\n- Romance\n- School\n- Slice of Life\n- Seinen";
            replyText(event.getReplyToken(), msg);
        } else if(textMessageContent.getText().equalsIgnoreCase("show popular manga")) {
            showCarousel(event.getReplyToken(), "", "");
        } else {
            if(textMessageContent.getText().contains("-q")) {
                String title = textMessageContent.getText().split("-q ")[1];
                String[] arr = title.split(" ");
                String fixTitle = String.join("%20", arr);
                showCarousel(event.getReplyToken(), "title", fixTitle);
            } else if(textMessageContent.getText().contains("-g")) {
                String genre = textMessageContent.getText().split("-g ")[1];
                int genreId = 0;
                if(genre.equalsIgnoreCase("action")) {
                    genreId = 1;
                    showCarousel(event.getReplyToken(), "genre", genreId + "");
                }
                if(genre.equalsIgnoreCase("adventure")) {
                    genreId = 2;
                    showCarousel(event.getReplyToken(), "genre", genreId + "");
                }
                if(genre.equalsIgnoreCase("comedy")) {
                    genreId = 4;
                    showCarousel(event.getReplyToken(), "genre", genreId + "");
                }
                if(genre.equalsIgnoreCase("drama")) {
                    genreId = 8;
                    showCarousel(event.getReplyToken(), "genre", genreId + "");
                }
                if(genre.equalsIgnoreCase("romance")) {
                    genreId = 22;
                    showCarousel(event.getReplyToken(), "genre", genreId + "");
                }
                if(genre.equalsIgnoreCase("school")) {
                    genreId = 23;
                    showCarousel(event.getReplyToken(), "genre", genreId + "");
                }
                if(genre.equalsIgnoreCase("slice of life")) {
                    genreId = 36;
                    showCarousel(event.getReplyToken(), "genre", genreId + "");
                }
                if(genre.equalsIgnoreCase("seinen")) {
                    genreId = 41;
                    showCarousel(event.getReplyToken(), "genre", genreId + "");
                }
                if(genreId == 0) {
                    msg = "Genre not found";
                    replyText(event.getReplyToken(), msg);
                }
            } else {
                msg = "Wrong command, please type help to get more commands";
                replyText(event.getReplyToken(), msg);
            }
        }
    }

    private void reply(ReplyMessage replyMessage) {
        try{
            lineMessagingClient.replyMessage(replyMessage).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void replyText(String replyToken, String message) {
        TextMessage textMessage = new TextMessage(message);
        ReplyMessage replyMessage = new ReplyMessage(replyToken, textMessage);
        reply(replyMessage);
    }

    private void replyText(String replyToken, Message message) {
        ReplyMessage replyMessage = new ReplyMessage(replyToken, message);
        reply(replyMessage);
    }

    private void replyFlexMessage(String replyToken) {
        try{
            ClassLoader classLoader = getClass().getClassLoader();
            String flexTemplate = IOUtils.toString(classLoader.getResourceAsStream("flex_message.json"));

            ObjectMapper objectMapper = ModelObjectMapper.createNewObjectMapper();
            FlexContainer flexContainer = objectMapper.readValue(flexTemplate, FlexContainer.class);

            ReplyMessage replyMessage = new ReplyMessage(replyToken, new FlexMessage("Manga Finder", flexContainer));
            reply(replyMessage);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showCarousel(String replyToken, String type, String data) {
        getManga(type, data);
        TemplateMessage templateMessage = carousel(mangaResponse);
        replyText(replyToken, templateMessage);
    }

    private void getManga(String type, String data) {
        String URI = "";
        if(type.equalsIgnoreCase("title")) {
            URI = "https://api.jikan.moe/v3/search/manga?q="+data+"&order_by=title&limit=3";
        } else if(type.equalsIgnoreCase("genre")) {
            URI = "https://api.jikan.moe/v3/search/manga?genre="+data+"&order_by=score&limit=10";
        } else {
            URI = "https://api.jikan.moe/v3/search/manga?order_by=score&sort=desc&limit=10";
        }

        try (CloseableHttpAsyncClient client = HttpAsyncClients.createDefault()) {
            client.start();

            HttpGet get = new HttpGet(URI);

            Future<HttpResponse> future = client.execute(get, null);
            HttpResponse responseGet = future.get();
            System.out.println("HTTP executed");
            System.out.println("HTTP Status of response: " + responseGet.getStatusLine().getStatusCode());

            InputStream inputStream = responseGet.getEntity().getContent();
            String encoding = StandardCharsets.UTF_8.name();
            String jsonResponse = IOUtils.toString(inputStream, encoding);

            System.out.println("Got result");
            System.out.println(jsonResponse);

            ObjectMapper objectMapper = new ObjectMapper();
            mangaResponse = objectMapper.readValue(jsonResponse, MangaResponse.class);
        } catch (InterruptedException | ExecutionException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public TemplateMessage carousel(MangaResponse mangaResponse) {
        String image, title, synopsis, link;
        CarouselColumn column;
        List<CarouselColumn> carouselColumn = new ArrayList<>();
        for (int i = 0; i < mangaResponse.getResults().size(); i++){
            image = mangaResponse.getResults().get(i).getImageUrl();
            title = mangaResponse.getResults().get(i).getTitle();
            synopsis = mangaResponse.getResults().get(i).getSynopsis();
            link = mangaResponse.getResults().get(i).getUrl();
            column = new CarouselColumn(image, title.substring(0, (title.length() < 40 ) ? title.length() : 40), synopsis.substring(0, (synopsis.length() < 50 ) ? synopsis.length() : 50) + "...",
                    Arrays.asList(
                            new URIAction("View Page", link)
                    )
            );

            carouselColumn.add(column);
        }

        CarouselTemplate carouselTemplate = new CarouselTemplate(carouselColumn);
        return new TemplateMessage("Your search result", carouselTemplate);
    }
}
