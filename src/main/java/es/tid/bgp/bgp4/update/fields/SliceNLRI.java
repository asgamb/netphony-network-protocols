package es.tid.bgp.bgp4.update.fields;

/**
 *  Node NLRI Format (RFC 4271). 
 * 
 * <a href="http://www.ietf.org/rfc/rfc4271.txt">RFC 4271</a>.
 * 
  The 'NLRI Type' field can contain one of the following values:

      Type = 1: Node NLRI

      Type = 2: Link NLRI

      Type = 3: IPv4 Topology Prefix NLRI

      Type = 4: IPv6 Topology Prefix NLRI
      
      Type = 5: IT Node

   The Node NLRI (NLRI Type = 1) is shown in the following figure.

    0                   1                   2                   3
    0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
   +-+-+-+-+-+-+-+-+
   |  Protocol-ID  |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                           Identifier                          |
   |                            (64 bits)                          |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   //                Local Node Descriptors (variable)            //
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

                      Figure 7: The Node NLRI format

 * @author pac
 *
 */
public class SliceNLRI extends LinkStateNLRI {
	private String nodeId;
	private String key;
	private String value;

	public SliceNLRI(){
		this.setNLRIType(NLRITypes.Slice_NLRI);

	}

	public SliceNLRI(byte[] bytes, int offset) {
		super(bytes,offset);
		decode();
	}
	@Override
	public void encode() {
		int len = 4;// The four bytes of the header


		byte bytesStringNodeId[] = nodeId.getBytes();
		len = len + 4 + bytesStringNodeId.length;

		byte byteskey[] = key.getBytes();
			len = len + 4 + byteskey.length;

		byte bytesvalue[] = value.getBytes();
			len += 4 + bytesvalue.length;

		//System.out.println("Andrea Slice len= "+String.valueOf(len));

		this.setTotalNLRILength(len); 
		
		this.setLength(len);
		
		
		this.bytes=new byte[len];
		this.encodeHeader();
				
		
		int offset=4;//Header
		offset = encodeHeaderSubTLV(1, bytesStringNodeId.length,offset);
		for(int i=0;i<bytesStringNodeId.length;i++){
			this.bytes[offset]=bytesStringNodeId[i];
			offset++;
		}
		//System.out.println("Andrea Slice step by step= "+String.valueOf(offset));

			offset = encodeHeaderSubTLV(2, byteskey.length,offset);
			for(int i=0;i<byteskey.length;i++){
				this.bytes[offset]=byteskey[i];
				offset++;
			}
		//System.out.println("Andrea Slice step by step= "+String.valueOf(offset));

			offset = encodeHeaderSubTLV(3, bytesvalue.length,offset);
			for(int i=0;i<bytesvalue.length;i++){
				this.bytes[offset]=bytesvalue[i];
				offset++;
			}

		//System.out.println("Andrea Slice step by step= "+String.valueOf(offset));
	}

	public void decode(){
		//Decoding ITNodeNL
		//Header 2(type)+2(length)
		int offset = 2;

		byte[] lengthNLRIBytes = new byte[2];
		System.arraycopy(this.bytes,offset, lengthNLRIBytes, 0, 2);
		int lengthNLRI = ((lengthNLRIBytes[0] << 8) & 0xFF00) | ((lengthNLRIBytes[1]) & 0xFF);
		offset+=2;


		int lengthResourcesgeted = 0;
		while (lengthResourcesgeted<lengthNLRI){
			//int typeResource = null;

			byte[] typeResourceBytes = new byte[2];
			System.arraycopy(this.bytes,offset, typeResourceBytes, 0, 2);
			int typeResource = ((typeResourceBytes[0] << 8) & 0xFF00) | ((typeResourceBytes[1]) & 0xFF);
			offset+=2;

			byte[] lengthResourceBytes = new byte[2];
			System.arraycopy(this.bytes,offset, lengthResourceBytes, 0, 2);
			int lengthResource = ((lengthResourceBytes[0] << 8) & 0xFF00) | ((lengthResourceBytes[1]) & 0xFF);
			offset+=2;
			int offset1= offset;
			byte[] valueResourceBytes = new byte[lengthResource];
			System.arraycopy(this.bytes,offset, valueResourceBytes, 0, lengthResource);
			String valueResource=new String(valueResourceBytes);
			//System.out.println(typeResource+" "+valueResource);
			offset+=lengthResource;
			int count =0;
			switch (typeResource) {
				case 1:
					this.nodeId = valueResource;
					break;
				case 2:
					this.key = valueResource;
					break;
				case 3:
					this.value = valueResource;
					break;
				default:
					System.out.println("Strange TLV Value-> "+String.valueOf(typeResource));
					break;
			}
			
			lengthResourcesgeted+=4+lengthResource;
		}
		//this.slicesList=newslicesList;
	}

	protected int encodeHeaderSubTLV(int type, int valueLength,int byteStart){
		this.bytes[byteStart]=(byte)(type>>>8 & 0xFF);
		this.bytes[byteStart+1]=(byte)(type & 0xFF);
		this.bytes[byteStart+2]=(byte)(valueLength>>>8 & 0xFF);
		this.bytes[byteStart+3]=(byte)(valueLength & 0xFF);
		return byteStart+4;
	}
	
	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String vv) {
		this.value = vv;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String kk) {
		this.key = kk;
	}
	
	//public LinkedList<MapKeyValue> getSlices() {
	//	return slicesList;
	//}

	//public void setSlices(LinkedList<MapKeyValue> slices) {
	//	this.slicesList = slices;
	//}

	@Override
	public String toString() {
		return "Slice NLRI [nodeID=" + nodeId + "key=" + key + ", value=" + value+"]";
	}


}
