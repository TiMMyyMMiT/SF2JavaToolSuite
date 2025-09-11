/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.mapterrain;

/**
 *
 * @author TiMMy
 */
public class LandEffectMovementType {
    
    LandEffect[] landEffects;

    public LandEffectMovementType(LandEffect[] landEffects) {
        this.landEffects = landEffects;
    }

    public LandEffect[] getLandEffects() {
        return landEffects;
    }
}
