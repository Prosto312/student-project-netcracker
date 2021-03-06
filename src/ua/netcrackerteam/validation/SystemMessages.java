package ua.netcrackerteam.validation;

import com.vaadin.ui.Window.Notification;

/**
 * @author AlexK
 * @version 1.0.0
 */
public enum SystemMessages {
    SQL_CONNECTION_ERROR("Ошибка", Notification.TYPE_TRAY_NOTIFICATION,
            "Не удалось подключиться к базе данных. Проверьте соединение с интернетом"),

    RUNTIME_ERROR("Ошибка выполнения", Notification.TYPE_TRAY_NOTIFICATION,
            "Пожалуйста перезагрузите страницу"),

    LOGIN_ERROR("Логин и/или пароль не верны!", Notification.TYPE_TRAY_NOTIFICATION),

    EMPTY_FIELDS("Заполните пожалуйста обязательное поле(поля)", Notification.TYPE_TRAY_NOTIFICATION),

    PASSWORD_ERROR("Неверный текущий пароль !", Notification.TYPE_TRAY_NOTIFICATION),

    REGISTRATION_SUCCESSFUL("Регистрация пользователя {0} завершена успешно!",  Notification.TYPE_TRAY_NOTIFICATION),

    REGISTRATION_SUCCESSFUL_WITHOUT_USERNAME("Регистрация пользователя завершена успешно!",  Notification.TYPE_TRAY_NOTIFICATION),

    CHANGE_PASSWORD_SUCCESSFUL("Изменение пароля юзера завершено успешно!",  Notification.TYPE_TRAY_NOTIFICATION);

    private String caption;
    private String description;
    private int type;
    private Notification notification;

    public String getCaption() {
        return caption;
    }

    public String getDescription() {
        return description;
    }

    public int getType() {
        return type;
    }

    public Notification getNotification() {
        return notification;
    }

    private SystemMessages(String caption, int type){
        this.caption = caption;
        this.type = type;
        notification = new Notification(caption, type);
        notification.setDescription(description);
    }

    private SystemMessages(String caption, int type, String description){
        this.caption = caption;
        this.type = type;
        this.description = description;
        notification = new Notification(caption, type);
        notification.setDescription(description);
    }

    private SystemMessages(String caption, int type, String description, int position){
        this.caption = caption;
        this.type = type;
        this.description = description;
        notification = new Notification(caption, type);
        notification.setDescription(description);
        notification.setPosition(position);
    }
}
