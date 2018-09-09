package database;

import data.wrapper.Book;
import data.wrapper.Member;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DataHelper {


    public static boolean insertNewBook(Book book) {
        try {
            PreparedStatement statement = DatabaseHandler.getInstance().getConnection().prepareStatement(
                    "INSERT INTO BOOK(id,title,author,publisher,isAvail,dept,subject,quantity,year) VALUES(?,?,?,?,?,?,?,?,?)");
            statement.setString(1, book.getId());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setString(4, book.getPublisher());
            statement.setBoolean(5, book.getAvailability());
            statement.setString(6, book.getDepartment());
            statement.setString(7, book.getSubejct());
            statement.setInt(8, book.getQuantity());
            statement.setString(9, book.getYear());

            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
        }
        return false;
    }

    public static boolean insertNewMember(Member member) {
        try {
            PreparedStatement statement = DatabaseHandler.getInstance().getConnection().prepareStatement(
                    "INSERT INTO MEMBER(id,name,mobile,email) VALUES(?,?,?,?)");
            statement.setString(1, member.getId());
            statement.setString(2, member.getName());
            statement.setString(3, member.getMobile());
            statement.setString(4, member.getEmail());
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
        }
        return false;
    }

    public static boolean isBookExists(String id) {
        try {
            String checkstmt = "SELECT COUNT(*) FROM BOOK WHERE id=?";
            PreparedStatement stmt = DatabaseHandler.getInstance().getConnection().prepareStatement(checkstmt);
            stmt.setString(1, id);
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

    public static boolean isMemberExists(String id) {
        try {
            String checkstmt = "SELECT COUNT(*) FROM MEMBER WHERE id=?";
            PreparedStatement stmt = DatabaseHandler.getInstance().getConnection().prepareStatement(checkstmt);
            stmt.setString(1, id);
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

    public static ResultSet getBookInfoWithIssueData(String id) {
        try {
            String query = "SELECT BOOK.title, BOOK.author, BOOK.isAvail, ISSUE.issueTime FROM BOOK LEFT JOIN ISSUE on BOOK.id = ISSUE.bookID where BOOK.id = ?";
            PreparedStatement stmt = DatabaseHandler.getInstance().getConnection().prepareStatement(query);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs;
        } catch (SQLException ex) {
        }
        return null;
    }
}
