package chemotaxis.g3;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ArrayList; 

public class Language {
    
    public Language() {
        ;
    }

    // for interfacing with the Agent, UDLR + RGB --> [-5, 2]
    // Given directed color "uRG_"
    // Returns current state "0+2*0+1.C"
    public int[] getCoord(String color, Int byte) 
        // TODO: implement directions with color and byte, 
        //          right now we are not using the byte info other than +- for previous movement 
        //          since an instruction stops + redirects movement 

        // retrieve box number 
        int box = colorToBoxNum.get(color);
        
        // if (box >= 28 && box <= 34) {
            // // TODO: a non-movement based communication!! like when instruction is to change motion of agent
            // ;
        // }

        // coords is something like {1,-2}
        int[] coords = boxNumToCoords[box];

        // return 
        return boxNumToCoords[box];
    }

    // for interfacing with the Controller
    // Given Angle b/w agent and target
    // returns color and direction for effective communication w/ agent
    public String getColor(Double angle) {
        // get intercepted box # from angle 
        int box = findClosestAngle(angle);

        // return directed color, like "d_GB"
        return boxNumToColor[box];
    }

    // change if remapping color + direction to a new cell 
    private Map<String, Integer> colorToBoxNum =  Map.ofEntries(
        Map.entry("d__B",  0),
        Map.entry("lR__",  1),
        Map.entry("d_GB",  2 ),
        Map.entry("lRG_",  3),
        Map.entry("dR__",  4),
        Map.entry("dRG_",  5),
        Map.entry("d_G_",  6),
        Map.entry("__G_",  7),
        Map.entry("l_G_",  8),
        Map.entry("l_GB",  9),
        Map.entry("l__B", 10),
        Map.entry("_R__", 11),
        Map.entry("___B", 12),
        Map.entry("uR__", 13),
        Map.entry("uRG_", 14),
        Map.entry("u_G_", 15),
        Map.entry("_RG_", 16),
        Map.entry("r_G_", 17),
        Map.entry("r_GB", 18),
        Map.entry("r__B", 19),
        Map.entry("u_GB", 20),
        Map.entry("rRG_", 21),
        Map.entry("u__B", 22),
        Map.entry("rR__", 23)
    );

    // never change 
    private int[][] boxNumToCoords = new int[][] {
                          {-1, 3},         {1, 3}, 
                          {-1, 2},         {1, 2}, 
        {-3, 1}, {-2, 1}, {-1, 1}, {0, 1}, {1, 1}, {2, 1}, {3, 1},
                          {-1, 0},         {1, 0},
        {-3,-1}, {-2,-1}, {-1,-1}, {0,-1}, {1,-1}, {2,-1}, {3,-1},                
                          {-1,-2},         {1,-2},
                          {-1,-3},         {1,-3}
    };

    // never change 
    private Integer findClosestAngle(double angle) {
        if (0.0 <= angle && angle < 180.0) {
            if (angle < 90.0) {
                if (angle < 45) {
                    if (angle < 18.43) return 12;
                    else if (angle < 26.57) return 10;
                    else return 9; 
                }
                else {
                    if (angle < 63.43) return 8;
                    else if (angle < 71.57) return 3;
                    else return 1;
                }
            }
            else {
                if (angle < 135) {
                    if (angle < 108.43) return 7;
                    else if (angle < 116.57) return 0;
                    else return 2;
                }
                else {
                    if (angle < 153.43) return 6;
                    else if (angle < 161.57) return 5;
                    else return 4;
                }
            }
        }
        else {
            if (angle < 270.0) {
                if (angle < 225) {
                    if (angle < 198.83) return 11;
                    else if (angle < 206.57) return 13;
                    else return 14;
                }
                else {
                    if (angle < 243.43) return 15;
                    else if (angle < 251.57) return 20;
                    else return 22;
                }
            }
            else {
                if (angle < 315) {
                    if (angle < 288.43) return 16;
                    else if (angle < 296.57) return 23;
                    else return 21;
                }
                else {
                    if (angle < 333.43) return 17;
                    else if (angle < 341.57) return 18;
                    else return 19;
                }
            }
        }
    }

    // change if remapping color + direction to a new cell 
    private String[] boxNumToColor = new String[] {
        "d__B", // 0
        "lR__", // 1
        "d_GB", // 2 
        "lRG_", // 3
        "dR__", // 4
        "dRG_", // 5
        "d_G_", // 6
        "__G_", // 7
        "l_G_", // 8
        "l_GB", // 9
        "l__B", // 10
        "_R__", // 11
        "___B", // 12
        "uR__", // 13
        "uRG_", // 14
        "u_G_", // 15
        "_RG_", // 16
        "r_G_", // 17
        "r_GB", // 18
        "r__B", // 19
        "u_GB", // 20
        "rRG_", // 21
        "u__B", // 22
        "rR__"  // 23
    };

    // do not change unless the translation is changing
    private Map<String, Integer> stateToByte =  Map.ofEntries(
        Map.entry('silent',    -128),
        Map.entry('pause',     -127),
        Map.entry('n/a',       -126),
        Map.entry('right',     -125),
        Map.entry('left',      -124),
        Map.entry('up',        -123),
        Map.entry('down',      -122),
        Map.entry('0+1*0+1.C', -121),
        Map.entry('0+1.0+1*C', -120),
        Map.entry('1+1*0+1.R', -119),
        Map.entry('0+1.1+1*R', -118),
        Map.entry('0+1*0-1.C', -117),
        Map.entry('0+1.0-1*C', -116),
        Map.entry('1+1*0-1.R', -115),
        Map.entry('0+1.1-1*R', -114),
        Map.entry('0-1*0+1.C', -113),
        Map.entry('0-1.0+1*C', -112),
        Map.entry('1-1*0+1.R', -111),
        Map.entry('0-1.1+1*R', -110),
        Map.entry('0-1*0-1.C', -109),
        Map.entry('0-1.0-1*C', -108),
        Map.entry('1-1*0-1.R', -107),
        Map.entry('0-1.1-1*R', -106),
        Map.entry('0+1*0+2.C', -105),
        Map.entry('0+1.0+2*C', -104),
        Map.entry('1+1*0+2.C', -103),
        Map.entry('0+1.1+2*C', -102),
        Map.entry('1+1*1+2.R', -101),
        Map.entry('1+1.1+2*R', -100),
        Map.entry('0+1.2+2*R', -99),
        Map.entry('0+1*0-2.C', -98),
        Map.entry('0+1.0-2*C', -97),
        Map.entry('1+1*0-2.C', -96),
        Map.entry('0+1.1-2*C', -95),
        Map.entry('1+1*1-2.R', -94),
        Map.entry('1+1.1-2*R', -93),
        Map.entry('0+1.2-2*R', -92),
        Map.entry('0-1*0+2.C', -91),
        Map.entry('0-1.0+2*C', -90),
        Map.entry('1-1*0+2.C', -89),
        Map.entry('0-1.1+2*C', -88),
        Map.entry('1-1*1+2.R', -87),
        Map.entry('1-1.1+2*R', -86),
        Map.entry('0-1.2+2*R', -85),
        Map.entry('0-1*0-2.C', -84),
        Map.entry('0-1.0-2*C', -83),
        Map.entry('1-1*0-2.C', -82),
        Map.entry('0-1.1-2*C', -81),
        Map.entry('1-1*1-2.R', -80),
        Map.entry('1-1.1-2*R', -79),
        Map.entry('0-1.2-2*R', -78),
        Map.entry('0+2*0+1.C', -77),
        Map.entry('0+2.0+1*C', -76),
        Map.entry('0+2*1+1.C', -75),
        Map.entry('1+2.0+1*C', -74),
        Map.entry('1+2*1+1.R', -73),
        Map.entry('1+2.1+1*R', -72),
        Map.entry('2+2*0+1.R', -71),
        Map.entry('0+2*0-1.C', -70),
        Map.entry('0+2.0-1*C', -69),
        Map.entry('0+2*1-1.C', -68),
        Map.entry('1+2.0-1*C', -67),
        Map.entry('1+2*1-1.R', -66),
        Map.entry('1+2.1-1*R', -65),
        Map.entry('2+2*0-1.R', -64),
        Map.entry('0-2*0+1.C', -63),
        Map.entry('0-2.0+1*C', -62),
        Map.entry('0-2*1+1.C', -61),
        Map.entry('1-2.0+1*C', -60),
        Map.entry('1-2*1+1.R', -59),
        Map.entry('1-2.1+1*R', -58),
        Map.entry('2-2*0+1.R', -57),
        Map.entry('0-2*0-1.C', -56),
        Map.entry('0-2.0-1*C', -55),
        Map.entry('0-2*1-1.C', -54),
        Map.entry('1-2.0-1*C', -53),
        Map.entry('1-2*1-1.R', -52),
        Map.entry('1-2.1-1*R', -51),
        Map.entry('2-2*0-1.R', -50),
        Map.entry('0+1*0+3.C', -49),
        Map.entry('0+1.0+3*C', -48),
        Map.entry('1+1*0+3.C', -47),
        Map.entry('0+1.1+3*C', -46),
        Map.entry('1+1*1+3.C', -45),
        Map.entry('1+1.1+3*C', -44),
        Map.entry('0+1.2+3*C', -43),
        Map.entry('1+1*2+3.R', -42),
        Map.entry('1+1.2+3*R', -41),
        Map.entry('0+1.3+3*R', -40),
        Map.entry('0+1*0-3.C', -39),
        Map.entry('0+1.0-3*C', -38),
        Map.entry('1+1*0-3.C', -37),
        Map.entry('0+1.1-3*C', -36),
        Map.entry('1+1*1-3.C', -35),
        Map.entry('1+1.1-3*C', -34),
        Map.entry('0+1.2-3*C', -33),
        Map.entry('1+1*2-3.R', -32),
        Map.entry('1+1.2-3*R', -31),
        Map.entry('0+1.3-3*R', -30),
        Map.entry('0-1*0+3.C', -29),
        Map.entry('0-1.0+3*C', -28),
        Map.entry('1-1*0+3.C', -27),
        Map.entry('0-1.1+3*C', -26),
        Map.entry('1-1*1+3.C', -25),
        Map.entry('1-1.1+3*C', -24),
        Map.entry('0-1.2+3*C', -23),
        Map.entry('1-1*2+3.R', -22),
        Map.entry('1-1.2+3*R', -21),
        Map.entry('0-1.3+3*R', -20),
        Map.entry('0-1*0-3.C', -19),
        Map.entry('0-1.0-3*C', -18),
        Map.entry('1-1*0-3.C', -17),
        Map.entry('0-1.1-3*C', -16),
        Map.entry('1-1*1-3.C', -15),
        Map.entry('1-1.1-3*C', -14),
        Map.entry('0-1.2-3*C', -13),
        Map.entry('1-1*2-3.R', -12),
        Map.entry('1-1.2-3*R', -11),
        Map.entry('0-1.3-3*R', -10),
        Map.entry('0+3*0+1.C', -9),
        Map.entry('0+3.0+1*C', -8),
        Map.entry('0+3.1+1*C', -7),
        Map.entry('1+3*0+1.C', -6),
        Map.entry('1+3.1+1*C', -5),
        Map.entry('1+3*1+1.C', -4),
        Map.entry('2+3*0+1.C', -3),
        Map.entry('2+3.1+1*R', -2),
        Map.entry('2+3*1+1.R', -1),
        Map.entry('3+3*0+1.R',  0),
        Map.entry('0+3*0-1.C',  1),
        Map.entry('0+3.0-1*C',  2),
        Map.entry('0+3.1-1*C',  3),
        Map.entry('1+3*0-1.C',  4),
        Map.entry('1+3.1-1*C',  5),
        Map.entry('1+3*1-1.C',  6),
        Map.entry('2+3*0-1.C',  7),
        Map.entry('2+3.1-1*R',  8),
        Map.entry('2+3*1-1.R',  9),
        Map.entry('3+3*0-1.R',  10),
        Map.entry('0-3*0+1.C',  11),
        Map.entry('0-3.0+1*C',  12),
        Map.entry('0-3.1+1*C',  13),
        Map.entry('1-3*0+1.C',  14),
        Map.entry('1-3.1+1*C',  15),
        Map.entry('1-3*1+1.C',  16),
        Map.entry('2-3*0+1.C',  17),
        Map.entry('2-3.1+1*R',  18),
        Map.entry('2-3*1+1.R',  19),
        Map.entry('3-3*0+1.R',  20),
        Map.entry('0-3*0-1.C',  21),
        Map.entry('0-3.0-1*C',  22),
        Map.entry('0-3.1-1*C',  23),
        Map.entry('1-3*0-1.C',  24),
        Map.entry('1-3.1-1*C',  25),
        Map.entry('1-3*1-1.C',  26),
        Map.entry('2-3*0-1.C',  27),
        Map.entry('2-3.1-1*R',  28),
        Map.entry('2-3*1-1.R',  29),
        Map.entry('3-3*0-1.R',  30)
    );

    private String[] byteToState(Integer i) {
        return byteToStateList[i+128];
    }

    private String[] byteToStateList = new String[] {
        // state         i   byte   
        'silent',    // 0 / -128
        'pause',     // 1 / -127
        'n/a',       // 2 / -126
        'right',     // 3 / -125
        'left',      // 4 / -124
        'up',        // 5 / -123
        'down',      // 6 / -122
        '0+1*0+1.C', // 7 / -121
        '0+1.0+1*C', // 8 / -120
        '1+1*0+1.R', // 9 / -119
        '0+1.1+1*R', // 10 / -118
        '0+1*0-1.C', // 11 / -117
        '0+1.0-1*C', // 12 / -116
        '1+1*0-1.R', // 13 / -115
        '0+1.1-1*R', // 14 / -114
        '0-1*0+1.C', // 15 / -113
        '0-1.0+1*C', // 16 / -112
        '1-1*0+1.R', // 17 / -111
        '0-1.1+1*R', // 18 / -110
        '0-1*0-1.C', // 19 / -109
        '0-1.0-1*C', // 20 / -108
        '1-1*0-1.R', // 21 / -107
        '0-1.1-1*R', // 22 / -106
        '0+1*0+2.C', // 23 / -105
        '0+1.0+2*C', // 24 / -104
        '1+1*0+2.C', // 25 / -103
        '0+1.1+2*C', // 26 / -102
        '1+1*1+2.R', // 27 / -101
        '1+1.1+2*R', // 28 / -100
        '0+1.2+2*R', // 29 / -99
        '0+1*0-2.C', // 30 / -98
        '0+1.0-2*C', // 31 / -97
        '1+1*0-2.C', // 32 / -96
        '0+1.1-2*C', // 33 / -95
        '1+1*1-2.R', // 34 / -94
        '1+1.1-2*R', // 35 / -93
        '0+1.2-2*R', // 36 / -92
        '0-1*0+2.C', // 37 / -91
        '0-1.0+2*C', // 38 / -90
        '1-1*0+2.C', // 39 / -89
        '0-1.1+2*C', // 40 / -88
        '1-1*1+2.R', // 41 / -87
        '1-1.1+2*R', // 42 / -86
        '0-1.2+2*R', // 43 / -85
        '0-1*0-2.C', // 44 / -84
        '0-1.0-2*C', // 45 / -83
        '1-1*0-2.C', // 46 / -82
        '0-1.1-2*C', // 47 / -81
        '1-1*1-2.R', // 48 / -80
        '1-1.1-2*R', // 49 / -79
        '0-1.2-2*R', // 50 / -78
        '0+2*0+1.C', // 51 / -77
        '0+2.0+1*C', // 52 / -76
        '0+2*1+1.C', // 53 / -75
        '1+2.0+1*C', // 54 / -74
        '1+2*1+1.R', // 55 / -73
        '1+2.1+1*R', // 56 / -72
        '2+2*0+1.R', // 57 / -71
        '0+2*0-1.C', // 58 / -70
        '0+2.0-1*C', // 59 / -69
        '0+2*1-1.C', // 60 / -68
        '1+2.0-1*C', // 61 / -67
        '1+2*1-1.R', // 62 / -66
        '1+2.1-1*R', // 63 / -65
        '2+2*0-1.R', // 64 / -64
        '0-2*0+1.C', // 65 / -63
        '0-2.0+1*C', // 66 / -62
        '0-2*1+1.C', // 67 / -61
        '1-2.0+1*C', // 68 / -60
        '1-2*1+1.R', // 69 / -59
        '1-2.1+1*R', // 70 / -58
        '2-2*0+1.R', // 71 / -57
        '0-2*0-1.C', // 72 / -56
        '0-2.0-1*C', // 73 / -55
        '0-2*1-1.C', // 74 / -54
        '1-2.0-1*C', // 75 / -53
        '1-2*1-1.R', // 76 / -52
        '1-2.1-1*R', // 77 / -51
        '2-2*0-1.R', // 78 / -50
        '0+1*0+3.C', // 79 / -49
        '0+1.0+3*C', // 80 / -48
        '1+1*0+3.C', // 81 / -47
        '0+1.1+3*C', // 82 / -46
        '1+1*1+3.C', // 83 / -45
        '1+1.1+3*C', // 84 / -44
        '0+1.2+3*C', // 85 / -43
        '1+1*2+3.R', // 86 / -42
        '1+1.2+3*R', // 87 / -41
        '0+1.3+3*R', // 88 / -40
        '0+1*0-3.C', // 89 / -39
        '0+1.0-3*C', // 90 / -38
        '1+1*0-3.C', // 91 / -37
        '0+1.1-3*C', // 92 / -36
        '1+1*1-3.C', // 93 / -35
        '1+1.1-3*C', // 94 / -34
        '0+1.2-3*C', // 95 / -33
        '1+1*2-3.R', // 96 / -32
        '1+1.2-3*R', // 97 / -31
        '0+1.3-3*R', // 98 / -30
        '0-1*0+3.C', // 99 / -29
        '0-1.0+3*C', // 100 / -28
        '1-1*0+3.C', // 101 / -27
        '0-1.1+3*C', // 102 / -26
        '1-1*1+3.C', // 103 / -25
        '1-1.1+3*C', // 104 / -24
        '0-1.2+3*C', // 105 / -23
        '1-1*2+3.R', // 106 / -22
        '1-1.2+3*R', // 107 / -21
        '0-1.3+3*R', // 108 / -20
        '0-1*0-3.C', // 109 / -19
        '0-1.0-3*C', // 110 / -18
        '1-1*0-3.C', // 111 / -17
        '0-1.1-3*C', // 112 / -16
        '1-1*1-3.C', // 113 / -15
        '1-1.1-3*C', // 114 / -14
        '0-1.2-3*C', // 115 / -13
        '1-1*2-3.R', // 116 / -12
        '1-1.2-3*R', // 117 / -11
        '0-1.3-3*R', // 118 / -10
        '0+3*0+1.C', // 119 / -9
        '0+3.0+1*C', // 120 / -8
        '0+3.1+1*C', // 121 / -7
        '1+3*0+1.C', // 122 / -6
        '1+3.1+1*C', // 123 / -5
        '1+3*1+1.C', // 124 / -4
        '2+3*0+1.C', // 125 / -3
        '2+3.1+1*R', // 126 / -2
        '2+3*1+1.R', // 127 / -1
        '3+3*0+1.R', // 128 / 0
        '0+3*0-1.C', // 129 / 1
        '0+3.0-1*C', // 130 / 2
        '0+3.1-1*C', // 131 / 3
        '1+3*0-1.C', // 132 / 4
        '1+3.1-1*C', // 133 / 5
        '1+3*1-1.C', // 134 / 6
        '2+3*0-1.C', // 135 / 7
        '2+3.1-1*R', // 136 / 8
        '2+3*1-1.R', // 137 / 9
        '3+3*0-1.R', // 138 / 10
        '0-3*0+1.C', // 139 / 11
        '0-3.0+1*C', // 140 / 12
        '0-3.1+1*C', // 141 / 13
        '1-3*0+1.C', // 142 / 14
        '1-3.1+1*C', // 143 / 15
        '1-3*1+1.C', // 144 / 16
        '2-3*0+1.C', // 145 / 17
        '2-3.1+1*R', // 146 / 18
        '2-3*1+1.R', // 147 / 19
        '3-3*0+1.R', // 148 / 20
        '0-3*0-1.C', // 149 / 21
        '0-3.0-1*C', // 150 / 22
        '0-3.1-1*C', // 151 / 23
        '1-3*0-1.C', // 152 / 24
        '1-3.1-1*C', // 153 / 25
        '1-3*1-1.C', // 154 / 26
        '2-3*0-1.C', // 155 / 27
        '2-3.1-1*R', // 156 / 28
        '2-3*1-1.R', // 157 / 29
        '3-3*0-1.R', // 158 / 30
    };

}