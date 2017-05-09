package es.tid.bgp.bgp4.update.tlv;

/**
 * 3.2.1.1.  Local Node Descriptors

   The Local Node Descriptors TLV (Type 256) contains Node Descriptors
   for the node anchoring the local end of the link.  The length of this
   TLV is variable.  The value contains one or more Node Descriptor Sub-
   TLVs defined in Section 3.2.1.3.

      0                   1                   2                   3
      0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
     +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     |              Type             |             Length            |
     +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     |                                                               |
     |               Node Descriptor Sub-TLVs (variable)             |
     |                                                               |
     +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

                Figure 9: Local Node Descriptors TLV format


 * @author pac porque madre mia mcs...
 *
 */
public class KeyTLV extends BGP4TLVFormat{


	public static final int Key_TLV = 1000;

	private String key;

	public KeyTLV(){
		super();
		this.setTLVType(KeyTLV.Key_TLV);
		//nodeDescriptorsSubTLVList=new ArrayList<NodeDescriptorsSubTLV>();
	}


	public KeyTLV(byte []bytes, int offset) {
		super(bytes, offset);
		decode();
	}
	
	public void encode(){
		int len = 4;//Header TLV
		if (key != null){
			byte bytesStringkey[]=key.getBytes();
			len+=bytesStringkey.length;

		}
		this.setTLVValueLength(len);
		this.setTlv_bytes(new byte[this.getTotalTLVLength()]);
		encodeHeader();
		int offset=4;//Header TLV

		if (key != null){
			byte bytesStringkey[]=key.getBytes();
			System.arraycopy(bytesStringkey,0,this.tlv_bytes,offset,bytesStringkey.length);
		}
	}
	
	
	public void decode(){
		//Decoding LocalNodeDescriptorsTLV
		boolean fin=false;
		int offset=4;//Position of the next subobject
//		if (ObjectLength==4){
//			fin=true;
//		}
		byte[] typeResourceBytes = new byte[2];
		System.arraycopy(this.tlv_bytes,offset, typeResourceBytes, 0, 2);
		int typeResource = ((typeResourceBytes[0] << 8) & 0xFF00) | ((typeResourceBytes[1]) & 0xFF);
		offset+=2;

		byte[] lengthResourceBytes = new byte[2];
		System.arraycopy(this.tlv_bytes,offset, lengthResourceBytes, 0, 2);
		int lengthResource = ((lengthResourceBytes[0] << 8) & 0xFF00) | ((lengthResourceBytes[1]) & 0xFF);
		offset+=2;

		byte[] valueResourceBytes = new byte[lengthResource];
		System.arraycopy(this.tlv_bytes,offset, valueResourceBytes, 0, lengthResource);
		key=new String(valueResourceBytes);

}


	public String getKeyTLV() {
		return key;
	}


	public void setKeyTLV(String keyx) {
		this.key = keyx;
	}



	@Override
	public String toString() {
		
		StringBuffer sb=new StringBuffer(1000);
		
		if (key != null)
			sb.append("\n\t>Slice key: "+key+"\n");
			
		return sb.toString();
	}
	
	
}
