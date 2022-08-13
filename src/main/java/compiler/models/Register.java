package compiler.models;

public interface Register {
    public static final String SP = "$sp";
    public static final String FP = "$fp";
    public static final String RA = "$ra";
    public static final String V0 = "$v0";
    public static final String A0 = "$a0";
    public static final String A1 = "$a1";
    public static final String A2 = "$a2";
    public static final String S0 = "$s0";
    public static final String T0 = "$t0";
    public static final String T1 = "$t1";
    public static final String T2 = "$t2";
    public static final String T3 = "$t3";
    public static final String R0 = "$zero";

    // Floating-Point Registers
    // Arguments
    public static final String FA0 = "$f12";
    public static final String FA1 = "$f13";
    public static final String FA2 = "$f14";
    public static final String FA3 = "$f15";
    public static final String FA5 = "$f16";
    public static final String FA6 = "$f17";
    public static final String FA7 = "$f18";
    public static final String FA8 = "$f19";

    // Temps
    public static final String FT0 = "$f1";
    public static final String FT1 = "$f3";
    public static final String FT2 = "$f4";
    public static final String FT3 = "$f5";
    public static final String FT4 = "$f6";
    public static final String FT5 = "$f7";
    public static final String FT6 = "$f8";
    public static final String FT7 = "$f9";
    public static final String FT8 = "$f10";
    public static final String FT9 = "$f11";
    public static final String FT10 = "$f20";
    public static final String FT11 = "$f21";
    public static final String FT12 = "$f22";
    public static final String FT13 = "$f23";

    // Function Returns
    public static final String FV0 = "$f0";
    public static final String FV1 = "$f2";

    // Saved
    public static final String FS0 = "$f24";
    public static final String FS1 = "$f25";
    public static final String FS2 = "$f26";
    public static final String FS3 = "$f27";
    public static final String FS4 = "$f28";
    public static final String FS5 = "$f29";
    public static final String FS6 = "$f30";
    public static final String FS7 = "$f31";

    // Status
    public static final String FSTATUS = "$fcsr";
}
