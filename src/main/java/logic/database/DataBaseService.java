package logic.database;

import java.sql.SQLException;
import java.util.List;

public interface DataBaseService {

    Object getQueryFirstResult(String query) throws SQLException;
    List<Object> getQueryAllResults(String query) throws SQLException;
    int update(String query) throws SQLException;
}
