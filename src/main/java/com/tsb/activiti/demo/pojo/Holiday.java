package com.tsb.activiti.demo.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author： tsb
 * @date： 2020/10/14
 * @description：
 * @modifiedBy：
 * @version: 1.0
 */
public class Holiday implements Serializable {
    private static final long serialVersionUID = 141328275365520700L;
    private Integer id;
    private String petitioner;  // 请假人
    private Integer num;        // 请假天数
    private Date beginDate;
    private Date endDate;
    private String reason;
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPetitioner() {
        return petitioner;
    }

    public void setPetitioner(String petitioner) {
        this.petitioner = petitioner;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
