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
@Table(name = "meeting_points")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MeetingPoints.findAll", query = "SELECT m FROM MeetingPoints m")
    , @NamedQuery(name = "MeetingPoints.findById", query = "SELECT m FROM MeetingPoints m WHERE m.id = :id")
    , @NamedQuery(name = "MeetingPoints.findByPointName", query = "SELECT m FROM MeetingPoints m WHERE m.pointName = :pointName")
    , @NamedQuery(name = "MeetingPoints.findByPointType", query = "SELECT m FROM MeetingPoints m WHERE m.pointType = :pointType")})
public class MeetingPoints implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "point_name")
    private String pointName;
    @Column(name = "point_type")
    private Integer pointType;
    @JoinColumn(name = "meetings_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Meetings meetingsId;
    @Transient
    private String pointTypeDesc;

    public MeetingPoints() {
    }

    public MeetingPoints(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public Integer getPointType() {
        return pointType;
    }

    public void setPointType(Integer pointType) {
        this.pointType = pointType;
    }

    public Meetings getMeetingsId() {
        return meetingsId;
    }

    public void setMeetingsId(Meetings meetingsId) {
        this.meetingsId = meetingsId;
    }

    public String getPointTypeDesc() {
        switch (this.pointType) {
            case 1:
                this.pointTypeDesc = "Not VIP Point";
                break;
            case 2:
                this.pointTypeDesc = "VIP Point";
                break;
        }
        return pointTypeDesc;
    }

    public void setPointTypeDesc(String pointTypeDesc) {
        this.pointTypeDesc = pointTypeDesc;
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
        if (!(object instanceof MeetingPoints)) {
            return false;
        }
        MeetingPoints other = (MeetingPoints) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.marased.model.MeetingPoints[ id=" + id + " ]";
    }
}
