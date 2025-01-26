package com.example.btzmobileapp.module.equipamento.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.btzmobileapp.module.equipamento.domain.Equipamento;

import java.util.List;

@Dao
public interface EquipamentoDao {
    @Insert
    long insert(Equipamento equipamento); // Retorna o ID do equipamento inserido

    @Update
    void update(Equipamento equipamento);

    @Delete
    void delete(Equipamento equipamento);

    @Query("SELECT * FROM equipamentos WHERE id = :id")
    Equipamento getEquipamentoById(Long id);

    @Query("SELECT * FROM equipamentos WHERE userId = :userId")
    List<Equipamento> getEquipamentosByUserId(Long userId);

    @Query("SELECT * FROM equipamentos WHERE qrCode = :qrCode")
    Equipamento getEquipamentoByQrCode(String qrCode);

    @Query("SELECT * FROM equipamentos")
    List<Equipamento> getAllEquipamentos();
}