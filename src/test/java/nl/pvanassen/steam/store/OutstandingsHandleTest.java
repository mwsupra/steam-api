package nl.pvanassen.steam.store;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class OutstandingsHandleTest extends OutstandingsHandle {

	@Test
	public void testEmptyHandle() throws IOException {
		OutstandingsHandle handle = new OutstandingsHandle();
		handle.handle(getClass().getResourceAsStream("/empty-marketpage.html"));
		assertNotNull("Expected object", handle.getOutstandings());
		assertEquals("Expected 0", 0, handle.getOutstandings().getAmount());
		assertEquals("Expected 0", 0, handle.getOutstandings().getItems());
	}

}
