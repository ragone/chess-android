/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package com.chessApp.backend;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class MyServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().println("Please use the form to POST to this url");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        String name = req.getParameter("name");
        if (name == null) {
            RequestDispatcher dispatcher = getServletContext().
                    getRequestDispatcher("/requestFail.html");
            dispatcher.forward(req, resp);
        }

        if(name.equalsIgnoreCase("Introduction")){
            RequestDispatcher dispatcher = getServletContext().
                    getRequestDispatcher("/introduction.html");
            dispatcher.forward(req, resp);
        }
        else if(name.equalsIgnoreCase("Pawn") || name.equalsIgnoreCase("en passant")){
            RequestDispatcher dispatcher = getServletContext().
                    getRequestDispatcher("/pawn.html");
            dispatcher.forward(req, resp);
        }
        else if(name.equalsIgnoreCase("Knight")){
            RequestDispatcher dispatcher = getServletContext().
                    getRequestDispatcher("/knight.html");
            dispatcher.forward(req, resp);
        }
        else if(name.equalsIgnoreCase("Bishop")){
            RequestDispatcher dispatcher = getServletContext().
                    getRequestDispatcher("/bishop.html");
            dispatcher.forward(req, resp);
        }
        else if(name.equalsIgnoreCase("Rook") || (name.equalsIgnoreCase("Castle") || name.equalsIgnoreCase("Castling"))){
            RequestDispatcher dispatcher = getServletContext().
                    getRequestDispatcher("/rook.html");
            dispatcher.forward(req, resp);
        }
        else if(name.equalsIgnoreCase("Queen")){
            RequestDispatcher dispatcher = getServletContext().
                    getRequestDispatcher("/queen.html");
            dispatcher.forward(req, resp);
        }
        else if(name.equalsIgnoreCase("King") || name.equalsIgnoreCase("check") || name.equalsIgnoreCase("checkmate") || name.equalsIgnoreCase("check-mate")){
            RequestDispatcher dispatcher = getServletContext().
                    getRequestDispatcher("/king.html");
            dispatcher.forward(req, resp);
        }
        else{
            RequestDispatcher dispatcher = getServletContext().
                    getRequestDispatcher("/requestFail.html");
            dispatcher.forward(req, resp);
        }
    }
}
