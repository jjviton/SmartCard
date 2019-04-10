/**************************************************************




***************************************************************/



package TESC;

import javacard.framework.*;

public class TESC extends Applet
{
	
	private final static byte TESC_CLA 	   = (byte)0x80; 
    private final static byte INS_GET_DATA = (byte)0xCA;
    private final static byte INS_PUT_DATA = (byte)0xDA;
    
    private final static byte P1_UID = (byte)0x23;
    private final static byte P1_TOKEN = (byte)0x32;
    
    /* Token de muestra y firma MAC 3DES */
    private final static byte TOKEN_TESC[] = {(byte)0x00, (byte)0x01 , (byte)0x00, (byte)0x01 , (byte)0x00, (byte)0x26 , (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xF1, (byte)0x00, (byte)0x05 , (byte)0x35, (byte)0x5B , (byte) 0x81, (byte)0x7A, (byte)0xD0, (byte)0xEA, (byte)0xB8, (byte)0xD1, (byte)0x1F, (byte)0x5E};
    private final static byte UID[]={(byte)0x4F, (byte)0xE9, (byte)0x7D, (byte)0x06};
    
    

	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new TESC().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
	}




	public void process(APDU apdu)
	{
		if (selectingApplet())
		{
			return;
		}

		byte[] buf = apdu.getBuffer();
		
		if(buf[ISO7816.OFFSET_CLA] != TESC_CLA)  return;   // wrong class

		switch (buf[ISO7816.OFFSET_INS])
		{
		case (byte)INS_GET_DATA:
			tesc_devolverToken(apdu);
			break;
		case (byte)INS_PUT_DATA:
			tesc_grabarToken(apdu);
			break;			
		default:
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}

	///////////////////////////////// Funciones internas
	private void tesc_devolverToken(APDU apdu){
		
		byte[] buf = apdu.getBuffer();

		if(buf[ISO7816.OFFSET_P1] == (byte)P1_UID){

				byte[] buffer = apdu.getBuffer();  
				short le = apdu.setOutgoing();	        
				apdu.setOutgoingLength((byte)(4));   
				apdu.sendBytesLong(UID, (short)0, (short)(4));  
		}
		
		if(buf[ISO7816.OFFSET_P1] == (byte)P1_TOKEN){

				byte[] buffer = apdu.getBuffer();  
				short le = apdu.setOutgoing();	        
				apdu.setOutgoingLength((byte)(16+8));   
				apdu.sendBytesLong(TOKEN_TESC, (short)0, (short)(16+8));  //0x18
		}
		
		
	}
	
	// Funcion para actualizar token, por ahora no hacemos nada. 
	private void tesc_grabarToken(APDU apdu){
		return;
	}
	
	////////////////////////////////////////////////////




}
