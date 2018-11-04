/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marased.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mahmoudyahia
 */
@Entity
@Table(name = "meeting_members_transaction")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MeetingMembersTransaction.findAll", query = "SELECT m FROM MeetingMembersTransaction m")
    , @NamedQuery(name = "MeetingMembersTransaction.findById", query = "SELECT m FROM MeetingMembersTransaction m WHERE m.id = :id")
    , @NamedQuery(name = "MeetingMembersTransaction.findByMemberTypeId", query = "SELECT m FROM MeetingMembersTransaction m WHERE m.memberTypeId = :memberTypeId")})
public class MeetingMembersTransaction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "Id")
    private Integer id;
    @Column(name = "member_Type_Id")
    private Integer memberTypeId;
    @JoinColumn(name = "meeting_Id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Meetings meetingId = new Meetings();
    @JoinColumn(name = "member_Id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Members memberId = new Members();
    @Transient
    private String memberPosition;

    public MeetingMembersTransaction() {
    }

    public MeetingMembersTransaction(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberTypeId() {
        return memberTypeId;
    }

    public void setMemberTypeId(Integer memberTypeId) {
        this.memberTypeId = memberTypeId;
    }

    public Meetings getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Meetings meetingId) {
        this.meetingId = meetingId;
    }

    public Members getMemberId() {
        return memberId;
    }

    public void setMemberId(Members memberId) {
        this.memberId = memberId;
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
        if (!(object instanceof MeetingMembersTransaction)) {
            return false;
        }
        MeetingMembersTransaction other = (MeetingMembersTransaction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.marased.model.MeetingMembersTransaction[ id=" + id + " ]";
    }

    public String getMemberPosition() {
        switch (this.memberTypeId) {
            case 1:
                this.memberPosition = "Chairman";
                break;
            case 2:
                this.memberPosition = "Chairman assistant";
                break;
            case 3:
                this.memberPosition = "Regular meeting member";
        }
        return memberPosition;
    }

    public void setMemberPosition(String memberPosition) {
        this.memberPosition = memberPosition;
    }

}
