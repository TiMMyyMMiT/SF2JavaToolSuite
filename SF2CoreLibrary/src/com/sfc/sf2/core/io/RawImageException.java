/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.core.io;

/**
 *
 * @author TiMMy
 */
public class RawImageException extends Exception {
    public RawImageException(String message) {
        super(message);
    }
    public RawImageException(Throwable cause) {
        super(cause);
    }
    public RawImageException(String message, Throwable cause) {
        super(message, cause);
    }
}
