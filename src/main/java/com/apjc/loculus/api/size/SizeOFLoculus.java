package com.apjc.loculus.api.size;

import java.util.List;
import java.io.*;
import org.ehcache.sizeof.SizeOf;
import com.apjc.loculus.Loculus;

public class SizeOFLoculus {
	
	private final static SizeOf SIZE_OF = SizeOf.newInstance();
	
	public static <T> boolean isCompressionEffectiveObj(List<T> data, CreaterLoculus<T> creater) {
		long sizeList = SIZE_OF.deepSizeOf(data);
		Loculus<T> loculus = creater.apply(data);
		long sizeLoculus= SIZE_OF.deepSizeOf(loculus.compress());
		loculus.close();
		return sizeList > sizeLoculus;
	}
	
	public static <T> long differenceObj(List<T> data, CreaterLoculus<T> creater) {
		long sizeList = SIZE_OF.deepSizeOf(data);
		Loculus<T> loculus = creater.apply(data);
		long sizeLoculus = SIZE_OF.deepSizeOf(loculus.compress());
		loculus.close();
		return sizeList - sizeLoculus;
	}
	
	public static <T extends Serializable> boolean isCompressionEffectiveWrite(List<T> data, CreaterLoculus<T> creater) throws IOException{
		long sizeList = sizeFile(data);
		Loculus<T> loculus = creater.apply(data);
		long sizeLoculus = sizeFile(loculus.compress());
		loculus.close();
		return sizeList > sizeLoculus;
	}
	
	public static <T extends Serializable> long differenceWrite(List<T> data, CreaterLoculus<T> creater) throws IOException{
		long sizeList = sizeFile(data);
		Loculus<T> loculus = creater.apply(data);
		long sizeLoculus = sizeFile(loculus.compress());
		loculus.close();
		return sizeList - sizeLoculus;
	}
	
	private static <T> long sizeFile(Object list) throws IOException{
		File file = new File("sizeOf-test.txt");
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
			oos.writeObject(list);
			long size = file.length();
			file.delete();
			return size;
		}
	}

}
