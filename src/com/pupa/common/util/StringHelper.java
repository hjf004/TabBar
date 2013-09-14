package com.pupa.common.util;

import android.annotation.SuppressLint;

public class StringHelper {
	@SuppressLint("NewApi")
	public static boolean notNullAndNotEmpty(String... strs) {
		boolean result = true;
		for (String str : strs) {
			result = result && (str != null && (!str.isEmpty()));
			if (!result)
				break;
		}
		return result;
	}
}
