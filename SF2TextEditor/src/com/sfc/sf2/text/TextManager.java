/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.text;

import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.text.io.AsmManager;
import com.sfc.sf2.text.io.TxtManager;
import com.sfc.sf2.text.io.DisassemblyManager;
import java.nio.file.Path;

/**
 *
 * @author wiz
 */
public class TextManager extends AbstractManager {
    
    private String[] gamescript;

    @Override
    public void clearData() {
        gamescript = null;
    }
       
    public String[] importDisassembly(Path basePath) {
        Console.logger().finest("ENTERING importDisassembly");
        gamescript = DisassemblyManager.importDisassembly(basePath);
        Console.logger().finest("EXITING importDisassembly");
        return gamescript;
    }
    
    public void exportDisassembly(Path basePath) {
        Console.logger().finest("ENTERING exportDisassembly");
        DisassemblyManager.exportDisassembly(gamescript, basePath.toString());
        Console.logger().finest("EXITING exportDisassembly");       
    }
    
    public String[] importTxt(Path filepath) {
        Console.logger().finest("ENTERING importTxt");
        gamescript = TxtManager.importTxt(filepath.toString());
        Console.logger().finest("EXITING importTxt");
        return gamescript;
    }
    
    public void exportTxt(Path filepath) {
        Console.logger().finest("ENTERING exportTxt");
        TxtManager.exportTxt(gamescript, filepath.toString());
        Console.logger().finest("EXITING exportTxt");     
    }
       
    public String[] importAsm(Path path) {
        Console.logger().finest("ENTERING importAsm");
        gamescript = AsmManager.importAsm(path.toString(), gamescript);
        Console.logger().finest("EXITING importAsm");
        return gamescript;
    }
    
    public String[] getGameScript() {
        return gamescript;
    }
    
    public void setGameScript(String[] gamescript) {
        this.gamescript = gamescript;
    }
}
