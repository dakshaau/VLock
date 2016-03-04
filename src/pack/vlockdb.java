package pack;
import java.sql.*;
public class vlockdb
{
public static void main(String args[])
	{
	
	Connection c = null;
	Statement stmt = null;
	try {
	Class.forName("org.sqlite.JDBC");
	c = DriverManager.getConnection("jdbc:sqlite:VLD.db");
	System.out.println("Opened database successfully");
	stmt = c.createStatement();
	
	
	
	
	
	
	
	/*String sql="CREATE TABLE USER(USERID INT NOT NULL PRIMARY KEY" +
			",UNAME VARCHAR(20) NOT NULL UNIQUE," +
			"EMAIL VARCHAR(50) NOT NULL,ENOT VARCHAR(3) NOT NULL," +
			" SECANS VARCHAR(20) NOT NULL," +
			"SECQ VARCHAR(100) NOT NULL,"+" PASSWORD VARCHAR(20) NOT NULL )";
	stmt.executeUpdate(sql);
	
	 sql="CREATE TABLE AUDIOSAMPLE "+"(ID INT NOT NULL,"+
			"UID INT NOT NULL,"+
			"SAMPLEPATH VARCHAR(50) NOT NULL" +
			",FOREIGN KEY (UID) REFERENCES USER(USERID) )";

			stmt.executeUpdate(sql);
			
	
	 sql = "CREATE TABLE FILEPATH" +
			"(ID INT PRIMARY KEY NOT NULL," +
			" FILEPATH VARCHAR(50) NOT NULL, " +
			" UID INT NOT NULL," +
			"FOREIGN KEY (UID) REFERENCES USER(USERID) )";
	stmt.executeUpdate(sql);*/
	
	String sql;/*="insert into USER " +
				"VALUES (1,'MASTER', 'NA','NA','NA','NA','flinstones');";
	stmt.executeUpdate(sql);*/
	//sql="delete from USER where USERID=21;";
	//sql="update USER set EMAIL='kaushal.singh_cs11@gla.ac.in',ENOT='yes',SECANS='dks',SECQ='hkjk',PASSWORD='dfhg' where USERID=2;";
	//stmt.executeUpdate(sql);
	sql="SELECT * FROM FILEPATH ;";
	ResultSet rst=stmt.executeQuery(sql);
	while(rst.next()){
		System.out.println(rst.getInt(1)+" "+rst.getString(2)+" "+rst.getInt(3));//+
				//" "+rst.getString(4)+" "+rst.getString(5)+" "+rst.getString(6)+" "+rst.getString(7));
	}
	//System.out.println("Count(kaushal) = "+rst.getLong(1));*/
	stmt.close();
	c.close();
	} catch ( Exception e ) {
	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	System.exit(0);
	}
	System.out.println("Table created successfully");
	}
}