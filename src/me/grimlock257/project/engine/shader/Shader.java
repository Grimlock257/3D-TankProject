package me.grimlock257.project.engine.shader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import me.grimlock257.project.engine.math.Matrix4f;
import me.grimlock257.project.engine.math.Vector2f;
import me.grimlock257.project.engine.math.Vector3f;
import me.grimlock257.project.utils.FileUtils;

/** Creates a shader program to be used within OpenGL */
public abstract class Shader {
	private int progID, vertID, fragID;

	// Read in the shader files and create them as a program
	public Shader(String vertPath, String fragPath) {
		progID = GL20.glCreateProgram();
		vertID = compileShaderObject(vertPath, GL20.GL_VERTEX_SHADER);
		fragID = compileShaderObject(fragPath, GL20.GL_FRAGMENT_SHADER);

		GL20.glAttachShader(progID, vertID);
		GL20.glAttachShader(progID, fragID);
		bindAttributes();
		GL20.glLinkProgram(progID);
		GL20.glValidateProgram(progID);
		getUniformLocations();
	}

	/** Load up shader files */
	private static int compileShaderObject(String file, int shaderType) {
		String shaderSource = FileUtils.loadFileAsString(file);

		int shadID = GL20.glCreateShader(shaderType);
		GL20.glShaderSource(shadID, shaderSource);
		GL20.glCompileShader(shadID);

		// See if there was an error with the compile, if so print the error to the console and exit
		if (GL20.glGetShaderi(shadID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println("SHADER: Could not compile shader: " + file);
			System.out.println(GL20.glGetShaderInfoLog(shadID, 500));
			System.exit(1);
		}

		return shadID;
	}

	/** Tell OpenGL to use this shader program */
	public void enable() {
		GL20.glUseProgram(progID);
	}

	/** Tell OpenGL to stop using this shader program */
	public void disable() {
		GL20.glUseProgram(0);
	}

	/** Memory Management. Safely remove the shaders from the program, and delete the program */
	public void delete() {
		disable();
		GL20.glDetachShader(progID, vertID);
		GL20.glDetachShader(progID, fragID);
		GL20.glDeleteShader(vertID);
		GL20.glDeleteShader(fragID);
		GL20.glDeleteProgram(progID);
	}

	/** Get all of the uniform locations for the shader in question */
	protected abstract void getUniformLocations();

	/** Get a uniform location from the current shader program */
	protected int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(progID, uniformName);
	}

	/** Bind all of the attributes so our shader programs can use them */
	protected abstract void bindAttributes();

	/** Bind an attribute to the shader program */
	protected void bindAttribute(int attributeNumber, String attributeName) {
		GL20.glBindAttribLocation(progID, attributeNumber, attributeName);
	}

	/** Load a Vector2f to the uniform location */
	protected void setUniformVec2f(int location, Vector2f vector) {
		GL20.glUniform2f(location, vector.x, vector.y);
	}

	/** Load a Vector3f to the uniform location */
	protected void setUniformVec3f(int location, Vector3f vector) {
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}

	/** Load a Matrix4f to the uniform location */
	protected void setUniformMat4f(int location, Matrix4f matrix) {
		GL20.glUniformMatrix4fv(location, false, matrix.toFloatBuffer());
	}
}