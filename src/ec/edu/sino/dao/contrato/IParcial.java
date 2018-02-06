/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.sino.dao.contrato;

import ec.edu.sino.negocios.entidades.Parcial;
import javafx.collections.ObservableList;

/**
 *
 * @author alexander
 */
public interface IParcial {
    
    void loginAdmin();

    void loginProfesor();

    int insertar(Parcial parcial) throws Exception;

    int modificar(Parcial parcial) throws Exception;

    int eliminar(Parcial parcial) throws Exception;

    Parcial obtener(String id) throws Exception;

    ObservableList<Parcial> obtener() throws Exception;
}