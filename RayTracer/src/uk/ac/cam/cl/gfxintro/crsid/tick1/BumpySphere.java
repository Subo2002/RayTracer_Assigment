
package uk.ac.cam.cl.gfxintro.crsid.tick1;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class BumpySphere extends Sphere {

	private float BUMP_FACTOR = 5f;
	private float[][] heightMap;
	private int bumpMapHeight;
	private int bumpMapWidth;

	public BumpySphere(Vector3 position, double radius, ColorRGB colour, String bumpMapImg) {
		super(position, radius, colour);
		try {
			BufferedImage inputImg = ImageIO.read(new File(bumpMapImg));
			bumpMapHeight = inputImg.getHeight();
			bumpMapWidth = inputImg.getWidth();
			heightMap = new float[bumpMapHeight][bumpMapWidth];
			for (int row = 0; row < bumpMapHeight; row++) {
				for (int col = 0; col < bumpMapWidth; col++) {
					float height = (float) (inputImg.getRGB(col, row) & 0xFF) / 0xFF;
					heightMap[row][col] = BUMP_FACTOR * height;
				}
			}
		} catch (IOException e) {
			System.err.println("Error creating bump map");
			e.printStackTrace();
		}
	}

	// Get normal to surface at position

	@Override
	public Vector3 getNormalAt(Vector3 P) {
		
		//TODO: return the normal modified by the bump map
        //Finding u & v
        Vector3 C = this.position;
        Vector3 Q = P.subtract(C);
        Vector3 N = Q.normalised();
        double r = Q.magnitude();

        double theta = Math.acos(N.y);
		double phi;
		if (Math.abs(N.y) == 1){
			phi = 0;
		}
        else {
			double sinPhi = (N.z)/Math.sin(theta);
			phi = Math.acos((N.x)/Math.sin(theta));
			if (sinPhi < 0) {
				phi = 2*Math.PI - phi;
			}
		}



        int u = (int)Math.floor((phi/(2*Math.PI))*(bumpMapWidth-1));
        int v = (int)Math.floor((theta/Math.PI)*(bumpMapHeight-1));

        //Finding Pu & Pv
        Vector3 Pu = new Vector3(-Math.sin(theta)*Math.sin(phi), 0, Math.sin(theta)*Math.cos(phi));
        Vector3 Pv = new Vector3(Math.cos(theta)*Math.cos(phi), -Math.sin(theta), Math.cos(theta)*Math.sin(phi));
		Pu = Pu.normalised();

        //Approximating Bu & Bv
        double Bu;
		if (u == bumpMapWidth-1) Bu = heightMap[v][u] - heightMap[v][0];
		else Bu = heightMap[v][u] - heightMap[v][u+1];
		double Bv;
		if (v == bumpMapHeight-1) Bv = heightMap[v][u] - heightMap[v-1][u];
		else Bv = heightMap[v][u] - heightMap[v+1][u];

		return (N.add(Pu.scale(Bu)).add(Pv.scale(Bv))).normalised();


	}



}
