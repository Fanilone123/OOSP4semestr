import java.util.HashMap;
import java.util.Map;

public class DatabaseAPI {
    // Интерфейс для работы с БД
    private DatabaseConnector dbConnector;
    
    // Кэш для read-only данных
    private Map<String, Object> readOnlyCache;
    
    // Флаг для отслеживания активности пользователя
    private boolean isUserActive;
    
    public DatabaseAPI(DatabaseConnector dbConnector) {
        this.dbConnector = dbConnector;
        this.readOnlyCache = new HashMap<>();
        this.isUserActive = false;
    }
    
    /**
     * Подключение пользователя к системе
     */
    public synchronized void connectUser() {
        if (isUserActive) {
            throw new IllegalStateException("Система уже занята другим пользователем");
        }
        isUserActive = true;
        System.out.println("Пользователь успешно подключен");
    }
    
    /**
     * Отключение пользователя от системы
     */
    public synchronized void disconnectUser() {
        isUserActive = false;
        System.out.println("Пользователь отключен");
    }
    
    /**
     * Загрузка данных из БД
     * @param dataId идентификатор данных
     * @return запрошенные данные
     */
    public Object loadData(String dataId) {
        checkUserActive();
        
        // Проверяем, есть ли данные в кэше (если они read-only)
        if (readOnlyCache.containsKey(dataId)) {
            System.out.println("Данные получены из кэша: " + dataId);
            return readOnlyCache.get(dataId);
        }
        
        // Если данных нет в кэше, загружаем из БД
        Object data = dbConnector.fetchData(dataId);
        
        // Если данные read-only, добавляем в кэш
        if (dbConnector.isReadOnly(dataId)) {
            readOnlyCache.put(dataId, data);
            System.out.println("Данные загружены из БД и добавлены в кэш: " + dataId);
        } else {
            System.out.println("Данные загружены из БД: " + dataId);
        }
        
        return data;
    }
    
    /**
     * Сохранение данных в БД
     * @param dataId идентификатор данных
     * @param data данные для сохранения
     */
    public void saveData(String dataId, Object data) {
        checkUserActive();
        
        // Если данные были в кэше как read-only, но теперь изменяются,
        // удаляем их из кэша (на случай, если флаг read-only изменился)
        if (readOnlyCache.containsKey(dataId)) {
            readOnlyCache.remove(dataId);
            System.out.println("Данные удалены из кэша: " + dataId);
        }
        
        dbConnector.storeData(dataId, data);
        System.out.println("Данные сохранены в БД: " + dataId);
    }
    
    /**
     * Формирование отчета
     * @param reportId идентификатор отчета
     * @return сформированный отчет
     */
    public Object generateReport(String reportId) {
        checkUserActive();
        // Для формирования отчета всегда запрашиваем актуальные данные из БД
        return dbConnector.generateReport(reportId);
    }
    
    /**
     * Обновление данных по таймеру
     */
    public void refreshData() {
        checkUserActive();
        // Очищаем кэш, чтобы при следующем запросе получить актуальные данные
        readOnlyCache.clear();
        System.out.println("Кэш очищен, данные будут обновлены при следующем запросе");
    }
    
    /**
     * Проверка, что пользователь активен
     */
    private void checkUserActive() {
        if (!isUserActive) {
            throw new IllegalStateException("Нет активного пользователя");
        }
    }
    
    // Интерфейс для работы с БД (может быть реализован для конкретной СУБД)
    public interface DatabaseConnector {
        Object fetchData(String dataId);
        void storeData(String dataId, Object data);
        boolean isReadOnly(String dataId);
        Object generateReport(String reportId);
    }
}s