package rfid;

import java.util.*;
import exception.*;

public class ItemInfo implements Cloneable {
	private String name;
	private String rfidTag;
	private String initPosition;
	private String newPosition;
	double price;

	public ItemInfo() {
		this.name = null;
		this.rfidTag = null;
		this.initPosition = null;
		this.newPosition = null;
		this.price = 0;
	}

	public ItemInfo(String name, String rfidTag, String initPosition, String newPosition, double price) {
		this.name = name;
		try {
			this.rfidTag = rfidTag;
			if (rfidTag.length() != 9)
				throw new InvalidRFIDException("RFID not valid");
		} catch (InvalidRFIDException e) {
			
		}
	}

	public static void main(String[] args) {
		ItemInfo ii = new ItemInfo("Dress Shirt", "00A5542FF", "s12345", "s12345", 30);
	}

}
