package com.chyzman.render.shader;

import org.joml.Matrix4f;

public class ShaderTextured extends Shader{

	private int locationProjection;
	
	public ShaderTextured() {
		super("Textured.vert", "Textured.frag");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "uvs");
	}

	public void setProjection(Matrix4f mat) {
		this.loadMatrix(locationProjection, mat);
	}

	@Override
	protected void getAllUniformLocations() {
		this.locationProjection = this.getUniformLocation("projection");
	}
}