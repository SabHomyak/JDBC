import java.sql.*;

public class Main {
    private static Connection connection = null;


    public static void main(String[] args) throws SQLException {
        try {
            connection = ConnectionDB.getConnection();
            createDB();
            populateDB();
            String records = selectRecords("apartments", Column.ID, "= 2");
            System.out.println(records);
            System.out.println("**********************");
            records = selectRecords("apartments", Column.PRICE, "> 100");
            System.out.println(records);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    public static void createDB() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("drop DATABASE IF EXISTS apartments_db");
            statement.execute("create DATABASE IF NOT EXISTS apartments_db");
            statement.execute("use apartments_db");
            statement.execute("create table if NOT EXISTS apartments(\n" +
                    "id int not null auto_increment primary key,\n" +
                    "district varchar(50) not null,\n" +
                    "address varchar(80) not null,\n" +
                    "number_of_rooms TINYINT not null,\n" +
                    "area double not null,\n" +
                    "price double not null\n" +
                    ");");

        }
    }

    public static void populateDB() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("insert into apartments (district, address, area,number_of_rooms,price)" +
                    "values ('Solomensky','st. Mikhail Maksimovich 49',34.6,1,88.296)");
            statement.execute("insert into apartments (district, address, area,number_of_rooms,price)" +
                    "values ('Goloseevsky','st. Bogdan Khmelnitsky, 48',44.6,2,101.100)");
            statement.execute("insert into apartments (district, address, area,number_of_rooms,price)" +
                    "values ('Solomensky','st. Mykola Grinchenko, 4B',50,2,99.99)");
            statement.execute("insert into apartments (district, address, area,number_of_rooms,price)" +
                    "values ('Pechersky','st. Peter Sahaidachny, 41',48,3,125.300)");
            statement.execute("insert into apartments (district, address, area,number_of_rooms,price)" +
                    "values ('Solomensky','st. M. Amosov, 12',50.6,2,88)");
            statement.execute("insert into apartments (district, address, area,number_of_rooms,price)" +
                    "values ('Darnitsky','st. Antonovicha, 50',34.8,1,75.123)");
        }
    }

    public static String selectRecords(String table, Column column, String condition) throws SQLException {
        StringBuilder sb = new StringBuilder();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " where " + column.name() + " " + condition)) {
            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            sb.append("Command: " + (statement.toString().substring(43)) + System.lineSeparator());
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                sb.append("|" + metaData.getColumnLabel(i) + "| ");
            }
            sb.append(System.lineSeparator());
            sb.append("-----------------------------------------------------");
            sb.append(System.lineSeparator());
            while (resultSet.next()) {
                sb.append("|" + resultSet.getString(1) + "| " +
                        "|" + resultSet.getString(2) + "| " +
                        "|" + resultSet.getString(3) + "| " +
                        "|" + resultSet.getString(4) + "| " +
                        "|" + resultSet.getString(5) + "| " +
                        "|" + resultSet.getString(6) + "| " + System.lineSeparator());

            }
            return sb.toString();
        }
    }
}
