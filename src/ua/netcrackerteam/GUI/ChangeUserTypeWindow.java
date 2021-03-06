package ua.netcrackerteam.GUI;

import com.vaadin.data.Property;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;
import ua.netcrackerteam.controller.GeneralController;

import java.util.Date;

/**
 * @author krygin
 */
public class ChangeUserTypeWindow extends Window implements AbstractSelect.NewItemHandler,
        Property.ValueChangeListener, Button.ClickListener{
    AdminUserManagementLayout adminUserManagementLayout;
    private final String currentUser;
    private final String userLogin;
    private ComboBox comboBox;
    private String selectedUserType = "";
    private Boolean lastAdded = false;
    private static final String[] userTypes = new String[] { "Admin", "HR", "Interviewer" };
    Button acceptChangeButton = new Button("change user type");

    public ChangeUserTypeWindow(AdminUserManagementLayout adminUserManagementLayout, String currentUser, String userLogin) {
        this.adminUserManagementLayout = adminUserManagementLayout;
        this.currentUser = currentUser;
        this.userLogin = userLogin;
        setModal(true);
        setWidth("30%");
        setIcon(new ThemeResource("icons/32/change-user-type.png"));
        setResizable(false);
        center();
        setCaption("Change User Type");
        VerticalLayout layout = (VerticalLayout) getContent();
        layout.setWidth("100%");
        layout.setSpacing(true);
        layout.setMargin(true);

        comboBox = new ComboBox("Выбирете категорию юзера для - " + currentUser);
        for (int i = 0; i < userTypes.length; i++) {
            comboBox.addItem(userTypes[i]);
        }
        comboBox.setNewItemHandler(this);
        comboBox.setNewItemsAllowed(false);
        comboBox.setRequired(true);
        comboBox.setImmediate(true);
        comboBox.addListener(this);
        layout.addComponent(comboBox);

        acceptChangeButton.addListener((Button.ClickListener) this);
        acceptChangeButton.setIcon(new ThemeResource("icons/32/ok.png"));
        layout.addComponent(acceptChangeButton);

        layout.setComponentAlignment(comboBox, Alignment.BOTTOM_CENTER);
        layout.setComponentAlignment(acceptChangeButton, Alignment.BOTTOM_CENTER);
    }

    public void buttonClick(Button.ClickEvent event) {
        Button source = event.getButton();
        if (source == acceptChangeButton) {
            if (selectedUserType.equals("")) {
                getWindow().showNotification("Братиша, выбери-ка Тип юзера А??!!!", Notification.TYPE_TRAY_NOTIFICATION);
            }  else {
                if (!GeneralController.checkUserTypeCategoty(currentUser, selectedUserType)){
                    GeneralController.changeUserType(currentUser, selectedUserType);
                    GeneralController.setAuditInterviews(6, "User Type changed for user - " + currentUser + " to - " + selectedUserType, userLogin, new Date());
                    Notification n = new Notification("Смена типа юзера " + currentUser + " на " + selectedUserType + " завершена успешно!", Notification.TYPE_TRAY_NOTIFICATION);
                    n.setDescription("На email юзера " + currentUser + " выслано письмо с уведомлением.");
                    n.setPosition(Notification.POSITION_CENTERED);
                    adminUserManagementLayout.getWindow().showNotification(n);
                    adminUserManagementLayout.refreshTableData();
                    adminUserManagementLayout.refreshTableData();
                    ChangeUserTypeWindow.this.close();
                } else {
                    getWindow().showNotification("Юзер уже является - " + selectedUserType + ". Выбирите для него другой тип юзера.", Notification.TYPE_TRAY_NOTIFICATION);
                }

            }
        }
    }

    public void valueChange(Property.ValueChangeEvent event) {
        if (!lastAdded) {
            getWindow().showNotification("Selected user type: " + event.getProperty(), Notification.TYPE_TRAY_NOTIFICATION);
            selectedUserType = String.valueOf(event.getProperty());
            if (selectedUserType == null) {
                selectedUserType = "";
            }
        }
        lastAdded = false;
    }

    @Override
    public void addNewItem(String newItemCaption) {

    }
}
