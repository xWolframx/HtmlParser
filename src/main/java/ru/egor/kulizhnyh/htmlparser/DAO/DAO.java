package ru.egor.kulizhnyh.htmlparser.DAO;

import ru.egor.kulizhnyh.htmlparser.HtmlParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public abstract class DAO <T> {
    private static Logger logger;

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

    private static final String URLDB = "jdbc:hsqldb:file:DateBase/wordsstatisticsdb";

    static Connection connection;

    static {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (ClassNotFoundException e) {
            String expceptionMessage = "\n----------------------------------------------------\n" +
                    e + "\n" +
                    "Ошибка при подключении драйвера БД\n" +
                    "----------------------------------------------------\n";
            logger.logp(Level.WARNING, "Ошибка в классе DAO", "в статическом блоке", expceptionMessage);
        }

        try {
            connection = DriverManager.getConnection(URLDB, "SA", "");
        } catch (SQLException e) {
            String exceptionMessage = "\n----------------------------------------------------\n" +
                    e + "\n" +
                    "Ошибка при подключении БД\n" +
                    "----------------------------------------------------\n";
            logger.logp(Level.WARNING, "Ошибка в классе DAO", "в статическом блоке", exceptionMessage);
        }
    }

    public static void createDB() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (ClassNotFoundException e) {
            String exceptionMessage = "\n----------------------------------------------------\n" +
                    e + "\n" +
                    "Ошибка при подключении драйвера БД\n" +
                    "----------------------------------------------------\n";
            logger.logp(Level.WARNING, "Ошибка в классе DAO", "в createDB()", exceptionMessage);
        }

        try {
            connection = DriverManager.getConnection(URLDB, "SA", "");
        } catch (SQLException e) {
            String exceptionMessage = "\n----------------------------------------------------\n" +
                    e + "\n" +
                    "Ошибка при подключении БД\n" +
                    "----------------------------------------------------\n";
            logger.logp(Level.WARNING, "Ошибка в классе DAO", "в createDB()", exceptionMessage);
        }

        StringBuilder sqlCreateDB = new StringBuilder();
        try {
            File file = new File("src/main/resources/templates/createDB.sql");
            //Получение sql - файла на чтение
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sqlCreateDB.append(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            String exceptionMessage = "\n----------------------------------------------------\n" +
                    e + "\n" +
                    "Ошибка при чтении sql-файла по созданию БД\n" +
                    "----------------------------------------------------\n";
            logger.logp(Level.WARNING, "Ошибка в классе DAO", "в createDB()", exceptionMessage);
        }

        try {
            Statement statement = connection.createStatement();
            statement.execute(sqlCreateDB.toString());
        } catch (SQLException e) {
            String exceptionMessage = "\n----------------------------------------------------\n" +
                    e + "\n" +
                    "Ошибка выполнение запроса на создание таблиц БД\n" +
                    "----------------------------------------------------\n";
            logger.logp(Level.WARNING, "Ошибка в классе DAO", "в createDB()", exceptionMessage);
        }

    }

    public abstract boolean insert(T saveObject);

    public abstract boolean delete(long id);

    public abstract List<T> getAll();
}
