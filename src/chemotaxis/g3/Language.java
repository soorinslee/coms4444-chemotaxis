package chemotaxis.g3;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class Language {
    
    public Language() {
        ;
    }

    // for interfacing with the Agent, UDLR + RGB --> [-5, 2]
    public int[] getCoord(String color) {
        // get directed color:

        // retrieve box number 
        int box = colorToBoxNum.get(color);
        if (box >= 28 && box <= 34) {
            // TODO: the color was placed on top of the cell!!!! change movement 
            ;
        }

        // return coordinates 
        return boxNumToCoords[box];
    }

    // for interfacing with the Controller, Angle --> UDLR + RGB
    public String getColor(Double angle) {
        // given angle between the agent and target:

        // get intercepted box # from angle 
        int box = findClosestAngle(angle);

        // return directed color 
        return boxNumToColor[box];
    }

    // change if remapping color + direction to a new cell 
    Map<String, Integer> colorToBoxNum = new HashMap<String, Integer>() {{
        put("rG",   0);
        put("rR",   1);
        put("lRGB", 2); 
        put("lGB",  3);
        put("lRB",  4);
        put("lRG",  5);
        put("lB",   6);
        put("lG",   7);
        put("lR",   8);
        put("dRGB", 9);
        put("dGB",  10);
        put("dRB",  11);
        put("dRG",  12);
        put("dB",   13);
        put("dG",   14);
        put("dR",   15);
        put("uRGB", 16);
        put("uGB",  17);
        put("uRB",  18);
        put("uRG",  19);
        put("uB",   20);
        put("uG",   21);
        put("uR",   22);
        put("rRGB", 23);
        put("rGB",  24);
        put("rRB",  25);
        put("rRG",  26);
        put("rB",   27);
        put("R",    28);
        put("G",    29);
        put("B",    30);
        put("RG",   31);
        put("RB",   32);
        put("GB",   33);
        put("RGB",  34);
    }};

    // never change 
    private int[][] boxNumToCoords = new int[][] {
        {5,0}, {5,1}, {4,2}, {4,3}, {3,4}, {2,4}, {1,5},
        {0,5}, {-1,5}, {-2,4}, {-3,4}, {-4,3}, {-4,2}, {-5,1},
        {-5,0}, {-5,-1}, {-4,-2}, {-4,-3}, {-3,-4}, {-2,-4}, {-1,-5},
        {0,-5}, {1,-5}, {2,-4}, {3,-4}, {4,-3}, {4,-2}, {5,-1}
    };

    // never change 
    private Integer findClosestAngle(double angle) {
        if (0.0 <= angle && angle < 180.0) {
            if (angle < 90.0) {
                if (angle < 36.87) {
                    if (angle < 11.31) return 0;
                    else if (angle < 26.57) return 1;
                    else return 2; 
                }
                else {
                    if (angle < 63.43) {
                        if (angle < 53.13) return 3;
                        else return 4;
                    }
                    else {
                        if (angle < 78.69) return 5;
                        else return 6;
                    }
                }
            }
            else {
                if (angle < 126.87) {
                    if (angle < 101.31) return 7;
                    else if (angle < 116.57) return 8;
                    else return 9;
                }
                else {
                    if (angle < 153.43) {
                        if (angle < 143.13) return 10;
                        else return 11;
                    }
                    else {
                        if (angle < 168.69) return 12;
                        else return 13;
                    }
                }
            }
        }
        else {
            if (angle < 270.0) {
                if (angle < 216.87) {
                    if (angle < 191.31) return 14;
                    else if (angle < 206.57) return 15;
                    else return 16;
                }
                else {
                    if (angle < 243.43) {
                        if (angle < 233.13) return 17;
                        else return 18;
                    }
                    else {
                        if (angle < 258.69) return 19;
                        else return 20;
                    }
                }
            }
            else {
                if (angle < 306.87) {
                    if (angle < 281.31) return 21;
                    else if (angle < 296.57) return 22;
                    else return 23;
                }
                else {
                    if (angle < 333.43) {
                        if (angle < 323.13) return 24;
                        else return 25;
                    }
                    else {
                        if (angle < 348.69) return 26;
                        else return 27;
                    }
                }
            }
        }
    }

    // change if remapping color + direction to a new cell 
    private String[] boxNumToColor = {
        "r_G_", // 0
        "rR__", // 1
        "lRGB", // 2 
        "l_GB", // 3
        "lR_B", // 4
        "lRG_", // 5
        "l__B", // 6
        "l_G_", // 7
        "lR__", // 8
        "dRGB", // 9
        "d_GB", // 10
        "dR_B", // 11
        "dRG_", // 12
        "d__B", // 13
        "d_G_", // 14
        "dR__", // 15
        "uRGB", // 16
        "u_GB", // 17
        "uR_B", // 18
        "uRG_", // 19
        "u__B", // 20
        "u_G_", // 21
        "uR__", // 22
        "rRGB", // 23
        "r_GB", // 24
        "rR_B", // 25
        "rRG_", // 26
        "r__B", // 27
    };

}

// Map<String, double> colorToBox = new HashMap<String, double>() {{
//     put("rG", 0.0);
//     put("rR", Math.toDegrees(Math.atan(1/5)));
//     put("lRGB", Math.toDegrees(Math.atan(2/4)));
//     put("lGB", Math.toDegrees(Math.atan(3/4)));
//     put("lRB", Math.toDegrees(Math.atan(4/3)));
//     put("lRG", Math.toDegrees(Math.atan(4/2)));
//     put("lB", Math.toDegrees(Math.atan(5/1)));
//     put("lG", 90.0);
//     put("lR", Math.toDegrees(Math.atan(1/5)) + 90);
//     put("dRGB", Math.toDegrees(Math.atan(2/4)) + 90);
//     put("dGB", Math.toDegrees(Math.atan(3/4)) + 90);
//     put("dRB", Math.toDegrees(Math.atan(4/3)) + 90);
//     put("dRG", Math.toDegrees(Math.atan(4/2)) + 90);
//     put("dB", Math.toDegrees(Math.atan(5/1)) + 90);
//     put("dG", 180.0);
//     put("dR", Math.toDegrees(Math.atan(1/5)) + 180);
//     put("uRGB", Math.toDegrees(Math.atan(2/4)) + 180);
//     put("uGB", Math.toDegrees(Math.atan(3/4)) + 180);
//     put("uRB", Math.toDegrees(Math.atan(4/3)) + 180);
//     put("uRG", Math.toDegrees(Math.atan(4/2)) + 180);
//     put("uB", Math.toDegrees(Math.atan(5/1)) + 180);
//     put("uG",270.0);
//     put("uR", Math.toDegrees(Math.atan(1/5)) + 270);
//     put("rRGB", Math.toDegrees(Math.atan(2/4)) + 270);
//     put("rGB", Math.toDegrees(Math.atan(3/4)) + 270);
//     put("rRB", Math.toDegrees(Math.atan(4/3)) + 270);
//     put("rRG", Math.toDegrees(Math.atan(4/2)) + 270);
//     put("rB", Math.toDegrees(Math.atan(5/1)) + 270);  
// }};

/* Values for all of positive arctan starting from 0:
0.0
11.309932474020213
26.56505117707799
36.86989764584402
53.13010235415598
63.43494882292202
78.69006752597979
90.0
101.30993247402021
116.56505117707799
126.86989764584402
143.13010235415598
153.43494882292202
168.6900
180.0
191.3099324740202
206.56505117707798
216.86989764584402
233.13010235415598
243.43494882292202
258.69006752597977
270.0
281.30993247402023
296.565051177078
306.86989764584405
323.13010235415595
333.434948822922
348.69006752597977
360.0
*/
