package com.example.lab9_base.Controller;

import com.example.lab9_base.Bean.Partido;
import com.example.lab9_base.Bean.Seleccion;
import com.example.lab9_base.Bean.Arbitro;
import com.example.lab9_base.Dao.DaoPartidos;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(name = "PartidoServlet", urlPatterns = {"/PartidoServlet", ""})
public class PartidoServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "guardar" : request.getParameter("action");
        DaoPartidos daoPartidos = new DaoPartidos();
        RequestDispatcher view;
        switch (action) {
            case "guardar":
                try {
                    int numeroJornada = Integer.parseInt(request.getParameter("numeroJornada"));
                    String fechaStr = request.getParameter("fecha");
                    int idLocal = Integer.parseInt(request.getParameter("seleccionLocal"));
                    int idVisitante = Integer.parseInt(request.getParameter("seleccionVisitante"));
                    int idArbitro = Integer.parseInt(request.getParameter("arbitro"));

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date fecha = dateFormat.parse(fechaStr);

                    if (idLocal == idVisitante) {
                        request.setAttribute("error", "La selección visitante no puede ser igual a la selección local.");
                        view = request.getRequestDispatcher("partidos/form.jsp");
                        view.forward(request, response);
                        return;
                    }

                    Partido partido = new Partido();
                    partido.setNumeroJornada(numeroJornada);
                    partido.setFecha(fecha);

                    Seleccion seleccionLocal = new Seleccion();
                    seleccionLocal.setIdSeleccion(idLocal);
                    partido.setSeleccionLocal(seleccionLocal);

                    Seleccion seleccionVisitante = new Seleccion();
                    seleccionVisitante.setIdSeleccion(idVisitante);
                    partido.setSeleccionVisitante(seleccionVisitante);

                    Arbitro arbitro = new Arbitro();
                    arbitro.setIdArbitro(idArbitro);
                    partido.setArbitro(arbitro);

                    // Check if partido already exists
                    if (daoPartidos.existePartido(partido)) {
                        request.setAttribute("error", "El partido ya existe.");
                        view = request.getRequestDispatcher("partidos/form.jsp");
                        view.forward(request, response);
                    } else {
                        // Save partido and redirect to list
                        daoPartidos.crearPartido(partido);
                        response.sendRedirect("PartidoServlet?action=lista");
                    }
                } catch (Exception e) {
                    request.setAttribute("error", "Datos inválidos.");
                    view = request.getRequestDispatcher("partidos/form.jsp");
                    view.forward(request, response);
                }
                break;

            default:
                response.sendRedirect("PartidoServlet?action=lista");
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        DaoPartidos daoPartidos = new DaoPartidos();
        RequestDispatcher view;

        switch (action) {
            case "lista":
                // Load and set partido list attribute
                request.setAttribute("listaPartidos", daoPartidos.listaDePartidos());
                view = request.getRequestDispatcher("index.jsp");
                view.forward(request, response);
                break;

            case "crear":
                // Forward to form for creating a partido
                view = request.getRequestDispatcher("partidos/form.jsp");
                view.forward(request, response);
                break;

            default:
                // Redirect to the list as default action
                response.sendRedirect("PartidoServlet?action=lista");
                break;
        }
    }
}


