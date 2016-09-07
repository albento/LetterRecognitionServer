/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alexandre.letteridentification;

import alexandre.letteridentification.model.Statistics;
import alexandre.letteridentification.util.NeuralNetwork;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author alexa
 */
public class TrainingAction extends Action
{

    @Override
    public void execute(HttpServletRequest request, PrintWriter out)
    {
        try
        {
            String image = request.getParameter("image");
            
            Character letter = image.split("-")[1].charAt(0);
            
            int index = Integer.valueOf(image.split("-")[2].split("_")[0]);
            int rank = Integer.valueOf(image.split("-")[2].split("_")[1].split("\\.")[0]);
            
            Statistics s = serv.findStatisticsByLetter(letter);
            
            if (s == null)
            {
                s = serv.createStatistics(letter);
            }
            
            if (rank == 1)
            {
                serv.updateStatistics(s, s.getNumber_first() + 1, s.getNumber_second(), s.getNumber_third(), s.getNumber_more());
            }
            else if (rank == 2)
            {
                serv.updateStatistics(s, s.getNumber_first(), s.getNumber_second() + 1, s.getNumber_third(), s.getNumber_more());
            }
            else if (rank == 3)
            {
                serv.updateStatistics(s, s.getNumber_first(), s.getNumber_second(), s.getNumber_third() + 1, s.getNumber_more());
            }
            else
            {
                serv.updateStatistics(s, s.getNumber_first(), s.getNumber_second(), s.getNumber_third(), s.getNumber_more() + 1);
            }
            
            int new_index = 1;
            
            File[] files_list = new File(ActionServlet.path + "\\Training\\" + letter).listFiles();

            Arrays.sort(files_list, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    try {
                        int i1 = Integer.parseInt(f1.getName().split("\\.")[0]);
                        int i2 = Integer.parseInt(f2.getName().split("\\.")[0]);
                        return i1 - i2;
                    } catch(NumberFormatException e) {
                        throw new AssertionError(e);
                    }
                }
            });
            
            if (files_list.length != 0)
            {
                new_index = Integer.valueOf(files_list[files_list.length - 1].getName().split("\\.")[0]) + 1;
            }
            
            Files.move(new File(ActionServlet.path + "\\Images to be analyzed\\" + letter + "\\Drawings\\" + image).toPath(), new File(ActionServlet.path + "\\Training\\" + letter + "\\" + new_index + ".png").toPath());
            Files.delete(new File(ActionServlet.path + "\\Images to be analyzed\\" + letter + "\\Computer Vision\\CV-" + letter + "-" + index + "_" + rank + ".png").toPath());
            
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
            
            BufferedImage im = ImageIO.read(new File(ActionServlet.path + "\\Training\\" + letter + "\\" + new_index + ".png"));
            
            Double[] input = serv.getCenteredImage(im);
            
            Double[] target = new Double[NeuralNetwork.NB_OUTPUT];
            
            int index_char = (int)letter;
            
            for (int j = 0; j < NeuralNetwork.NB_OUTPUT; j++)
            {
                if (j == index_char - 65)
                {
                    target[j] = 1.;
                }
                else
                {
                    target[j] = 0.;
                }
            }
            
            serv.trainNetwork(input, target);
        }
        catch (IOException ex)
        {
            Logger.getLogger(TrainingAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Throwable ex) {
            Logger.getLogger(TrainingAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

