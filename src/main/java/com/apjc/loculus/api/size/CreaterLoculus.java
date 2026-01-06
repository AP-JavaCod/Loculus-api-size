package com.apjc.loculus.api.size;

import java.util.List;
import com.apjc.loculus.Loculus;

@FunctionalInterface
public interface CreaterLoculus <T> {

	Loculus<T> apply(List<T> list);
	
}
