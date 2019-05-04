package logic.database;

import utils.DBConnector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataBaseWrapper implements DataBaseService {

    @Override
    public Object getQueryFirstResult(String query) throws SQLException {
        ResultSet resultSet = DBConnector.connector().statement.executeQuery(query);
        return resultSet.next() ? resultSet.getObject(1) : null;
    }

    @Override
    public List<Object> getQueryAllResults(String query) throws SQLException {
        List<Object> results = new ArrayList<>();
        ResultSet resultSet = DBConnector.connector().statement.executeQuery(query);
        while (resultSet.next()) {
            results.add(resultSet.getString(1));
        }
        return results;

    }

    @Override
    public int update(String query) throws SQLException {
        return DBConnector.connector().statement.executeUpdate(query);
    }
}
