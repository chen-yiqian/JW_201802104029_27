package bysj.Dao;

import bysj.Daomain.School;
import bysj.Util.JdbcHelper;
import bysj.Dao.SchoolDao;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class SchoolDao {
	private static SchoolDao schoolDao = new SchoolDao();
	private static Collection<School> schools;
	public static SchoolDao getInstance(){
		return schoolDao;
	}

	public Collection<School> findAll() throws SQLException {
		//创建一个HashSet对象，并把它加到set
		Set schools=new HashSet<School>();
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建语句盒子对象
		Statement statement = connection.createStatement();
		//执行sql查询语句并获得结果集对象
		ResultSet resultSet= statement.executeQuery("select * from school");
		//若结果集仍有下一条记录，则执行循环体
		while (resultSet.next()){
			//获得数据库的信息，并用于在网页中显示
			School school = new School(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"));
			//添加到集合中
			schools.add(school);
		}
		return schools;
	}
	
	public School find(Integer id) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        String deleteSchool_sql = "SELECT * FROM school where id = ?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(deleteSchool_sql);
        //为预编译语句赋值
        preparedStatement.setInt(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        School school = null;
        //由于id不能取重复值，故结果集中最多有一条记录
        //若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Degree对象
        //若结果集中没有记录，则本方法返回null
        if(resultSet.next()){
            school = new School(resultSet.getInt("id"),
                    resultSet.getString("description"),
                    resultSet.getString("no"),
                    resultSet.getString("remarks"));
        }
        //关闭资源
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return school;
	}
	
	public boolean update(School school) throws SQLException {
		Connection connection=JdbcHelper.getConn();
		String updatesql="update school set description=?,no=?,remarks=? where id=?";
		PreparedStatement preparedStatement=connection.prepareStatement(updatesql);
		preparedStatement.setString(1,school.getDescription());
		preparedStatement.setString(2,school.getNo());
		preparedStatement.setString(3,school.getRemarks());
		preparedStatement.setInt(4,school.getId());
		int affected=preparedStatement.executeUpdate();
		JdbcHelper.close(preparedStatement,connection);
		return affected>0;
	}
	
	public boolean add(School school) throws SQLException {
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句
		String addSchool_sql = "insert into school(description,no,remarks) values " + "(?,?,?)";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(addSchool_sql);
		//为预编译参数赋值
		pstmt.setString(1, school.getDescription());
		pstmt.setString(2, school.getNo());
		pstmt.setString(3, school.getRemarks());
		//执行预编译对象的executeUpdate方法，获取添加的记录行数
		int affectedRowNum=pstmt.executeUpdate();
		//关闭
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum>0;
	}

	public boolean delete(Integer id) throws SQLException {
		//获得连接对象
		Connection connection=JdbcHelper.getConn();
		//创建sql语句
		String deleteSchool_sql="DELETE FROM school WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt=connection.prepareStatement(deleteSchool_sql);
		//为预编译参数赋值
		pstmt.setInt(1,id);
		int affectedRowNum=pstmt.executeUpdate();
		JdbcHelper.close(pstmt,connection);
		//得到id值
		return affectedRowNum > 0;
	}
}
