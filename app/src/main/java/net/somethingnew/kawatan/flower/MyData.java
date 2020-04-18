package net.somethingnew.kawatan.flower;

public class MyData {
    static String[] folderNameArray = {"Cupcake", "Donut", "Eclair", "Froyo", "Gingerbread", "Honeycomb", "Ice Cream Sandwich","JellyBean", "Kitkat", "Lollipop", "Marshmallow"};
    static Integer[] folderDrawableArray = {R.drawable.cupcake, R.drawable.donut, R.drawable.eclair,
            R.drawable.froyo, R.drawable.gingerbread, R.drawable.honeycomb, R.drawable.ics,
            R.drawable.jellybean, R.drawable.kitkat, R.drawable.lollipop,R.drawable.marsh};

    static String[] cardFrontArray    = {"新垣", "広瀬", "有村", "吉岡", "土屋", "本田", "川口", "小松","清野", "川栄", "新木", "高畑"};
    static String[] cardBackArray       = {"結衣", "すず", "架純", "里帆", "太鳳", "翼",   "春奈", "菜奈","奈名", "李奈", "優子", "充希"};
    static Integer[] cardDrawableArray = {R.drawable.cupcake,R.drawable.cupcake,R.drawable.donut, R.drawable.eclair,
            R.drawable.froyo, R.drawable.gingerbread, R.drawable.honeycomb, R.drawable.ics,
            R.drawable.jellybean, R.drawable.kitkat, R.drawable.lollipop,R.drawable.marsh};

    /*
    static String[] pastelColorArray = {"#A1","#A2","#A3","#A4","#A5","#A6",
                                        "#B1","#B2","#B3","#B4","#B5","#B6",
                                        "#C1","#C2","#C3","#C4","#C5","#C6",
                                        "#D1","#D2","#D3","#D4","#D5","#D6",
                                        "#E1","#E2","#E3","#E4","#E5","#E6",
                                        "#F1","#F2","#F3","#F4","#F5","#F6",
                                        "#G1","#G2","#G3","#G4","#G5","#G6",
                                        "#H1","#H2","#H3","#H4","#H5","#H6"
                                        };

     */

    // 文字色
    static String[] textColorArray = {
            "#000000","#808080","#FFFFFF","#FF0000","#FFFF00","#00FF00",
            "#00FFFF","#0000FF","#FF00FF","#800000","#008000","#800080"
    };

    // 背景パステル色
    static String[] pastelColorArray = {
            "#AADDDD","#ABDADE","#A9DEDE","#99DEDE","#AFE3E3","#BBF2F2",
            "#FFFFBB","#F9FFBA","#FFFFB8","#FFFFA6","#E6E6A8","#F5F5B3",
            "#FFBBDD","#AADDDD","#AADDDD","#AADDDD","#AADDDD","#AADDDD",
            "#FFBBBB","#AADDDD","#AADDDD","#AADDDD","#AADDDD","#AADDDD",
            "#99DDFF","#AADDDD","#AADDDD","#AADDDD","#AADDDD","#AADDDD",
            "#AABBEE","#AADDDD","#AADDDD","#AADDDD","#AADDDD","#AADDDD",
            "#AADDAA","#AADDDD","#AADDDD","#AADDDD","#AADDDD","#AADDDD",
            "#FFDDDD","#AADDDD","#AADDDD","#AADDDD","#AADDDD","#AADDDD"
    };

    static String[] iconNameArray = {
            "jewelry_076_01","jewelry_076_05","jewelry_076_06","jewelry_076_07","jewelry_076_08","jewelry_076_09",
            "jewelry_076_10","jewelry_076_11","jewelry_076_16","jewelry_076_17","jewelry_076_18","jewelry_076_21",
            "jewelry_076_22","jewelry_076_23","jewelry_076_26","jewelry_076_27","jewelry_076_29","jewelry_076_30",
            "jewelry_076_31","jewelry_076_32","jewelry_076_33","jewelry_076_34","jewelry_076_35","jewelry_076_37",
            "jewelry_076_38","jewelry_076_41","jewelry_078_01","jewelry_078_02","jewelry_078_05","jewelry_078_08",
            "jewelry_078_11","jewelry_078_12","jewelry_078_13","jewelry_078_14","jewelry_078_16","jewelry_078_18",
            "jewelry_078_20","jewelry_078_21","jewelry_078_25","jewelry_078_28","jewelry_078_30","jewelry_078_33",
            "jewelry_078_34","jewelry_078_35","jewelry_080_02","jewelry_080_03","jewelry_080_04","jewelry_080_05",
            "jewelry_080_06","jewelry_080_07","jewelry_080_08","jewelry_080_09","jewelry_080_10","jewelry_080_11",
            "jewelry_080_12","jewelry_080_13","jewelry_080_15","jewelry_080_16","jewelry_080_17","jewelry_080_18",
            "jewelry_080_19","jewelry_080_20","jewelry_080_21","jewelry_080_22","jewelry_080_23","jewelry_080_24",
            "jewelry_080_25","jewelry_080_26","jewelry_080_27","jewelry_080_28","jewelry_080_29","jewelry_080_30",
            "jewelry_080_31","jewelry_080_32","jewelry_080_33","jewelry_080_35","jewelry_080_36","jewelry_080_37",
            "jewelry_080_38","jewelry_080_39","jewelry_080_40","jewelry_080_41","jewelry_080_42","jewelry_080_43",
            "jewelry_080_44","jewelry_082_01","jewelry_082_02","jewelry_082_03","jewelry_082_04","jewelry_082_05",
            "jewelry_082_06","jewelry_082_07","jewelry_082_08","jewelry_082_09","jewelry_082_10","jewelry_082_11",
            "jewelry_082_12","jewelry_082_13","jewelry_082_14","jewelry_082_15","jewelry_082_16","jewelry_082_17",
            "jewelry_082_18","jewelry_082_19","jewelry_082_20","jewelry_082_21","jewelry_082_22","jewelry_082_23",
            "jewelry_082_24","jewelry_082_25","jewelry_082_26","jewelry_082_27","jewelry_082_28","jewelry_082_29",
            "jewelry_082_30","jewelry_082_31","jewelry_082_32","jewelry_082_33","jewelry_082_34","jewelry_082_35",
            "jewelry_082_36","jewelry_082_37","jewelry_082_38","jewelry_082_39","jewelry_082_40","jewelry_082_41",
            "jewelry_082_42","jewelry_082_43","jewelry_082_44","jewelry_082_45","jewelry_082_46","jewelry_082_47",
            "jewelry_082_48","jewelry_082_49","jewelry_082_50","jewelry_082_51","jewelry_082_52","jewelry_082_53",
            "jewelry_082_54","jewelry_082_55","jewelry_082_56","jewelry_082_57","jewelry_082_58","jewelry_084_22",
            "jewelry_084_23","jewelry_084_24","jewelry_084_25","jewelry_084_26","jewelry_084_27","jewelry_084_28",
            "jewelry_084_30","jewelry_086_02","jewelry_086_03","jewelry_086_04","jewelry_086_06","jewelry_086_07",
            "jewelry_086_09","jewelry_086_10","jewelry_086_15","jewelry_086_16","jewelry_086_17","jewelry_086_18",
            "jewelry_086_20","jewelry_086_22","jewelry_086_24"
    };

    static String[] fusenNameArray = {
            "fusen_01","fusen_02","fusen_03","fusen_04","fusen_05","fusen_06"
    };

}
