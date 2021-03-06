/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.netcrackerteam.GUI;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.Runo;
import ua.netcrackerteam.controller.HRPage;
import ua.netcrackerteam.controller.InterviewerPage;
import ua.netcrackerteam.controller.bean.HRInterview;
import ua.netcrackerteam.controller.exceptions.HRException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anna Kushnirenko
 */
@SuppressWarnings("serial")
public class HRInterviewsLayout extends VerticalLayout {
    private final Panel rightPanel;
    private final Panel sidebar;
    private final int screenHeight;
    private InterviewsTable table;
    private boolean editable = false;
    private HRPage controller = new HRPage();

    private BottomLayout bottomLayout;
	private Component positionsInfoLayout;

    public HRInterviewsLayout(int height) {
        this.screenHeight = height;
        setHeight(screenHeight,UNITS_PIXELS);
        setWidth("100%");
        setMargin(true, false, false, false);
        
        HorizontalSplitPanel splitH = new HorizontalSplitPanel();
        splitH.setStyleName(Runo.SPLITPANEL_REDUCED);
        splitH.setSplitPosition(230, Sizeable.UNITS_PIXELS);
        addComponent(splitH);
        
        sidebar = new Panel("Меню");
        fillSideBar();
        splitH.setFirstComponent(sidebar);
        
        rightPanel = new Panel("Список собеседований");
        fillRightPanel();
        splitH.setSecondComponent(rightPanel);
    }

    private void fillSideBar() {
        VerticalLayout vl = (VerticalLayout) sidebar.getContent();
        vl.setSpacing(true);
        
        sidebar.setHeight("100%");
        Button addInterview = new Button("Добавить собеседование");
        addInterview.setStyleName(Runo.BUTTON_LINK);
        addInterview.setIcon(new ThemeResource("icons/32/document-add.png"));
        addInterview.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                editable = false;
                table.setValue(null);
                BottomLayout old = bottomLayout;
                bottomLayout = new BottomLayout();
                rightPanel.replaceComponent(old, bottomLayout);
            }
        });
        sidebar.addComponent(addInterview);
        
        Button editInterview = new Button("Редактировать собеседование");
        editInterview.setStyleName(Runo.BUTTON_LINK);
        editInterview.setIcon(new ThemeResource("icons/32/document-edit.png"));
        editInterview.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if(table.getValue() == null) {
                    getWindow().showNotification("Выберите собеседование!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                } else {
                    editable = true;
                    editInterview();
                }
            }
        });
        sidebar.addComponent(editInterview);
        
        Button deleteInterview = new Button("Удалить собеседование");
        deleteInterview.setStyleName(Runo.BUTTON_LINK);
        deleteInterview.setIcon(new ThemeResource("icons/32/document-delete.png"));
        deleteInterview.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if(table.getValue() == null) {
                    getWindow().showNotification("Выберите собеседование!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                } else {
                    deleteInterview();
                }
            }
        });
        sidebar.addComponent(deleteInterview);
        positionsInfoLayout = getPositionsInfoLayout();
        sidebar.addComponent(positionsInfoLayout);
    }

    private Component getPositionsInfoLayout() {
        VerticalLayout infoLayout = new VerticalLayout();
        infoLayout.setSpacing(true);
        String restOfPositionsCaption =  "Остаток мест: " + controller.getRestOfPositionsOnInterviews();
        Label restOfPositions = new Label(restOfPositionsCaption);
        restOfPositions.setDescription("Суммарный остаток мест на всех собеседованиях");
        infoLayout.addComponent(restOfPositions);
        String countOnReservedInterviewCaption =  "На резервном: " + controller.getCountOfStudentsOnReservedInterview();
        Label countOnReservedInterview = new Label(countOnReservedInterviewCaption);
        countOnReservedInterview.setDescription("Количество студентов, зарегистрированных на резервное собеседование");
        infoLayout.addComponent(countOnReservedInterview);
        return infoLayout;
    }
    
    private void refreshPositionsInfoLayout() {
    	Component newPositionsLayout = getPositionsInfoLayout();
    	sidebar.replaceComponent(positionsInfoLayout, newPositionsLayout);
    	positionsInfoLayout = newPositionsLayout;
    }

    private void fillRightPanel() {
        rightPanel.setHeight("100%");
        VerticalLayout vl = (VerticalLayout) rightPanel.getContent();
        vl.setMargin(false);
        vl.setSpacing(true);
        
        table = new InterviewsTable();
        rightPanel.addComponent(table);
        
        bottomLayout = new BottomLayout();
        bottomLayout.setVisible(false);
        rightPanel.addComponent(bottomLayout);
    }
    
    private void deleteInterview() {
         final Window confirm = new Window("Внимание!");
         VerticalLayout vl = (VerticalLayout) confirm.getContent();
         vl.setSpacing(true);
         vl.setMargin(true);
         confirm.setModal(true);
         confirm.setWidth("20%");
         confirm.setResizable(false);
         confirm.center();
         confirm.addComponent(new Label("Вы уверены что хотите удалить собеседование(я)?"));
         HorizontalLayout layout = new HorizontalLayout();
         Button ok = new Button("ОК");
         ok.setWidth("100");
         ok.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Set<HRInterview> interviews = (Set<HRInterview>) table.getValue();
                for(HRInterview interview: interviews) {
                    try {
                        controller.deleteInterview(interview.getId(), interview.getReserve());
                    }catch (HRException hrE) {
                        getWindow().showNotification(hrE.getMessage(), Window.Notification.TYPE_TRAY_NOTIFICATION);
                    }
                }
                refreshTable();
                getWindow().removeWindow(confirm);
            }
        });
         Button cancel = new Button("Отмена");
         cancel.setWidth("100");
         cancel.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                getWindow().removeWindow(confirm);
            }
        });
         layout.addComponent(ok);
         layout.addComponent(cancel);
         confirm.addComponent(layout);
         getWindow().addWindow(confirm);
    }
    
    private void editInterview() {


        Set<HRInterview> interviews = (Set<HRInterview>) table.getValue();
        if(interviews.size()==1)  {
            BottomLayout old = bottomLayout;
            bottomLayout = new BottomLayout();
            HRInterview interview = interviews.iterator().next();
            bottomLayout.setDate(interview.getDate());
            bottomLayout.setStartTime(interview.getStartTime());
            bottomLayout.setEndTime(interview.getEndTime());
            bottomLayout.setIntervCount(interview.getInterviewersNum());
            bottomLayout.setPositionsCount(interview.getPositionNum());
            bottomLayout.duration.setRequired(false);
            bottomLayout.duration.setReadOnly(true);
            //bottomLayout.setReserve(interview.getReserve());
            rightPanel.replaceComponent(old, bottomLayout);
        } else {
            getWindow().showNotification("Выберите только одно собеседование из списка!", Window.Notification.TYPE_TRAY_NOTIFICATION);
        }
    }
    
    private void refreshTable() {
        Table old = table;
        table = new InterviewsTable();
        table.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                bottomLayout.setVisible(false);
            }
        });
        rightPanel.replaceComponent(old, table);
    }

    private class InterviewsTable extends Table{
        
        public String[] NATURAL_COL_ORDER = new String[] {
                "date", "startTime", "endTime", 
                "positionNum", "restOfPositions", "interviewersNum"};
        
        public String[] COL_HEADERS_RUSSIAN = new String[] {
                "Дата", "Время начала", "Время окончания",
                "Количество мест", "Остаток мест", "Количество интервьюеров"};
        
        private InterviewsTable() {
            super();
            List<HRInterview> interviews = controller.getInterviewsList();
            BeanItemContainer<HRInterview> bean = new BeanItemContainer<HRInterview>(HRInterview.class, interviews);
            setContainerDataSource(bean);
            setWidth("100%");
            setHeight(300,UNITS_PIXELS);
            setSelectable(true);
            setImmediate(true);
            setMultiSelect(true);
            setColumnReorderingAllowed(true);
            setColumnCollapsingAllowed(true);
            setVisibleColumns(NATURAL_COL_ORDER);
            setColumnHeaders(COL_HEADERS_RUSSIAN);
        }
    }
    
    private class TimeSelection extends GridLayout{
        private final NativeSelect hours;
        private final NativeSelect minutes;

        public TimeSelection(String caption) {
            setWidth("120");
            setColumns(3);
            setRows(2);
            addComponent(new Label(caption),0,0,2,0);
            hours = new NativeSelect();
            minutes = new NativeSelect();
            for (int i = 0; i < 24; i++) {
                String strHours = i + "";
                if(i<10) {
                    strHours = "0" + i;
                }
                hours.addItem(strHours);
            }
            minutes.addItem("00");
            minutes.addItem("15");
            minutes.addItem("30");
            minutes.setImmediate(true);
            minutes.addItem("45");
            hours.setValue("00");
            minutes.setValue("00");
            hours.setNullSelectionAllowed(false);
            hours.setImmediate(true);
            minutes.setNullSelectionAllowed(false);
            addComponent(hours,0,1);
            addComponent(new Label(": "),1,1);
            addComponent(minutes,2,1);
        }

        public Date getDate(Date day) throws ParseException {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy ");
            String strDate = formatter.format(day) + hours.getValue() + minutes.getValue();
            formatter = new SimpleDateFormat("dd/MM/yyyy HHmm");
            Date date = formatter.parse(strDate);
            return date;
        }
        
        public void setTime(String strTime) {
            int index = strTime.indexOf(":");
            hours.setValue(strTime.substring(0,index));
            minutes.setValue(strTime.substring(index+1));
        }

    }
    
    private class BottomLayout extends GridLayout{
        private TextField positionNum;
        private TextField intervNum;
        private TextField duration;
        private TextField reserve;
        private TimeSelection startTime;
        private TimeSelection endTime;
        private PopupDateField date;
        private Button saveEdit;

        public BottomLayout() {
            setWidth("100%");
            setSpacing(true);
            setMargin(true);
            setColumns(3);
            setImmediate(true);
            setRows(4);
            fillBottomLayout();
        }
        
        private void fillBottomLayout() {
            date = new PopupDateField("Дата собеседования");
            date.setResolution(PopupDateField.RESOLUTION_DAY);
            date.setImmediate(true);
            date.setWidth("150");
            date.setValue(new Date());
            addComponent(date,0,0);
            setComponentAlignment(date, Alignment.BOTTOM_LEFT);

            startTime = new TimeSelection("Начало собеседования: ");
            addComponent(startTime,1,0);

            endTime = new TimeSelection("Конец собеседования: ");
            addComponent(endTime,2,0);

            intervNum = new TextField("Количество интервьюеров");
            intervNum.setWidth("150");
            intervNum.setRequired(true);
            intervNum.addValidator(new IntegerValidator("Ошибка! Введите число"));
            addComponent(intervNum,0,1);

            duration = new TextField("Длительность 1го (мин)");
            duration.setWidth("150");
            duration.setRequired(true);
            duration.addValidator(new IntegerValidator("Ошибка! Введите число"));
            addComponent(duration,1,1);

            positionNum = new TextField("Количество студентов");
            positionNum.setWidth("150");
            positionNum.setRequired(true);
            positionNum.addValidator(new IntegerValidator("Ошибка! Введите число"));
            addComponent(positionNum,2,1);
            
            saveEdit = new Button("Сохранить");
            saveEdit.setWidth("150");
            saveEdit.addListener(new Button.ClickListener() {

                @Override
                public void buttonClick(ClickEvent event) {
                    saveInterview();
                }
            });
            addComponent(saveEdit,1,3);
        }

        public boolean isValidForSave() {
            try {
                if(intervNum.isValid() && duration.isValid() && positionNum.isValid() ) {
                    Date start = startTime.getDate((Date) date.getValue());
                    Date end = endTime.getDate((Date) date.getValue());
                    Calendar calStart = Calendar.getInstance();
                    calStart.setTime(start);
                    Calendar calEnd = Calendar.getInstance();
                    calEnd.setTime(end);
                    long diff = calEnd.getTimeInMillis() - calStart.getTimeInMillis();
                    if(diff<0) {
                        getWindow().showNotification("Время начала не может быть меньше времени окончания!",
                                Window.Notification.TYPE_TRAY_NOTIFICATION);
                    } else {
                        return true; 
                    }
                }
             } catch (ParseException ex) {
                    Logger.getLogger(HRInterviewsLayout.class.getName()).log(Level.SEVERE, null, ex);
             }
            return false;
            
        }
        
        private void saveInterview() {
            if(isValidForSave()) {
                try {
                    Date start = startTime.getDate((Date) date.getValue());
                    Date end = endTime.getDate((Date) date.getValue());
                    int intNum = Integer.parseInt(intervNum.getValue().toString());
                    int posNum = Integer.parseInt(positionNum.getValue().toString());
                    if(!editable) {
                        int oneDuration = Integer.parseInt(duration.getValue().toString());
                        controller.saveNewInterviews(start, end, intNum, posNum, oneDuration);
                        getWindow().showNotification("Собеседование успешно добавлено!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        InterviewerPage.sendLetterToStudentWithFormToReservInterview();
                    } else {
                        Set<HRInterview> interviews = (Set<HRInterview>) table.getValue();
                        controller.editInterview(interviews.iterator().next().getId(), start, end, intNum, posNum);
                        getWindow().showNotification("Собеседование успешно изменено!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                    }
                    setVisible(false);
                    refreshTable();
                    refreshPositionsInfoLayout();
                } catch (ParseException ex) {
                    Logger.getLogger(HRInterviewsLayout.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

		private void setDate(String newStrDate) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date newDate = new Date();
            try {
                newDate = formatter.parse(newStrDate);
            } catch (ParseException ex) {
                Logger.getLogger(HRInterviewsLayout.class.getName()).log(Level.SEVERE, null, ex);
            }
            date.setValue(newDate);
        }

        private void setStartTime(String newStartTime) {
            startTime.setTime(newStartTime);
        }

        private void setEndTime(String newEndTime) {
            endTime.setTime(newEndTime);
        }

        private void setIntervCount(int newInterviewersNum) {
            intervNum.setValue(newInterviewersNum);
        }

        private void setPositionsCount(int newPositionNum) {
            positionNum.setValue(newPositionNum);
        }

        private void setReserve(int newReserve) {
            reserve.setValue(newReserve);
        }
        
    }
   
}
