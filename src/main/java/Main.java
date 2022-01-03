import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final String dbURL="jdbc:mysql://localhost:3306/java24";
        final String user="root";
        final String password="Password123";

        char again = 'y';
        Scanner scanner = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(dbURL,user,password)){
System.out.println("Connected to database");

while (again=='y'){
    System.out.println("Are you user or librarian? u/l");
    char action = scanner.nextLine().charAt(0);
    if(action == 'u'){
        System.out.println("n - new user");
        System.out.println("e - existing user");
        char userDefinition = scanner.nextLine().charAt(0);
        if(userDefinition == 'n'){
            System.out.println("Enter full name:");
            String fullname = scanner.nextLine();
            System.out.println("Enter email address:");
            String email = scanner.nextLine();
            System.out.println("Enter a password:");
            String userPassword = scanner.nextLine();
            insertData(conn, fullname, email, userPassword);
        }else{
            System.out.println("Enter your name:");
            String name = scanner.nextLine();
            userExist(conn, name);
            String userPassword = scanner.nextLine();

            if(userPassword.equals (readPassword(conn, name))){
                System.out.println("s - see my books");
                System.out.println("b - borrow a book");
                System.out.println("g - give back a book");
                char userAction = scanner.nextLine().charAt(0);
                if (userAction == 's') {
                    userBooks(conn);
                } else if (userAction == 'b') {
                    readBooks(conn);
                } else {
                    userBooks(conn);
                }
            }else{
                System.out.println("Password did not match. Try again");
            }

        }
    }else{
        System.out.println("u - see all users");
        System.out.println("b - see all books");
        System.out.println("a - add a book");
        System.out.println("r - remove a book");
        char librarianAction = scanner.nextLine().charAt(0);
        if(librarianAction == 'a'){
            System.out.println("Add author:");
            String author = scanner.nextLine();
            System.out.println("Add title:");
            String title = scanner.nextLine();
            System.out.println("How many exemplars?");
            int exemplars = scanner.nextInt();
            insertBook(conn, author,title,exemplars);
        }else if(librarianAction == 'r') {
            System.out.println("Title of book you want to delete?");
            String title = scanner.nextLine();
            deleteBook(conn, title);
        }else if(librarianAction=='b'){
            readBooks(conn);
        }else{
            readData(conn);
        }
    }

}
System.out.println("Continue to use app? y/n");
again = scanner.nextLine().charAt(0);

        }catch (SQLException e){
            System.out.println("Something went wrong" + e);
        }
    }
    public static void readData(Connection conn)throws SQLException{
        String sql="select * from users;";
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            String fullname = resultSet.getString("fullname");
            String email = resultSet.getString("email");
            String pswd = resultSet.getString("password");
            int user_id = resultSet.getInt("user_id");
String output = "user %d info: %s / %s";
System.out.println(String.format(output, user_id, fullname, email));
        }
    }
    public  static void insertData(Connection conn, String fullname, String email, String password) throws SQLException {
        String sql = "insert into users (fullname, email, password) values (?, ?, ?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, fullname);
        preparedStatement.setString(2, email);
        preparedStatement.setString(3, password);
        int rowInserted = preparedStatement.executeUpdate();
        if(rowInserted>0){
            System.out.println("A user added");
        }else{
            System.out.println("Something went wrong, a user not added");
        }
    }
    public static void readBooks(Connection conn)throws SQLException{
        String sql="select * from books;";
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            String author = resultSet.getString("author");
            String title = resultSet.getString("title");
            int book_id = resultSet.getInt("book_id");
            int exemplars = resultSet.getInt("exemplars");
            String output = "book %d info: %s / %s / %d exemplars";
            System.out.println(String.format(output, book_id, author, title, exemplars));
        }
    }
    public  static void insertBook(Connection conn, String author, String title, int exemplars) throws SQLException {
        String sql = "insert into books (author, title, exemplars) values (?, ?, ?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, author);
       preparedStatement.setString(2, title);
       preparedStatement.setInt(3, exemplars);
       int rowInserted = preparedStatement.executeUpdate();
       if(rowInserted>0){
           System.out.println("A book inserted");
       }else{
           System.out.println("Something went wrong");
       }
   }
    public static void deleteBook(Connection conn, String title) throws SQLException {
        String sql = "delete from books where title = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(2, title);
        int bookDeleted = preparedStatement.executeUpdate();
        if (bookDeleted > 0) {
            System.out.println("A book deleted");
        } else {
            System.out.println("A book has not deleted yet");
        }
    }
    public static void userBooks(Connection conn)throws SQLException{
        String sql="select * from library;";
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            int user_id = resultSet.getInt("user_id");
            String fullname = resultSet.getString("fullname");
            int book_id = resultSet.getInt("book_id");
            String author = resultSet.getString("author");
            String title = resultSet.getString("title");
            int exemplars = resultSet.getInt("exemplars");


            String output = "Borrowed books: \n\t user_id: %d \n\t fullname: %s" +
                    "\n\t book_id: %d \n\t author: %s \n\t title: %s \n\t exemplars: %d" ;
            System.out.println(String.format(output, user_id, fullname, book_id, author, title, exemplars));
        }
    }
    public static void userExist(Connection conn, String name)throws SQLException{
        String sql = "SELECT fullname FROM users WHERE fullname = '" + name + "';";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        int rowInserted = preparedStatement.executeUpdate();
        if(rowInserted>0){
            System.out.println("Enter your password");
        }else{
            System.out.println("User not found");
        }
    }
    public static String readPassword(Connection conn, String name)throws SQLException {
        String sql = "SELECT password FROM users WHERE fullname = '" + name + "';";
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        String pswd = resultSet.getString("password");
        return pswd;
    }
}
