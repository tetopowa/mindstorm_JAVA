package test.java;

import static org.junit.Assert.*;
import mindstorm.Lampe;

import org.junit.Test;

public class LampTest {

	Lampe lampe = new Lampe(true);
	
	@Test
	public void testEtat() {
		lampe.setEtat(true);
		assertEquals(true, lampe.isEtat());
		
		lampe.allumer();
		assertEquals(true, lampe.isEtat());

		lampe.eteindre();
		assertEquals(false, lampe.isEtat());
	}

}
