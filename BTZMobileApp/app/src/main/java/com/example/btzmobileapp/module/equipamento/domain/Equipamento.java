package com.example.btzmobileapp.module.equipamento.domain;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.btzmobileapp.module.user.domain.User;
import com.example.btzmobileapp.util.DateConverter;

import java.util.Date;

@Entity(
        tableName = "equipamentos",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE
        )
)
@TypeConverters(DateConverter.class)
public class Equipamento {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String name;
    private String code;
    private Date lastInventoryDate; // Mudança para Date
    private String qrCode;
    private Long userId;

    public Equipamento(String name, String code, Date lastInventoryDate, String qrCode, Long userId) {
        this.name = name;
        this.code = code;
        this.lastInventoryDate = lastInventoryDate;
        this.qrCode = qrCode;
        this.userId = userId;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getLastInventoryDate() {
        return lastInventoryDate;
    }

    public void setLastInventoryDate(Date lastInventoryDate) {
        this.lastInventoryDate = lastInventoryDate;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return name + " - " + code;
    }
}
