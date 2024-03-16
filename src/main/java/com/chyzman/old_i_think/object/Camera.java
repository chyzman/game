package com.chyzman.old_i_think.object;

import com.chyzman.old_i_think.Game;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class Camera extends GameObject {
    public Vector3d deltaPos = new Vector3d();
    public Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
    public Vector3f cameraUp    = new Vector3f(0.0f, 1.0f,  0.0f);
    public float pitch, yaw;

    @Override
    public void update() {
        Matrix4f transform = Game.window.getViewMatrix();
//        pos(pos.add(deltaPos));
        var cameraPos = new Vector3f((float) pos.x, (float) pos.y, (float) pos.z);

        var view = new Matrix4f();

        Vector3f front = new Vector3f();
        front.x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        front.y = (float) Math.sin(Math.toRadians(pitch));
        front.z = (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        cameraFront = front.normalize();
        transform.set(getViewMatrix());
//         also re-calculate the Right and Up vector
//        Right = glm::normalize(glm::cross(Front, WorldUp));  // normalize the vectors, because their length gets closer to 0 the more you look up or down which results in slower movement.
//        Up    = glm::normalize(glm::cross(Right, Front));
    }



    public Matrix4f getViewMatrix() {
        var cameraPos = new Vector3f((float) pos.x, (float) pos.y, (float) pos.z);
        return new Matrix4f().lookAt(cameraPos, new Vector3f(cameraPos).add(cameraFront), cameraUp);
    }
}