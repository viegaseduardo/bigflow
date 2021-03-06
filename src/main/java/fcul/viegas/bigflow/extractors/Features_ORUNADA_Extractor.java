/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.extractors;

import fcul.viegas.bigflow.dto.Features_A_DTO;
import fcul.viegas.bigflow.dto.Features_A_B_DTO;
import fcul.viegas.bigflow.dto.Features_ORUNADA_A_B_Middleware_DTO;
import fcul.viegas.bigflow.dto.Features_ORUNADA_A_Middleware_DTO;
import fcul.viegas.bigflow.dto.Features_ORUNADA_DTO;
import fcul.viegas.bigflow.dto.NetworkPacketDTO;

/**
 *
 * @author viegas
 */
public class Features_ORUNADA_Extractor {

    public static void extractFeatures_A_B(Features_A_B_DTO featAB, NetworkPacketDTO networkPacket) {

        //number of packets
        featAB.getFeatures_ORUNADA_A_B_Middleware().setNumberOfPackets(featAB.getFeatures_ORUNADA_A_B_Middleware().getNumberOfPackets() + 1);

        //we extract percentage features by computing the average of flag values (1 if set, 0 otherwise)
        //average SYN
        featAB.getFeatures_ORUNADA_A_B_Middleware().getNumberOfSYN().addNumber((networkPacket.getTcp_syn()) ? 1 : 0);
        //average ACK
        featAB.getFeatures_ORUNADA_A_B_Middleware().getNumberOfACK().addNumber((networkPacket.getTcp_ack()) ? 1 : 0);
        //average RST
        featAB.getFeatures_ORUNADA_A_B_Middleware().getNumberOfRST().addNumber((networkPacket.getTcp_rst()) ? 1 : 0);
        //average FIN
        featAB.getFeatures_ORUNADA_A_B_Middleware().getNumberOfFIN().addNumber((networkPacket.getTcp_fin()) ? 1 : 0);
        //average CWR 
        featAB.getFeatures_ORUNADA_A_B_Middleware().getNumberOfCWR().addNumber((networkPacket.getTcp_cwr()) ? 1 : 0);
        //average URG
        featAB.getFeatures_ORUNADA_A_B_Middleware().getNumberOfURG().addNumber((networkPacket.getTcp_urg()) ? 1 : 0);

        //average network packet size
        featAB.getFeatures_ORUNADA_A_B_Middleware().getAveragePacketSize().addNumber(networkPacket.getPacket_size());

        //average TTL
        featAB.getFeatures_ORUNADA_A_B_Middleware().getAverageTTL().addNumber(networkPacket.getTimeToLive());

        //average ICMP redirect
        featAB.getFeatures_ORUNADA_A_B_Middleware().getAverageICMPRedirect().addNumber((networkPacket.getIcmp_type() == 5) ? 1 : 0);

        //average ICMP time Exceeded
        featAB.getFeatures_ORUNADA_A_B_Middleware().getAverageICMPTimeExceeded().addNumber((networkPacket.getIcmp_type() == 11) ? 1 : 0);

        //average ICMP Unreachable
        featAB.getFeatures_ORUNADA_A_B_Middleware().getAverageICMPUnreachable().addNumber((networkPacket.getIcmp_type() == 3) ? 1 : 0);

        //average ICMP others
        if (networkPacket.getIcmp_type() != 5 && networkPacket.getIcmp_type() != 11 && networkPacket.getIcmp_type() != 3) {
            featAB.getFeatures_ORUNADA_A_B_Middleware().getAverageICMPOther().addNumber(1);
        } else {
            featAB.getFeatures_ORUNADA_A_B_Middleware().getAverageICMPOther().addNumber(0);
        }
    }

    public static void extractFeatures_A(Features_A_DTO featA, NetworkPacketDTO networkPacket) {
        //add different destination ip addresses
        featA.getFeatures_ORUNADA_A_Middleware().getUniqueDestinationIPs().add(networkPacket.getDestinationIP().hashCode());
        //add different destination services
        featA.getFeatures_ORUNADA_A_Middleware().getUniqueDestinationPorts().add((networkPacket.getDestinationIP() + networkPacket.getDestinationPort()).hashCode());
    }
    
    public static void extractFeatureDTO(Features_ORUNADA_DTO featOrunada, Features_A_B_DTO featAB, Features_A_DTO featA) {
        Features_ORUNADA_A_Middleware_DTO featOrunadaA = featA.getFeatures_ORUNADA_A_Middleware();
        Features_ORUNADA_A_B_Middleware_DTO featOrunadaAB = featAB.getFeatures_ORUNADA_A_B_Middleware();
        
        featOrunada.setNumberOfPackets(Long.valueOf(featOrunadaAB.getNumberOfPackets()));
        featOrunada.setPercentageOfSYNPackets(featOrunadaAB.getAverageOfSYN().getAverage());
        featOrunada.setPercentageOfACKPackets(featOrunadaAB.getAverageOfACK().getAverage());
        featOrunada.setPercentageOfRSTPackets(featOrunadaAB.getAverageOfRST().getAverage());
        featOrunada.setPercentageOfFINPackets(featOrunadaAB.getAverageOfFIN().getAverage());
        featOrunada.setPercentageOfCWRPackets(featOrunadaAB.getAverageOfCWR().getAverage());
        featOrunada.setPercentageOfURGPackets(featOrunadaAB.getAverageOfURG().getAverage());
        featOrunada.setAveragePacketSize(featOrunadaAB.getAveragePacketSize().getAverage());
        featOrunada.setMeanTTL(featOrunadaAB.getAverageTTL().getAverage());
        featOrunada.setPercentageICMPRedirect(featOrunadaAB.getAverageICMPRedirect().getAverage());
        featOrunada.setPercentageICMPTimeExceeded(featOrunadaAB.getAverageICMPTimeExceeded().getAverage());
        featOrunada.setPercentageICMPUnreacheable(featOrunadaAB.getAverageICMPUnreachable().getAverage());
        featOrunada.setPercentageICMPOtherTypes(featOrunadaAB.getAverageICMPOther().getAverage());
        
        featOrunada.setNumberOfDifferentDestinations(featOrunadaA.getUniqueDestinationIPs().size());
        featOrunada.setNumberOfDifferentServices(featOrunadaA.getUniqueDestinationPorts().size());
    }
}
