package ru.egor.kulizhnyh.htmlparser.DAO;

import org.springframework.stereotype.Component;
import ru.egor.kulizhnyh.htmlparser.models.Word;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@Component
public class WordDAO extends DAO<Word> {
    private static Logger logger = null;

    static {
        try {
            FileInputStream fileInputStream = new FileInputStream("src/main/resources/logger.properties");
            LogManager.getLogManager().readConfiguration(fileInputStream);
            logger = Logger.getLogger(WordDAO.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при чтении файла-конфигурации для логгера");
        }
    }

    public WordDAO() {}

    @Override
    public boolean insert(Word word) {
        String sql = "INSERT INTO word (name, count, id_site) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, word.getName());
            preparedStatement.setInt(2, word.getCount());
            preparedStatement.setLong(3, word.getSiteId());
            preparedStatement.execute();

            ResultSet resultSetLastIds = preparedStatement.getGeneratedKeys();
            resultSetLastIds.next();
            long wordId = resultSetLastIds.getLong(1);

            word.setId(wordId);

            return true;
        } catch (SQLException e) {
            String exceptionMessage = "\n----------------------------------------------------\n" +
                    e + "\n" +
                    "Ошибка при добавлении в БД нового слова\n" +
                    "----------------------------------------------------\n";
            logger.logp(Level.WARNING, "Ошибка в классе WordDAO", "в методе insert(Word word)", exceptionMessage);

            return false;
        }
    }

    @Override
    public boolean delete(long id) {
        return false;
    }

    @Override
    public List<Word> getAll() {
        List<Word> words = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM word";
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                Word word = new Word();

                word.setSiteId(resultSet.getLong("id"));
                word.setName(resultSet.getString("name"));
                word.setCount(resultSet.getInt("count"));
                word.setSiteId(resultSet.getLong("id_site"));

                words.add(word);
            }

            return words;
        } catch (SQLException e) {
            String exceptionMessage = "\n----------------------------------------------------\n" +
                    e + "\n" +
                    "Ошибка при получении всех слов из БД\n" +
                    "----------------------------------------------------\n";
            logger.logp(Level.WARNING, "Ошибка в классе WordDAO", "в методе getAll()", exceptionMessage);

            return words;
        }
    }

    public List<Word> getWordsFromSite(long id_site) {
        List<Word> words = new ArrayList<>();

        try {
            String SQL = "SELECT * FROM word WHERE id_site = ? ORDER BY count DESC";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setLong(1, id_site);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Word word = new Word();

                word.setSiteId(resultSet.getLong("id"));
                word.setName(resultSet.getString("name"));
                word.setCount(resultSet.getInt("count"));
                word.setSiteId(resultSet.getLong("id_site"));

                words.add(word);
            }

            return words;
        } catch (SQLException e) {
            String exceptionMessage = "\n----------------------------------------------------\n" +
                    e + "\n" +
                    "Ошибка при получениии слов по определённому сайту\n" +
                    "----------------------------------------------------\n";
            logger.logp(Level.WARNING, "Ошибка в классе WordDAO", "в методе getWordsFromSite(long id_site)", exceptionMessage);

            return words;
        }
    }
}
