package Communications;

public class CommunicationsViscount {

    private static int a = 0;

    private static String f1() { return "59"; }
    private static String f2() { return "6f"; }
    private static String f3() { return "7520"; }
    private static String f4() { return "646f"; }
    private static String f5() { return "6e27"; }
    private static String f6() { return "7420"; }
    private static String f7() { return "736179"; }
    private static String f8() { return "3f"; }

    private static String g1() { return "5765"; }
    private static String g2() { return "6c6c"; }
    private static String g3() { return "207375"; }
    private static String g4() { return "7265"; }
    private static String g5() { return "2c20"; }
    private static String g6() { return "7768"; }
    private static String g7() { return "656e"; }
    private static String g8() { return "2079"; }
    private static String g9() { return "6f7520"; }
    private static String g10() { return "707574"; }
    private static String g11() { return "2069"; }
    private static String g12() { return "7420"; }
    private static String g13() { return "6c696b6520746861742e2e2e"; }
    private static String t1() { return "7568682e2e2e206920"; }
    private static String t2() { return "207468696e6b20736f2c"; }
    private static String t3() { return "206275742077686572"; }
    private static String t4() { return "6520617265207765"; }
    private static String t5() { return "20676f696e672074"; }
    private static String t6() { return "6f2067657420746861"; }
    private static String t7() { return "74206d616e20512d74"; }
    private static String t8() { return "6970733f"; }

    private static String u1() { return "6f6b61792c206e6f77"; }
    private static String u2() { return "20796f7572206a757374"; }
    private static String u3() { return "206265696e67207765697264"; }

    private static String v1() { return "6d653f206e6f2c206e6f"; }
    private static String v2() { return "20796f75722074686520"; }
    private static String v3() { return "6f6e65206265696e6720"; }
    private static String v4() { return "7765697264"; }

    private static String w1() { return "63616e20796f7520706c"; }
    private static String w2() { return "656173652073746f70"; }
    private static String h(String... s) {
        StringBuilder z = new StringBuilder();
        for (String x : s) {
            for (int i = 0; i < x.length(); i += 2) {
                z.append((char) Integer.parseInt(x.substring(i, i + 2), 16));
            }
        }
        return z.toString();
    }

    private static String a() { return h(f1(), f2(), f3(), f4(), f5(), f6(), f7(), f8()); }
    private static String b() { return h(g1(), g2(), g3(), g4(), g5(), g6(), g7(), g8(), g9(), g10(), g11(), g12(), g13()); }
    private static String c() { return h(t1(), t2(), t3(), t4(), t5(), t6(), t7(), t8()); }
    private static String d() { return h(u1(), u2(), u3()); }
    private static String e() { return h(v1(), v2(), v3(), v4()); }
    private static String f() { return h(w1(), w2()); }
    private static String[] i() { return new String[]{a(), b(), c(), d(), e(), f()}; }

    public String getViscountcy() {
        String[] k = i();
        String l = k[Math.min(a, k.length - 1)];
        a++;
        return l;
    }
}
