package com.space.sim.gui.cameras;

import java.util.ArrayList;
import java.util.List;

import com.space.sim.math.Hyperball;
import com.space.sim.math.SimpleLine;
import com.space.sim.universe.Vector;

/**
 * This class streamlines {@link Camera} usage for drawing.  Not only
 * does it project balls and lines into 2D, but it also sorts the
 * projections in the order that they should be drawn.
 * <p>
 * To use this class properly, at the start of every frame
 * {@link CamMan#prepareForNextDraw() prepareForNextDraw()} should be called.
 * Afterwards, to add any balls and lines that need to be drawn,
 * {@link CamMan#addBalls(List) addBalls(List)} and
 * {@link CamMan#addLines(List) addLines(List)} should be called respectively.
 * FInally, {@link CamMan#makeDrawingList() makeDrawingList()} will provide an
 * {@code ArrayList} of projections of the balls and lines such that objects in
 * the beginning of the {@code ArrayList} should be drawn behind objects later
 * in the {@code ArrayList}.
 * 
 * @author David Krug
 * @version August 25, 2024
 * @param <V> The type of vectors that the camera will see
 */
abstract class CamMan<V extends Vector<V>> {
	
	/**
	 * The first step in the preparation process.  This method removes
	 * all of the objects that the {@code CamMan} was told to organize
	 * in the last frame.  If this method is called after another method
	 * in this class, the entire preparation process will need to be restarted.
	 */
	protected abstract void prepareForNextDraw();
	
	
	/**
	 * An intermediate step in the preparation process.  This method adds
	 * the desired balls to the {@code CamMan}, and removes balls
	 * that will not be seen by the camera.
	 * 
	 * @param balls	The balls to be drawn
	 */
	protected abstract void addBalls(List<? extends Hyperball<V>> balls);
	
	
	/**
	 * An intermediate step in the preparation process.  This method adds
	 * the desired lines to the {@code CamMan}.  This method can be
	 * called multiple times in the same frame without worry.
	 * 
	 * @param lines	The lines that should be drawn
	 */
	protected abstract void addLines(List<SimpleLine<V>> lines);
	
	
	/**
	 * The final step in the preparation process.  This method will return
	 * an {@code ArrayList} of {@link Hyperball}{@code <Vector2>} and
	 * {@link SimpleLine}{@code <Vector2>} instances for a
	 * {@link com.space.sim.gui.Painter Painter}.  The {@code ArrayList} is
	 * ordered such that objects in the beginning of the {@code ArrayList}
	 * should be drawn behind objects later in the {@code ArrayList}.
	 * 
	 * @return		A list of circles and lines 
	 */
	protected abstract ArrayList<Object> makeDrawingList();

}
