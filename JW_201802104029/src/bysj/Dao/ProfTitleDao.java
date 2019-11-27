package bysj.Dao;

import bysj.Daomain.ProfTitle;
import bysj.Service.ProfTitleService;
import bysj.Util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public final class ProfTitleDao {
	private static ProfTitleDao profTitleDao=new ProfTitleDao();
	public static ProfTitleDao getInstance(){
		return profTitleDao;
	}
	public Collection<ProfTitle> findAll() throws SQLException {
		//创建一个HashSet对象，并把它加到set
		Set proftitles=new HashSet<ProfTitle>();
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建语句盒子对象
		Statement statement = connection.createStatement();
		//执行sql查询语句并获得结果集对象
		ResultSet resultSet= statement.executeQuery("select * from proftitle");
		//若结果集仍有下一条记录，则执行循环体
		while (resultSet.next()){
			//获得数据库的信息，并用于在网页中显示
			ProfTitle profTitle = new ProfTitle(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"));
			//添加到集合中
			proftitles.add(profTitle);
		}
		return proftitles;
	}

	public ProfTitle find(Integer id)throws SQLException {
		ProfTitle profTitle = null;
		Connection connection=JdbcHelper.getConn();
		String findid="select * from proftitle where id =?";
		PreparedStatement preparedStatement=connection.prepareStatement(findid);
		preparedStatement.setInt(1,id);
		ResultSet resultSet=preparedStatement.executeQuery();
		if(resultSet.next()){
			profTitle=new ProfTitle(
					resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks"));
		}
		JdbcHelper.close(preparedStatement,connection);
		return profTitle;
	}

	public boolean update(ProfTitle profTitle)throws SQLException{
		Connection connection=JdbcHelper.getConn();
		String updatesql="update proftitle set description=?,no=?,remarks=? where id=?";
		PreparedStatement preparedStatement=connection.prepareStatement(updatesql);
		preparedStatement.setString(1,profTitle.getDescription());
		preparedStatement.setString(2,profTitle.getNo());
		preparedStatement.setString(3,profTitle.getRemarks());
		preparedStatement.setInt(4,profTitle.getId());
		int affected=preparedStatement.executeUpdate();
		JdbcHelper.close(preparedStatement,connection);
		return affected>0;
	}

	public boolean add(ProfTitle profTitle)throws SQLException{
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句
		String addProftitle_sql = "insert into proftitle(no,description,remarks) values " + "(?,?,?)";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(addProftitle_sql);
		//为预编译参数赋值
		pstmt.setString(1, profTitle.getNo());
		pstmt.setString(2, profTitle.getDescription());
		pstmt.setString(3, profTitle.getRemarks());
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
		String deleteProftitle_sql="DELETE FROM proftitle WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt=connection.prepareStatement(deleteProftitle_sql);
		//为预编译参数赋值
		pstmt.setInt(1,id);
		int affectedRowNum=pstmt.executeUpdate();
		JdbcHelper.close(pstmt,connection);
		//得到id值
		return affectedRowNum > 0;
	}
}

