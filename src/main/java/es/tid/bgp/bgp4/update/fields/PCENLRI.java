package es.tid.bgp.bgp4.update.fields;

import es.tid.bgp.bgp4.update.tlv.*;
import es.tid.bgp.bgp4.update.tlv.node_link_prefix_descriptor_subTLVs.*;

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
	//private PCEv4ScopeTLV PCEv4Scope;
	private PCEv4DomainTLV PCEv4Domain;
	private PCEv4NeighboursTLV PCEv4Neigbour;


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
		//if (PCEv4Scope!=null){
		//	PCEv4Scope.encode();
		//	len=len+PCEv4Scope.getTotalTLVLength();
		//}
		if (PCEv4Domain!=null){
			PCEv4Domain.encode();
			len=len+PCEv4Domain.getTotalTLVLength();
		}

		if (PCEv4Neigbour!=null){
			PCEv4Neigbour.encode();
			len=len+PCEv4Neigbour.getTotalTLVLength();
		}

		this.setTotalNLRILength(len);
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
			offset=offset+PCEv4Descriptors.getTotalTLVLength();
		}
		//if (PCEv4Scope!=null){
		//	System.arraycopy(PCEv4Scope.getTlv_bytes(), 0, this.bytes, offset,PCEv4Scope.getTotalTLVLength());
		//	offset=offset+PCEv4Scope.getTotalTLVLength();
		//}
		if (PCEv4Domain!=null){
			System.arraycopy(PCEv4Domain.getTlv_bytes(), 0, this.bytes, offset,PCEv4Domain.getTotalTLVLength());
			offset=offset+PCEv4Domain.getTotalTLVLength();
		}
		if (PCEv4Neigbour!=null){
			System.arraycopy(PCEv4Neigbour.getTlv_bytes(), 0, this.bytes, offset,PCEv4Neigbour.getTotalTLVLength());
			//offset=offset+PCEv4Neigbour.getTotalTLVLength();
		}

		
	}
	public void decode(){
		//Decoding PCE NLRI
		boolean fin=false;
		int offset = 4; //Cabecera del LinkState NLRI
		protocolID = this.bytes[offset];
		offset=offset +1; //identifier
		

		long routingUniverseIdentifieraux1 = ((  ((long)bytes[offset]&0xFF)   <<24)& 0xFF000000) |  (((long)bytes[offset+1]<<16) & 0xFF0000) | (((long)bytes[offset+2]<<8) & 0xFF00) |(((long)bytes[offset+3]) & 0xFF);
		long routingUniverseIdentifieraux2 = ((  ((long)bytes[offset+4]&0xFF)   <<24)& 0xFF000000) |  (((long)bytes[offset+5]<<16) & 0xFF0000) | (((long)bytes[offset+6]<<8) & 0xFF00) |(((long)bytes[offset+7]) & 0xFF);
		//this.setRoutingUniverseIdentifier((2^32)*routingUniverseIdentifieraux1+routingUniverseIdentifieraux2);
		this.setRoutingUniverseIdentifier((routingUniverseIdentifieraux1 <<32)&0xFFFFFFFF00000000L | routingUniverseIdentifieraux2);
		offset = offset +8;

		this.PCEv4Descriptors=new PCEv4DescriptorsTLV(this.bytes, offset);

		while (!fin) {
			int TlvType= BGP4TLVFormat.getType(this.bytes, offset);
			int TlvLength=BGP4TLVFormat.getTotalTLVLength(this.bytes, offset);
			switch(TlvType){
				case PCEDescriptorsTLVTypes.PCE_DESCRIPTORS_TLV_TYPE_DOMAIN_ID:
					PCEv4Domain = new PCEv4DomainTLV(this.bytes, offset);
					break;
				case PCEDescriptorsTLVTypes.PCE_DESCRIPTORS_TLV_TYPE_NEIGBOUR_ID:
					PCEv4Neigbour = new PCEv4NeighboursTLV(this.bytes, offset);
					break;
				default:
					log.debug("Local Node Descriptor subtlv Unknown, "+TlvType);
					break;
			}
			offset=offset+TlvLength;
			if (offset>=this.TotalNLRILength){
				fin=true;
			}
			else{
				log.debug("sigo leyendo NodeDescriptorsSubTLV ");
			}
		}









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

	public PCEv4DomainTLV getPCEv4DomainID() {
		return PCEv4Domain;
	}

	public void setPCEv4DomainID(PCEv4DomainTLV PCEDomainID) {
		this.PCEv4Domain = PCEDomainID;
	}

	public PCEv4NeighboursTLV getPCEv4NeighbourID() {
		return PCEv4Neigbour;
	}

	public void setPCEv4NeighbourID(PCEv4NeighboursTLV PCENeighbourID) {
		this.PCEv4Neigbour = PCENeighbourID;
	}





	@Override
	public String toString() {
		return "PCENLRI [protocolID=" + protocolID + ", routingUniverseIdentifier="
				+ routingUniverseIdentifier + ", PCEDescriptors="
				+ PCEv4Descriptors.toString()+ ", PCEv4DomainTLV="
				+ PCEv4Domain.toString()+", PCEv4NeighboursTLV="
				+ PCEv4Neigbour.toString()+ "]";
	}

	public long getRoutingUniverseIdentifier() {
		return routingUniverseIdentifier;
	}

	public void setRoutingUniverseIdentifier(long routingUniverseIdentifier) {
		this.routingUniverseIdentifier = routingUniverseIdentifier;
	}


}
