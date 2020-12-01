package com.amaan.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 佛祖保佑，永无BUG
 *
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-11-30 17:35
 */
public class RunData implements Serializable {
    private int id;
    private double rotate;
    private double current;
    private int month;
    private String garageId;
    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getRotate() {
        return rotate;
    }

    public void setRotate(double rotate) {
        this.rotate = rotate;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getGarageId() {
        return garageId;
    }

    public void setGarageId(String garageId) {
        this.garageId = garageId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "RunData{" +
                "id=" + id +
                ", rotate=" + rotate +
                ", current=" + current +
                ", month=" + month +
                ", garageId='" + garageId + '\'' +
                ", date=" + date +
                '}';
    }
}
