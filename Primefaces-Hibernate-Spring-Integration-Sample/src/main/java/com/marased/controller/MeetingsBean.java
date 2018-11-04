/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marased.controller;

import com.marased.commons.Types;
import com.marased.dao.daoGeneral;
import com.marased.model.MeetingMembersTransaction;
import com.marased.model.MeetingPoints;
import com.marased.model.Meetings;
import com.marased.model.Members;
import com.marased.utils.CommonDateUtil;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author mahmoudyahia
 */
@ManagedBean(name = "meetingsBean")
@ViewScoped
public class MeetingsBean implements Serializable {

    //Variables definition part ..!
    @ManagedProperty("#{daoGeneral}")
    private daoGeneral daoGeneral;
    private Meetings meetings;
    private List<Meetings> meetingsList;
    private List<Types> meetingTypesList;
    private MeetingPoints meetingPoints;
    private List<MeetingPoints> meetingsPointsList;
    private List<Types> pointTypesList;
    private Members member;
    private List<Members> membersList;
    private MeetingMembersTransaction meetingMembersTransaction;
    private List<Types> memberPositionsList;
    private List<Members> membersMeetingList;
    private List<MeetingMembersTransaction> meetingMembersTransactionsList;
    //Global formats ..!
    private String dateFormat = "dd/MM/yyyy";
    private String timeFormat = "HH:mm:ss";
    //Dates and Times..!
    CommonDateUtil dateUtil = new CommonDateUtil();
    private boolean meetingsCollapsedValue;

    //
    /**
     * Creates a new instance of Meetings
     */
    //Constructors part
    public MeetingsBean() {
    }
    //

    //Initiation part
    @PostConstruct
    public void init() {
        meetings = new Meetings();
        meetingsList = new ArrayList<Meetings>();
        meetingsList = daoGeneral.getAllObjects(Meetings.class);
        //
        meetingPoints = new MeetingPoints();
        //
        member = new Members();
        membersList = new ArrayList<Members>();
        membersList = daoGeneral.getAllObjects(Members.class);
        //
        meetingMembersTransaction = new MeetingMembersTransaction();
        membersMeetingList = new ArrayList<Members>();
        membersMeetingList = daoGeneral.getAllObjects(Members.class);
        // Type Lists
        Types meetingTypesRegular = new Types(1, "Regular");
        Types meetingTypesUrgent = new Types(2, "Urgent");
        meetingTypesList = new ArrayList<Types>();
        meetingTypesList.add(meetingTypesRegular);
        meetingTypesList.add(meetingTypesUrgent);
        /**
         * *************************************************
         */
        Types pointTypeNotVip = new Types(1, "Not VIP Point");
        Types pointTypetVip = new Types(2, "VIP Point");
        setPointTypesList(new ArrayList<Types>());
        getPointTypesList().add(pointTypetVip);
        getPointTypesList().add(pointTypeNotVip);
        /**
         * *************************************************
         */

        Types memberPositionChairman = new Types(1, "Chairman");
        Types memberPositionChairmanAssistant = new Types(2, "Chairman assistant");
        Types memberPositionMeetingMember = new Types(3, "Regular meeting member");
        memberPositionsList = new ArrayList<Types>();
        memberPositionsList.add(memberPositionChairman);
        memberPositionsList.add(memberPositionChairmanAssistant);
        memberPositionsList.add(memberPositionMeetingMember);
        //
        meetingsCollapsedValue = true;
    }
    //

    //Redirction Part ..!
    public void sendRedirct(String pagePath) {
        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            extContext.redirect(pagePath + ".xhtml");
        } catch (IOException ex) {
            Logger.getLogger(MeetingsBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void registerMember() {
        membersList = new ArrayList<Members>();
        membersList = daoGeneral.getAllObjects(Members.class);
        sendRedirct("registerMembers");
    }

    public void registerMeeting() {
        sendRedirct("meetings");
    }

    public void goToMainPage() {
        sendRedirct("welcome");
    }

    private String checkDatabaseRecords(Date meetingStartDate, Date meetingStartTime) {
        List<Meetings> meetingsLocalList = daoGeneral.getAllObjects(Meetings.class);

        String saveMessage = "";
        if (meetingsLocalList != null && !meetingsLocalList.isEmpty()) {
            for (Meetings meetingObj : meetingsLocalList) {
                //Dates ..!
                //Date existMeetingStartDate = dateUtil.getCutomDateFormat(dateFormat, meetingObj.getMeetingStartDate());
                Date existMeetingEndDate = dateUtil.getCutomDateFormat(dateFormat, meetingObj.getMeetingEndDate());
                //Times ..!
                //Date existMeetingStartTime = dateUtil.getCutomTimeFormat(timeFormat, meetingObj.getMeetingStartDate());
                Date existMeetingEndTime = dateUtil.getCutomDateFormat(timeFormat, meetingObj.getMeetingEndDate());

                if (this.meetings.getId() != null && meetingObj.equals(this.meetings)) {
                    saveMessage = "saveSuccessfully";
                } else {
                    if (meetingStartDate.compareTo(existMeetingEndDate) != 0) {
                        //save ..!
                        saveMessage = "saveSuccessfully";
                    } else {
                        // == check times ..!
                        if (meetingStartTime.compareTo(existMeetingEndTime) >= 0) {
                            //Save ..!
                            saveMessage = "saveSuccessfully";
                        } else {
                            saveMessage = "Error: Time intersection ..!";
                            break;
                        }
                    }
                }
            }
        } else {
            //Save direct ..!
            saveMessage = "saveSuccessfully";
        }
        //Save method ..!
        //Check the iterate result ..!
        if (saveMessage.equalsIgnoreCase("saveSuccessfully")) {
            daoGeneral.save(this.meetings);
            FacesContext.getCurrentInstance().addMessage("meetingGrowlMessages", new FacesMessage(FacesMessage.SEVERITY_INFO, "", "The meeting data saved successfully ..!"));
            this.meetings = new Meetings();

        } else {
            //Error: error here will depends on saveMessage result ..!
            FacesContext.getCurrentInstance().addMessage("meetingGrowlMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "", saveMessage));
        }
        return "meetings";
    }

    //
    //Meeting part ..!
    //Register the Meeting Master Values ..!
    public String saveMeeting() {
        //Dates ..!
        Date todayDate = dateUtil.getCutomDateFormat(dateFormat, new Date());
        Date meetingStartDate = dateUtil.getCutomDateFormat(dateFormat, this.meetings.getMeetingStartDate());
        Date meetingEndDate = dateUtil.getCutomDateFormat(dateFormat, this.meetings.getMeetingEndDate());
        //Times..!
        Date todayTime = dateUtil.getCutomTimeFormat(timeFormat, new Date());
        Date meetingStartTime = dateUtil.getCutomTimeFormat(timeFormat, this.meetings.getMeetingStartDate());
        Date meetingEndTime = dateUtil.getCutomTimeFormat(timeFormat, this.meetings.getMeetingEndDate());

        if (meetingStartDate.compareTo(todayDate) > 0) {
            //Check input dates with each other ..!
            //Check start meetingStartDate and meetingEndDate ..!
            if (meetingEndDate.compareTo(meetingStartDate) > 0) {
                //Check DB ..!
                checkDatabaseRecords(meetingStartDate, meetingStartTime);
            } else if (meetingEndDate.compareTo(meetingStartDate) == 0) {
                //Check Times ..!
                if (meetingEndTime.compareTo(meetingStartTime) > 0) {
                    //Check DB ..!
                    checkDatabaseRecords(meetingStartDate, meetingStartTime);
                } else {
                    //Error: meeting end time must be greater than meeting start time ..!
                    FacesContext.getCurrentInstance().addMessage("meetingGrowlMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Error: meeting end time must be greater than meeting start time ..!"));
                    return "";
                }
            } else {
                //Error: Meeting end date must be greater than or equal meeting start date ..!
                FacesContext.getCurrentInstance().addMessage("meetingGrowlMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Error: Meeting end date must be greater than or equal meeting start date ..!"));
                return "";
            }
        } else if (meetingStartDate.compareTo(todayDate) == 0) {
            //check input dates with today dates ..!
            //Check today time ..!
            if (meetingStartTime.compareTo(todayTime) > 0) {
                //Check start meetingStartDate and meetingEndDate ..!
                if (meetingEndDate.compareTo(meetingStartDate) > 0) {
                    //Check DB ..!
                    checkDatabaseRecords(meetingStartDate, meetingStartTime);
                } else if (meetingEndDate.compareTo(meetingStartDate) == 0) {
                    //Check Times ..!
                    if (meetingEndTime.compareTo(meetingStartTime) > 0) {
                        //Check DB ..!
                        checkDatabaseRecords(meetingStartDate, meetingStartTime);
                    } else {
                        //Error: meeting end time must be greater than meeting start time ..!
                        FacesContext.getCurrentInstance().addMessage("meetingGrowlMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Error: meeting end time must be greater than meeting start time ..!"));
                        return "";
                    }
                } else {
                    //Error: Meeting end date must be greater than or equal meeting start date ..!
                    FacesContext.getCurrentInstance().addMessage("meetingGrowlMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Error: Meeting end date must be greater than or equal meeting start date ..!"));
                    return "";
                }
            } else {
                //Error: meeting start time must be greater than meeting NOW ..!
                FacesContext.getCurrentInstance().addMessage("meetingGrowlMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Error: meeting start time must be greater than meeting NOW ..!"));
                return "";
            }
        } else {
            //Error: meeting start date must be greater than or equal today ..!
            FacesContext.getCurrentInstance().addMessage("meetingGrowlMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Error: meeting start date must be grater than today ..!"));
            return "";
        }
        meetingsCollapsedValue = true;
        return "meetings";
    }

    //Meeting points part ..!
    public String updateMeetingView(Meetings meeting) {
        Date todayDate = dateUtil.getCutomDateFormat(dateFormat, new Date());
        Date todayTime = dateUtil.getCutomDateFormat(timeFormat, new Date());

        Date meetingStartDate = dateUtil.getCutomDateFormat(dateFormat, meeting.getMeetingStartDate());
        Date meetingStartTime = dateUtil.getCutomTimeFormat(timeFormat, meeting.getMeetingStartDate());

        if (meetingStartDate.compareTo(todayDate) > 0) {
            this.meetings = meeting;
        } else if (meetingStartDate.compareTo(todayDate) == 0 && meetingStartTime.compareTo(todayTime) > 0) {
            this.meetings = meeting;
        } else {
            //Error: you can't update out of date time meeting ..!
            FacesContext.getCurrentInstance().addMessage("meetingGrowlMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "you can't update out of date time meeting ..!"));
        }
        meetingsCollapsedValue = false;
        return "";
    }

    public String deleteMeeting(Meetings meeting) {
        daoGeneral.delete(meeting);
        meetingsList.remove(meeting);
        FacesContext.getCurrentInstance().addMessage("meetingGrowlMessages", new FacesMessage(FacesMessage.SEVERITY_WARN, "", "The meeting information deleted successfully ..!"));
        return "";
    }

    public String addNewPointViewRequirements(Meetings meetingsObj) {
        this.meetings = meetingsObj;
        meetingsPointsList = new ArrayList<MeetingPoints>();
        meetingsPointsList = daoGeneral.getObjectsByParameter(MeetingPoints.class, "meetingsId", this.meetings);
        return "";
    }

    public String addNewMeetingMemberViewRequirements(Meetings meetingsObj) {
        this.meetings = meetingsObj;
        //get all members of meeting then, remove them from membersMeetingList
        meetingMembersTransactionsList = new ArrayList<MeetingMembersTransaction>();
        meetingMembersTransactionsList = daoGeneral.getObjectsByParameter(MeetingMembersTransaction.class, "meetingId", this.meetings);
        membersMeetingList.remove(this.meetings.getMeetingAdminMember());
        return "";
    }

    // Save the points of eatch meeting ...!
    public String savePoint() {
        if (this.meetings != null) {
            if (this.meetingPoints.getId() != null) {
                daoGeneral.save(this.meetingPoints);
                FacesContext.getCurrentInstance().addMessage("meetingPointsGrowlMessages", new FacesMessage(FacesMessage.SEVERITY_INFO, "", "The meeting point information updated successfully ..!"));
            } else {
                this.meetingPoints.setMeetingsId(this.meetings);
                daoGeneral.save(this.meetingPoints);
                FacesContext.getCurrentInstance().addMessage("meetingPointsGrowlMessages", new FacesMessage(FacesMessage.SEVERITY_INFO, "", "The meeting point information saved successfully ..!"));
                meetingsPointsList.add(this.meetingPoints);
            }
            this.meetingPoints = new MeetingPoints();
            return "";
        } else {
            FacesContext.getCurrentInstance().addMessage("meetingPointsGrowlMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "You must select a meeting to add points to it..!"));
            return "";
        }
    }

    public String updateMeetingPointsView(MeetingPoints meetingPoints) {
        this.meetingPoints = meetingPoints;
        return "";
    }

    public String deleteMeetingPoint(MeetingPoints meetingPoints) {
        daoGeneral.delete(meetingPoints);
        meetingsPointsList.remove(meetingPoints);
        FacesContext.getCurrentInstance().addMessage("meetingPointsGrowlMessages", new FacesMessage(FacesMessage.SEVERITY_WARN, "", "The meeting point information deleted successfully ..!"));
        return "";
    }
    //

    //Members part ..!
    public String saveMember() {
        if (this.member.getId() != null) {
            daoGeneral.save(this.member);
            FacesContext.getCurrentInstance().addMessage("membersGrowlMessages", new FacesMessage(FacesMessage.SEVERITY_INFO, "", "The member information updated successfully ..!"));
        } else {
            daoGeneral.save(this.member);
            FacesContext.getCurrentInstance().addMessage("membersGrowlMessages", new FacesMessage(FacesMessage.SEVERITY_INFO, "", "The member information saved successfully ..!"));
        }
        return "registerMembers";
    }

    public void updateMemberView(Members member) {
        this.member = member;
    }

    public String deleteMember(Members member) {
        daoGeneral.delete(member);
        membersList.remove(member);
        FacesContext.getCurrentInstance().addMessage("membersGrowlMessages", new FacesMessage(FacesMessage.SEVERITY_WARN, "", "The member information deleted successfully ..!"));
        return "";
    }
    //

    //Meeting members part ..!
    public String saveMeetingMember() {
        if (this.meetings != null) {

            List<MeetingMembersTransaction> meetingMembersTransactionCheckList = daoGeneral.getObjectsByParameter(MeetingMembersTransaction.class, "meetingId", this.meetings);
            String saveMessage = "";
            if (this.meetingMembersTransaction.getId() != null) {
                daoGeneral.save(this.meetingMembersTransaction);
                FacesContext.getCurrentInstance().addMessage("meetingMembersGrowlMessages", new FacesMessage(FacesMessage.SEVERITY_INFO, "", "The member of meeting update successfully ..!"));
            } else {
                if (meetingMembersTransactionCheckList != null && !meetingMembersTransactionCheckList.isEmpty()) {
                    for (MeetingMembersTransaction meetingMembersTransactionObj : meetingMembersTransactionCheckList) {
                        if (this.meetingMembersTransaction.getMemberId().getId().equals(meetingMembersTransactionObj.getMemberId().getId())) {
                            //message Error: member already exist ..!
                            saveMessage = "message Error: member already exist ..!";
                            break;
                        } else {
                            if (this.meetingMembersTransaction.getMemberTypeId().equals(meetingMembersTransactionObj.getMemberTypeId())) {
                                //message Error: position already exist ..!
                                saveMessage = "message Error: position already exist ..!";
                                break;
                            } else {
                                //Save successfully ..!
                                saveMessage = "saveSuccessfully";
                            }
                        }
                    }
                    if (saveMessage.equalsIgnoreCase("saveSuccessfully")) {
                        this.meetingMembersTransaction.setMeetingId(this.meetings);
                        Integer meetingMembersId = this.meetingMembersTransaction.getMemberId().getId();
                        List meetingMember = daoGeneral.getObjectsByParameter(Members.class, "id", meetingMembersId);
                        this.member = (Members) meetingMember.get(0);
                        this.meetingMembersTransaction.setMemberId(this.member);
                        daoGeneral.save(this.meetingMembersTransaction);
                        meetingMembersTransactionsList.add(this.meetingMembersTransaction);
                        FacesContext.getCurrentInstance().addMessage("meetingMembersGrowlMessages", new FacesMessage(FacesMessage.SEVERITY_INFO, "", "The member of meeting saved successfully ..!"));
                        this.meetingMembersTransaction = new MeetingMembersTransaction();
                        return "";
                    } else {
                        FacesContext.getCurrentInstance().addMessage("meetingGrowlMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "", saveMessage));
                        return "";
                    }
                } else {
                    this.meetingMembersTransaction.setMeetingId(this.meetings);
                    Integer meetingMembersId = this.meetingMembersTransaction.getMemberId().getId();
                    List meetingMember = daoGeneral.getObjectsByParameter(Members.class, "id", meetingMembersId);
                    this.member = (Members) meetingMember.get(0);
                    this.meetingMembersTransaction.setMemberId(this.member);
                    daoGeneral.save(this.meetingMembersTransaction);
                    meetingMembersTransactionsList.add(this.meetingMembersTransaction);
                    FacesContext.getCurrentInstance().addMessage("meetingMembersGrowlMessages", new FacesMessage(FacesMessage.SEVERITY_INFO, "", "The member of meeting saved successfully ..!"));
                    this.meetingMembersTransaction = new MeetingMembersTransaction();
                    return "";
                }
            }
            this.meetingMembersTransaction = new MeetingMembersTransaction();
            return "";
        } else {
            FacesContext.getCurrentInstance().addMessage("meetingMembersGrowlMessages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "You must select a meeting to add members to it..!"));
            return "";
        }
    }

    public String updateMeetingMembersView(MeetingMembersTransaction meetingMembersTransactionObj) {
        this.meetingMembersTransaction = meetingMembersTransactionObj;
        return "";
    }

    public String deleteMeetingMembers(MeetingMembersTransaction meetingMembersTransactionObj) {
        daoGeneral.delete(meetingMembersTransactionObj);
        meetingMembersTransactionsList.remove(meetingMembersTransactionObj);
        FacesContext.getCurrentInstance().addMessage("meetingMembersGrowlMessages", new FacesMessage(FacesMessage.SEVERITY_WARN, "", "The member of meeting deleted successfully ..!"));
        return "";
    }

    /**
     * @return the daoGeneral
     */
    public daoGeneral getDaoGeneral() {
        return daoGeneral;
    }

    /**
     * @param daoGeneral the daoGeneral to set
     */
    public void setDaoGeneral(daoGeneral daoGeneral) {
        this.daoGeneral = daoGeneral;
    }

    /**
     * @return the meetings
     */
    public Meetings getMeetings() {
        return meetings;
    }

    /**
     * @param meetings the meetings to set
     */
    public void setMeetings(Meetings meetings) {
        this.meetings = meetings;
    }

    /**
     * @return the meetingsList
     */
    public List<Meetings> getMeetingsList() {
        return meetingsList;
    }

    /**
     * @param meetingsList the meetingsList to set
     */
    public void setMeetingsList(List<Meetings> meetingsList) {
        this.meetingsList = meetingsList;
    }

    /**
     * @return the meetingTypesList
     */
    public List<Types> getMeetingTypesList() {
        return meetingTypesList;
    }

    /**
     * @param meetingTypesList the meetingTypesList to set
     */
    public void setMeetingTypesList(List<Types> meetingTypesList) {
        this.meetingTypesList = meetingTypesList;
    }

    /**
     * @return the meetingPoints
     */
    public MeetingPoints getMeetingPoints() {
        return meetingPoints;
    }

    /**
     * @param meetingPoints the meetingPoints to set
     */
    public void setMeetingPoints(MeetingPoints meetingPoints) {
        this.meetingPoints = meetingPoints;
    }

    /**
     * @return the pointTypesList
     */
    public List<Types> getPointTypesList() {
        return pointTypesList;
    }

    /**
     * @param pointTypesList the pointTypesList to set
     */
    public void setPointTypesList(List<Types> pointTypesList) {
        this.pointTypesList = pointTypesList;
    }

    public List<MeetingPoints> getMeetingsPointsList() {
        return meetingsPointsList;
    }

    public void setMeetingsPointsList(List<MeetingPoints> meetingsPointsList) {
        this.meetingsPointsList = meetingsPointsList;
    }

    public Members getMember() {
        return member;
    }

    public void setMember(Members member) {
        this.member = member;
    }

    public List<Members> getMembersList() {
        return membersList;
    }

    public void setMembersList(List<Members> membersList) {
        this.membersList = membersList;
    }

    public MeetingMembersTransaction getMeetingMembersTransaction() {
        return meetingMembersTransaction;
    }

    public void setMeetingMembersTransaction(MeetingMembersTransaction meetingMembersTransaction) {
        this.meetingMembersTransaction = meetingMembersTransaction;
    }

    public List<Types> getMemberPositionsList() {
        return memberPositionsList;
    }

    public void setMemberPositionsList(List<Types> memberPositionsList) {
        this.memberPositionsList = memberPositionsList;
    }

    public List<Members> getMembersMeetingList() {
        return membersMeetingList;
    }

    public void setMembersMeetingList(List<Members> membersMeetingList) {
        this.membersMeetingList = membersMeetingList;
    }

    public List<MeetingMembersTransaction> getMeetingMembersTransactionsList() {
        return meetingMembersTransactionsList;
    }

    public void setMeetingMembersTransactionsList(List<MeetingMembersTransaction> meetingMembersTransactionsList) {
        this.meetingMembersTransactionsList = meetingMembersTransactionsList;
    }

    public boolean isMeetingsCollapsedValue() {
        return meetingsCollapsedValue;
    }

    public void setMeetingsCollapsedValue(boolean meetingsCollapsedValue) {
        this.meetingsCollapsedValue = meetingsCollapsedValue;
    }
}
