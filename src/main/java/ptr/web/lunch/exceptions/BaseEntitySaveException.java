package ptr.web.lunch.exceptions;

public class BaseEntitySaveException extends RuntimeException{

    private static final long serialVersionUID = 1;

    private String entityName;

    public BaseEntitySaveException(String msg) {
        super(msg);
    }

    public BaseEntitySaveException(String msg, String entityName) {
        super(msg);
        setEntityName(entityName);
    }

    public BaseEntitySaveException(Throwable cause) {
        super(cause);
    }

    public BaseEntitySaveException(String msg, Throwable cause) {
        super(msg, cause);
    }


    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
}
