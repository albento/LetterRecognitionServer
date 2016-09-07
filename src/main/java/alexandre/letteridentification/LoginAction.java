package alexandre.letteridentification;

import alexandre.letteridentification.model.Administrator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alexandre
 */
public class LoginAction extends Action
{

    @Override
    public void execute(HttpServletRequest request, PrintWriter out)
    {
        try
        {            
            HttpSession session = request.getSession();

            String email = request.getParameter("email");
            String password = request.getParameter("password");

            Administrator a = serv.findAdministratorByEmail(email);
            
            if (a != null)
            {
                if (password.equals(a.getPassword()))
                {
                    session.setAttribute("administrator", a);
                    
                    JsonObject result = new JsonObject();
                    
                    result.addProperty("administrator", a.getId());
                    result.addProperty("link", "administration.html");
                    
                    JsonObject container = new JsonObject();
                    container.add("result", result);
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String json = gson.toJson(container);
                    out.println(json);
                }
                else
                {
                    session.invalidate();
                }
            }
            else
            {
                session.invalidate();
            }
        }
        catch (Throwable ex)
        {
            Logger.getLogger(LoginAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
