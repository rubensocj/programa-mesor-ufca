/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package r;

import org.rosuda.JRI.RMainLoopCallbacks;
import org.rosuda.JRI.Rengine;

/**
 * RConsole.java
 *
 * @version 1.0 22/10/2017
 * @author Rubens Júnior
 */
public class RConsole implements RMainLoopCallbacks {

    // -------------------------------------------------------------------------
    // Métodos.
    // -------------------------------------------------------------------------

    @Override
    public void rWriteConsole(Rengine rngn, String string, int i) {
    }

    @Override
    public void rBusy(Rengine rngn, int i) {
    }

    @Override
    public String rReadConsole(Rengine rngn, String string, int i) {
        return ".";
    }

    @Override
    public void rShowMessage(Rengine rngn, String string) {
    }

    @Override
    public String rChooseFile(Rengine rngn, int i) {
        return ".";
    }

    @Override
    public void rFlushConsole(Rengine rngn) {
    }

    @Override
    public void rSaveHistory(Rengine rngn, String string) {
    }

    @Override
    public void rLoadHistory(Rengine rngn, String string) {
    }
    
}