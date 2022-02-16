package ru.egor.kulizhnyh.htmlparser.services;

import org.springframework.stereotype.Service;
import ru.egor.kulizhnyh.htmlparser.DAO.SiteDAO;
import ru.egor.kulizhnyh.htmlparser.DAO.WordDAO;
import ru.egor.kulizhnyh.htmlparser.HtmlParser;
import ru.egor.kulizhnyh.htmlparser.models.Site;
import ru.egor.kulizhnyh.htmlparser.models.Word;

import java.util.Map;

@Service
public class HtmlParserService {
    public boolean addSiteInDateBase(HtmlParser htmlParser, SiteDAO siteDAO, WordDAO wordDAO) {
        if (htmlParser.checkSite()) {

            Map<String, Integer> words = htmlParser.getWordsStatistics();

            Site site = new Site(htmlParser.getStringUrl(),
                    htmlParser.getDomain(),
                    htmlParser.getTitleHtml(),
                    words.size());

            siteDAO.insert(site);

            long siteId = site.getId();

            for (Map.Entry<String, Integer> wordMap : words.entrySet()) {
                String name_word = wordMap.getKey();
                int count = wordMap.getValue();

                Word word = new Word(name_word, count, siteId);

                wordDAO.insert(word);
            }

            return true;
        } else {
            return false;
        }
    }
}
