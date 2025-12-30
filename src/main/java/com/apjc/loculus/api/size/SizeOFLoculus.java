package com.apjc.loculus.api.size;

import java.util.List;
import java.io.*;
import org.ehcache.sizeof.SizeOf;
import com.apjc.loculus.Loculus;
import com.apjc.loculus.FileLoculus;

public class SizeOFLoculus {
	
	public static <T> boolean isCompressionEffectiveObj(List<T> data) {
		SizeOf size = SizeOf.newInstance();
		Loculus<T> loculus = new Loculus<>(data);
		long listSize = size.sizeOf(data);
		long loculusSize = size.sizeOf(loculus);
		loculus.close();
		return listSize > loculusSize;
	}
	
	public static <T extends Serializable> boolean isCompressionEffectiveWrite(List<T> data) throws IOException{
		File file = new File("SizeOf-test.txt");
		writeList(data, file);
		long sizeList = file.length();
		Loculus<T> loculus = new Loculus<>(data);
		FileLoculus.serializableFile(loculus, file);
		loculus.close();
		long sizeLoculus = file.length();
		file.delete();
		return sizeList > sizeLoculus;
	}
	
	private static <T> void writeList(List<T> list, File file) throws IOException{
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
			oos.writeObject(list);
		}
	}

}
