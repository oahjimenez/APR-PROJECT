/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.aguapotablerural.dao.repository;

import java.time.LocalDate;
import main.java.aguapotablerural.model.CostoMetroCubico;

/**
 *
 * @author carlo
 */
public interface CostoMetroCubicoRepository {
    public CostoMetroCubico getCostoMetroCubico(LocalDate fecha);
}
