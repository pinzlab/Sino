/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.sino.dao.metodos;

import ec.edu.sino.accesodatos.DBConnection;
import ec.edu.sino.accesodatos.DBObject;
import ec.edu.sino.dao.contrato.IParcial;
import ec.edu.sino.negocios.entidades.Curso;
import ec.edu.sino.negocios.entidades.Materia;
import ec.edu.sino.negocios.entidades.Parcial;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/*
 * @author alexander
 */
public class MParcial implements IParcial {

    private String usuario;
    private String clave;

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
    public int insertar(Parcial parcial) throws Exception {
        int modificados = 0;
        DBConnection connection = new DBConnection(usuario, clave);
        String sql = "INSERT INTO public.parcial("
                + "quimestre, descripcion, tarea, individual, grupal, nota_parcial) "
                + "VALUES (?, ?, ?, ?, ?, ?);";
        List<DBObject> dbos = new ArrayList<>();
        dbos.add(new DBObject(1, parcial.getQuimestre().getId()));
        dbos.add(new DBObject(2, parcial.getDescripcion()));
        dbos.add(new DBObject(3, parcial.getTarea()));
        dbos.add(new DBObject(4, parcial.getIndividual()));
        dbos.add(new DBObject(5, parcial.getGrupal()));
        dbos.add(new DBObject(6, parcial.getNotaParcial()));
        if (parcial.getId() != 0) {
            sql = "INSERT INTO public.parcial("
                    + "quimestre, descripcion, tarea, individual, grupal, nota_parcial, id) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?);";
            dbos.add(new DBObject(7, parcial.getId()));
        }

        try {
            modificados = connection.executeCommand(sql, dbos);
        } catch (Exception e) {
            // throw e;
            System.err.println(e.getMessage());
        }
        return modificados;
    }

    @Override
    public int modificar(Parcial parcial) throws Exception {
        int modificados = 0;
        DBConnection connection = new DBConnection(usuario, clave);
        String sql = "UPDATE public.parcial set "
                + "quimestre=?, descripcion=?, tarea=?, individual=?, grupal=?, "
                + "nota_parcial=? WHERE id=?;";
        List<DBObject> dbos = new ArrayList<>();
        dbos.add(new DBObject(1, parcial.getQuimestre().getId()));
        dbos.add(new DBObject(2, parcial.getDescripcion()));
        dbos.add(new DBObject(3, parcial.getTarea()));
        dbos.add(new DBObject(4, parcial.getIndividual()));
        dbos.add(new DBObject(5, parcial.getGrupal()));
        dbos.add(new DBObject(6, parcial.getNotaParcial()));
        dbos.add(new DBObject(7, parcial.getId()));

        try {
            modificados = connection.executeCommand(sql, dbos);
        } catch (Exception e) {
            throw e;
        }
        return modificados;
    }

    @Override
    public int eliminar(Parcial parcial) throws Exception {
        int eliminados = 0;
        String sql = "DELETE FROM parcial WHERE id=?;";
        List<DBObject> dbos = new ArrayList<>();
        dbos.add(new DBObject(1, parcial.getId()));
        DBConnection con = new DBConnection(usuario, clave);
        try {
            eliminados = con.executeCommand(sql, dbos);
        } catch (Exception e) {
            throw e;
        }

        return eliminados;
    }

    @Override
    public Parcial obtener(int id) throws Exception {
        Parcial parcial = null;
        String sql = "SELECT id, quimestre, descripcion, tarea, individual, grupal, "
                + "promedio_evaluacion, nota_parcial, promedio FROM public.parcial "
                + "WHERE id=?;";
        List<DBObject> dbos = new ArrayList<>();
        dbos.add(new DBObject(1, id));
        DBConnection con = new DBConnection(usuario, clave);
        try {
            ResultSet rst = con.executeQuery(sql, dbos);
            while (rst.next()) {

                parcial = new Parcial();
                parcial.setId(rst.getInt("id"));
                parcial.setQuimestre(new MQuimestre(usuario, clave).obtener(rst.getInt("quimestre")));
                parcial.setDescripcion(rst.getString("descripcion"));
                parcial.setTarea(rst.getFloat("tarea"));
                parcial.setIndividual(rst.getFloat("individual"));
                parcial.setGrupal(rst.getFloat("grupal"));
                parcial.setPromedioEvaluacion(rst.getFloat("promedio_evaluacion"));
                parcial.setNotaParcial(rst.getFloat("nota_parcial"));
                parcial.setPromedio(rst.getFloat("promedio"));

            }

        } catch (SQLException e) {
            throw e;
        }
        return parcial;
    }

    @Override
    public ObservableList<Parcial> obtener(Curso curso, Materia materia, String quimestre, String descripcion) throws Exception {
        ObservableList<Parcial> lista = FXCollections.observableArrayList();
        String sql = " select p.id, p.quimestre,p.descripcion, p.tarea, p.individual,p.grupal,"
                + " 	p.promedio_evaluacion, p.nota_parcial,p.promedio from parcial p"
                + "	inner join quimestre q on p.quimestre = q.id"
                + "   	inner join ciclo ci on ci.id= q.ciclo"
                + "   	inner join alumno al on ci.alumno = al.cedula"
                + "	inner join asignatura_curso ac on  ac.id=ci.asignatura_curso"
                + "	where ac.curso=? and ac.materia=? and q.descripcion=? "
                + "	and p.descripcion=? order by al.apellido;";
        List<DBObject> dbos = new ArrayList<>();
        dbos.add(new DBObject(1, curso.getId()));
        dbos.add(new DBObject(2, materia.getId()));
        dbos.add(new DBObject(3, quimestre));
        dbos.add(new DBObject(4, descripcion));
        DBConnection con = new DBConnection(usuario, clave);
        try {
            ResultSet rst = con.executeQuery(sql,dbos);
            while (rst.next()) {

                Parcial parcial = new Parcial();
                parcial.setId(rst.getInt("id"));
                parcial.setQuimestre(new MQuimestre(usuario, clave).obtener(rst.getInt("quimestre")));
                parcial.setDescripcion(rst.getString("descripcion"));
                parcial.setTarea(rst.getFloat("tarea"));
                parcial.setIndividual(rst.getFloat("individual"));
                parcial.setGrupal(rst.getFloat("grupal"));
                parcial.setPromedioEvaluacion(rst.getFloat("promedio_evaluacion"));
                parcial.setNotaParcial(rst.getFloat("nota_parcial"));
                parcial.setPromedio(rst.getFloat("promedio"));
                lista.add(parcial);
            }

        } catch (SQLException e) {
            throw e;
        }
        return lista;
    }

}
