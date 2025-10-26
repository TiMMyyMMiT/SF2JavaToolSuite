/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.core.io.asm;

/**
 *
 * @author TiMMy
 */
public class AsmException extends Exception {
    public AsmException(String message) {
        super(message);
    }
    public AsmException(Throwable cause) {
        super(cause);
    }
    public AsmException(String message, Throwable cause) {
        super(message, cause);
    }
}
