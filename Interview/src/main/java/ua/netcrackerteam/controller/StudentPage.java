package ua.netcrackerteam.controller;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ua.netcrackerteam.DAO.*;
import ua.netcrackerteam.configuration.HibernateUtil;
import ua.netcrackerteam.configuration.ShowHibernateSQLInterceptor;

import javax.imageio.ImageIO;
import javax.interceptor.Interceptors;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Student controller class for using in View
 *
 * @author Filipenko, Zhokha Maksym
 */
public class StudentPage {

    @Interceptors(ShowHibernateSQLInterceptor.class)
    //public static List<String> getUniversityList() {
    public static List<Institute> getUniversityList() {

        Session session = null;
        org.hibernate.Query re = null;
        List instituteList = null;
        List<String> result = new ArrayList<String>();
        try {
            Locale.setDefault(Locale.ENGLISH);
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            re = session.createQuery("from Institute");
            instituteList = re.list();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

       /* for (Object currObj : instituteList) {
            Institute currInst = (Institute) currObj;
            result.add(currInst.getName());
        }*/
        return instituteList;
    }

    @Interceptors(ShowHibernateSQLInterceptor.class)
    public static List<Faculty> getFacultyListByInstitute(Institute currInstitute) {

        Session session = null;
        org.hibernate.Query re = null;
        List facultyList = null;
        List selectedFaculty = null;
        List selectedInstitute = null;
        List<String> result = new ArrayList<String>();
        try {
            Locale.setDefault(Locale.ENGLISH);
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            re = session.createQuery("from Institute where upper(name) ='" + currInstitute.getName().toUpperCase() + "'");
            selectedInstitute = re.list();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

        if ((!selectedInstitute.isEmpty()) && (selectedInstitute.size() == 1)) {
            Institute newInst = (Institute) selectedInstitute.get(0);
            try {
                Locale.setDefault(Locale.ENGLISH);
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                re = session.createQuery("from Faculty where institute ='" + newInst.getInstituteId() + "'");
                selectedFaculty = re.list();
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (session != null && session.isOpen()) {
                    session.close();
                }
            }

 /*           for (Object currObj : selectedFaculty) {
                Faculty currFaculty = (Faculty) currObj;
                result.add(currFaculty.getName());
            }
*/
        }
        return selectedFaculty;
    }

    @Interceptors(ShowHibernateSQLInterceptor.class)
    public static List<Cathedra> getCathedraListByFaculty(Faculty currFaculty, Institute currInstitute) {

        Session session = null;
        org.hibernate.Query re = null;
        List cathedraList = null;
        List selectedFaculty = null;
        List selectedCathedra = null;
        List<String> result = new ArrayList<String>();
        try {
            Locale.setDefault(Locale.ENGLISH);
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            re = session.createQuery("from Faculty as facul where upper(facul.name) = '" + currFaculty.getName().toUpperCase() + "'" + " and  upper(facul.institute.name) = '" + currInstitute.getName().toUpperCase() + "'" );
            selectedFaculty = re.list();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

        if ((!selectedFaculty.isEmpty()) && (selectedFaculty.size() == 1)) {
            Faculty newFaculty = (Faculty) selectedFaculty.get(0);
            try {
                Locale.setDefault(Locale.ENGLISH);
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                re = session.createQuery("from Cathedra where faculty ='" + newFaculty.getIdFaculty() + "'");
                selectedCathedra = re.list();
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (session != null && session.isOpen()) {
                    session.close();
                }
            }
/*
            for (Object currObj : selectedCathedra) {
                Cathedra currCathedra = (Cathedra) currObj;
                result.add(currCathedra.getName());
            }*/

        }
        return selectedCathedra;
    }

    @Interceptors(ShowHibernateSQLInterceptor.class)
    public static boolean verificationOfTheExistenceSomthing (String tableForVerification, String inWhichColumn, String someThing) {
        Session session = null;
        org.hibernate.Query re = null;
        List selectedBranch = null;

        try {
            Locale.setDefault(Locale.ENGLISH);
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            re = session.createQuery("from " + tableForVerification + " where upper(" + inWhichColumn + ") ='" + someThing.toUpperCase() + "'");
            selectedBranch = re.list();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

        if ((selectedBranch == null) || (selectedBranch.isEmpty())) {
            return true;
        }
        else {
            return false;
        }
    }

    @Interceptors(ShowHibernateSQLInterceptor.class)
    public static List<Object> searchSomething (String tableForSearch, String inWhichColumn, String someThing) {
        Session session = null;
        org.hibernate.Query re = null;
        List selectedSomething = null;

        try {
            Locale.setDefault(Locale.ENGLISH);
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            re = session.createQuery("from " + tableForSearch + " where upper(" + inWhichColumn + ") ='" + someThing.toUpperCase() + "'");
            selectedSomething = re.list();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return selectedSomething;
    }

    @Interceptors(ShowHibernateSQLInterceptor.class)
    public static void insertNewBranch(String branchName, BranchCategory currBranchCat) {

        Session session = null;
        org.hibernate.Query re = null;
        Transaction transaction = null;
        //for (String currBranch:BranchNames) {
            if (verificationOfTheExistenceSomthing("Branch", "name" ,branchName)) {
                try {
                    Locale.setDefault(Locale.ENGLISH);
                    session = HibernateUtil.getSessionFactory().getCurrentSession();
                    transaction = session.beginTransaction();
                    Branch newBranch = new Branch();
                    newBranch.setName(branchName);
                    newBranch.setDescription(branchName);
                    newBranch.setBranchCategory(currBranchCat);
                    session.save(newBranch);
                    transaction.commit();
                } catch (Exception e) {
                    System.out.println(e);
                } finally {
                    if (session != null && session.isOpen()) {
                        session.close();
                    }
                }
            }
        //}
    }

    @Interceptors(ShowHibernateSQLInterceptor.class)
    public static void insertNewContacts(String contactType) {

        Session session = null;
        org.hibernate.Query re = null;
        Transaction transaction = null;

        if (verificationOfTheExistenceSomthing("ContactCategory","category" ,contactType)) {
            try {
                Locale.setDefault(Locale.ENGLISH);
                session = HibernateUtil.getSessionFactory().getCurrentSession();
                transaction = session.beginTransaction();
                ContactCategory newContactType = new ContactCategory();
                newContactType.setCategory(contactType);
                session.save(newContactType);
                transaction.commit();
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (session != null && session.isOpen()) {
                    session.close();
                }
            }
        }
    }

    public static void main(String args[]) {

        //List<String> newList = getUniversityList();

        // List<String> newListFaculty = getFacultyListByInstitute("Одеський національний політехнічний університет");

        //List<String> newListCathedra = getCathedraListByFaculty("Інститут комп.ютерних систем", "Одеський національний політехнічний університет");

//        String[] listOfBranch = new String[3];
//        listOfBranch[0] = "1C";
//        listOfBranch[1] = "2C";
//        listOfBranch[2] = "3C";
//        insertNewBranch(listOfBranch);
//
//        String newContactType = "skype";
//        insertNewContacts(newContactType);
//
//        StudentData newStudentData = new StudentData();
//        newStudentData.setStudentFirstName("Василий");
//        newStudentData.setStudentLastName("Familiya");
//        newStudentData.setStudentMiddleName("MiddleName");
//        newStudentData.setStudentExperienceProjects("some projects");
//        newStudentData.setStudentReasonOffer("???????");
//        newStudentData.setStudentSelfAdditionalInformation("I'm a best of the best");


        StudentData std = StudentPage.getStudentDataByUserName("iviarkiz");
        System.out.println(std);

        /*StudentData newStudentData = new StudentData();
        newStudentData.setStudentFirstName("Алексей");
        newStudentData.setStudentLastName("Филипенко");
        newStudentData.setStudentMiddleName("Андреевич");
        newStudentData.setStudentInstitute(getUniversityList().get(0));
        newStudentData.setStudentFaculty(getFacultyListByInstitute(getUniversityList().get(0)).get(0));
        newStudentData.setStudentCathedra(getCathedraListByFaculty(newStudentData.getStudentFaculty(), newStudentData.getStudentInstitute()).get(0));
        newStudentData.setStudentInstituteCourse(6);
        newStudentData.setStudentInstituteGradYear(2012);
        newStudentData.setStudentInterestStudy("+");
        newStudentData.setStudentInterestWork("+");
        newStudentData.setStudentInterestDevelopment("+");
        newStudentData.setStudentInterestOther("+");
        newStudentData.setStudentExperienceProjects("many 1C projects");
        newStudentData.setStudentReasonOffer("Nya");
        newStudentData.setStudentSelfAdditionalInformation("kavai");
        //addNewForm(newStudentData, "briarey");

        newStudentData.setStudentEmailFirst("filipenko.aleksey@gmail.com");
        newStudentData.setStudentEmailSecond("goldlegat@mail.ru");
        newStudentData.setStudentEmailSecond("abrakadabra@mail.ru");
        newStudentData.setStudentOtherContactType("skype");
        newStudentData.setStudentOtherContact("forsakenstrelok");
        newStudentData.setStudentCPlusPlusMark(3.5);
        newStudentData.setStudentJavaMark(5);
        newStudentData.setStudentLanguage1("1C");
        newStudentData.setStudentLanguage1Mark(4);
        newStudentData.setStudentKnowledgeNetwork(2);
        newStudentData.setStudentKnowledgeEfficientAlgorithms(1);
        newStudentData.setStudentKnowledgeOOP(3);
        newStudentData.setStudentEnglishReadMark(2);
        newStudentData.setStudentEnglishSpeakMark(2);
        newStudentData.setStudentEnglishWriteMark(2);
        addNewForm(newStudentData, "briarey");*/



    }

    public static void addNewForm(StudentData newStudentData, String userName, int statusParam) {

        DAOStudentImpl currDAOStImpl = new DAOStudentImpl();
        DAOCommon currDAOComm = new DAOCommon();

        Form newForm = new Form();
        newForm.setFirstName        (newStudentData.getStudentFirstName());
        newForm.setMiddleName       (newStudentData.getStudentMiddleName());
        newForm.setLastName         (newStudentData.getStudentLastName());
        //newForm.setInstitute        (newStudentData.getStudentInstitute());
        newForm.setCathedra         (newStudentData.getStudentCathedra());
        //newForm.setFaculty          (newStudentData.getStudentFaculty());
        newForm.setInstituteYear    (Integer.parseInt(newStudentData.getStudentInstituteCourse()));
        newForm.setInstituteGradYear(Integer.parseInt(newStudentData.getStudentInstituteGradYear()));

        newForm.setInterestStudy    (newStudentData.getStudentInterestStudy());
        newForm.setInterestWork     (newStudentData.getStudentInterestWork());

        newForm.setInterestDeepSpec (newStudentData.getStudentWorkTypeDeepSpec());
        newForm.setInterestManagment(newStudentData.getStudentWorkTypeManagement());
        newForm.setInterestSale     (newStudentData.getStudentWorkTypeSale());
        newForm.setInterestVarious  (newStudentData.getStudentWorkTypeVarious());

        newForm.setExecProject      (newStudentData.getStudentExperienceProjects());
        newForm.setReason           (newStudentData.getStudentReasonOffer());
        newForm.setExtraInfo        (newStudentData.getStudentSelfAdditionalInformation());
        newForm.setInterestBranchOther(newStudentData.getStudentInterestOther());
        newForm.setInterestBranchSoft(newStudentData.getStudentInterestDevelopment());
        newForm.setInterestOther    (newStudentData.getStudentWorkTypeOther());
        try {
            newForm.setUser             ((UserList)currDAOComm.getUserByName(userName).get(0));
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        newForm.setExtraKnowledge   ("ExtraKnowledge");
        //Filipenko//25.02.12//20.14
        //+
        //newForm.setAdverts(newStudentData.getStudentHowHearAboutCentre());

        //user photo adding
        byte[] bFile = newStudentData.getPhoto();
        newForm.setPhoto(bFile);

        if (statusParam == 1) {
            List<Object> listOfStatus = searchSomething("Status", "name", "Зарегистрирована");
            Status currStatus = (Status)listOfStatus.get(0);
            newForm.setStatus(currStatus);
        }
        else if(statusParam == 2) {
            List<Object> listOfStatus = searchSomething("Status", "name", "Требует подтверждения");
            Status currStatus = (Status)listOfStatus.get(0);
            newForm.setStatus(currStatus);
        }
        currDAOStImpl.addForm(newForm);
        //advert
        for (Object currAdvert:newStudentData.getStudentHowHearAboutCentre()) {
            List<Object> listOfAdvertCat = searchSomething("AdvertCategory", "description", (String)currAdvert);
            AdvertCategory currAdvertCat = (AdvertCategory)listOfAdvertCat.get(0);
            Advert newAdvert = new Advert();
            newAdvert.setAdvertCategory(currAdvertCat);
            newAdvert.setForm(newForm);
            currDAOComm.addSomethingNew(newAdvert);
        }
        //advert other
        List<Object> listOfAdvertCatOther = searchSomething("AdvertCategory", "description", "Другое");
        AdvertCategory currAdvertCatOther = (AdvertCategory)listOfAdvertCatOther.get(0);
        Advert newAdvert = new Advert();
        newAdvert.setAdvertCategory(currAdvertCatOther);
        newAdvert.setForm(newForm);
        newAdvert.setOther(newStudentData.getStudentHowHearAboutCentreOther());
        currDAOComm.addSomethingNew(newAdvert);
        //contacts//email1
        if (!newStudentData.getStudentEmailFirst().equals("")) {
            List<Object> listOfEmails = searchSomething("ContactCategory", "category", "email1");
            ContactCategory email = (ContactCategory)listOfEmails.get(0);
            Contact newEmailContact = new Contact();
            newEmailContact.setContactCategory  (email);
            newEmailContact.setInfo             (newStudentData.getStudentEmailFirst().trim());
            newEmailContact.setForm           (newForm);
            currDAOComm.addSomethingNew(newEmailContact);
        }
        //contacts//email2
        if (!newStudentData.getStudentEmailSecond().equals("")) {
            List<Object> listOfEmails = searchSomething("ContactCategory", "category", "email2");
            ContactCategory email = (ContactCategory)listOfEmails.get(0);
            Contact newEmailContact = new Contact();
            newEmailContact.setContactCategory  (email);
            newEmailContact.setInfo             (newStudentData.getStudentEmailSecond().trim());
            newEmailContact.setForm           (newForm);
            currDAOComm.addSomethingNew(newEmailContact);
        }
        //contacts//cellphone
        if (!newStudentData.getStudentTelephone().equals("")) {
            List<Object> listOfCellphone = searchSomething("ContactCategory", "category", "cellphone1");
            ContactCategory cellPhone = (ContactCategory)listOfCellphone.get(0);
            Contact newEmailContact = new Contact();
            newEmailContact.setContactCategory  (cellPhone);
            newEmailContact.setInfo             (newStudentData.getStudentTelephone().trim());
            newEmailContact.setForm           (newForm);
            currDAOComm.addSomethingNew(newEmailContact);
        }
        //contacts//other
        if (!newStudentData.getStudentOtherContactType().equals("") &&
                !newStudentData.getStudentOtherContact().equals("")) {
            //create
            insertNewContacts(newStudentData.getStudentOtherContactType().trim());
            //filling
            List<Object> listOfOther = searchSomething("ContactCategory", "category", newStudentData.getStudentOtherContactType());
            ContactCategory otherContact = (ContactCategory)listOfOther.get(0);
            Contact newEmailContact = new Contact();
            newEmailContact.setContactCategory  (otherContact);
            newEmailContact.setInfo             (newStudentData.getStudentOtherContact().trim());
            newEmailContact.setForm           (newForm);
            currDAOComm.addSomethingNew(newEmailContact);
        }

        //knowledge//C++
        List<Object> listOfBranchCPP = searchSomething("Branch", "name", "C++");
        Branch BranchCPP = (Branch)listOfBranchCPP.get(0);
        Knowledge knowCPP = new Knowledge();
        knowCPP.setBranch (BranchCPP);
        knowCPP.setForm   (newForm);
        knowCPP.setScore(newStudentData.getStudentCPlusPlusMark());
        currDAOComm.addSomethingNew(knowCPP);
        //knowledge//java
        List<Object> listOfBranchJava = searchSomething("Branch", "name", "java");
        Branch BranchJava = (Branch)listOfBranchJava.get(0);
        Knowledge knowJava = new Knowledge();
        knowJava.setBranch (BranchJava);
        knowJava.setForm   (newForm);
        knowJava.setScore    (newStudentData.getStudentJavaMark());
        currDAOComm.addSomethingNew(knowJava);


        List<Object> listOfBranchCatProgLang = searchSomething("BranchCategory", "discription", "Языки программирования");
        BranchCategory branchCategoryProgLang = (BranchCategory)listOfBranchCatProgLang.get(0);

        List<Object> listOfBranchCatIT = searchSomething("BranchCategory", "discription", "Знания в области IT технологий");
        BranchCategory branchCategoryIT = (BranchCategory)listOfBranchCatIT.get(0);

        List<Object> listOfBranchCatLang = searchSomething("BranchCategory", "discription", "Знания иностранных языков");
        BranchCategory branchCategoryLang = (BranchCategory)listOfBranchCatLang.get(0);
        //lang//other 1
        if (!newStudentData.getStudentLanguage1().trim().equals("")) {
            insertNewBranch(newStudentData.getStudentLanguage1().trim(), branchCategoryProgLang);
            List<Object> listOfBranchLang1 = searchSomething("Branch", "name", newStudentData.getStudentLanguage1().trim());
            Branch BranchLang1 = (Branch)listOfBranchLang1.get(0);
            Knowledge knowLang1 = new Knowledge();
            knowLang1.setBranch (BranchLang1);
            knowLang1.setForm   (newForm);
            knowLang1.setScore    (newStudentData.getStudentLanguage1Mark());
            currDAOComm.addSomethingNew(knowLang1);
        }
        //lang//other 2
        if (!newStudentData.getStudentLanguage2().trim().equals("")) {
            insertNewBranch(newStudentData.getStudentLanguage2().trim(), branchCategoryProgLang);
            List<Object> listOfBranchLang2 = searchSomething("Branch", "name", newStudentData.getStudentLanguage2().trim());
            Branch BranchLang2 = (Branch)listOfBranchLang2.get(0);
            Knowledge knowLang2 = new Knowledge();
            knowLang2.setBranch (BranchLang2);
            knowLang2.setForm   (newForm);
            knowLang2.setScore    (newStudentData.getStudentLanguage2Mark());
            currDAOComm.addSomethingNew(knowLang2);
        }
        //lang//other 3
        if (!newStudentData.getStudentLanguage3().trim().equals("")) {
            insertNewBranch(newStudentData.getStudentLanguage3().trim(), branchCategoryProgLang);
            List<Object> listOfBranchLang3 = searchSomething("Branch", "name", newStudentData.getStudentLanguage3().trim());
            Branch BranchLang3 = (Branch)listOfBranchLang3.get(0);
            Knowledge knowLang3 = new Knowledge();
            knowLang3.setBranch (BranchLang3);
            knowLang3.setForm   (newForm);
            knowLang3.setScore    (newStudentData.getStudentLanguage3Mark());
            currDAOComm.addSomethingNew(knowLang3);
        }

        //knowledge//other 1
        if (!newStudentData.getStudentKnowledgeOther1().trim().equals("")) {
            insertNewBranch(newStudentData.getStudentKnowledgeOther1().trim(), branchCategoryIT);
            List<Object> listOfBranchOther1 = searchSomething("Branch", "name", newStudentData.getStudentKnowledgeOther1().trim());
            Branch BranchOther1 = (Branch)listOfBranchOther1.get(0);
            Knowledge knowOther1 = new Knowledge();
            knowOther1.setBranch (BranchOther1);
            knowOther1.setForm   (newForm);
            knowOther1.setScore    (newStudentData.getStudentKnowledgeOther1Mark());
            currDAOComm.addSomethingNew(knowOther1);
        }
        //knowledge//other 2
        if (!newStudentData.getStudentKnowledgeOther2().trim().equals("")) {
            insertNewBranch(newStudentData.getStudentKnowledgeOther2().trim(), branchCategoryIT);
            List<Object> listOfBranchOther2 = searchSomething("Branch", "name", newStudentData.getStudentKnowledgeOther2().trim());
            Branch BranchOther2 = (Branch)listOfBranchOther2.get(0);
            Knowledge knowOther2 = new Knowledge();
            knowOther2.setBranch (BranchOther2);
            knowOther2.setForm   (newForm);
            knowOther2.setScore    (newStudentData.getStudentKnowledgeOther2Mark());
            currDAOComm.addSomethingNew(knowOther2);
        }
        //knowledge//other 3
        if (!newStudentData.getStudentKnowledgeOther3().trim().equals("")) {
            insertNewBranch(newStudentData.getStudentKnowledgeOther3().trim(), branchCategoryIT);
            List<Object> listOfBranchOther3 = searchSomething("Branch", "name", newStudentData.getStudentKnowledgeOther3().trim());
            Branch BranchOther3 = (Branch)listOfBranchOther3.get(0);
            Knowledge knowOther3 = new Knowledge();
            knowOther3.setBranch (BranchOther3);
            knowOther3.setForm   (newForm);
            knowOther3.setScore    (newStudentData.getStudentKnowledgeOther3Mark());
            currDAOComm.addSomethingNew(knowOther3);
        }
        //knowledge//nets
        List<Object> listOfBranchNets = searchSomething("Branch", "name", "Сетевые технологии");
        Branch BranchNets = (Branch)listOfBranchNets.get(0);
        Knowledge knowNets = new Knowledge();
        knowNets.setBranch (BranchNets);
        knowNets.setForm   (newForm);
        knowNets.setScore    (newStudentData.getStudentKnowledgeNetwork());
        currDAOComm.addSomethingNew(knowNets);
        //knowledge//Algorithms
        List<Object> listOfBranchAlg = searchSomething("Branch", "name", "Эффективные алгоритмы");
        Branch BranchAlg = (Branch)listOfBranchAlg.get(0);
        Knowledge knowAlg = new Knowledge();
        knowAlg.setBranch (BranchAlg);
        knowAlg.setForm   (newForm);
        knowAlg.setScore    (newStudentData.getStudentKnowledgeEfficientAlgorithms());
        currDAOComm.addSomethingNew(knowAlg);
        //knowledge//OOP
        List<Object> listOfBranchOOP = searchSomething("Branch", "name", "ООП");
        Branch BranchOOP = (Branch)listOfBranchOOP.get(0);
        Knowledge knowOOP = new Knowledge();
        knowOOP.setBranch (BranchOOP);
        knowOOP.setForm   (newForm);
        knowOOP.setScore    (newStudentData.getStudentKnowledgeOOP());
        currDAOComm.addSomethingNew(knowOOP);
        //knowledge//DB
        List<Object> listOfBranchDB = searchSomething("Branch", "name", "БД");
        Branch BranchDB = (Branch)listOfBranchDB.get(0);
        Knowledge knowDB = new Knowledge();
        knowDB.setBranch (BranchDB);
        knowDB.setForm   (newForm);
        knowDB.setScore    (newStudentData.getStudentKnowledgeDB());
        currDAOComm.addSomethingNew(knowDB);
        //knowledge//Web
        List<Object> listOfBranchWeb = searchSomething("Branch", "name", "Web");
        Branch BranchWeb = (Branch)listOfBranchWeb.get(0);
        Knowledge knowWeb = new Knowledge();
        knowWeb.setBranch (BranchWeb);
        knowWeb.setForm   (newForm);
        knowWeb.setScore    (newStudentData.getStudentKnowledgeWeb());
        currDAOComm.addSomethingNew(knowWeb);
        //knowledge//GUI
        List<Object> listOfBranchGUI = searchSomething("Branch", "name", "GUI");
        Branch BranchGUI = (Branch)listOfBranchGUI.get(0);
        Knowledge knowGUI = new Knowledge();
        knowGUI.setBranch (BranchGUI);
        knowGUI.setForm   (newForm);
        knowGUI.setScore    (newStudentData.getStudentKnowledgeGUI());
        currDAOComm.addSomethingNew(knowGUI);
        //knowledge//net prog
        List<Object> listOfBranchNetProg = searchSomething("Branch", "name", "Сетевое программирование");
        Branch BranchNetProg = (Branch)listOfBranchNetProg.get(0);
        Knowledge knowNetProg = new Knowledge();
        knowNetProg.setBranch (BranchNetProg);
        knowNetProg.setForm   (newForm);
        knowNetProg.setScore    (newStudentData.getStudentKnowledgeNetworkProgramming());
        currDAOComm.addSomethingNew(knowNetProg);
        //knowledge//prog eng
        List<Object> listOfBranchProgEng = searchSomething("Branch", "name", "Проектирование программ");
        Branch BranchProgEng = (Branch)listOfBranchProgEng.get(0);
        Knowledge knowProgEng = new Knowledge();
        knowProgEng.setBranch (BranchProgEng);
        knowProgEng.setForm   (newForm);
        knowProgEng.setScore    (newStudentData.getStudentKnowledgeProgramDesign());
        currDAOComm.addSomethingNew(knowProgEng);
        //knowledge//english read
        List<Object> listOfBranchEngRead = searchSomething("Branch", "name", "Английский(чтение)");
        Branch BranchEngRead = (Branch)listOfBranchEngRead.get(0);
        Knowledge knowEngRead = new Knowledge();
        knowEngRead.setBranch (BranchEngRead);
        knowEngRead.setForm   (newForm);
        knowEngRead.setScore    (newStudentData.getStudentEnglishReadMark());
        currDAOComm.addSomethingNew(knowEngRead);
        //knowledge//english write
        List<Object> listOfBranchEngWrite = searchSomething("Branch", "name", "Английский(письмо)");
        Branch BranchEngWrite = (Branch)listOfBranchEngWrite.get(0);
        Knowledge knowEngWrite = new Knowledge();
        knowEngWrite.setBranch (BranchEngWrite);
        knowEngWrite.setForm   (newForm);
        knowEngWrite.setScore    (newStudentData.getStudentEnglishWriteMark());
        currDAOComm.addSomethingNew(knowEngWrite);
        //knowledge//english write
        List<Object> listOfBranchEngSpeak = searchSomething("Branch", "name", "Английский(речь)");
        Branch BranchEngSpeak = (Branch)listOfBranchEngSpeak.get(0);
        Knowledge knowEngSpeak = new Knowledge();
        knowEngSpeak.setBranch (BranchEngSpeak);
        knowEngSpeak.setForm   (newForm);
        knowEngSpeak.setScore    (newStudentData.getStudentEnglishSpeakMark());
        currDAOComm.addSomethingNew(knowEngSpeak);
        if (statusParam == 1) {
            List<Object> listOfStatus = searchSomething("Status", "name", "Зарегистрирована");
            Status currStatus = (Status)listOfStatus.get(0);
            newForm.setStatus(currStatus);
        }
        else if(statusParam == 2) {
            List<Object> listOfStatus = searchSomething("Status", "name", "Требует подтверждения");
            Status currStatus = (Status)listOfStatus.get(0);
            newForm.setStatus(currStatus);
        }

    }



    /**
     * Returns StudentData object, needed to fill form in UI,
     * by transforming Form object to StudentData object
     * @param UserName  user name (login)
     * @return object, needed to fill form using Vaadin
     */
    @Interceptors(ShowHibernateSQLInterceptor.class)
    public static StudentData getStudentDataByUserName(String UserName) {
        StudentData std = new StudentData();
        Form form = new DAOStudentImpl().getFormByUserName(UserName);
        if (form != null)
        {
            std.setIdForm(form.getIdForm());
            std.setStudentLastName(form.getLastName());
            std.setStudentFirstName(form.getFirstName());
            std.setStudentMiddleName(form.getMiddleName());
            std.setStudentInstitute(form.getCathedra().getFaculty().getInstitute());
            std.setStudentInstituteCourse(form.getInstituteYear().toString());
            std.setStudentFaculty(form.getCathedra().getFaculty());
            std.setStudentCathedra(form.getCathedra());
            std.setStudentInstituteGradYear(form.getInstituteGradYear().toString());

            std.setPhoto(form.getPhoto());

            Set contacts = form.getContacts();
            Iterator iterCont = contacts.iterator();
            while(iterCont.hasNext()) {
                Contact contact = (Contact) iterCont.next();
                String contactCategory = contact.getContactCategory().getCategory();
                if (contactCategory.equals("email1")) {
                    std.setStudentEmailFirst(contact.getInfo());
                }
                else if (contactCategory.equals("email2")) {
                    std.setStudentEmailSecond(contact.getInfo());
                }
                else if (contactCategory.equals("cellphone1")) {
                    std.setStudentTelephone(contact.getInfo());
                }
                else {
                    std.setStudentOtherContactType(contactCategory);
                    std.setStudentOtherContact(contact.getInfo());
                }
            }

            std.setStudentInterestStudy(form.getInterestStudy());
            std.setStudentInterestWork(form.getInterestWork());
            std.setStudentInterestDevelopment(form.getInterestBranchSoft());
            std.setStudentInterestOther(form.getInterestBranchOther());
            std.setStudentWorkTypeDeepSpec(form.getInterestDeepSpec());
            std.setStudentWorkTypeVarious(form.getInterestVarious());
            std.setStudentWorkTypeManagement(form.getInterestManagment());
            std.setStudentWorkTypeSale(form.getInterestSale());
            std.setStudentWorkTypeOther(form.getInterestOther());

            Set knowledges = form.getKnowledges();
            Iterator iterKnow = knowledges.iterator();
            Set<Knowledge> otherKnowledges = new HashSet();
            while(iterKnow.hasNext()) {
                Knowledge knowledge = (Knowledge) iterKnow.next();
                String branch = knowledge.getBranch().getName();
                if (branch.equals("C++")) {
                    std.setStudentCPlusPlusMark(knowledge.getScore());
                }
                else if (branch.equals("Java")) {
                    std.setStudentJavaMark(knowledge.getScore());
                }
                else if (branch.equals("Сетевые технологии")) {
                    std.setStudentKnowledgeNetwork(knowledge.getScore());
                }
                else if (branch.equals("Эффективные алгоритмы")) {
                    std.setStudentKnowledgeEfficientAlgorithms(knowledge.getScore());
                }
                else if (branch.equals("ООП")) {
                    std.setStudentKnowledgeOOP(knowledge.getScore());
                }
                else if (branch.equals("БД")) {
                    std.setStudentKnowledgeDB(knowledge.getScore());
                }
                else if (branch.equals("Web")) {
                    std.setStudentKnowledgeWeb(knowledge.getScore());
                }
                else if (branch.equals("GUI")) {
                    std.setStudentKnowledgeGUI(knowledge.getScore());
                }
                else if (branch.equals("Сетевое программирование")) {
                    std.setStudentKnowledgeNetworkProgramming(knowledge.getScore());
                }
                else if (branch.equals("Проектирование программ")) {
                    std.setStudentKnowledgeProgramDesign(knowledge.getScore());
                }
                else if (branch.trim().equalsIgnoreCase("Английский(чтение)")) {
                    std.setStudentEnglishReadMark(knowledge.getScore());
                }
                else if (branch.trim().equalsIgnoreCase("Английский(письмо)")) {
                    std.setStudentEnglishWriteMark(knowledge.getScore());
                }
                else if (branch.trim().equalsIgnoreCase("Английский(речь)")) {
                    std.setStudentEnglishSpeakMark(knowledge.getScore());
                }
                else {
                    otherKnowledges.add(knowledge);
                }
            }
            Iterator iterOtherKnow = otherKnowledges.iterator();
            Knowledge know;
            if(iterOtherKnow.hasNext()) {
                know = (Knowledge) iterOtherKnow.next();
                std.setStudentKnowledgeOther1(know.getBranch().getName());
                std.setStudentKnowledgeOther1Mark(know.getScore());
            }
            if(iterOtherKnow.hasNext()) {
                know = (Knowledge) iterOtherKnow.next();
                std.setStudentKnowledgeOther2(know.getBranch().getName());
                std.setStudentKnowledgeOther2Mark(know.getScore());
            }
            if(iterOtherKnow.hasNext()) {
                know = (Knowledge) iterOtherKnow.next();
                std.setStudentKnowledgeOther3(know.getBranch().getName());
                std.setStudentKnowledgeOther3Mark(know.getScore());
            }

//            std.setStudentLanguage1();
//            std.setStudentLanguage1Mark();
//            std.setStudentLanguage2();
//            std.setStudentLanguage2Mark();
//            std.setStudentLanguage3();
//            std.setStudentLanguage3Mark();

            std.setStudentExperienceProjects(form.getExecProject());


            Set adverts = form.getAdverts();
            LinkedHashSet linkedAdverts = new LinkedHashSet();
            Iterator iterAdv = adverts.iterator();
            while(iterAdv.hasNext()) {
                Advert advert = (Advert) iterAdv.next();
                String advertDecription = advert.getAdvertCategory().getDescription();
                if(advertDecription.trim().equalsIgnoreCase("Другое")) {
                    std.setStudentHowHearAboutCentreOther(advert.getOther());
                    iterAdv.remove();
                } else {
                    linkedAdverts.add(advertDecription);
                }
            }
            if (std.getStudentHowHearAboutCentreOther() == null) { std.setStudentHowHearAboutCentreOther("");}
            std.setStudentHowHearAboutCentre(linkedAdverts);
            
            
            std.setStudentReasonOffer(form.getReason());
            std.setStudentSelfAdditionalInformation(form.getExtraInfo());
        }
        return std;

    }
    
    public static byte[] scalePhoto(byte[] photoArray) {
        byte[] newPhotoArray = null;
        ImageIcon imageIcon = new ImageIcon(photoArray);
        Image image = imageIcon.getImage();
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        double ratio = Math.min((double) 200/width, (double) 300/height);
        double newWidth = ratio * width;
        double newHeight = ratio * height;
        BufferedImage bi = new BufferedImage((int)Math.round(newWidth), (int)Math.round(newHeight), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bi.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.drawImage(image, 0, 0,(int)Math.round(newWidth), (int)Math.round(newHeight), null); 
    	g2d.dispose();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, "JPEG", baos);
        } catch (IOException ex) {
            Logger.getLogger(StudentPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        newPhotoArray = baos.toByteArray();
        return newPhotoArray;
    }

}