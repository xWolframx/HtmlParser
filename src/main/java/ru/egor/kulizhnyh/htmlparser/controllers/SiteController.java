package ru.egor.kulizhnyh.htmlparser.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.egor.kulizhnyh.htmlparser.DAO.SiteDAO;
import ru.egor.kulizhnyh.htmlparser.DAO.WordDAO;
import ru.egor.kulizhnyh.htmlparser.HtmlParser;
import ru.egor.kulizhnyh.htmlparser.services.HtmlParserService;

@Controller
@RequestMapping()
public class SiteController {

    private final SiteDAO siteDAO;
    private final WordDAO wordDAO;
    private final HtmlParserService htmlParserService;

    @Autowired
    public SiteController(SiteDAO siteDAO, WordDAO wordDAO, HtmlParserService htmlParserService) {
        this.siteDAO = siteDAO;
        this.wordDAO = wordDAO;
        this.htmlParserService = htmlParserService;
    }

    @GetMapping()
    public String defaultPage() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String homePage() {
        return "views/index";
    }

    @GetMapping("/allsitestatistics")
    public String allSite(Model model) {
        model.addAttribute("sites", siteDAO.getAll());

        return "views/allsitestatistics";
    }

    @GetMapping("/{siteId}")
    public String site(@PathVariable("siteId") long siteId, Model model) {
        model.addAttribute("site", siteDAO.getSite(siteId));
        model.addAttribute("words", wordDAO.getWordsFromSite(siteId));

        return "views/site";
    }

    @GetMapping("/new")
    public String newSite(Model model) {
        model.addAttribute("htmlparser", new HtmlParser());

        return "views/new";
    }

    @PostMapping("/createSite")
    public String createNewSite(@ModelAttribute("htmlparser") HtmlParser htmlParser, Model model) {
        if (htmlParserService.addSiteInDateBase(htmlParser, siteDAO, wordDAO)) {
            return "redirect:/allsitestatistics";
        } else {
            model.addAttribute("alert", "Сайт не доступен");
            return "views/new";
        }
    }

    @DeleteMapping("/{id_site}")
    public String delete(@PathVariable("id_site") long id_site) {
        siteDAO.delete(id_site);

        return "redirect:/allsitestatistics";
    }

}
