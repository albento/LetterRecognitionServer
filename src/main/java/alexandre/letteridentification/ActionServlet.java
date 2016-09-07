/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alexandre.letteridentification;

import alexandre.letteridentification.service.Service;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Alexandre
 */
@WebServlet(urlPatterns = {"/ActionServlet"})
public class ActionServlet extends HttpServlet {
    
    public static String path = "C:\\Users\\alexa\\OneDrive\\Documents\\NetBeansProjects\\LetterRecognitionServer\\src\\main\\webapp";
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setCharacterEncoding("UTF-8");
        
        String todo = request.getParameter("action");

        Service serv = new Service();
        PrintWriter out = response.getWriter();
        response.setCharacterEncoding("UTF-8");
        
        
        System.out.println("Action!");
        
        if (request.getSession(false) == null && (request.getParameter("action").equals("training") || request.getParameter("action").equals("discard") || (request.getParameter("action").equals("list") && request.getParameter("type").equals("images_to_be_analyzed"))) && !request.getParameter("action").equals("login"))
        {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else
        {
            switch(todo)
            {
                case "training" :
                    System.out.println("Training action!");

                    TrainingAction acTraining = new TrainingAction();
                    acTraining.setServices(serv);
                    acTraining.execute(request, out);

                    break;

                case "upload" :
                    System.out.println("Upload action!");

                    UploadAction acUpload = new UploadAction();
                    acUpload.setServices(serv);
                    acUpload.execute(request, out);

                    break;

                case "test" :
                    System.out.println("Test action!");

                    TestAction acTest = new TestAction();
                    acTest.setServices(serv);
                    acTest.execute(request, out);

                    break;

                case "list" :
                    System.out.println("List action!");

                    ListAction acList = new ListAction();
                    acList.setServices(serv);
                    acList.execute(request, out);

                    break;

                case "discard" :
                    System.out.println("Discard action!");

                    DiscardAction acDiscard = new DiscardAction();
                    acDiscard.setServices(serv);
                    acDiscard.execute(request, out);

                    break;

                case "login" :
                    System.out.println("Login action!");

                    LoginAction acLogin = new LoginAction();
                    acLogin.setServices(serv);
                    acLogin.execute(request, out);

                    break;
            }
        }
    }
    

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ActionServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ActionServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
}
