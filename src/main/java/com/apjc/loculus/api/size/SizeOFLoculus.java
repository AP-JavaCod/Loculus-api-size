package com.apjc.loculus.api.size;

import java.util.List;
import java.io.*;
import org.ehcache.sizeof.SizeOf;
import com.apjc.loculus.Loculus;
import com.apjc.loculus.FileLoculus;

public class SizeOFLoculus {
	
	public static <T> boolean isCompressionEffectiveObj(List<T> data) {
		Sizer sizer = calculateObj(data);
		return sizer.sizeList() > sizer.sizeLoculus();
	}
	
	public static <T> long differenceObj(List<T> data) {
		Sizer sizer = calculateObj(data);
		return sizer.sizeList() - sizer.sizeLoculus();
	}
	
	public static <T extends Serializable> boolean isCompressionEffectiveWrite(List<T> data) throws IOException{
		Sizer sizer = calculateWrite(data);
		return sizer.sizeList() > sizer.sizeLoculus();
	}
	
	public static <T extends Serializable> long differenceWrite(List<T> data) throws IOException{
		Sizer sizer = calculateWrite(data);
		return sizer.sizeList() - sizer.sizeLoculus();
	}
	
	private static <T> void writeList(List<T> list, File file) throws IOException{
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
			oos.writeObject(list);
		}
	}
	
	private static <T> Sizer calculateObj(List<T> list) {
		SizeOf size = SizeOf.newInstance();
		Loculus<T> loculus = new Loculus<>(list);
		Sizer sizer = new Sizer(size.deepSizeOf(list), size.deepSizeOf(loculus));
		loculus.close();
		return sizer;
	}
	
	private static <T extends Serializable> Sizer calculateWrite(List<T> list) throws IOException{
		File file = new File("sizeOf-test.txt");
		writeList(list, file);
		long sizeList = file.length();
		file.delete();
		Loculus<T> loculus = new Loculus<T>(list);
		FileLoculus.serializableFile(loculus, file);
		loculus.close();
		long sizeLoculus = file.length();
		file.delete();
		return new Sizer(sizeList, sizeLoculus);
	}
	
	private static  record Sizer(long sizeList, long sizeLoculus){
		
	}

}
