package ru.egor.kulizhnyh.htmlparser.DAO;

import org.springframework.stereotype.Component;
import ru.egor.kulizhnyh.htmlparser.models.Site;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@Component
public class SiteDAO extends DAO<Site> {
    private static Logger logger;

    static {
        try {
            FileInputStream fileInputStream = new FileInputStream("src/main/resources/logger.properties");
            LogManager.getLogManager().readConfiguration(fileInputStream);
            logger = Logger.getLogger(SiteDAO.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при чтении файла-конфигурации для логгера");
        }
    }

    public SiteDAO() {}

    @Override
    public boolean insert(Site site) {
        String sql = "INSERT INTO site (url, domain, title, countwords) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, site.getUrl());
            preparedStatement.setString(2, site.getDomain());
            preparedStatement.setString(3, site.getTitle());
            preparedStatement.setInt(4, site.getCountWords());
            preparedStatement.execute();

            ResultSet resultSetLastIds = preparedStatement.getGeneratedKeys();
            resultSetLastIds.next();
            long siteId = resultSetLastIds.getLong(1);

            site.setId(siteId);

            return true;
        } catch (SQLException e) {
            String exceptionMessage = "\n----------------------------------------------------\n" +
                    e + "\n" +
                    "Ошибка при добавлении в БД нового сайта\n" +
                    "----------------------------------------------------\n";
            logger.logp(Level.WARNING, "Ошибка в классе SiteDAO", "в методе insert(Site site)", exceptionMessage);
            return false;
        }
    }

    @Override
    public boolean delete(long id) {
        String sql = "DELETE FROM site WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.execute();

            return true;
        } catch (SQLException e) {
            String exceptionMessage = "\n----------------------------------------------------\n" +
                    e + "\n" +
                    "Ошибка при удалении записи о сайте по id в БД\n" +
                    "----------------------------------------------------\n";
            logger.logp(Level.WARNING, "Ошибка в классе SiteDAO", "в методе delete(long id)", exceptionMessage);

            return false;
        }
    }

    @Override
    public List<Site> getAll() {
        List<Site> sites = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM site";
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                Site site = new Site();

                site.setId(resultSet.getLong("id"));
                site.setUrl(resultSet.getString("url"));
                site.setDomain(resultSet.getString("domain"));
                site.setTitle(resultSet.getString("title"));
                site.setCountWords(resultSet.getInt("countWords"));

                sites.add(site);
            }

            return sites;
        } catch (SQLException e) {
            String exceptionMessage = "\n----------------------------------------------------\n" +
                    e + "\n" +
                    "Ошибка при получении всех сайтов из БД\n" +
                    "----------------------------------------------------\n";
            logger.logp(Level.WARNING, "Ошибка в классе SiteDAO", "в методе getAll()", exceptionMessage);

            return sites;
        }
    }

    public Site getSite(long siteId) {
        Site site = new Site();

        try {
            String SQL = "SELECT * FROM site WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setLong(1, siteId);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            site.setId(resultSet.getLong("id"));
            site.setUrl(resultSet.getString("url"));
            site.setDomain(resultSet.getString("domain"));
            site.setTitle(resultSet.getString("title"));
            site.setCountWords(resultSet.getInt("countWords"));

            return site;
        } catch (SQLException e) {
            String exceptionMessage = "\n----------------------------------------------------\n" +
                    e + "\n" +
                    "Ошибка при получении сайта по id с БД\n" +
                    "----------------------------------------------------\n";
            logger.logp(Level.WARNING, "Ошибка в классе SiteDAO", "в методе getSite(long siteId)", exceptionMessage);

            return site;
        }
    }
}
