/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.sino.dao.metodos;

import ec.edu.sino.accesodatos.DBConnection;
import ec.edu.sino.accesodatos.DBObject;
import ec.edu.sino.dao.contrato.IPeriodo;
import ec.edu.sino.negocios.entidades.Periodo;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author alexander
 */
public class MPeriodo implements IPeriodo {

    private String usuario;
    private String clave;

    public MPeriodo() {
    }

    public MPeriodo(String usuario, String clave) {
        this.usuario = usuario;
        this.clave = clave;
    }

    
    @Override
    public void loginAdmin() {
        usuario = "admin";
        clave = "adm!np4$";
    }

    @Override
    public void loginProfesor() {
        usuario = "profesor";
        clave = "prop4$";
    }

    @Override
    public int insertar(Periodo periodo) throws Exception {
        int modificados = 0;
        DBConnection connection = new DBConnection(usuario, clave);
        String sql = "INSERT INTO public.periodo"
                + "(fecha_inicio, fecha_fin, director, subdirector, coordinador) "
                + "VALUES (?, ?, ?, ?, ?);";
        List<DBObject> dbos = new ArrayList<>();
        dbos.add(new DBObject(1, periodo.getFechaInicio()));
        dbos.add(new DBObject(2, periodo.getFechaFin()));
        dbos.add(new DBObject(3, periodo.getDirector()));
        dbos.add(new DBObject(4, periodo.getSubdirector()));
        dbos.add(new DBObject(5, periodo.getCoordinador()));

        try {
            modificados = connection.executeCommand(sql, dbos);
        } catch (Exception e) {
            throw e;
        }
        return modificados;
    }

    @Override
    public int modificar(Periodo periodo) throws Exception {
        int modificados = 0;
        DBConnection connection = new DBConnection(usuario, clave);
        String sql = "UPDATE public.periodo SET "
                + "fecha_inicio=?, fecha_fin=?, director=?, subdirector=?, coordinador=? "
                + "WHERE id=?;";
        List<DBObject> dbos = new ArrayList<>();
        dbos.add(new DBObject(1, periodo.getFechaInicio()));
        dbos.add(new DBObject(2, periodo.getFechaFin()));
        dbos.add(new DBObject(3, periodo.getDirector()));
        dbos.add(new DBObject(4, periodo.getSubdirector()));
        dbos.add(new DBObject(5, periodo.getCoordinador()));

        try {
            modificados = connection.executeCommand(sql, dbos);
        } catch (Exception e) {
            throw e;
        }
        return modificados;
    }

    @Override
    public int eliminar(Periodo periodo) throws Exception {
        int eliminados = 0;
        String sql = "DELETE FROM public.periodo WHERE id=?;";
        List<DBObject> dbos = new ArrayList<>();
        dbos.add(new DBObject(1, periodo.getId()));
        DBConnection con = new DBConnection(usuario, clave);
        try {
            eliminados = con.executeCommand(sql, dbos);
        } catch (Exception e) {
            throw e;
        }

        return eliminados;
    }

    @Override
    public Periodo obtener(int id) throws Exception {
        Periodo periodo = null;
        String sql = "SELECT id, fecha_inicio, fecha_fin, director, subdirector, coordinador "
                + "FROM public.periodo where id=?;";
        List<DBObject> dbos = new ArrayList<>();
        dbos.add(new DBObject(1, id));
        DBConnection con = new DBConnection(usuario, clave);
        try {
            ResultSet rst = con.executeQuery(sql, dbos);
            while (rst.next()) {
                periodo = new Periodo();
                periodo.setId(rst.getInt("id"));
                periodo.setFechaInicio(rst.getDate("fecha_inicio"));
                periodo.setFechaFin(rst.getDate("fecha_fin"));
                periodo.setDirector(rst.getString("director"));
                periodo.setSubdirector(rst.getString("subdirector"));
                periodo.setCoordinador("coordinador");

            }

        } catch (SQLException e) {
            throw e;
        }
        return periodo;
    }

    @Override
    public ObservableList<Periodo> obtener() throws Exception {
        ObservableList<Periodo> lista = FXCollections.observableArrayList();
        String sql = "SELECT id, fecha_inicio, fecha_fin, director, subdirector, coordinador "
                + "FROM public.periodo ORDER by fecha_inicio desc;";
        DBConnection con = new DBConnection(usuario, clave);
        try {
            ResultSet rst = con.executeQuery(sql);
            while (rst.next()) {

                Periodo periodo = new Periodo();
                periodo.setId(rst.getInt("id"));
                periodo.setFechaInicio(rst.getDate("fecha_inicio"));
                periodo.setFechaFin(rst.getDate("fecha_fin"));
                periodo.setDirector(rst.getString("director"));
                periodo.setSubdirector(rst.getString("subdirector"));
                periodo.setCoordinador("coordinador");
                lista.add(periodo);
            }

        } catch (SQLException e) {
            throw e;
        }
        return lista;
    }

}