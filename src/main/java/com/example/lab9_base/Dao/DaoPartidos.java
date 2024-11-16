package com.example.lab9_base.Dao;

import com.example.lab9_base.Bean.Partido;
import com.example.lab9_base.Bean.Seleccion;
import com.example.lab9_base.Bean.Arbitro;
import java.sql.*;
import java.util.ArrayList;

public class DaoPartidos extends DaoBase {

    public ArrayList<Partido> listaDePartidos() {
        ArrayList<Partido> partidos = new ArrayList<>();

        String sql = "SELECT p.numeroJornada, p.fecha, "
                + "sl.nombre AS seleccionLocal, sv.nombre AS seleccionVisitante, "
                + "e.nombre AS estadio, a.nombre AS arbitro "
                + "FROM partido p "
                + "JOIN seleccion sl ON p.seleccionLocal = sl.idSeleccion "
                + "JOIN seleccion sv ON p.seleccionVisitante = sv.idSeleccion "
                + "JOIN estadio e ON sl.estadio_idEstadio = e.idEstadio "
                + "JOIN arbitro a ON p.arbitro = a.idArbitro";

        try (Connection conn = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Partido partido = new Partido();
                partido.setNumeroJornada(rs.getInt("numeroJornada"));
                partido.setFecha(rs.getDate("fecha"));

                Seleccion seleccionLocal = new Seleccion();
                seleccionLocal.setNombre(rs.getString("seleccionLocal"));
                partido.setSeleccionLocal(seleccionLocal);

                Seleccion seleccionVisitante = new Seleccion();
                seleccionVisitante.setNombre(rs.getString("seleccionVisitante"));
                partido.setSeleccionVisitante(seleccionVisitante);

                Arbitro arbitro = new Arbitro();
                arbitro.setNombre(rs.getString("arbitro"));
                partido.setArbitro(arbitro);

                partidos.add(partido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return partidos;
    }

    public void crearPartido(Partido partido) {
        String sql = "INSERT INTO partido (seleccionLocal, seleccionVisitante, arbitro, fecha, numeroJornada) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, partido.getSeleccionLocal().getIdSeleccion());
            ps.setInt(2, partido.getSeleccionVisitante().getIdSeleccion());
            ps.setInt(3, partido.getArbitro().getIdArbitro());
            ps.setDate(4, new java.sql.Date(partido.getFecha().getTime()));
            ps.setInt(5, partido.getNumeroJornada());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean existePartido(Partido partido) {
        String sql = "SELECT COUNT(*) FROM partido WHERE seleccionLocal = ? AND seleccionVisitante = ? AND fecha = ?";
        try (Connection conn = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, partido.getSeleccionLocal().getIdSeleccion());
            ps.setInt(2, partido.getSeleccionVisitante().getIdSeleccion());
            ps.setDate(3, new java.sql.Date(partido.getFecha().getTime()));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}











