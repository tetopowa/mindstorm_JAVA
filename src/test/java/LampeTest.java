package test.java;

import static org.junit.Assert.*;
import mindstorm.Lampe;

import org.junit.Test;

public class LampeTest {

	Lampe lampe = new Lampe(true);
	
	@Test
	public void testEtat() {
		lampe.setEtat(true);
		assertEquals(true, lampe.isEtat());
		
		lampe.setEtat(false);
		assertEquals(false, lampe.isEtat());
	}
}
