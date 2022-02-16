package ru.egor.kulizhnyh.htmlparser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.*;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@Component
public class HtmlParser {
    private static Logger logger;
    private int responseCode;

    static {
        try {
            FileInputStream fileInputStream = new FileInputStream("src/main/resources/logger.properties");
            LogManager.getLogManager().readConfiguration(fileInputStream);
            logger = Logger.getLogger(HtmlParser.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при чтении файла-конфигурации для логгера");
        }
    }

    private String stringUrl;

    public HtmlParser() {}

    public HtmlParser(String stringUrl) {
        this.stringUrl = stringUrl;
    }

    public String getStringUrl() {
        return stringUrl;
    }

    public void setStringUrl(String stringUrl) {
        this.stringUrl = stringUrl;
    }

    private String getStringHtml() {
        StringBuilder inputHTML = new StringBuilder();
        try {
            URL url = new URL(this.stringUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String currentLineHTML = in.readLine();
            while (currentLineHTML != null) {
                inputHTML.append(currentLineHTML.toLowerCase()).append("\n");
                currentLineHTML = in.readLine();
            }
            in.close();

        } catch (IOException e) {
            String exceptionMessage = "\n----------------------------------------------------\n" +
                    e + "\n" +
                    "Ошибка произошла при получении html-страницы\n" +
                    "----------------------------------------------------\n";
            logger.logp(Level.WARNING, "Ошибка в классе HtmlParser", "в методе getStringHtml()", exceptionMessage);
        }

        return inputHTML.toString();
    }

    private String[] splitStringHtml(String stringHtml) {
        String[] htmlSplit;
        String inputHTMLParse = Jsoup.parse(stringHtml).text();
        String regex = "[,|. |! |? |:|;|\\[|\\]|\\-|\\(|\\)|\\=|\"|«|»|\n|\r|\t|']";
        htmlSplit = inputHTMLParse.split(regex);
        return htmlSplit;
    }

    /*
    Более оптимально использовать данную функцию,
    т.к. данное регулярное выражение "[^\\p{IsAlphabetic}]+" само выделяет
    все слова. Убирая пробелы, запятые, цифры и прочие символы. Тогда как
    в методе со списком разделителей необходимо указывать все возможные
    варианты. Например, если описать "[,|. |!|? |:|;|\\[|\\]|\\-|\\(|\\)|\\=|\"|«|»|\n|\r|\t|']"
    т.е. необходимо предусмотреть все возможные символы,
    могут встречаться специфические символы, например « ». И писать описывать эти
    символы долго и может быть некоторые не учесть. И на практике придётся вносить изменения
    в список символов. Так же нужно будет вычленять цифры,
    что потребует внечти определённые изменения в "[,|. |! |? |:|;|\\[|\\]|\\-|\\(|\\)|\\=|\"|«|»|\n|\r|\t|']"
    Поэтому оптимальнее использовать метод ниже
     */
    private String[] splitStringHtmlOptimally(String stringHtml) {
        String[] htmlSplit;
        String inputHTMLParse = Jsoup.parse(stringHtml).text();
        String regex = "[^\\p{IsAlphabetic}]+";
        htmlSplit = inputHTMLParse.split(regex);

        return htmlSplit;
    }

    public TreeMap<String, Integer> getWordsStatistics() {
        TreeMap<String, Integer> wordsMap = new TreeMap<>();

        String[] wordsFromHtml = splitStringHtmlOptimally(getStringHtml());

        for (String wordTemp : wordsFromHtml) {
            if (wordsMap.containsKey(wordTemp)) wordsMap.put(wordTemp, wordsMap.get(wordTemp) + 1);
            else wordsMap.put(wordTemp, 1);
        }

        return wordsMap;
    }

    public boolean checkSite() {
        try {
            URL checkUrl = new URL(stringUrl);
            URLConnection connection = checkUrl.openConnection();
            connection.connect();

            HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
            responseCode = httpURLConnection.getResponseCode();

            if (responseCode == 200 || responseCode == 203 || responseCode == 206) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            String exceptionMessage = "\n----------------------------------------------------\n" +
                    e + "\n" +
                    "Ошибка произошла при проверки сайта на доступность\n" +
                    "Код ответа: " + responseCode + "\n" +
                    "----------------------------------------------------\n";
            logger.logp(Level.WARNING, "Ошибка в классе HtmlParser", "в методе checkSite()", exceptionMessage);

            return false;
        }
    }

    public String getTitleHtml() {
        String title = "";
        try {
            Document document = Jsoup.connect(stringUrl).get();
            title = document.title();
        } catch (IOException e) {
            String exceptionMessage = "\n----------------------------------------------------\n" +
                    e + "\n" +
                    "Ошибка при получении title страницы " + stringUrl + "\n" +
                    "----------------------------------------------------\n";
            logger.logp(Level.WARNING, "Ошибка в классе HtmlParser", "в методе getTitleHtml()", exceptionMessage);
        }

        return title;
    }

    public String getDomain() {
        String domain;
        int fromIndexDomain = stringUrl.indexOf('/') + 2;
        int endIndexDomain = stringUrl.indexOf('/', fromIndexDomain);
        if (endIndexDomain == -1) endIndexDomain = stringUrl.length();
        domain = stringUrl.substring(fromIndexDomain, endIndexDomain);

        return domain;
    }
}
