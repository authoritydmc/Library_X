package database;

import data.wrapper.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ui.listbook.BookListController;
import ui.listmember.MemberListController;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class DatabaseHandler {


    private static DatabaseHandler handler = null;


    private static final String DB_URL = "jdbc:mysql://localhost:3306/LIB_X?createDatabaseIfNotExist=true";
    // private static final String DB_URL = "jdbc:derby:database;create=true";
    private static Connection conn = null;
    private static Statement stmt = null;

    static {
        createConnection();
        inflateDB();
    }

    private DatabaseHandler() {
    }

    public static DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }

    private static void inflateDB() {
        List<String> tableData = new ArrayList<>();
        try {
            Set<String> loadedTables = getDBTables();
            System.out.println("Already loaded tables " + loadedTables);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(DatabaseHandler.class.getClass().getResourceAsStream("/resources/database/tables.xml"));
            NodeList nList = doc.getElementsByTagName("table-entry");
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                Element entry = (Element) nNode;
                String tableName = entry.getAttribute("name");
                String query = entry.getAttribute("col-data");
                if (!loadedTables.contains(tableName.toLowerCase())) {
                    tableData.add(String.format("CREATE TABLE %s (%s)", tableName, query));
                }
            }
            if (tableData.isEmpty()) {
                System.out.println("Tables are already loaded");
            } else {
                System.out.println("Inflating new tables.");
                createTables(tableData);
            }
        } catch (Exception ex) {
        }
    }

    private static void createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(DB_URL, "root", "zxc");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cant load database", "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private static Set<String> getDBTables() throws SQLException {
        Set<String> set = new HashSet<>();
        DatabaseMetaData dbmeta = conn.getMetaData();
        readDBTable(set, dbmeta, "TABLE", null);
        return set;
    }

    private static void readDBTable(Set<String> set, DatabaseMetaData dbmeta, String searchCriteria, String schema) throws SQLException {
        ResultSet rs = dbmeta.getTables(null, schema, null, new String[]{searchCriteria});
        while (rs.next()) {
            set.add(rs.getString("TABLE_NAME").toLowerCase());
        }
    }

    public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        } finally {
        }
        return result;
    }

    public boolean execAction(String qu) {
        try {
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return false;
        } finally {
        }
    }

    public boolean deleteBook(BookListController.Book book) {
        try {
            String deleteStatement = "DELETE FROM BOOK WHERE ID = ?";
            PreparedStatement stmt = conn.prepareStatement(deleteStatement);
            stmt.setString(1, book.getId());
            int res = stmt.executeUpdate();
            if (res == 1) {
                return true;
            }
        } catch (SQLException ex) {
        }
        return false;
    }

    public boolean isBookAlreadyIssued(BookListController.Book book) {
        try {
            String checkstmt = "SELECT COUNT(*) FROM ISSUE WHERE bookid=?";
            PreparedStatement stmt = conn.prepareStatement(checkstmt);
            stmt.setString(1, book.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println(count);
                return (count > 0);
            }
        } catch (SQLException ex) {
        }
        return false;
    }

    public boolean deleteMember(MemberListController.Member member) {
        try {
            String deleteStatement = "DELETE FROM MEMBER WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(deleteStatement);
            stmt.setString(1, member.getId());
            int res = stmt.executeUpdate();
            if (res == 1) {
                return true;
            }
        } catch (SQLException ex) {
        }
        return false;
    }

    public boolean isMemberHasAnyBooks(MemberListController.Member member) {
        try {
            String checkstmt = "SELECT COUNT(*) FROM ISSUE WHERE memberID=?";
            PreparedStatement stmt = conn.prepareStatement(checkstmt);
            stmt.setString(1, member.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println(count);
                return (count > 0);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean updateBook(Book book) {
        try {
            System.out.println("Updating Book");
            String update = "UPDATE BOOK SET TITLE=?, AUTHOR=?, PUBLISHER=? ,description=?,dept=?,year=?,subject=?,quantity=? WHERE ID=? ";
            PreparedStatement stmt = conn.prepareStatement(update);
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getPublisher());
            stmt.setString(4, book.getDescription());
            stmt.setString(5, book.getDepartment());
            stmt.setString(6, book.getYear());
            stmt.setString(7, book.getSubejct());
            stmt.setInt(8, book.getQuantity());

            stmt.setString(9, book.getId());
            System.out.println(stmt);
            int res = stmt.executeUpdate();
            return (res > 0);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean updateMember(MemberListController.Member member) {
        try {
            String update = "UPDATE MEMBER SET NAME=?, EMAIL=?, MOBILE=? WHERE ID=?";
            PreparedStatement stmt = conn.prepareStatement(update);
            stmt.setString(1, member.getName());
            stmt.setString(2, member.getEmail());
            stmt.setString(3, member.getMobile());
            stmt.setString(4, member.getId());
            int res = stmt.executeUpdate();
            return (res > 0);
        } catch (SQLException ex) {
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        DatabaseHandler.getInstance();
    }

    public ObservableList<PieChart.Data> getBookGraphStatistics() {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        try {
            String qu1 = "SELECT COUNT(*) FROM BOOK";
            String qu2 = "SELECT COUNT(*) FROM ISSUE";
            ResultSet rs = execQuery(qu1);
            if (rs.next()) {
                int count = rs.getInt(1);
                data.add(new PieChart.Data("Total Books (" + count + ")", count));
            }
            rs = execQuery(qu2);
            if (rs.next()) {
                int count = rs.getInt(1);
                data.add(new PieChart.Data("Issued Books (" + count + ")", count));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public ObservableList<PieChart.Data> getMemberGraphStatistics() {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        try {
            String qu1 = "SELECT COUNT(*) FROM MEMBER";
            String qu2 = "SELECT COUNT(DISTINCT memberID) FROM ISSUE";
            ResultSet rs = execQuery(qu1);
            if (rs.next()) {
                int count = rs.getInt(1);
                data.add(new PieChart.Data("Total Members (" + count + ")", count));
            }
            rs = execQuery(qu2);
            if (rs.next()) {
                int count = rs.getInt(1);
                data.add(new PieChart.Data("Active (" + count + ")", count));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private static void createTables(List<String> tableData) throws SQLException {
        Statement statement = conn.createStatement();
        statement.closeOnCompletion();
        for (String command : tableData) {
            System.out.println(command);
            statement.addBatch(command);
        }
        statement.executeBatch();
    }

    public Connection getConnection() {
        return conn;
    }
}
