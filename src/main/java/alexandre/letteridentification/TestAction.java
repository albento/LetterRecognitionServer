/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alexandre.letteridentification;

import alexandre.letteridentification.util.NeuralNetwork;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author alexa
 */
public class TestAction extends Action
{

    @Override
    public void execute(HttpServletRequest request, PrintWriter out)
    {
        
        try
        {
            String src_image = request.getParameter("image");

            Base64.Decoder decoder = Base64.getDecoder();

            byte[] src = decoder.decode(src_image);

            InputStream in = new ByteArrayInputStream(src);
            
            BufferedImage image = ImageIO.read(in);
            
            Double[] input = serv.getCenteredImage(image);
            
            HashMap<Character, Double> res = new LinkedHashMap();
            
            Double[] results = serv.testNetwork(input);
            
            for (int i = 65; i <= 90; i++)
            {
                Character letter = (char)i;
                res.put(letter, Double.valueOf((int)((results[i-65]) * 10000)) / 100);
            }
            
            JsonArray results_obj = new JsonArray();
            
            for (Character l : res.keySet())
            {
                JsonObject el = new JsonObject();
                el.addProperty("letter", l);
                el.addProperty("value", res.get(l));
                
                results_obj.add(el);
            }
            
            JsonObject computer_vision = new JsonObject();
            
            JsonArray points = new JsonArray();
            
            for (int i = 0; i < Math.sqrt(NeuralNetwork.NB_INPUT); i++)
            {
                for (int j = 0; j < Math.sqrt(NeuralNetwork.NB_INPUT); j++)
                {
                    if (input[(int)(i * Math.sqrt(NeuralNetwork.NB_INPUT) + j)].equals(1.))
                    {
                        JsonObject p = new JsonObject();
                        p.addProperty("x", i);
                        p.addProperty("y", j);
                        
                        points.add(p);
                    }
                }
            }
            
            computer_vision.add("points", points);
            computer_vision.addProperty("dimension", (int)Math.sqrt(NeuralNetwork.NB_INPUT));
            
            JsonObject container = new JsonObject();
            container.add("results", results_obj);
            container.add("computer_vision", computer_vision);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(container);
            out.println(json);
        }
        catch (IOException ex)
        {
            Logger.getLogger(TestAction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Throwable ex)
        {
            Logger.getLogger(TestAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
