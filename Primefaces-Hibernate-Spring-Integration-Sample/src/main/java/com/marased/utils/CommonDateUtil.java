/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marased.utils;

import com.marased.model.Meetings;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author mahmoudyahia
 */
public class CommonDateUtil {

    public Date getCutomDateFormat(String dateFormat, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        Date customDate = null;
        try {
            //Date Fromat
            String customDateString = simpleDateFormat.format(date);
            customDate = simpleDateFormat.parse(customDateString);

        } catch (ParseException ex) {
            Logger.getLogger(CommonDateUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return customDate;
    }

    public Date getCutomTimeFormat(String timeFormat, Date date) {
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat(timeFormat);
        Date customTime = null;
        try {
            //Time Format
            String customTimeString = simpleTimeFormat.format(date);
            customTime = simpleTimeFormat.parse(customTimeString);
        } catch (ParseException ex) {
            Logger.getLogger(CommonDateUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return customTime;
    }
}
