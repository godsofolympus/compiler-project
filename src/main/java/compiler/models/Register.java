package compiler.models;

public interface Register {
    public static final String SP = "$sp";
    public static final String FP = "$fp";
    public static final String RA = "$ra";
    public static final String V0 = "$v0";
    public static final String A0 = "$a0";
    public static final String T0 = "$t0";
    public static final String T1 = "$t1";
    public static final String R0 = "$zero";

    // Floating-Point Registers
    // Arguments
    public static final String DA0 = "$f12";
    public static final String DA1 = "$f14";
    public static final String DA2 = "$f16";
    public static final String DA3 = "$f18";

    // Temps
    public static final String DT0 = "$f4";
    public static final String DT1 = "$f6";
    public static final String DT2 = "$f8";
    public static final String DT3 = "$f10";
    public static final String DT4 = "$f20";
    public static final String DT5 = "$f22";

    // Function Returns
    public static final String DV0 = "$f0";
    public static final String DV1 = "$f2";

    // Saved
    public static final String DS0 = "$f24";
    public static final String DS1 = "$f26";
    public static final String DS2 = "$f28";
    public static final String DS3 = "$f30";

    // Status
    public static final String DSTATUS = "$fcsr";
}
