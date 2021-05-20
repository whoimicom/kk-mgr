package kim.kin.utils;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Objects;

/**
 * @author choky
 */
@Service
public class DataSourcePropertiesUtil {
    private final DataSourceProperties dataSourceProperties;
    private final DataSourceTransactionManager dataSourceTransactionManager;

    public DataSourcePropertiesUtil(DataSourceProperties dataSourceProperties, DataSourceTransactionManager dataSourceTransactionManager) {
        this.dataSourceProperties = dataSourceProperties;
        this.dataSourceTransactionManager = dataSourceTransactionManager;
    }

    public void test() throws SQLException {
        String databaseProductName = Objects.requireNonNull(dataSourceTransactionManager.getDataSource()).getConnection()
                .getMetaData().getDatabaseProductName();
        System.out.println(databaseProductName);
        printAllFields(dataSourceProperties);
    }

    public static void printAllFields(Object obj) {
        Class<?> cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();
        System.out.println("共有" + fields.length + "个属性");
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                System.out.println(field.getName() + ":" + field.get(obj));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
