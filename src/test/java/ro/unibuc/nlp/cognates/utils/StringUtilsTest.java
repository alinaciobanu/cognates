package ro.unibuc.nlp.cognates.utils;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void testEquality() {
		
		String s1 = "catelus";
		String s2 = "cățeluș";
		
		Assert.assertTrue(StringUtils.areEqual(s1, s2, true));
		Assert.assertFalse(StringUtils.areEqual(s1, s2, false));
		Assert.assertFalse(StringUtils.areEqual(s1, s2, 0));
		Assert.assertTrue(StringUtils.areEqual(s1, s2, -1, true));
		
		s1 = "catelusului";
		s2 = "cățelușei";
		
		Assert.assertFalse(StringUtils.areEqual(s1, s2, true));
		Assert.assertFalse(StringUtils.areEqual(s1, s2, 0, true));
		Assert.assertFalse(StringUtils.areEqual(s1, s2, 4, false));
		Assert.assertTrue(StringUtils.areEqual(s1, s2, 4, true));
		Assert.assertFalse(StringUtils.areEqual(s1, s2, 4));
		
		s1 = "cățelușului";
		Assert.assertTrue(StringUtils.areEqual(s1, s2, 4));
		Assert.assertFalse(StringUtils.areEqual(s1, s2, -1));
	}
	
	@Test
	public void testIllegalArguments() {
		
		try {
			StringUtils.areEqual(null, "", 1, true);
		}
		catch (Exception e) {
			Assert.assertTrue(e instanceof IllegalArgumentException);
		}
		
		try {
			StringUtils.areEqual("", null, 1, true);
		}
		catch (Exception e) {
			Assert.assertTrue(e instanceof IllegalArgumentException);
		}
	}
}