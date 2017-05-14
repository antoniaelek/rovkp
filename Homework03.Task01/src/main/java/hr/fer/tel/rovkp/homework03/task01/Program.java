/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rovkp.homework03.task01;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aelek
 */
public class Program {
    public static void main(String[] args) {
        try {
            String path = "/home/aelek/Documents/Projects/rovkp/Homework03.Task01/src/main/resources/jester_items.dat";
            Map<Integer, String> map = TextCollection.parseInputFile(path);
            System.out.println(map.getOrDefault(1, "Element with id 1 not found"));
        
        } catch (IOException ex) {
            Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
