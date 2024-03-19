package com.chyzman.component.fisics;

import org.ode4j.ode.internal.DxBody;
import org.ode4j.ode.internal.DxWorld;

public class PhysicsObject extends DxBody {
    public PhysicsObject(DxWorld w) {
        super(w);
    }
}