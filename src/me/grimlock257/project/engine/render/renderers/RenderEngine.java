package me.grimlock257.project.engine.render.renderers;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import me.grimlock257.project.engine.font.FontEngine;
import me.grimlock257.project.engine.math.Matrix4f;
import me.grimlock257.project.engine.physics.rendering.Collider;
import me.grimlock257.project.engine.render.PolygonMode;
import me.grimlock257.project.engine.render.ProjectionType;
import me.grimlock257.project.engine.shader.shaders.ColliderShader;
import me.grimlock257.project.engine.shader.shaders.EntityShader;
import me.grimlock257.project.engine.shader.shaders.FontShader;
import me.grimlock257.project.engine.shader.shaders.GUIShader;
import me.grimlock257.project.engine.texture.GUITexture;
import me.grimlock257.project.entities.Entity;
import me.grimlock257.project.entities.TurretBarrel;
import me.grimlock257.project.entities.TurretBody;
import me.grimlock257.project.entities.world.Camera;
import me.grimlock257.project.entities.world.Light;
import me.grimlock257.project.models.ModelTextured;
import me.grimlock257.project.terrains.Terrain;
import me.grimlock257.project.utils.MatrixUtils;
import me.grimlock257.project.utils.managers.DisplayManager;

/** This is the main render class. Everything that needs to be rendered is passed to this class, this will then send
 * those items to the correct renderer class to render that type of thing */
public class RenderEngine {
	// Variables relating to projection
	private static final float FOV = 70f;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 10000f;

	// Variables for projection information
	private ProjectionType projectionType;
	private Matrix4f projectionMatrix;

	// Set up the entity renderer/shader
	private EntityShader entityShader = new EntityShader();
	private EntityRenderer entityRenderer = new EntityRenderer(entityShader);

	// Set up the collider renderer/shader
	private ColliderShader colliderShader = new ColliderShader();
	private ColliderRenderer colliderRenderer = new ColliderRenderer(colliderShader);

	// Set up the terrain renderer
	private TerrainRenderer terrainRenderer = new TerrainRenderer(entityShader);

	// Set up the GUI renderer/shader
	private GUIShader guiShader = new GUIShader();
	private GUIRenderer guiRenderer = new GUIRenderer(guiShader);

	// Set up the Font renderer/shader
	private FontShader fontShader = new FontShader();
	private FontRenderer fontRenderer = new FontRenderer(fontShader);

	// Whether to renderer colliders or not
	private boolean renderColliders = true;

	// Set up maps/lists to store the items that need to be rendered
	private HashMap<ModelTextured, ArrayList<Entity>> entities = new HashMap<ModelTextured, ArrayList<Entity>>();
	private ArrayList<Terrain> terrains = new ArrayList<Terrain>();
	private ArrayList<Collider> colliders = new ArrayList<Collider>();
	private ArrayList<GUITexture> guis = new ArrayList<GUITexture>();

	public RenderEngine() {
		enableFaceCulling();
		enableDepthTest();
		setClearColor(1f, 1f, 1f);

		updateProjectionType(ProjectionType.PERSPECTIVE);
	}

	/** Render everything that needs to be rendered for the current update */
	public void render(Light light, Camera camera) {
		// Prepare the renderer to render
		reset();

		// The structure for the following blocks of code are: Starting the shader, setting uniform values, calling the
		// render method in the appropriate renderer, then stopping the shader

		entityShader.enable();
		entityShader.setUniformProjectionMat4f(projectionMatrix);
		entityShader.setUniformViewMat4f(camera);
		entityShader.setUniformLight(light);
		entityRenderer.render(entities);
		terrainRenderer.render(terrains);
		entityShader.disable();

		if (renderColliders) {
			colliderShader.enable();
			colliderShader.setUniformProjectionMat4f(projectionMatrix);
			colliderShader.setUniformViewMat4f(camera);
			disableFaceCulling();
			setPolygonMode(PolygonMode.LINE);
			colliderRenderer.render(colliders);
			setPolygonMode(PolygonMode.FILL);
			enableFaceCulling();
			colliderShader.disable();
		}

		guiShader.enable();
		guiRenderer.render(guis);
		guiShader.disable();

		fontShader.enable();
		enableBlend();
		disableDepthTest();
		fontRenderer.render(FontEngine.getTexts());
		disableBlend();
		enableDepthTest();
		fontShader.disable();

		// Empty the lists so that they don't build up and cause lag
		entities.clear();
		terrains.clear();
		colliders.clear();
		guis.clear();
	}

	/** Add an entity to the list of entities to be rendered this update */
	public void addEntity(Entity entity) {
		ModelTextured entityModel = entity.getModel();
		ArrayList<Entity> entitySet = entities.get(entityModel);

		// if (renderColliders)
		if (renderColliders && !(entity instanceof TurretBody || entity instanceof TurretBarrel))
			colliders.add(entity.getCollider());

		if (entitySet != null) {
			entitySet.add(entity);
		} else {
			ArrayList<Entity> newEntitySet = new ArrayList<Entity>();
			newEntitySet.add(entity);
			entities.put(entityModel, newEntitySet);
		}
	}

	/** Add a terrain to the list of terrains to be rendered this update */
	public void addTerrain(Terrain terrain) {
		terrains.add(terrain);
	}

	/** Add a GUI to the list of GUIs to be rendered this update */
	public void addGUI(GUITexture... guis) {
		for (GUITexture gui : guis) {
			this.guis.add(gui);
		}
	}

	/** Reset the context, making it blank, so that it is ready to be rendered to */
	private void reset() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public void delete() {
		entityShader.delete();
		colliderShader.delete();
		fontShader.delete();
		guiShader.delete();
	}

	/** Set the clear colour for OpenGL */
	protected void setClearColor(float r, float g, float b) {
		glClearColor(r, g, b, 1);
	}

	/** Enable face culling so faces not visible to the camera aren't rendered, improving performance and render time */
	protected void enableFaceCulling() {
		// Tell OpenGL which faces to cull
		GL11.glCullFace(GL_BACK);

		GL11.glEnable(GL11.GL_CULL_FACE);
	}

	/** Disable face culling, meaning all faces will be rendered */
	protected void disableFaceCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	/** Enable depth test so that OpenGL renders things in the correct order */
	protected void enableDepthTest() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	/** Disable depth test, OpenGL will render things in the order they are given to OpenGL */
	protected void disableDepthTest() {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

	/** Enable alpha blending, allows OpenGL to use transparency when drawing textures */
	protected void enableBlend() {
		// Tell OpenGL how to blend
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glEnable(GL11.GL_BLEND);
	}

	/** Disables blending, alpha values will be ignored by OpenGL */
	protected void disableBlend() {
		GL11.glDisable(GL11.GL_BLEND);
	}

	/** Set the polygon mode for OpenGL to draw with */
	protected void setPolygonMode(PolygonMode mode) {
		switch (mode) {
			case FILL:
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
				break;
			case LINE:
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
				break;
		}
	}

	/** Update the projection type */
	public void updateProjectionType(ProjectionType projectionType) {
		this.projectionType = projectionType;

		switch (this.projectionType) {
			case ORTHOGRAPHIC:
				this.projectionMatrix = MatrixUtils.createOrthographicMatrix(-16f, 16f, -9f, 9f, NEAR_PLANE, FAR_PLANE);
				break;
			case PERSPECTIVE:
				this.projectionMatrix = MatrixUtils.createPerspectiveMatrix(DisplayManager.getVidmode().width(), DisplayManager.getVidmode().height(), FOV, NEAR_PLANE, FAR_PLANE);
				break;
		}
	}

	/** Reverse the current option for whether to render collision boxes to the screen */
	public void setRenderColliders() {
		renderColliders = !renderColliders;
	}
}