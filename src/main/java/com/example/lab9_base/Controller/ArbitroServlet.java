package com.example.lab9_base.Controller;

import com.example.lab9_base.Bean.Arbitro;
import com.example.lab9_base.Dao.DaoArbitros;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ArbitroServlet", urlPatterns = {"/ArbitroServlet"})
public class ArbitroServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        DaoArbitros daoArbitros = new DaoArbitros();
        RequestDispatcher view;

        switch (action) {
            case "lista":
                ArrayList<Arbitro> lista = daoArbitros.listarArbitros();
                request.setAttribute("listaArbitros", lista);
                view = request.getRequestDispatcher("/arbitros/list.jsp");
                view.forward(request, response);
                break;

            case "crear":
                ArrayList<String> paises = new ArrayList<>();
                paises.add("Peru");
                paises.add("Chile");
                paises.add("Argentina");
                paises.add("Paraguay");
                paises.add("Uruguay");
                paises.add("Colombia");
                request.setAttribute("paises", paises);
                view = request.getRequestDispatcher("/arbitros/form.jsp");
                view.forward(request, response);
                break;

            case "borrar":
                int idArbitro = Integer.parseInt(request.getParameter("id"));
                daoArbitros.borrarArbitro(idArbitro);
                response.sendRedirect("ArbitroServlet?action=lista");
                break;

            default:
                response.sendRedirect("ArbitroServlet?action=lista");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "guardar" : request.getParameter("action");
        DaoArbitros daoArbitros = new DaoArbitros();
        RequestDispatcher view;

        switch (action) {
            case "guardar":
                String nombre = request.getParameter("nombre");
                String pais = request.getParameter("pais");

                if (nombre == null || pais == null || nombre.isEmpty() || pais.isEmpty()) {
                    request.setAttribute("error", "Ningún campo debe estar vacío");
                    ArrayList<String> paises = new ArrayList<>();
                    paises.add("Peru");
                    paises.add("Chile");
                    paises.add("Argentina");
                    paises.add("Paraguay");
                    paises.add("Uruguay");
                    paises.add("Colombia");
                    request.setAttribute("paises", paises);
                    view = request.getRequestDispatcher("/arbitros/form.jsp");
                    view.forward(request, response);
                } else {
                    Arbitro arbitro = new Arbitro();
                    arbitro.setNombre(nombre);
                    arbitro.setPais(pais);
                    daoArbitros.crearArbitro(arbitro);
                    response.sendRedirect("ArbitroServlet?action=lista");
                }
                break;

            case "buscar":
                String criterio = request.getParameter("tipo");
                String buscar = request.getParameter("buscar");
                ArrayList<Arbitro> resultados;
                if (criterio.equals("nombre")) {
                    resultados = daoArbitros.busquedaNombre(buscar);
                } else {
                    resultados = daoArbitros.busquedaPais(buscar);
                }
                request.setAttribute("listaArbitros", resultados);
                view = request.getRequestDispatcher("/arbitros/list.jsp");
                view.forward(request, response);
                break;
        }
    }
}


