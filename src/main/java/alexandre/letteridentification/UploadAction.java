/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alexandre.letteridentification;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author alexa
 */
public class UploadAction extends Action
{

    @Override
    public void execute(HttpServletRequest request, PrintWriter out)
    {
        try
        {
            Character letter = request.getParameter("letter").charAt(0);
            String image_src = request.getParameter("image");
            String image_computer = request.getParameter("image_computer");
            int rank = Integer.valueOf(request.getParameter("rank"));

            String path = ActionServlet.path + "\\Images to be analyzed\\" + letter + "\\Drawings";
            
            if (!new File(path).exists())
            {
                new File(path).mkdirs();
            }
            int index = 1;
            
            File[] files_list = new File(path).listFiles();
            
            Arrays.sort(files_list, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    try {
                        int i1 = Integer.parseInt(f1.getName().split("\\.")[0].split("-")[2].split("_")[0]);
                        int i2 = Integer.parseInt(f2.getName().split("\\.")[0].split("-")[2].split("_")[0]);
                        return i1 - i2;
                    } catch(NumberFormatException e) {
                        throw new AssertionError(e);
                    }
                }
            });
            
            if (files_list.length != 0)
            {
                index = Integer.valueOf(files_list[files_list.length - 1].getName().split("-")[2].split("_")[0]) + 1;
            }
            
            System.out.println(index);

            Base64.Decoder decoder = Base64.getDecoder();

            byte[] src = decoder.decode(image_src);

            InputStream in = new ByteArrayInputStream(src);
            BufferedImage bImageFromConvert;

            bImageFromConvert = ImageIO.read(in);
            
            ImageIO.write(bImageFromConvert, "PNG", new File(path + "\\DRAWING-" + letter + "-" + index + "_" + rank + ".png"));
            
            path = ActionServlet.path + "\\Images to be analyzed\\" + letter + "\\Computer Vision";
            
            if (!new File(path).exists())
            {
                new File(path).mkdirs();
            }
            index = 1;
            
            files_list = new File(path).listFiles();
            
            Arrays.sort(files_list, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    try {
                        int i1 = Integer.parseInt(f1.getName().split("\\.")[0].split("-")[2].split("_")[0]);
                        int i2 = Integer.parseInt(f2.getName().split("\\.")[0].split("-")[2].split("_")[0]);
                        return i1 - i2;
                    } catch(NumberFormatException e) {
                        throw new AssertionError(e);
                    }
                }
            });
            
            if (files_list.length != 0)
            {
                index = Integer.valueOf(files_list[files_list.length - 1].getName().split("-")[2].split("_")[0]) + 1;
            }

            src = decoder.decode(image_computer);

            in = new ByteArrayInputStream(src);

            bImageFromConvert = ImageIO.read(in);
            
            ImageIO.write(bImageFromConvert, "PNG", new File(path + "\\CV-" + letter + "-" + index + "_" + rank + ".png"));
        }
        catch (IOException ex)
        {
            Logger.getLogger(UploadAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
