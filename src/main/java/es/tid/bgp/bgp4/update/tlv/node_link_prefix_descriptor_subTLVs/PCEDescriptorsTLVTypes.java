package es.tid.bgp.bgp4.update.tlv.node_link_prefix_descriptor_subTLVs;

public class PCEDescriptorsTLVTypes {
/*	      +--------------------+-------------------+----------+
           | Sub-TLV Code Point | Description       |   Length |
           +--------------------+-------------------+----------+
           |         	       | Neighbor PCE		| variable |
           |        514 	   | Area-ID      		| variable |
           |       		       |           |         |
           +--------------------+-------------------+----------+

                     Table 2: Node Descriptor Sub-TLVs             */
	
	//Para node descriptors
	public static final int PCE_DESCRIPTORS_TLV_TYPE_SCOPE=1070;//Will be set to proper value
	public static final int PCE_DESCRIPTORS_TLV_TYPE_NEIGBOUR_ID=1071;//Will be set to proper value
	public static final int PCE_DESCRIPTORS_TLV_TYPE_DOMAIN_ID=1072;


}
