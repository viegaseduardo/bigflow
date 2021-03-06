/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.definitions;

/**
 *
 * @author viegas
 */
public class Definitions {

    public static final Float PROTOCOL_NONE = 0.0f;
    public static final Float PROTOCOL_TCP = 1.0f;
    public static final Float PROTOCOL_UDP = 2.0f;
    public static final Float PROTOCOL_ICMP = 3.0f;
    public static final Float PROTOCOL_OTHER = 4.0f;

    public static final Long TIME_WINDOW_NETWORK_PACKET_FEATURE_EXTRACTOR_A_B = 15000l;
    public static final Long TIME_WINDOW_NETWORK_PACKET_FEATURE_EXTRACTOR_A = 15000l;

    public static final Long NO_VALUE_MAXIMUM = 0l;
    public static final Long NO_VALUE_MINIMUM = 2000l;

    public static final String FIELD_DELIM = ";";
    public static final String FIELD_DELIM_WEKA = ",";

    public static String CLASS_DESCRIPTION_FILE = "";

    public static String ANY_IP_ADDRESS = "any";
    public static Integer ANY_PORT_ADDRESS = -1;

    public static String FEATURES_CLASS_ASSIGNER_NORMAL_TAXONOMY = "normal_taxonomy";
    public static Float FEATURES_CLASS_ASSIGNER_NORMAL_DISTANCE = 0.0f;
    public static Integer FEATURES_CLASS_ASSIGNER_NORMAL_NB_DETECTORS = 0;
    public static String FEATURES_CLASS_ASSIGNER_NORMAL_LABEL = "normal";
    public static String FEATURES_CLASS_ASSIGNER_ATTACK_LABEL = "attack";

    public static String SPARK_FEATURE_SET = "";

    public static Integer SPARK_ORUNADA_FIRST_FEATURE_INDEX = 0;
    public static Integer SPARK_ORUNADA_LAST_FEATURE_INDEX = 15;
    public static Integer SPARK_ORUNADA_NUMBER_OF_FEATURES = SPARK_ORUNADA_LAST_FEATURE_INDEX - SPARK_ORUNADA_FIRST_FEATURE_INDEX;

    public static Integer SPARK_NIGEL_NUMBER_OF_FEATURES = 21;
    public static Integer SPARK_NIGEL_FIRST_FEATURE_INDEX = SPARK_ORUNADA_LAST_FEATURE_INDEX;
    public static Integer SPARK_NIGEL_LAST_FEATURE_INDEX = SPARK_ORUNADA_LAST_FEATURE_INDEX + SPARK_NIGEL_NUMBER_OF_FEATURES;

    public static Integer SPARK_MOORE_NUMBER_OF_FEATURES = 58;
    public static Integer SPARK_MOORE_FIRST_FEATURE_INDEX = SPARK_NIGEL_LAST_FEATURE_INDEX;
    public static Integer SPARK_MOORE_LAST_FEATURE_INDEX = SPARK_NIGEL_LAST_FEATURE_INDEX + SPARK_MOORE_NUMBER_OF_FEATURES;

    public static Integer SPARK_VIEGAS_NUMBER_OF_FEATURES = 48;
    public static Integer SPARK_VIEGAS_FIRST_FEATURE_INDEX = SPARK_MOORE_LAST_FEATURE_INDEX;
    public static Integer SPARK_VIEGAS_LAST_FEATURE_INDEX = SPARK_MOORE_LAST_FEATURE_INDEX + SPARK_VIEGAS_NUMBER_OF_FEATURES;

}
