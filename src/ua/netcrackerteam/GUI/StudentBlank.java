/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.netcrackerteam.GUI;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.terminal.StreamResource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Reindeer;
import ua.netcrackerteam.DAO.Entities.Cathedra;
import ua.netcrackerteam.DAO.Entities.Faculty;
import ua.netcrackerteam.DAO.Entities.Institute;
import ua.netcrackerteam.controller.StudentPage;
import ua.netcrackerteam.controller.bean.StudentData;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 *
 * @author akush_000
 */
@SuppressWarnings("serial")
public class StudentBlank extends VerticalLayout implements FieldEvents.BlurListener, Upload.SucceededListener,
                                   Upload.Receiver, Upload.StartedListener {
    private String username;
    private int status = 1;
    private Button saveEdit;
    private Panel contacts;
    private Button addAnotherContactsBut;
    private Window addContact;
    private Panel interests;
    private Panel accomplishments;
    private Panel persInfo;
    private Button addPrLangBut;
    private Window addPrLangWindow;
    private TextField prLangName;
    private GridLayout glayoutPrLang;
    private GridLayout glayoutKnow;
    private Button addKnowlegeBut;
    private Window addKnowlegeWindow;
    private TextField knowlName;
    private ButtonsListener buttonsListener = new ButtonsListener();
    private Upload photoUpload;
    private GridLayout glayoutEng;
    private GridLayout glayoutWorkType;
    private GridLayout glayoutWorkSphere;
    private GridLayout glayoutWhatInterest;
    private final OptionGroup agreement;
    private final Label agreementText;
    private long maxSize = 300000; //300Kb
    private StudentData stData;
    private final BeanItem<StudentData> bean;
    private static final List<String> workTypes = Arrays.asList(new String[] {"Реклама в ВУЗе", "Интернет","От знакомых","Реклама (СМИ)","Другое (уточните)"});
    
    private boolean isInEditMode = false;
    
    private ComboBox universities;
    private ComboBox faculties;
    private ComboBox cathedras;
    private TextField firstName;
    private TextField middleName;
    private TextField lastName;
    private TextField universityYear;
    private TextField universityGradYear;
    private TextField email1;
    private TextField email2;
    private TextField telephone;
    private TextField newInstitute;
    private TextField newFaculty;
    private TextField newCathedra;
    private Slider sliderC;
    private Slider sliderJava;
    private Slider sliderNT;
    private Slider sliderEA;
    private Slider sliderOOP;
    private Slider sliderDB;
    private Slider sliderWeb;
    private Slider sliderGUI;
    private Slider sliderWP;
    private Slider sliderPP;
    private TextArea expirience;
    private Slider reading;
    private Slider writing;
    private Slider speaking;
    private OptionGroup advert;
    private TextField anotherAdvert;
    private TextArea whyYou;
    private TextArea moreInfo;
    private TextField anotherContact;
    private ArrayList<Slider> programLangList = new ArrayList<Slider>();
    private ArrayList<Slider> knowlegesList = new ArrayList<Slider>();
    private InterestSelection eduCenter;
    private InterestSelection workNC;
    private InterestSelection mrSpec;
    private InterestSelection sale;
    private InterestSelection development;
    private TextField anotherWorkSphere;
    private InterestSelection deepSpec;
    private InterestSelection variousWork;
    private TextField anotherWorkType;
    private Embedded photo;
    private ValueChangeListener facultListener;
    private ByteArrayOutputStream baos;
    private byte[] photoArray;
    private final MainPage mainPage;
    private GridLayout glayout1 = new GridLayout(3,6);
    private String editorName;
    private Button navigateToInterview = new Button("Запись на собеседование");
    private ConfirmationToInterviewTime confirmationToInterviewTime;
    private final HorizontalLayout hlayout = new HorizontalLayout();
    private TabSheet pageTabs;

    public StudentBlank(String username, MainPage mainPage, String editorName) {
        this.username = username;
        this.mainPage = mainPage;
        this.editorName = editorName;
        stData = StudentPage.getStudentDataByUserName(username);
        bean = new BeanItem<StudentData>(stData);
        setMargin(true);
        setSpacing(true);
        setWidth("100%");
        persInfo = new Panel("Персональная информация");
        persInfoPanelFill();
        Embedded img = new Embedded(null, new ThemeResource("images/form-logo.png"));
        String s = null;
        try {
            WebApplicationContext context = (WebApplicationContext) mainPage.getContext();
            File file = new File (context.getHttpSession().getServletContext().getRealPath("/WEB-INF/resources/form-text.txt") );
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            byte[] array = new byte[in.available()];
            in.read(array);
            s = new String(array);
            in.close();
        } catch (IOException ex) {
            System.out.println("File form-text.txt is not found");
        }
        Label label = new Label(s);
        label.setContentMode(Label.CONTENT_XHTML);
        label.setStyleName("form-info");
        addComponent(img);
        addComponent(label);
        addComponent(persInfo);
        contacts = new Panel("Контакты");
        addComponent(contacts);
        contactsPanelFill();
        interests = new Panel("Интересы");
        addComponent(interests);
        interestsPanelFill();
        accomplishments = new Panel("Достоинства");
        addComponent(accomplishments);
        accomplishmentsPanelFill();
        agreement = new OptionGroup();
        agreement.addItem("Согласен с условиями соглашения: ");
        agreement.setMultiSelect(true);
        agreement.setRequired(true);
        addComponent(agreement);
        agreementText = new Label("Я даю согласие на хранение, обработку и использование моих персональных данных с целью возможного обучения и трудоустройства в компании НЕТКРЕКЕР на данный момент и в будущем.");
        agreementText.setWidth("750");
        addComponent(agreementText);
        hlayout.setWidth("100%");
        hlayout.setSpacing(true);
        addComponent(hlayout);
        saveEdit = new Button("Сохранить");
        saveEdit.setWidth("200");
        saveEdit.addListener(buttonsListener);


        navigateToInterview.setWidth(250);
        navigateToInterview.addListener(buttonsListener);

        hlayout.addComponent(saveEdit);
        hlayout.setComponentAlignment(saveEdit, Alignment.MIDDLE_CENTER);
        if(!stData.getStudentFirstName().equals("")) {
            //getSavedData();
            setEditable(false);
            status = 2;
        }
    }

  
    private void persInfoPanelFill() {
        
        persInfo.setWidth("100%");

        glayout1.setWidth("100%");
        glayout1.setSpacing(true);
        glayout1.setMargin(true);
        persInfo.setContent(glayout1);
        firstName = new TextField("Имя", (Property) bean.getItemProperty("studentFirstName"));
        firstName.addValidator(new RegexpValidator("[а-яА-ЯіІёЇїЁa-zA-Z0-9]{3,50}", "Поле должно содержать хотя бы 3 символа."));
        middleName = new TextField("Отчество", (Property) bean.getItemProperty("studentMiddleName"));
        middleName.addValidator(new RegexpValidator("[а-яА-ЯіІёЇїЁa-zA-Z0-9]{3,50}", "Поле должно содержать хотя бы 3 символа."));
        lastName = new TextField("Фамилия", (Property) bean.getItemProperty("studentLastName"));
        lastName.addValidator(new RegexpValidator("[а-яА-ЯіІёЇїЁa-zA-Z0-9-]{3,50}", "Поле должно содержать хотя бы 3 символа."));
        
        newInstitute = new TextField("Ваш ВУЗ",(Property) bean.getItemProperty("studentOtherInstitute"));
        newCathedra = new TextField("Ваша кафедра",(Property) bean.getItemProperty("studentOtherCathedra"));
        newFaculty = new TextField("Ваш факультет",(Property) bean.getItemProperty("studentOtherFaculty"));

        //newFaculty.addValidator(new RegexpValidator("[а-яА-ЯЇїёЁa-zA-Z0-9_. -]{2,200}", "Поле должно содержать хотя бы 2 и не более 200 символов."));
        //newInstitute.addValidator(new RegexpValidator("[а-яА-ЯЇїёЁa-zA-Z0-9_. -]{2,200}", "Поле должно содержать хотя бы 2 и не более 200 символов."));
        //newCathedra.addValidator(new RegexpValidator("[а-яА-ЯЇїёЁa-zA-Z0-9_. -]{2,200}", "Поле должно содержать хотя бы 2 и не более 200 символов."));
        
        List<Institute>insts = StudentPage.getUniversityList();
        BeanItemContainer<Institute> objects = new BeanItemContainer<Institute>(Institute.class, insts);
        universities = new ComboBox("ВУЗ",objects);
        universities.setPropertyDataSource((Property) bean.getItemProperty("studentInstitute"));
        universities.setItemCaptionPropertyId("name");
        universities.setImmediate(true);
        faculties = new ComboBox("Факультет");
        faculties.setPropertyDataSource((Property) bean.getItemProperty("studentFaculty"));
        faculties.setItemCaptionPropertyId("name");
        faculties.setImmediate(true);
        cathedras = new ComboBox("Кафедра");
        cathedras.setImmediate(true);
        cathedras.setPropertyDataSource((Property) bean.getItemProperty("studentCathedra"));
        cathedras.setItemCaptionPropertyId("name");
        universities.addListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                try {
                    faculties.removeAllItems();
                    cathedras.removeAllItems();
                    Institute currUniver = (Institute) universities.getValue();
                    if (currUniver != null) {
                        List<Faculty> currentFaculties = StudentPage.getFacultyListByInstitute(currUniver);
                        BeanItemContainer<Faculty> objects = new BeanItemContainer<Faculty>(Faculty.class, currentFaculties);
                        faculties.setContainerDataSource(objects);
                        newInstitute.setValue("");
                        newFaculty.setValue("");
                        newCathedra.setValue("");
                    } 
                    if(currUniver != null && currUniver.getName().equals("Другое")) {
                        newInstitute.setVisible(true);
                    }
                    else {
                        newInstitute.setVisible(false);
                        newCathedra.setVisible(false);
                        newFaculty.setVisible(false);
                    }
                } catch (NullPointerException ex) {
                }
            }
        });
        facultListener = new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                if ( faculties.size() > 0 ) {
                    cathedras.removeAllItems();
                    Faculty currFaculty = (Faculty)faculties.getValue();
                    if(currFaculty != null) {
                        List <Cathedra> currentCathedras = StudentPage.getCathedraListByFaculty(currFaculty);
                        BeanItemContainer<Cathedra> objects = new BeanItemContainer<Cathedra>(Cathedra.class, currentCathedras);
                        cathedras.setContainerDataSource(objects);

                    }
                    if(currFaculty != null && currFaculty.getName().equals("Другое")) {
                        newFaculty.setVisible(true);
                    } else {
                        newFaculty.setVisible(false);
                        newCathedra.setVisible(false);
                        newInstitute.setVisible(false);
                    }
                }
            }
        };
        cathedras.addListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if ( cathedras.size() > 0 ) {
                    Cathedra currCathedra = (Cathedra) cathedras.getValue();
                    if(currCathedra!= null && currCathedra.getName().equals("Другое")) {
                        newCathedra.setVisible(true);
                    } else {

                        newFaculty.setVisible(false);
                        newCathedra.setVisible(false);
                        newInstitute.setVisible(false);
                    }
                }
            }
            
        });
        faculties.addListener(facultListener);
        universityYear = new TextField("Курс",(Property) bean.getItemProperty("studentInstituteCourse"));
        universityGradYear = new TextField("Год окончания",(Property) bean.getItemProperty("studentInstituteGradYear"));
        universityYear.addValidator(new RegexpValidator("\\d{1,1}", "Номер курса должен быть длиной не более 1 символа"));
        universityGradYear.addValidator(new RegexpValidator("\\d{1,4}", "Год окончания не должен быть длиной не более 4 символов"));
        glayout1.addComponent(lastName, 0, 0);
        glayout1.addComponent(firstName,1,0);
        glayout1.addComponent(middleName,0,1);
        glayout1.addComponent(universities,1,1);
        glayout1.addComponent(universityYear,0,2);
        glayout1.addComponent(faculties,1,2);
        glayout1.addComponent(cathedras,0,3);
        glayout1.addComponent(universityGradYear,1,3);

        Iterator<Component> i = glayout1.getComponentIterator();
        while (i.hasNext()) {
            Component c = (Component) i.next();
            c.setWidth("220");
            if(c instanceof TextField) {
                textFieldConfig((TextField)c);
            } else if(c instanceof ComboBox) {
                ComboBoxConfig((ComboBox)c);
            }
        }
        
        HorizontalLayout anotherLayout = new HorizontalLayout();
        anotherLayout.setSpacing(true);
        anotherLayout.addComponent(newCathedra);
        anotherLayout.addComponent(newFaculty);
        anotherLayout.addComponent(newInstitute);
        i = anotherLayout.getComponentIterator();
        while (i.hasNext()) {
            TextField c = (TextField) i.next();
            c.setWidth("220");
            c.setVisible(false);
            anotherLayout.setComponentAlignment(c, Alignment.TOP_LEFT);
        }
        glayout1.addComponent(anotherLayout,0,5,2,5);

        photoUpload = new Upload("Фото* (выберите файл и нажмите кнопку \"Загрузка\")",this);
        photoUpload.setButtonCaption("Загрузка");
        photoUpload.addListener((Upload.SucceededListener) this);
        photoUpload.addListener((Upload.StartedListener) this);
        glayout1.addComponent(photoUpload,0,4,2,4);
        photo = new Embedded("");
        photo.setWidth("200");
        glayout1.addComponent(photo,2,0,2,3);
        glayout1.setComponentAlignment(photo, Alignment.TOP_CENTER);
        photoArray = stData.getPhoto();
        if(photoArray != null) {
            showPhoto();
        }
       
    }

    private void contactsPanelFill() {
        GridLayout glayout2 = new GridLayout(3,2);
        glayout2.setMargin(true);
        glayout2.setSpacing(true);
        contacts.setContent(glayout2);
        email1 = new TextField("Email 1",(Property) bean.getItemProperty("studentEmailFirst"));
        email1.addValidator(new EmailValidator("Email должен содержать знак '@' и полный домен."));
        email2 = new TextField("Email 2",(Property) bean.getItemProperty("studentEmailSecond"));
        email2.addValidator(new EmailValidator("Email должен содержать знак '@' и полный домен."));
        telephone = new TextField("Телефон",(Property) bean.getItemProperty("studentTelephone"));
        addAnotherContactsBut = new Button("Добавить контакт");
        addAnotherContactsBut.setDescription("Максимум 1 дополнительный контакт");
        addAnotherContactsBut.addListener(buttonsListener);
        glayout2.addComponent(email1);
        glayout2.addComponent(email2);
        glayout2.addComponent(telephone);
        contacts.addComponent(addAnotherContactsBut);
        Iterator<Component> i = contacts.getComponentIterator();
        while (i.hasNext()) {
            Component c = (Component) i.next();
            c.setWidth("220");
            if(c instanceof TextField) {
                textFieldConfig((TextField)c);
            }
        }
        email2.setRequired(false);
        addAnotherContactsBut.setWidth("200");
    }

    private void interestsPanelFill() {
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setWidth("100%");
        vlayout.setMargin(true);
        glayoutWhatInterest = new GridLayout(3,1);
        glayoutWhatInterest.setSpacing(true);
        interests.setContent(vlayout);
        Label whatInterest = new Label("<h3>Что заинтересовало:</h3>",Label.CONTENT_XHTML);
        vlayout.addComponent(whatInterest);
        vlayout.addComponent(glayoutWhatInterest);
        eduCenter = new InterestSelection("Учебный центр/стажировка:", bean.getItemProperty("studentInterestStudy"));
        glayoutWhatInterest.addComponent(eduCenter);  
        workNC = new InterestSelection("Работа в компании NetCracker:", bean.getItemProperty("studentInterestWork"));
        glayoutWhatInterest.addComponent(workNC);
        Label workSphere = new Label("<h3>Интересующая область деятельности:</h3>",Label.CONTENT_XHTML);
        vlayout.addComponent(workSphere);
        glayoutWorkSphere = new GridLayout(3,1);
        glayoutWorkSphere.setSpacing(true);
        vlayout.addComponent(glayoutWorkSphere);
        development = new InterestSelection("Разработка ПО:", bean.getItemProperty("studentInterestDevelopment"));
        //anotherWorkSphere = new TextField("Другие: ", bean.getItemProperty("studentInterestOther")==null ? String.valueOf(bean.getItemProperty("studentInterestOther")):"");
        anotherWorkSphere = new TextField("Другие: ", bean.getItemProperty("studentInterestOther"));
        anotherWorkSphere.setWidth("250");
        glayoutWorkSphere.addComponent(development);
        glayoutWorkSphere.addComponent(anotherWorkSphere);
        glayoutWorkSphere.setComponentAlignment(development, Alignment.BOTTOM_LEFT);
        Label whatWorkType = new Label("<h3>Тип работы:</h3>",Label.CONTENT_XHTML);
        vlayout.addComponent(whatWorkType);
        glayoutWorkType = new GridLayout(2,3);
        glayoutWorkType.setSpacing(true);
        vlayout.addComponent(glayoutWorkType);
        deepSpec = new InterestSelection("Глубокая специализация: ", bean.getItemProperty("studentWorkTypeDeepSpec"));
        variousWork = new InterestSelection("Разнообразная работа: ", bean.getItemProperty("studentWorkTypeVarious"));
        mrSpec = new InterestSelection("Руководство специалистами: ", bean.getItemProperty("studentWorkTypeManagement"));
        sale = new InterestSelection("Продажи", bean.getItemProperty("studentWorkTypeSale"));
        glayoutWorkType.addComponent(deepSpec);
        glayoutWorkType.addComponent(variousWork);
        glayoutWorkType.addComponent(mrSpec);
        glayoutWorkType.addComponent(sale);
        //anotherWorkType = new TextField("Другие: ", bean.getItemProperty("studentWorkTypeOther") == null?String.valueOf(bean.getItemProperty("studentWorkTypeOther")):"");
        anotherWorkType = new TextField("Другие: ", bean.getItemProperty("studentWorkTypeOther"));
        anotherWorkType.setWidth("250");
        glayoutWorkType.addComponent(anotherWorkType);
    }

    private void accomplishmentsPanelFill() {
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setWidth("100%");
        vlayout.setMargin(true);
        glayoutPrLang = new GridLayout(3,3);
        glayoutPrLang.setSpacing(true);
        accomplishments.setContent(vlayout);
        Label progrLang = new Label("Владение языками программирования:"
                + " 1 – писал простые программы с книгой/справкой; 3 – хорошо помню синтаксис и некоторые "
                + "библиотеки; 5 – написал крупный проект");
        vlayout.addComponent(progrLang);
        vlayout.addComponent(glayoutPrLang);
        sliderC = new Slider("C++");
        sliderC.setPropertyDataSource((Property) bean.getItemProperty("studentCPlusPlusMark"));
        sliderConfig(sliderC,1);
        glayoutPrLang.addComponent(sliderC);
        sliderJava = new Slider("Java");
        sliderJava.setPropertyDataSource((Property) bean.getItemProperty("studentJavaMark"));
        sliderConfig(sliderJava,1);
        glayoutPrLang.addComponent(sliderJava);
        addPrLangBut = new Button("Добавить язык");
        addPrLangBut.setDescription("Максимум 3 дополнительных языка");
        addPrLangBut.addListener(buttonsListener);
        addPrLangBut.setWidth("200");
        vlayout.addComponent(addPrLangBut);
        Label knowledge = new Label("Как ты оцениваешь свои знания по разделам: ");
        vlayout.addComponent(knowledge);
        glayoutKnow = new GridLayout(3,2);
        glayoutKnow.setSpacing(true);
        vlayout.addComponent(glayoutKnow);
        sliderNT = new Slider("Сетевые технологии");
        sliderNT.setPropertyDataSource((Property) bean.getItemProperty("studentKnowledgeNetwork"));
        sliderEA = new Slider("Эффективные алгоритмы");
        sliderEA.setPropertyDataSource((Property) bean.getItemProperty("studentKnowledgeEfficientAlgorithms"));
        sliderOOP = new Slider("Объектно-ориент. программирование");
        sliderOOP.setPropertyDataSource((Property) bean.getItemProperty("studentKnowledgeOOP"));
        sliderDB = new Slider("Базы данных");
        sliderDB.setPropertyDataSource((Property) bean.getItemProperty("studentKnowledgeDB"));
        sliderWeb = new Slider("Web");
        sliderWeb.setPropertyDataSource((Property) bean.getItemProperty("studentKnowledgeWeb"));
        sliderGUI = new Slider("Графический интерфейс (не Web)");
        sliderGUI.setPropertyDataSource((Property) bean.getItemProperty("studentKnowledgeGUI"));
        sliderWP = new Slider("Сетевое программирование");
        sliderWP.setPropertyDataSource((Property) bean.getItemProperty("studentKnowledgeNetworkProgramming"));
        sliderPP = new Slider("Проектирование программ");
        sliderPP.setPropertyDataSource((Property) bean.getItemProperty("studentKnowledgeProgramDesign"));
        glayoutKnow.addComponent(sliderNT);
        glayoutKnow.addComponent(sliderEA);
        glayoutKnow.addComponent(sliderOOP);
        glayoutKnow.addComponent(sliderDB);
        glayoutKnow.addComponent(sliderWeb);
        glayoutKnow.addComponent(sliderGUI);
        glayoutKnow.addComponent(sliderWP);
        glayoutKnow.addComponent(sliderPP);        
        Iterator<Component> i = glayoutKnow.getComponentIterator();
        while (i.hasNext()) {
            Component c = (Component) i.next();
            sliderConfig(c,0);
        }
        addKnowlegeBut = new Button("Другие разделы");
        addKnowlegeBut.setDescription("Максимум 3 дополнительных раздела");
        addKnowlegeBut.addListener(buttonsListener);
        addKnowlegeBut.setWidth("200");
        vlayout.addComponent(addKnowlegeBut);
        expirience = new TextArea("Если у тебя уже есть опыт работы и/или выполненные учебные проекты, опиши их: ",
                (Property) bean.getItemProperty("studentExperienceProjects"));
        expirience.setWidth("700");
        expirience.setRows(4);
        expirience.setMaxLength(200);
        expirience.setRequired(true);
        expirience.setWordwrap(true);
        expirience.addListener(this);
        vlayout.addComponent(expirience);
        Label english = new Label("Уровень английского языка (от 1 = elementary до 5 = advanced): ");
        vlayout.addComponent(english);
        glayoutEng = new GridLayout(3,1);
        vlayout.addComponent(glayoutEng);
        glayoutEng.setSpacing(true);
        reading = new Slider("Чтение");
        reading.setPropertyDataSource((Property) bean.getItemProperty("studentEnglishReadMark"));
        writing = new Slider("Письмо");
        writing.setPropertyDataSource((Property) bean.getItemProperty("studentEnglishWriteMark"));
        speaking = new Slider("Устная речь");
        speaking.setPropertyDataSource((Property) bean.getItemProperty("studentEnglishSpeakMark"));
        glayoutEng.addComponent(reading);
        glayoutEng.addComponent(writing);
        glayoutEng.addComponent(speaking);
        i = glayoutEng.getComponentIterator();
        while (i.hasNext()) {
            Component c = (Component) i.next();
            sliderConfig(c,1);
        }
        advert = new OptionGroup("Откуда ты узнал о наборе в учебный центр?", workTypes);
        advert.setMultiSelect(true);
        advert.setValue(stData.getStudentHowHearAboutCentre());
        advert.setImmediate(true);
        advert.setWidth("220");
        advert.setRequired(true);
        vlayout.addComponent(advert);
        anotherAdvert = new TextField(bean.getItemProperty("studentHowHearAboutCentreOther"));
        anotherAdvert.setWidth("220");
        anotherAdvert.setMaxLength(30);
        vlayout.addComponent(anotherAdvert);
        whyYou = new TextArea("Почему тебя обязательно надо взять в NetCracker (важные достоинства; возможно, обещания :) )",
                (Property) bean.getItemProperty("studentReasonOffer"));
        whyYou.setWidth("700");
        whyYou.setRows(3);
        whyYou.setMaxLength(200);
        whyYou.setRequired(true);
        whyYou.addListener(this);
        vlayout.addComponent(whyYou);
        moreInfo = new TextArea("Дополнительные сведения о себе: олимпиады, поощрения, курсы, сертификаты, личные качества, др.",
                (Property) bean.getItemProperty("studentSelfAdditionalInformation"));
        moreInfo.setWidth("700");
        moreInfo.setRequired(true);
        moreInfo.setRows(3);
        moreInfo.setMaxLength(200);
        moreInfo.addListener(this);
        vlayout.addComponent(moreInfo);   
    }
    
    
    private void addNewContact() {
        addContact = new Window("Добавить контакт");
        addContact.setModal(true);
        addContact.setWidth("20%");
        addContact.setResizable(false);
        addContact.center();
        VerticalLayout layout = new VerticalLayout();
        addContact.setContent(layout);
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setWidth("100%");
        final TextField contactType = new TextField("Тип контакта",(Property) bean.getItemProperty("studentOtherContactType"));
        contactType.setRequired(true);
        Button okBut = new Button("Добавить");
        okBut.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                if(contactType.isValid()) {
                    addNewContactField((String)contactType.getValue(),(Property) bean.getItemProperty("studentOtherContact"));
                    getWindow().removeWindow(addContact);
                }
            }
        });
        layout.addComponent(contactType);
        layout.addComponent(okBut);
        layout.setComponentAlignment(contactType, Alignment.TOP_CENTER);
        layout.setComponentAlignment(okBut, Alignment.TOP_CENTER);
        getWindow().addWindow(addContact);
    }
    
    private void addNewContactField(String type, Property value) {
        GridLayout gl = (GridLayout) contacts.getContent();
        if(anotherContact != null) {
            gl.removeComponent(anotherContact);
        }
        anotherContact = new TextField(type,value);
        textFieldConfig(anotherContact);
        anotherContact.addValidator(new RegexpValidator("[а-яА-ЯёЇїЁa-zA-Z0-9@_. -]{3,100}", "Поле должно содержать хотя бы 3 символа."));
        gl.removeComponent(addAnotherContactsBut);
        gl.addComponent(anotherContact,0,1);
        anotherContact.setWidth("220");
        gl.addComponent(addAnotherContactsBut,1,1);
        gl.setComponentAlignment(addAnotherContactsBut, Alignment.BOTTOM_LEFT);
    }
    private void addProgrammingLanguage() {
        addPrLangWindow = new Window("Добавить язык");
        addPrLangWindow.setModal(true);
        addPrLangWindow.setWidth("20%");
        addPrLangWindow.setResizable(false);
        addPrLangWindow.center();
        VerticalLayout layout = new VerticalLayout();
        addPrLangWindow.setContent(layout);
        layout.setMargin(true);
        layout.setSpacing(true);
        final int currLang = programLangList.size()+1;
        prLangName = new TextField("Язык",(Property) bean.getItemProperty("studentLanguage" + currLang));
        prLangName.setRequired(true);
        Button okBut = new Button("Добавить");
        okBut.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                if(prLangName.isValid()) {
                    Slider newSlider = new Slider((String)prLangName.getValue());
                    newSlider.setPropertyDataSource((Property) bean.getItemProperty("studentLanguage"+ currLang+"Mark"));
                    sliderConfig(newSlider,1);
                    programLangList.add(newSlider);
                    glayoutPrLang.addComponent(programLangList.get(programLangList.size()-1));
                    getWindow().removeWindow(addPrLangWindow);
                }
            }
        });
        layout.addComponent(prLangName);
        layout.addComponent(okBut);
        layout.setComponentAlignment(prLangName, Alignment.TOP_CENTER);
        layout.setComponentAlignment(okBut, Alignment.TOP_CENTER);
        getWindow().addWindow(addPrLangWindow);
    }
    
    private void sliderConfig(Component slider, int min) {
        Slider sl = (Slider) slider;
        sl.setWidth("220");
        sl.setMin(min);
        sl.setMax(5);
    }

    private void addKnowlege() {
        addKnowlegeWindow = new Window("Добавить раздел");
        addKnowlegeWindow.setModal(true);
        addKnowlegeWindow.setWidth("20%");
        addKnowlegeWindow.setResizable(false);
        addKnowlegeWindow.center();
        VerticalLayout layout = new VerticalLayout();
        addKnowlegeWindow.setContent(layout);
        layout.setMargin(true);
        layout.setSpacing(true);
        final int currKnow = knowlegesList.size()+1;
        knowlName = new TextField("Раздел (в области IT или сетей)",(Property) bean.getItemProperty("studentKnowledgeOther" + currKnow));
        knowlName.setRequired(true);
        knowlName.addValidator(new RegexpValidator("[а-яА-ЯЇїёЁa-zA-Z0-9_. -]{3,100}", "Поле должно содержать хотя бы 3 символа."));
        Button okBut = new Button("Добавить");
        okBut.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                if(knowlName.isValid()) {
                    Slider newSlider = new Slider((String)knowlName.getValue());
                    newSlider.setPropertyDataSource((Property) bean.getItemProperty("studentKnowledgeOther"+ currKnow+"Mark"));
                    sliderConfig(newSlider,0);
                    knowlegesList.add(newSlider);
                    glayoutKnow.addComponent(knowlegesList.get(knowlegesList.size()-1));
                    getWindow().removeWindow(addKnowlegeWindow);
                }
            }
        });
        layout.addComponent(knowlName);
        layout.addComponent(okBut);
        layout.setComponentAlignment(knowlName, Alignment.TOP_CENTER);
        layout.setComponentAlignment(okBut, Alignment.TOP_CENTER);
        getWindow().addWindow(addKnowlegeWindow);
    }
    
    private void textFieldConfig(TextField tf) {
        tf.addListener(this);
        tf.setRequired(true);
    }
    
    private void ComboBoxConfig(ComboBox cb) {
        cb.setRequired(true);
        cb.setInputPrompt("Выберите из списка");
        cb.addListener(this);
        cb.setNewItemsAllowed(false);
        cb.setImmediate(true);
        cb.setNullSelectionAllowed(false);
    }

    public void blur(BlurEvent event) {
        Object source = event.getComponent();
        if(source instanceof TextField) {
            TextField tf = (TextField) source;
            tf.isValid();
        } else if(source instanceof ComboBox){
            ComboBox cb = (ComboBox) source;
            cb.isValid();
        } else if(source instanceof TextArea){
            TextArea ta = (TextArea) source;
            ta.isValid();
        }
    }
    
    /**
     * Switch editable mode for blank
     * @param editable false - all components read only
     * @return true if all components valid
     */
    private void setEditable(boolean editable) {
        GridLayout l = (GridLayout) persInfo.getContent();
        for (int i= 0; i<l.getColumns(); i++) {
            for (int j = 0; j < l.getRows(); j++) {
                Component c = l.getComponent(i,j);
                if(c != null) {
                    if (c instanceof Upload) {
                    c.setVisible(editable);
                    } else if (c instanceof ComboBox) {
                        ComboBox c1 = (ComboBox) c;
                        Label lab = new Label(c1.getCaption()+"<br>"+c1.getValue().toString());
                        lab.setContentMode(Label.CONTENT_XHTML);
                        l.replaceComponent(c, lab);
                    } else if (c instanceof Label) {
                        if (i == 1 && j == 1) {
                            l.replaceComponent(c, universities);
                            faculties.removeListener(facultListener);
                        } else if (i == 1 && j == 2) {
                            l.replaceComponent(c, faculties);
                            Institute currUniver = (Institute) universities.getValue();
                            List<Faculty> currentFaculties = StudentPage.getFacultyListByInstitute(currUniver);
                            BeanItemContainer<Faculty> objects = new BeanItemContainer<Faculty>(Faculty.class, currentFaculties);
                            faculties.setContainerDataSource(objects);
                            faculties.setPropertyDataSource((Property) bean.getItemProperty("studentFaculty"));
                            faculties.addListener(facultListener);
                        } else if (i == 0 && j == 3) {
                            l.replaceComponent(c, cathedras);
                            List <Cathedra> currentCathedras = StudentPage.getCathedraListByFaculty((Faculty)faculties.getValue());
                            BeanItemContainer<Cathedra> objects = new BeanItemContainer<Cathedra>(Cathedra.class, currentCathedras);
                            cathedras.setContainerDataSource(objects);
                            cathedras.setPropertyDataSource((Property) bean.getItemProperty("studentCathedra"));
                        } 
                    } else {
                        c.setReadOnly(!editable);
                    }
                }
            }
        }
        newFaculty.setReadOnly(!editable);
        newCathedra.setReadOnly(!editable);
        newInstitute.setReadOnly(!editable);
        getSavedData();
        Iterator<Component> i = contacts.getComponentIterator();
        while (i.hasNext()) {
            Component c = (Component) i.next();
            if (c instanceof Button) {
                c.setVisible(editable);
            }
            else {
                c.setReadOnly(!editable);
            }
        }
        i = glayoutWhatInterest.getComponentIterator();
        while (i.hasNext()) {
            Component c = (Component) i.next();
            c.setReadOnly(!editable);
        }
        i = glayoutWorkSphere.getComponentIterator();
        while (i.hasNext()) {
            Component c = (Component) i.next();
            c.setReadOnly(!editable);
        }
        i = glayoutWorkType.getComponentIterator();
        while (i.hasNext()) {
            Component c = (Component) i.next();
            c.setReadOnly(!editable);
        }
        i = accomplishments.getComponentIterator();
        while (i.hasNext()) {
            Component c = (Component) i.next();
            if (c instanceof Button) {
                c.setVisible(editable);
            } else {
                c.setReadOnly(!editable);
            }
        }
        i = glayoutPrLang.getComponentIterator();
        while (i.hasNext()) {
            Component c = (Component) i.next();
            c.setReadOnly(!editable);
        }  
        i = glayoutKnow.getComponentIterator();
        while (i.hasNext()) {
            Component c = (Component) i.next();
            c.setReadOnly(!editable);
        } 
        i = glayoutEng.getComponentIterator();
        while (i.hasNext()) {
            Component c = (Component) i.next();
            c.setReadOnly(!editable);
        }
        agreement.setVisible(editable);
        agreementText.setVisible(editable);
        if(editable) {
            saveEdit.setCaption("Сохранить");
        } else {
            saveEdit.setCaption("Редактировать");
            isInEditMode = true;
            hlayout.addComponent(navigateToInterview);
        }
    }

    private boolean checkAllValid() {
        GridLayout l = (GridLayout) persInfo.getContent();
        Iterator<Component> i = l.getComponentIterator();
        while (i.hasNext()) {
            Component c = (Component) i.next();
            if ((c instanceof TextField) && !(c.getCaption().equals("Ваша кафедра"))&& !(c.getCaption().equals("Ваш факультет"))&& !(c.getCaption().equals("Ваш ВУЗ"))) {
                TextField c1 = (TextField) c;
                if (!c1.isValid()) {
                    return false;
                }
            } else if (c instanceof ComboBox) {
                ComboBox c1 = (ComboBox) c;
                if (!c1.isValid()) {
                    return false;
                }
            } 
        }
        i = contacts.getComponentIterator();
        while (i.hasNext()) {
            Component c = (Component) i.next();
            if (c instanceof TextField) {
                TextField c1 = (TextField) c;
                if (!c1.isValid()) {
                    return false;
                }
            }
        }
        i = accomplishments.getComponentIterator();
        while (i.hasNext()) {
            Component c = (Component) i.next();
            if (c instanceof OptionGroup) {
                OptionGroup c1 = (OptionGroup) c;
                if (!c1.isValid()) {
                    return false;
                }
            } else if (c instanceof TextArea) {
                TextArea c1 = (TextArea) c;
                if (!c1.isValid()) {
                    return false;
                }
            } 
        }
        if(!agreement.isValid()) {
            return false;
        }
        if(photoArray == null) {
            return false;
        }
        return true;            
    }

    @Override
    public void uploadSucceeded(SucceededEvent event) {
        photoArray = baos.toByteArray();
        stData.setPhoto(photoArray);
        showPhoto();
    }

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        baos = new ByteArrayOutputStream(); 
        return baos;
    }

    @Override
    public void uploadStarted(StartedEvent event) {
        if (maxSize < event.getContentLength()) {
            photoUpload.interruptUpload();
            getWindow().showNotification("Недопустимый размер! Максимум 300Kb",Notification.TYPE_TRAY_NOTIFICATION);
            return;
        }
        String filename = event.getFilename();
        int i = filename.lastIndexOf('.');
        String ext = null;
        if (i > 0 &&  i < filename.length() - 1) {
            ext = filename.substring(i+1).toLowerCase();
        }
        if (ext != null && (ext.equals("jpeg") || ext.equals("jpg"))) {
        }
        else {
            photoUpload.interruptUpload();
            getWindow().showNotification("Файл не является изображением! Допустимые форматы: JPEG",Notification.TYPE_TRAY_NOTIFICATION);
        }
    }
   
    /**
     * Prepare saved form to view 
     */
    private void getSavedData() {
        if(!stData.getStudentOtherContact().equals("")) {
            addNewContactField(stData.getStudentOtherContactType(), bean.getItemProperty("studentOtherContact"));
        }
        if(!stData.getStudentLanguage1().equals("")) {
            addNewSavedSlider(stData.getStudentLanguage1(),bean.getItemProperty("studentLanguage1Mark"),glayoutPrLang, true);
        }
        if(!stData.getStudentLanguage2().equals("")) {
            addNewSavedSlider(stData.getStudentLanguage2(),bean.getItemProperty("studentLanguage2Mark"),glayoutPrLang, true);
        }
        if(!stData.getStudentLanguage3().equals("")) {
            addNewSavedSlider(stData.getStudentLanguage3(),bean.getItemProperty("studentLanguage3Mark"),glayoutPrLang, true);
        }
        if(!stData.getStudentKnowledgeOther1().equals("")) {
            addNewSavedSlider(stData.getStudentKnowledgeOther1(), bean.getItemProperty("studentKnowledgeOther1Mark"), glayoutKnow, false);
        }
        if(!stData.getStudentKnowledgeOther2().equals("")) {
            addNewSavedSlider(stData.getStudentKnowledgeOther2(),bean.getItemProperty("studentKnowledgeOther2Mark"),glayoutKnow, false);
        }
        if(!stData.getStudentKnowledgeOther3().equals("")) {
            addNewSavedSlider(stData.getStudentKnowledgeOther3(),bean.getItemProperty("studentKnowledgeOther3Mark"),glayoutKnow, false);
        }
        if(!stData.getStudentOtherInstitute().equals("")) {
            newInstitute.setVisible(true);
        }
        if(!stData.getStudentOtherCathedra().equals("")) {
            newCathedra.setVisible(true);
        }
        if(!stData.getStudentOtherFaculty().equals("")) {
            newFaculty.setVisible(true);
        }
    }

    private void addNewSavedSlider(String name, Property value, Layout lo, boolean  isProgramLang) {
        Iterator<Component> i;
        if (isProgramLang) {
            i = glayoutPrLang.getComponentIterator();
        }else {
            i = glayoutKnow.getComponentIterator();
        }
        Slider currSlider = null;
        while (i.hasNext()) {
            Component c = (Component) i.next();
            if (c instanceof Slider) {
                currSlider = (Slider) c;
                if (currSlider.getCaption().trim().equals(name.trim())) {
                    break;
                }
            }
            currSlider = null;
        }
        if (currSlider == null) {
            currSlider = new Slider(name);
            currSlider.setPropertyDataSource(value);
            sliderConfig(currSlider,1);
            programLangList.add(currSlider);
            lo.addComponent(programLangList.get(programLangList.size()-1));
        }else {
            currSlider.setPropertyDataSource(value);
            sliderConfig(currSlider,1);
        };
    }

    private void showPhoto() {
        StreamResource.StreamSource imagesource = new StreamResource.StreamSource() {
            @Override
            public InputStream getStream() {
                    photoArray = ua.netcrackerteam.controller.StudentPage.scalePhoto(photoArray,200,300);
                    return new ByteArrayInputStream(photoArray);
            }
        };
        StreamResource imageresource = new StreamResource(imagesource, "photo.jpg", mainPage);
        imageresource.setCacheTime(0);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String filename = "photo-" + df.format(new Date()) + ".jpg";
        imageresource.setFilename(filename);
        photo.setType(Embedded.TYPE_IMAGE);
        photo.setMimeType("image/jpeg");
        photo.setSource(imageresource);
        photo.requestRepaint();
    }
 
    private class ButtonsListener implements Button.ClickListener {

        @Override
        public void buttonClick(ClickEvent event) {
            Button source = event.getButton();
                if (source == addAnotherContactsBut) { 
                    if(anotherContact == null) {
                        addNewContact();
                    }
                    else {
                        getWindow().showNotification(new Notification("Вы добавили максимальное количество контактов.",Notification.TYPE_TRAY_NOTIFICATION));
                    }
                } else if(source == addPrLangBut) {
                    if(programLangList == null || programLangList.size()<3) {
                        addProgrammingLanguage();
                    }
                    else {
                        getWindow().showNotification(new Notification("Вы добавили максимальное количество языков.",Notification.TYPE_TRAY_NOTIFICATION));
                    }                    
                } else if (source == addKnowlegeBut) {
                    if(knowlegesList == null || knowlegesList.size()<3) {
                        addKnowlege();
                    }
                    else {
                        getWindow().showNotification(new Notification("Вы добавили максимальное количество разделов.",Notification.TYPE_TRAY_NOTIFICATION));
                    }
                } else if(source == navigateToInterview) {
                    pageTabs = mainPage.getPanel().getTabSheet();
                    pageTabs.setSelectedTab(2);
                } else if (source == saveEdit) {
                    if (saveEdit.getCaption().equals("Сохранить")) {
                        if (checkAllValid()) {
                            stData.setStudentHowHearAboutCentre((Collection) advert.getValue());
                            //stData.setStudentInterestOther((String)anotherWorkSphere.getValue());
                            //stData.setStudentWorkTypeOther((String)anotherWorkType.getValue());
                            setEditable(false);
                            StudentPage.addNewForm(stData, username, status, editorName);
                            if(status == 1) {
                                confirmationToInterviewTime = new ConfirmationToInterviewTime(mainPage, username);
                                getWindow().addWindow(confirmationToInterviewTime);
                            }

                            if (!isInEditMode) {
                            	hlayout.addComponent(navigateToInterview);
                            }
                            if (getWindow().getParent() != null) {
                            	Window currentWindow = getWindow();
                            	getWindow().getParent().removeWindow(currentWindow);
                            }
                        } else {
                            Notification n = new Notification("Проверьте правильность заполнения полей!", Notification.TYPE_TRAY_NOTIFICATION);
                            n.setDescription("Поля, отмеченные *, обязательны для заполнения. Убедитесь, что Вы загрузили фото.");
                            getWindow().showNotification(n);
                        }
                    } else {
                        setEditable(true);
                        status = 2;
                    }

                }
        }
    }
    
    public class InterestSelection extends HorizontalLayout implements Button.ClickListener{
        Label caption;
        ArrayList<Button> but = new ArrayList<Button>();
        String select = "-";
        private Property property;
        
        public InterestSelection(String caption, Property prop) {
            this.property = prop;
            setWidth("250");
            this.caption = new Label(caption);
            this.caption.setStyleName(Reindeer.LABEL_SMALL);
            this.caption.setWidth("100");
            addComponent(this.caption);
            but.add(new NativeButton("-"));
            but.add(new NativeButton("±"));
            but.add(new NativeButton("+"));
            but.add(new NativeButton("?"));
            for(Button b : but) {
                b.setWidth("30");
                b.setDisableOnClick(true);
                b.addListener(this);
                addComponent(b); 
                if(!property.getValue().equals("")) {
                    if(b.getCaption().equals(property.getValue().toString())) {
                        b.setEnabled(false);
                        select = (String) property.getValue();
                    }
                }
            }
            if(property.getValue().toString().equals("")) {
                property.setValue(select);
                but.get(0).setEnabled(false);
            } 
        }

        public void buttonClick(ClickEvent event) {
            Button clickedButton = event.getButton();
            for(Button b : but) {
                if(b!=clickedButton) {
                    b.setEnabled(true);
                }
                else {
                    select = b.getCaption();
                    property.setValue(select);
                }
            }
        }
        
        public String getValue() {
            return select;
        }
        
        @Override
        public void setReadOnly(boolean readOnly) {
            for(Button b : but) {
                if (b.getCaption().equals(select)) {
                    b.setEnabled(readOnly);
                    if(readOnly) {
                        b.removeListener(this);
                    } else {
                        b.addListener(this);
                    }
                    b.setDisableOnClick(!readOnly);
                } 
                else {
                    b.setEnabled(!readOnly);
                }
            }
        }
    }
    
}
