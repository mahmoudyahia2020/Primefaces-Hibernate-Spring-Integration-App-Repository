/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marased.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mahmoudyahia
 */
@Entity
@Table(name = "meetings")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Meetings.findAll", query = "SELECT m FROM Meetings m")
    , @NamedQuery(name = "Meetings.findById", query = "SELECT m FROM Meetings m WHERE m.id = :id")
    , @NamedQuery(name = "Meetings.findByMeetingEndDate", query = "SELECT m FROM Meetings m WHERE m.meetingEndDate = :meetingEndDate")
    , @NamedQuery(name = "Meetings.findByMeetingName", query = "SELECT m FROM Meetings m WHERE m.meetingName = :meetingName")
    , @NamedQuery(name = "Meetings.findByMeetingStartDate", query = "SELECT m FROM Meetings m WHERE m.meetingStartDate = :meetingStartDate")
    , @NamedQuery(name = "Meetings.findByMeetingType", query = "SELECT m FROM Meetings m WHERE m.meetingType = :meetingType")})
public class Meetings implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "meeting_end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date meetingEndDate;
    @Column(name = "meeting_name")
    private String meetingName;
    @Column(name = "meeting_start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date meetingStartDate;
    @Column(name = "meeting_type")
    private Integer meetingType;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "meetingsId")
    private List<MeetingPoints> meetingPointsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "meetingId")
    private List<MeetingMembersTransaction> meetingMembersTransactionList;
    @JoinColumn(name = "meeting_Admin_Member", referencedColumnName = "id")
    @ManyToOne
    private Members meetingAdminMember = new Members();
    @Transient
    private String meetingWaitingPeriod;
    @Transient
    private int disabledFlag = 0;

    public Meetings() {
    }

    public Meetings(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getMeetingEndDate() {
        return meetingEndDate;
    }

    public void setMeetingEndDate(Date meetingEndDate) {
        this.meetingEndDate = meetingEndDate;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public Date getMeetingStartDate() {
        return meetingStartDate;
    }

    public void setMeetingStartDate(Date meetingStartDate) {
        this.meetingStartDate = meetingStartDate;
    }

    public Integer getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(Integer meetingType) {
        this.meetingType = meetingType;
    }

    @XmlTransient
    public List<MeetingPoints> getMeetingPointsList() {
        return meetingPointsList;
    }

    public void setMeetingPointsList(List<MeetingPoints> meetingPointsList) {
        this.meetingPointsList = meetingPointsList;
    }

    @XmlTransient
    public List<MeetingMembersTransaction> getMeetingMembersTransactionList() {
        return meetingMembersTransactionList;
    }

    public void setMeetingMembersTransactionList(List<MeetingMembersTransaction> meetingMembersTransactionList) {
        this.meetingMembersTransactionList = meetingMembersTransactionList;
    }

    public Members getMeetingAdminMember() {
        return meetingAdminMember;
    }

    public void setMeetingAdminMember(Members meetingAdminMember) {
        this.meetingAdminMember = meetingAdminMember;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Meetings)) {
            return false;
        }
        Meetings other = (Meetings) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.marased.model.Meetings[ id=" + id + " ]";
    }

    public String getMeetingWaitingPeriod() {
        SimpleDateFormat customFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date dStart = null;
        Date dSysDate = null;
        Date dEnd = null;
        Date sysDate = new Date();

        try {
            String meetingStartDateStringCustomFormat = customFormat.format(this.meetingStartDate);
            String meetingEndDateStringCustomFormat = customFormat.format(this.meetingEndDate);

            String sysDateStringCustomFormat = customFormat.format(sysDate);

            dStart = customFormat.parse(meetingStartDateStringCustomFormat);
            dSysDate = customFormat.parse(sysDateStringCustomFormat);
            dEnd = customFormat.parse(meetingEndDateStringCustomFormat);

            //in milliseconds
            long startMeetingDiff = dStart.getTime() - dSysDate.getTime();

            long endMeetingDiff = dEnd.getTime() - dSysDate.getTime();

            long startMeetingDiffSeconds = startMeetingDiff / 1000 % 60;
            long startMeetingDiffMinutes = startMeetingDiff / (60 * 1000) % 60;
            long startMeetingDiffHours = startMeetingDiff / (60 * 60 * 1000) % 24;
            long startMeetingDiffDays = startMeetingDiff / (24 * 60 * 60 * 1000);

            long endMeetingDiffSeconds = endMeetingDiff / 1000 % 60;
            long endMeetingDiffMinutes = endMeetingDiff / (60 * 1000) % 60;
            long endMeetingDiffHours = endMeetingDiff / (60 * 60 * 1000) % 24;
            long endMeetingDiffDays = endMeetingDiff / (24 * 60 * 60 * 1000);

            //Mix startMeetingDay and endMeetingDay
            //Check dates and times ..!
            if (sysDate.compareTo(this.meetingEndDate) > 0) {
                meetingWaitingPeriod = "Meeting Done ..!";
                disabledFlag = 1;
            } else if (sysDate.compareTo(this.meetingStartDate) > 0 && sysDate.compareTo(this.meetingEndDate) <= 0) {
                meetingWaitingPeriod = "Meeting In-Progress..! and it will take : " + Math.abs(endMeetingDiffDays) + " day(s)" + Math.abs(endMeetingDiffHours) + " hour(s)" + Math.abs(endMeetingDiffMinutes) + " Minute(s)";
                disabledFlag = 1;
            } else if (sysDate.compareTo(this.meetingStartDate) <= 0 && sysDate.compareTo(this.meetingEndDate) < 0) {
                meetingWaitingPeriod = "Meeting will start after : " + Math.abs(startMeetingDiffDays) + " day(s) and " + Math.abs(startMeetingDiffHours) + " hour(s)" + Math.abs(startMeetingDiffMinutes) + " Minute(s)";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return meetingWaitingPeriod;
    }

    public void setMeetingWaitingPeriod(String meetingWaitingPeriod) {
        this.meetingWaitingPeriod = meetingWaitingPeriod;
    }

    /**
     * @return the disabledFlag
     */
    public int getDisabledFlag() {
        return disabledFlag;
    }

    /**
     * @param disabledFlag the disabledFlag to set
     */
    public void setDisabledFlag(int disabledFlag) {
        this.disabledFlag = disabledFlag;
    }

}
