package main.model.gfx;

import java.awt.image.BufferedImage; 
import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;

import main.consts.FilePathConsts;
import main.enums.GFXType;
import main.util.FileUtil;


public class ShapedGFX {
	
	protected Vector<BufferedImage> frames;
	
	public ShapedGFX(GFXType type){
		frames=new Vector<BufferedImage>();
		loadImages(type);
	}

	protected void loadImages(GFXType type){
		File path=null;
		FilenameFilter filter=null;
		
		if(type==GFXType.EXPLOSION){
			filter=new ExplosionSpritesFilter();
			path=new File(FilePathConsts.explosionSpritesPath);
		}else if(type==GFXType.SHOOT_BULLET){
			filter=new ShootSpritesFilter();
			path=new File(FilePathConsts.explosionSpritesPath);
		}else if(type==GFXType.TOWER_SHOOT){
			filter=new TowerShootSpritesFilter();
			path=new File(FilePathConsts.towersSpritesPath);
		}
		
		for(File f : path.listFiles(filter)){
			if(f.getAbsolutePath().toLowerCase().endsWith(".gif")){
				BufferedImage[] array=FileUtil.readImages(f.getAbsolutePath());
				for(int a=0;a<array.length;a++)
					frames.add(array[a]);
			}else{
				frames.add(FileUtil.readImage(f.getAbsolutePath()));
			}
		}
	}

	/**
	 *
	 * @param image array index
	 * @return gfx frame
	 */
	public BufferedImage getFrame(int index){
		return frames.get(index);
	}

	public int getFrameCount(){
		return frames.size();
	}
	
	//Para que sorongo haces inner classes aca?
	private class ExplosionSpritesFilter implements FilenameFilter{

		@Override
		public boolean accept(File dir, String name) {
			if(name.startsWith("explosion")){
				if(name.endsWith(".png")) return true;
				if(name.endsWith(".jpg")) return true;
				if(name.endsWith(".gif")) return true;
			}
			return false;
		}

	}

	private class TowerShootSpritesFilter implements FilenameFilter{
		@Override
		public boolean accept(File dir, String name) {
			if(name.toLowerCase().endsWith("ani.gif")) return true;
			return false;
		}

	}


	private class ShootSpritesFilter implements FilenameFilter{
		@Override
		public boolean accept(File dir, String name) {
			if(name.startsWith("shoot")){
				if(name.endsWith(".png")) return true;
				if(name.endsWith(".jpg")) return true;
				if(name.endsWith(".gif")) return true;
			}
			return false;
		}

	}
}
