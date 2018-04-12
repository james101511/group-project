package DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.sql.DataSource;

//import com.sun.xml.internal.ws.Closeable;

public class DataBase
{
	private DataSource dataSource;

	public DataBase(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	public List<User> getUsers() throws Exception
	{
		List<User> users = new ArrayList<>();

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;

		try
		{
			// get a connection
			myConn = dataSource.getConnection();
			// create sql statement
			String sql = "select *from Users order by Lastname";
			myStmt = myConn.createStatement();
			// execute query
			myRs = myStmt.executeQuery(sql);
			// process result set
			while (myRs.next())
			{
				// retrieve data from result set row
				int id = myRs.getInt("id");
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				String password = myRs.getString("password");
				// create new student object
				User tempuser = new User(id, firstName, lastName, email, password);
				// add it to the list of students
				users.add(tempuser);

			}

			return users;
		}

		finally
		{
			// close JDBC objects
			Close(myConn, myStmt, myRs);
		}

	}

	private void Close(Connection myConn, Statement myStmt, ResultSet myRs)
	{
		try
		{
			if (myRs != null)
			{
				myRs.close();
			}
			if (myStmt != null)
			{
				myStmt.close();
			}
			if (myConn != null)
			{
				myConn.close();// does't really close it, just puts back in connection pool
			}

		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}

	public void addUser(User theuser) throws Exception
	{
		Connection myConn = null;
		PreparedStatement myStmt = null;
		try
		{
			// get db connection
			myConn = dataSource.getConnection();
			// create sql for insert
			String sql = "insert into Users" + "(FirstName,LastName,Email,Password)" + "values(?,?,?,?)";
			myStmt = myConn.prepareStatement(sql);
			// set the param values for the student
			myStmt.setString(1, theuser.getFirstName());
			myStmt.setString(2, theuser.getLastName());
			myStmt.setString(3, theuser.getEmail());
			myStmt.setString(4, theuser.getPassword());
			// execute sql insert
			myStmt.execute();
		}
		finally
		{
			// clean up JDBC objects
			Close(myConn, myStmt, null);
		}

	}

	public User login(User theuser) throws Exception
	{
		Connection myConn = null;
		PreparedStatement myStmt = null;

		try
		{
			myConn = dataSource.getConnection();
			// create sql for insert
			String sql = "select * from Users where Email = ? and  password= ? ";
			myStmt = myConn.prepareStatement(sql);
			// set the param values for the student
			myStmt.setString(1, theuser.getEmail());
			myStmt.setString(2, theuser.getPassword());
			ResultSet set = myStmt.executeQuery();
			// execute sql insert
			while (set.next())
			{
				theuser.setFirstName(set.getString("FirstName"));
				theuser.setLastName(set.getString("LastName"));
				return theuser;
			}
			return null;
		}
		finally
		{
			// clean up JDBC objects
			Close(myConn, myStmt, null);
		}
	}

	public void addProject(Project project) throws Exception
	{
		Connection myConn = null;
		PreparedStatement myStmt = null;
		try
		{
			// get db connection
			myConn = dataSource.getConnection();
			// create sql for insert
			String sql = "insert into Project" + "(ProjectName,SubTask,StartDate,EndDate)" + "values(?,?,?,?)";
			myStmt = myConn.prepareStatement(sql);
			// set the param values for the student
			myStmt.setString(1, project.getProjectName());
			myStmt.setString(2, project.getTask());
			myStmt.setString(3, project.getStartDate());
			myStmt.setString(4, project.getEndDate());
			// myStmt.setBoolean(5, true);
			// execute sql insert
			myStmt.execute();
		}
		finally
		{
			// clean up JDBC objects
			Close(myConn, myStmt, null);
		}

	}

	public void addManager(Involve involve) throws SQLException
	{
		Connection myConn = null;
		PreparedStatement myStmt = null;
		try
		{
			// get db connection
			myConn = dataSource.getConnection();
			// create sql for insert
			String sql = "insert into Involve" + "(ProjectName,Email,Admin)" + "values(?,?,?)";
			myStmt = myConn.prepareStatement(sql);
			// set the param values for the student
			myStmt.setString(1, involve.getProjectName());
			myStmt.setString(2, involve.getEmail());
			myStmt.setBoolean(3, true);
			// execute sql insert
			myStmt.execute();
		}
		finally
		{
			// clean up JDBC objects
			Close(myConn, myStmt, null);
		}

	}

	public List<Involve> CheckInvolve(Involve involve) throws Exception
	{
		Connection myConn = null;
		PreparedStatement myStmt = null;
		List<Involve> involves = new ArrayList<>();
		try
		{
			myConn = dataSource.getConnection();
			// create sql for insert
			String sql = "select * from Involve where Email = ? ";
			myStmt = myConn.prepareStatement(sql);
			// set the param values for the student
			myStmt.setString(1, involve.getEmail());
			ResultSet set = myStmt.executeQuery();
			// execute sql insert
			while (set.next())
			{
				String ProjectName = set.getString("ProjectName");
				String Email = set.getString("Email");
				boolean admin = true;
				if (set.getString("Admin").equals("0"))
				{
					admin = false;
				}
				Involve TempInvolve = new Involve(ProjectName, Email, admin);
				involves.add(TempInvolve);

			}
			return involves;
		}
		finally
		{
			// clean up JDBC objects
			Close(myConn, myStmt, null);
		}
	}

	public void addMember(Involve involve) throws SQLException
	{
		Connection myConn = null;
		PreparedStatement myStmt = null;
		try
		{
			myConn = dataSource.getConnection();
			// create sql for insert
			String sql = "insert into Involve" + "(ProjectName,Email,Admin)" + "values(?,?,?)";
			myStmt = myConn.prepareStatement(sql);
			// set the param values for the student
			myStmt.setString(1, involve.getProjectName());
			myStmt.setString(2, involve.getEmail());
			myStmt.setBoolean(3, false);
			// execute sql insert
			myStmt.execute();
		}
		finally
		{
			// clean up JDBC objects
			Close(myConn, myStmt, null);
		}

	}

	public List<Involve> checkProject(Involve involve) throws SQLException
	{

		Connection myConn = null;
		PreparedStatement myStmt = null;
		List<Involve> involves = new ArrayList<>();
		try
		{
			myConn = dataSource.getConnection();
			// create sql for insert
			String sql = "select * from Involve where ProjectName = ? ";
			myStmt = myConn.prepareStatement(sql);
			// set the param values for the student
			myStmt.setString(1, involve.getProjectName());
			ResultSet set = myStmt.executeQuery();
			// execute sql insert
			while (set.next())
			{
				String ProjectName = set.getString("ProjectName");
				String Email = set.getString("Email");
				boolean admin = true;
				if (set.getString("Admin").equals("0"))
				{
					admin = false;
				}
				Involve TempInvolve = new Involve(ProjectName, Email, admin);

				involves.add(TempInvolve);
			}
			return involves;
		}
		finally
		{
			// clean up JDBC objects
			Close(myConn, myStmt, null);
		}
	}

	public List<Task> checkTask(Task task) throws SQLException
	{
		Connection myConn = null;
		PreparedStatement myStmt = null;
		List<Task> tasks = new ArrayList<>();
		try
		{
			myConn = dataSource.getConnection();
			String sql = "select * from Task where ProjectName = ? ";
			myStmt = myConn.prepareStatement(sql);
			myStmt.setString(1, task.getProjectName());
			ResultSet set = myStmt.executeQuery();
			while (set.next())
			{
				String TaskName = set.getString("TaskName");
				String StartDate = set.getString("StartDate");
				String EndDate = set.getString("EndDate");
				Task task2 = new Task(task.getProjectName(), TaskName, StartDate, EndDate);
				tasks.add(task2);
			}
			return tasks;
		}
		finally
		{
			// clean up JDBC objects
			Close(myConn, myStmt, null);
		}
	}

	public List<TaskInvolve> checkTaskDetail(TaskInvolve taskInvolve) throws SQLException
	{
		Connection myConn = null;
		PreparedStatement myStmt = null;
		List<TaskInvolve> taskInvolves = new ArrayList<>();
		try
		{
			myConn = dataSource.getConnection();
			String sql = "select * from task_involve where taskName = ? and projectName=? ";
			myStmt = myConn.prepareStatement(sql);
			myStmt.setString(1, taskInvolve.getTaskName());
			myStmt.setString(2, taskInvolve.getProjectName());
			ResultSet set = myStmt.executeQuery();
			while (set.next())
			{
				String userEmail = set.getString("userEmail");
				TaskInvolve taskInvolve2 = new TaskInvolve(taskInvolve.getTaskName(), userEmail,
						taskInvolve.getProjectName());
				taskInvolves.add(taskInvolve2);
			}
			return taskInvolves;
		}
		finally
		{
			// clean up JDBC objects
			Close(myConn, myStmt, null);
		}
	}

	public boolean checkadmin(String projectName, String email) throws SQLException
	{
		Connection myConn = null;
		PreparedStatement myStmt = null;
		boolean admin = true;

		try
		{
			myConn = dataSource.getConnection();
			String sql = "select * from Involve where ProjectName = ? and Email = ? ";
			myStmt = myConn.prepareStatement(sql);
			myStmt.setString(1, projectName);
			myStmt.setString(2, email);
			ResultSet set = myStmt.executeQuery();
			while (set.next())
			{

				String admin2 = set.getString("Admin");
				if (admin2.equals("0"))
				{
					admin = false;
				}

			}
			return admin;
		}
		finally
		{
			// clean up JDBC objects
			Close(myConn, myStmt, null);
		}

	}

	public void addTask(Task task) throws SQLException
	{
		Connection myConn = null;
		PreparedStatement myStmt = null;
		try
		{
			myConn = dataSource.getConnection();
			// create sql for insert
			String sql = "insert into Task" + "(TaskName,ProjectName,StartDate,EndDate)" + "values(?,?,?,?)";
			myStmt = myConn.prepareStatement(sql);
			// set the param values for the student
			myStmt.setString(1, task.getTaskName());
			myStmt.setString(2, task.getProjectName());
			myStmt.setString(3, task.getStartDate());
			myStmt.setString(4, task.getEndDate());
			// execute sql insert
			myStmt.execute();
		}
		finally
		{
			// clean up JDBC objects
			Close(myConn, myStmt, null);
		}

	}

	public void deleteTask(String projectName, String taskName) throws SQLException
	{
		Connection myConn = null;
		PreparedStatement myStmt = null;
		try
		{
			myConn = dataSource.getConnection();
			// create sql for insert
			String sql = "DELETE FROM Task WHERE ProjectName = ? and TaskName =?";
			// DELETE FROM TaskInvolve WHERE projectName =? and TaskName =? ;
			myStmt = myConn.prepareStatement(sql);
			myStmt.setString(1, projectName);
			myStmt.setString(2, taskName);
			// myStmt.setString(3, projectName);
			// myStmt.setString(4, taskName);

			// execute sql insert
			myStmt.execute();
		}
		finally
		{
			// clean up JDBC objects
			Close(myConn, myStmt, null);
		}

	}

	public void editTask(String projectName, String taskName, String startDate, String endDate) throws SQLException
	{
		Connection myConn = null;
		PreparedStatement myStmt = null;
		try
		{
			myConn = dataSource.getConnection();
			String sql = "UPDATE Task SET StartDate= ?,EndDate= ? WHERE projectName = ? and TaskName=?;";
			myStmt = myConn.prepareStatement(sql);
			myStmt.setString(1, startDate);
			myStmt.setString(2, endDate);
			myStmt.setString(3, projectName);
			myStmt.setString(4, taskName);

			// execute sql insert
			myStmt.execute();
		}
		finally
		{
			// clean up JDBC objects
			Close(myConn, myStmt, null);
		}

	}

	public List<String> checkTaskInvolve(String projectName, String email) throws SQLException
	{
		Connection myConn = null;
		PreparedStatement myStmt = null;
		List<String> taskNames = new ArrayList<>();
		try
		{
			myConn = dataSource.getConnection();
			String sql = "select * from task_involve where userEmail = ? and projectName=?";
			myStmt = myConn.prepareStatement(sql);
			myStmt.setString(1, email);
			myStmt.setString(2, projectName);
			ResultSet set = myStmt.executeQuery();
			while (set.next())
			{
				String taskName = set.getString("taskName");
				taskNames.add(taskName);
			}
			return taskNames;
		}
		finally
		{
			// clean up JDBC objects
			Close(myConn, myStmt, null);
		}

	}

	public void addTaskMember(TaskInvolve taskInvolve) throws SQLException
	{
		Connection myConn = null;
		PreparedStatement myStmt = null;
		try
		{
			myConn = dataSource.getConnection();
			// create sql for insert
			String sql = "insert into task_involve" + "(userEmail,taskName,projectName)" + "values(?,?,?)";
			myStmt = myConn.prepareStatement(sql);

			myStmt.setString(1, taskInvolve.getUserEmail());
			myStmt.setString(2, taskInvolve.getTaskName());
			myStmt.setString(3, taskInvolve.getProjectName());

			// execute sql insert
			myStmt.execute();
		}
		finally
		{
			// clean up JDBC objects
			Close(myConn, myStmt, null);
		}
	}

	public void deleteTaskMember(TaskInvolve taskInvolve) throws SQLException
	{
		Connection myConn = null;
		PreparedStatement myStmt = null;
		try
		{
			myConn = dataSource.getConnection();
			// create sql for insert
			String sql = "DELETE FROM task_involve WHERE projectName = ? and taskName =? and userEmail=?";
			// DELETE FROM TaskInvolve WHERE projectName =? and TaskName =? ;
			myStmt = myConn.prepareStatement(sql);
			myStmt.setString(1, taskInvolve.getProjectName());
			myStmt.setString(2, taskInvolve.getTaskName());
			myStmt.setString(3, taskInvolve.getUserEmail());
			// myStmt.setString(3, projectName);
			// myStmt.setString(4, taskName);

			// execute sql insert
			myStmt.execute();
		}
		finally
		{
			// clean up JDBC objects
			Close(myConn, myStmt, null);
		}
		
	}

}
