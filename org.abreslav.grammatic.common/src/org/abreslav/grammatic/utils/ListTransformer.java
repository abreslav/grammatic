package org.abreslav.grammatic.utils;

import java.util.ArrayList;
import java.util.List;

public class ListTransformer {

	public interface IElementTransformer<F, T> {
		T transform(final F f);
	}
	
	public static <F, T> List<T> transformList(List<? extends F> list, IElementTransformer<? super F, ? extends T> transformer) {
		List<T> result = new ArrayList<T>(list.size());
		for (F f : list) {
			result.add(transformer.transform(f));
		}
		return result;
	}
}
