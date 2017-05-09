package es.tid.bgp.bgp4.update.fields;

import java.util.LinkedList;

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
public class ITNodeNLRI extends LinkStateNLRI {
	private String nodeId;
	private String controllerIT;
	private String cpu;
	private String mem;
	private String storage;
	//private LinkedList<MapKeyValue> slicesList;

	public ITNodeNLRI(){
		this.setNLRIType(NLRITypes.IT_Node_NLRI);
		
	}

	public ITNodeNLRI(byte[] bytes, int offset) {
		super(bytes,offset);
		decode();
	}
	@Override
	public void encode() {
		int len = 4;// The four bytes of the header

		byte bytesStringControllerIT[]=null;
		byte bytesStringCpu[] = null;
		byte bytesStringMem[] = null;
		byte bytesStringStorage[] = null;

		byte bytesStringNodeId[] = nodeId.getBytes();
		len = len + 4 + bytesStringNodeId.length;

		if (controllerIT != null){
			bytesStringControllerIT = controllerIT.getBytes();
			len = len + 4 + bytesStringControllerIT.length;
		}
		if (cpu != null) {
			//byte bytesStringCpu[] = cpu.getBytes();
			bytesStringCpu = cpu.getBytes();
			len += 4 + bytesStringCpu.length;
		}
		if (mem != null) {
			bytesStringMem = mem.getBytes();
			len += 4 + bytesStringMem.length;
		}
		if (storage != null) {
			bytesStringStorage = storage.getBytes();
			len += 4 + bytesStringStorage.length;
		}
		/*if ((slicesList!=null)&&(slicesList.size()>0)) {
			for (int k = 0; k < slicesList.size(); k++) {
				MapKeyValue temp = slicesList.get(k);
				//byte bytesStringkey[]=temp.key.getBytes();
				//byte bytesStringvalue[]=temp.value.getBytes();
				KeyTLV keyT = new KeyTLV();
				keyT.setKeyTLV(temp.key);
				keyT.encode();
				len = 4+ len + keyT.getTotalTLVLength();
				ValueTLV valueT = new ValueTLV();
				valueT.setValueTLV(temp.value);
				valueT.encode();
				len = len + valueT.getTotalTLVLength();
				keyT=null;
				valueT=null;
			}
		}
		System.out.println("Andrea len= "+String.valueOf(len));
		*/


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

		if ((controllerIT != null)&&(bytesStringControllerIT!=null)){
			offset = encodeHeaderSubTLV(2, bytesStringControllerIT.length,offset);
			for(int i=0;i<bytesStringControllerIT.length;i++){
				this.bytes[offset]=bytesStringControllerIT[i];
				offset++;
			}
		}
		if( (cpu != null)&&(bytesStringCpu!=null)) {

			offset = encodeHeaderSubTLV(3, bytesStringCpu.length,offset);
			for(int i=0;i<bytesStringCpu.length;i++){
				this.bytes[offset]=bytesStringCpu[i];
				offset++;
			}
		}
		if ((mem != null)&&(bytesStringMem!=null)) {
			offset = encodeHeaderSubTLV(4, bytesStringMem.length, offset);
			for (int i = 0; i < bytesStringMem.length; i++) {
				this.bytes[offset] = bytesStringMem[i];
				offset++;
			}
		}
		if ((storage != null)&&(bytesStringStorage!=null)) {
			offset = encodeHeaderSubTLV(5, bytesStringStorage.length,offset);
			for(int i=0;i<bytesStringStorage.length;i++){
				this.bytes[offset]=bytesStringStorage[i];
				offset++;
			}
		}
		/*
		if ((slicesList!=null)&&(slicesList.size()>0)) {
			for (int k=0; k< slicesList.size(); k++){
				MapKeyValue temp= slicesList.get(k);
				//key
				KeyTLV ukeyT= new KeyTLV();
				ukeyT.setKeyTLV(temp.key);
				ukeyT.encode();
				//value
				ValueTLV uvalueT= new ValueTLV();
				uvalueT.setValueTLV(temp.value);
				System.out.println("Key Iinside= "+ ukeyT.getKeyTLV());
				System.out.println("Value Iinside= "+ uvalueT.getValueTLV());
				uvalueT.encode();
				int totLen=ukeyT.getTotalTLVLength()+uvalueT.getTotalTLVLength();
				offset = encodeHeaderSubTLV(6,totLen,offset);
				System.arraycopy(ukeyT.getTlv_bytes(),0,this.bytes,offset,ukeyT.getTotalTLVLength());
				offset=offset+ukeyT.getTotalTLVLength();
				System.arraycopy(uvalueT.getTlv_bytes(),0,this.bytes,offset,uvalueT.getTotalTLVLength());
				offset=offset+uvalueT.getTotalTLVLength();
				System.out.println("Andrea step by step len= "+ String.valueOf(offset));
				uvalueT=null;
				ukeyT=null;
			}
		}
		 */

	}
	public void decode(){
		//Decoding ITNodeNL
		//Header 2(type)+2(length)
		int offset = 2;
		LinkedList<MapKeyValue> newslicesList;
		newslicesList=	new LinkedList<MapKeyValue>();

		byte[] lengthITNodeNLRIBytes = new byte[2];
		System.arraycopy(this.bytes,offset, lengthITNodeNLRIBytes, 0, 2);
		int lengthITNodeNLRI = ((lengthITNodeNLRIBytes[0] << 8) & 0xFF00) | ((lengthITNodeNLRIBytes[1]) & 0xFF);
		offset+=2;
				
		int lengthResourcesgeted = 0;
		while (lengthResourcesgeted<lengthITNodeNLRI){
			//int typeResource = null;
			//System.out.println("Decode Andrea ");

			byte[] typeResourceBytes = new byte[2];
			System.arraycopy(this.bytes,offset, typeResourceBytes, 0, 2);
			int typeResource = ((typeResourceBytes[0] << 8) & 0xFF00) | ((typeResourceBytes[1]) & 0xFF);
			offset+=2;
			//System.out.println("Type= "+String.valueOf(typeResource));

			byte[] lengthResourceBytes = new byte[2];
			System.arraycopy(this.bytes,offset, lengthResourceBytes, 0, 2);
			int lengthResource = ((lengthResourceBytes[0] << 8) & 0xFF00) | ((lengthResourceBytes[1]) & 0xFF);
			offset+=2;
			//System.out.println("Lenght/Offset1= "+String.valueOf(lengthResource));
			int offset1= offset;
			byte[] valueResourceBytes = new byte[lengthResource];
			System.arraycopy(this.bytes,offset, valueResourceBytes, 0, lengthResource);
			String valueResource=new String(valueResourceBytes);
			offset+=lengthResource;
			int count =0;
			switch (typeResource) {
				case 1:
					this.nodeId = valueResource;
					break;
				case 2:
					this.controllerIT = valueResource;
					break;
				case 3:
					this.cpu = valueResource;
					break;
				case 4:
					this.mem = valueResource;
					break;
				case 5:
					this.storage = valueResource;
					break;

				/*
				case 6:
					System.out.println("Arrivato 6");
					byte[] typeBytes = new byte[2];
					System.arraycopy(this.bytes,offset1, typeBytes, 0, 2);
					int typeInt = ((typeBytes[0] << 8) & 0xFF00) | ((typeBytes[1]) & 0xFF);
					offset1+=2;
					System.out.println("first type in the value: "+String.valueOf(typeInt));
					if (typeInt==1000){
						byte[] lengthBytes = new byte[2];
						System.arraycopy(this.bytes,offset1, lengthBytes, 0, 2);
						int lengthX = ((lengthBytes[0] << 8) & 0xFF00) | ((lengthBytes[1]) & 0xFF);
						offset1+=2;
						byte[] keyBytes = new byte[lengthX];
						System.arraycopy(this.bytes,offset1, keyBytes, 0, lengthX);
						String key=new String(keyBytes);
						System.out.println("Key Received: "+key);
						offset1+=lengthX;

						byte[] typeVBytes = new byte[2];
						System.arraycopy(this.bytes,offset1, typeVBytes, 0, 2);
						int typeVInt = ((typeVBytes[0] << 8) & 0xFF00) | ((typeVBytes[1]) & 0xFF);
						offset1+=2;
						System.out.println("Second type in the value: "+String.valueOf(typeVInt));
						if (typeVInt==1001){
							byte[] lengthVBytes = new byte[2];
							System.arraycopy(this.bytes,offset1, lengthVBytes, 0, 2);
							int lengthV = ((lengthVBytes[0] << 8) & 0xFF00) | ((lengthVBytes[1]) & 0xFF);
							offset1+=2;
							byte[] valueBytes = new byte[lengthV];
							System.arraycopy(this.bytes,offset1, valueBytes, 0, lengthV);
							String value=new String(valueBytes);
							System.out.println("Value Received: "+value);
							MapKeyValue element = new MapKeyValue();
							element.setKey(key);
							element.setValue(value);
							newslicesList.add(element);
						}
					}


					break;
				 */

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
	
	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getMem() {
		return mem;
	}

	public void setMem(String mem) {
		this.mem = mem;
	}
	
	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}
	//public LinkedList<MapKeyValue> getSlices() {
	//	return slicesList;
	//}

	//public void setSlices(LinkedList<MapKeyValue> slices) {
	//	this.slicesList = slices;
	//}

	@Override
	public String toString() {
		return "ITNodeNLRI [nodeID=" + nodeId + "controllerIT=" + controllerIT + ", cpu="
				+ cpu + ", mem="
				+ mem+  ", storage="+ storage+"]";
	}

	public String getControllerIT() {
		return controllerIT;
	}

	public void setControllerIT(String controllerIT) {
		this.controllerIT = controllerIT;
	}

}
