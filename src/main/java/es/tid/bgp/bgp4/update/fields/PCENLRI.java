package es.tid.bgp.bgp4.update.fields;

import es.tid.bgp.bgp4.update.tlv.PCEv4DescriptorsTLV;
import es.tid.bgp.bgp4.update.tlv.RoutingUniverseIdentifierTypes;

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
public class PCENLRI extends LinkStateNLRI {
	private int protocolID;//inicializado a 0(unknown)
	private long routingUniverseIdentifier;
	private PCEv4DescriptorsTLV PCEv4Descriptors;

	public PCENLRI(){
		this.setNLRIType(NLRITypes.PCE_NLRI);
		this.setRoutingUniverseIdentifier(RoutingUniverseIdentifierTypes.Level3Identifier);

	}

	public PCENLRI(byte[] bytes, int offset) {
		super(bytes,offset);
		decode();
	}
	@Override
	public void encode() {
		int len=4+1+8;// The four bytes of the header plus the 4 first bytes)
		if (PCEv4Descriptors!=null){
			PCEv4Descriptors.encode();
			len=len+PCEv4Descriptors.getTotalTLVLength();
		}
		
		
		this.setTotalNLRILength(len); 
		//len = len+1; //Length (1 octet, NRLI) 
		this.setLength(len);
		this.bytes=new byte[len];
		this.encodeHeader();
	
		this.bytes[4]=(byte)protocolID;
		this.bytes[5]=(byte)(routingUniverseIdentifier>>>56 & 0xFF);
		this.bytes[6]=(byte)(routingUniverseIdentifier>>>48 & 0xFF);
		this.bytes[7]=(byte)(routingUniverseIdentifier >>> 40 & 0xFF);
		this.bytes[8]=(byte)(routingUniverseIdentifier>>>32 & 0xFF);
		this.bytes[9]=(byte)(routingUniverseIdentifier>>>24 & 0xFF);
		this.bytes[10]=(byte)(routingUniverseIdentifier >>> 16 & 0xFF);
		this.bytes[11]=(byte)(routingUniverseIdentifier >>>8 & 0xFF);
		this.bytes[12]=(byte)(routingUniverseIdentifier & 0xFF);
		
		int offset=13;
		
		if (PCEv4Descriptors!=null){
			System.arraycopy(PCEv4Descriptors.getTlv_bytes(), 0, this.bytes, offset,PCEv4Descriptors.getTotalTLVLength());
			//offset=offset+PCEv4Descriptors.getTotalTLVLength();
		}
		
		
	}
	public void decode(){
		//Decoding PCENLRI
		int offset = 4; //Cabecera del LinkState NLRI
		protocolID = this.bytes[offset];
		offset=offset +1; //identifier
		

		long routingUniverseIdentifieraux1 = ((  ((long)bytes[offset]&0xFF)   <<24)& 0xFF000000) |  (((long)bytes[offset+1]<<16) & 0xFF0000) | (((long)bytes[offset+2]<<8) & 0xFF00) |(((long)bytes[offset+3]) & 0xFF);
		long routingUniverseIdentifieraux2 = ((  ((long)bytes[offset+4]&0xFF)   <<24)& 0xFF000000) |  (((long)bytes[offset+5]<<16) & 0xFF0000) | (((long)bytes[offset+6]<<8) & 0xFF00) |(((long)bytes[offset+7]) & 0xFF);
		//this.setRoutingUniverseIdentifier((2^32)*routingUniverseIdentifieraux1+routingUniverseIdentifieraux2);
		this.setRoutingUniverseIdentifier((routingUniverseIdentifieraux1 <<32)&0xFFFFFFFF00000000L | routingUniverseIdentifieraux2);
		offset = offset +8;
		//byte[] ip=new byte[4];
		//System.arraycopy(this.bytes,offset, ip, 0, 4);

		this.PCEv4Descriptors=new PCEv4DescriptorsTLV(this.bytes, offset);
	}

	public int getProtocolID() {
		return protocolID;
	}

	public void setProtocolID(int protocolID) {
		this.protocolID = protocolID;
	}

	public PCEv4DescriptorsTLV getPCEv4Descriptors() {
		return PCEv4Descriptors;
	}

	public void setPCEv4Descriptors(PCEv4DescriptorsTLV PCEDescriptors) {
		this.PCEv4Descriptors = PCEDescriptors;
	}

	@Override
	public String toString() {
		return "PCENLRI [protocolID=" + protocolID + ", routingUniverseIdentifier="
				+ routingUniverseIdentifier + ", PCEDescriptors="
				+ PCEv4Descriptors.toString()+ "]";
	}

	public long getRoutingUniverseIdentifier() {
		return routingUniverseIdentifier;
	}

	public void setRoutingUniverseIdentifier(long routingUniverseIdentifier) {
		this.routingUniverseIdentifier = routingUniverseIdentifier;
	}


}
