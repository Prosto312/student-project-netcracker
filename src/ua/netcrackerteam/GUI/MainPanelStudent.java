/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.netcrackerteam.GUI;

import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.VerticalLayout;



/**
 * Panel for Student view
 * @author Anna Kushnirenko
 */
public class MainPanelStudent extends MainPanel {
    private StudentBlank blankLayout;
    private InterviewLayout interviewLayout;
    private SettingsLayout settingsLayout;
    
    public MainPanelStudent(final HeaderLayout hlayout,final MainPage mainPage) {
        super(hlayout,mainPage);
        setContent(getUserLayout(hlayout));
        final Component c1 = new VerticalLayout();
        final Component c2 = new VerticalLayout();
        final Component c3 = new VerticalLayout();
        tabSheet.addTab(c1,"Анкета");
        tabSheet.addTab(c2,"Собеседование");
        tabSheet.addTab(c3,"Настройка");
        tabSheet.addListener(new TabSheet.SelectedTabChangeListener() {

            @Override
            public void selectedTabChange(SelectedTabChangeEvent event) {
                final TabSheet source = (TabSheet) event.getSource();
                if(source.getSelectedTab() == c1) {
                    blankLayout = new StudentBlank(hlayout.getUserName(),mainPage,hlayout.getUserName());
                    source.replaceComponent(c1, blankLayout);
                    blankLayout.addComponent(new Footer());
                } else if (source.getSelectedTab() == c2) {
                    interviewLayout = new InterviewLayout(hlayout.getUserName(), mainPage);
                    source.replaceComponent(c2, interviewLayout);
                    interviewLayout.addComponent(new Footer());
                } else if (source.getSelectedTab() == c3) {
                    settingsLayout = new SettingsLayout(hlayout.getUserName(), mainPage);
                    source.replaceComponent(c3, settingsLayout);
                    settingsLayout.addComponent(new Footer());
                }
            }
        });
    }
   
}


