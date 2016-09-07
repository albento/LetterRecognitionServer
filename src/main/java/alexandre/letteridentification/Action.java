/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alexandre.letteridentification;

import alexandre.letteridentification.service.Service;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author alexa
 */
public abstract class Action {
    
    Service serv;
    
    public abstract void execute(HttpServletRequest request, PrintWriter out);
    
    public void setServices(Service serv)
    {
        this.serv = serv;
    }
    
    public void send()
    {
        
    }
}