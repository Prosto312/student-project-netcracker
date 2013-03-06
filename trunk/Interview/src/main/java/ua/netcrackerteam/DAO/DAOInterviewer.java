package ua.netcrackerteam.DAO;

import java.util.List;

/**
 *Interface for Interviewer DAO
 * Interviewer can see list of interview datetimes. Can choose interview.
 * Each interview item has a list of students which this interview assigned to.
 * But also list is divided in user interface in two sublists. One sublist -
 * for students which don't have marks from this interviewer
 * Another sublist - for students which have already passed the interview and
 * got marks also from this interviewer
 * 
 * @author Zhokha Maksym
 */
public interface DAOInterviewer {
    
    /**
     * Returns forms of students which don't have marks from this interviewer
     * @param idInterview id of interview assigned for students
     * @param interviewerUsername username of interviewer 
     * @return list of form objects related to the specified interview
     */
    List<Form> getFormsWithoutMark(int idInterview, String interviewerUsername);
    
    /**
     * Returns list of form objects related to the specified interview and
     * have marks from specified interviewer
     * @param idInterview id of interview assigned for students
     * @param interviewerUsername username of interviewer 
     * @return list of form objects related to the specified interview and
     * have marks from specified interviewer
     */
    List<Form> getFormsWithMark(int idInterview, String interviewerUsername);
    
    /**
     * Returns mark which interviewer gave to the student and which is presented
     * in text
     * @param idForm id form of student
     * @param interviewerUsername username of interviewer 
     * @return mark which interviewer gave to the student
     */
    String getStudentInterviewMark(int idForm, String interviewerUsername);
    
    /**
     * Saves interview mark for student which interviewer gave to this student
     * @param idForm id form of student
     * @param interviewerUsername username of interviewer 
     * @param mark mark represented in text field
     */    
    void saveStudentInterviewMark(int idForm, String interviewerUsername, String mark);  
    
    
    
}