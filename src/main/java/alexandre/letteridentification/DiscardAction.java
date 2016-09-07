/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alexandre.letteridentification;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author alexa
 */
public class DiscardAction extends Action
{

    @Override
    public void execute(HttpServletRequest request, PrintWriter out)
    {
        try
        {
            String image = request.getParameter("image");
            image = image.split("-")[1] + "-" + image.split("-")[2];
            
            Character letter = image.charAt(0);
            
            Files.delete(new File(ActionServlet.path + "\\Images to be analyzed\\" + letter + "\\Drawings\\DRAWING-" + image).toPath());
            Files.delete(new File(ActionServlet.path + "\\Images to be analyzed\\" + letter + "\\Computer Vision\\CV-" + image).toPath());
            
            if (new File(ActionServlet.path + "\\Images to be analyzed\\" + letter + "\\Drawings").listFiles().length == 0)
            {
                File dir = new File(ActionServlet.path + "\\Images to be analyzed\\" + letter);
                String[] entries = dir.list();
                for (String e : entries)
                {
                    File f = new File(dir.getPath(), e);
                    f.delete();
                }
                dir.delete();
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(UploadAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
