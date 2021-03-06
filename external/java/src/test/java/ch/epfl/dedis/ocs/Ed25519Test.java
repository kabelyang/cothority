package ch.epfl.dedis.ocs;

import ch.epfl.dedis.lib.crypto.Ed25519;
import ch.epfl.dedis.lib.crypto.*;
import ch.epfl.dedis.lib.exception.CothorityCryptoException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.*;

class Ed25519Test {
    private final static Logger logger = LoggerFactory.getLogger(Ed25519Test.class);
    @Test
    void point() {
        String point = "3B6A27BCCEB6A42D62A3A8D02A6F0D73653215771DE243A63AC048A18B59DA29";
        Point point2 = new Point(point);
        assertEquals(point, point2.toString());
        assertEquals(32, point2.toBytes().length);
        assertTrue(new Point(point2.toBytes()).equals(point2));

        byte[] point_bytes = DatatypeConverter.parseHexBinary("3B6A27BCCEB6A42D62A3A8D02A6F0D73653215771DE243A63AC048A18B59DA29");
        assertTrue(new Point(point_bytes).equals(point2));
    }

    @Test
    void pubStoreLoad() throws Exception {
        String short_str = "short2";
        String long_str = "this is a string too long to be embedded";

        Point pub = Point.pubStore(short_str.getBytes());
        byte[] ret = pub.pubLoad();
        assertEquals(short_str, new String(ret));

        try {
            Point.pubStore(long_str.getBytes());
            throw new CothorityCryptoException("this should not pass");
        } catch (CothorityCryptoException e) {
        }
    }

    @Test
    void toPrivate() {
        KeyPair kp = new KeyPair();

        Point pub = kp.Scalar.scalarMult(null);
        assertTrue(pub.equals(kp.Point));

        Scalar onep = kp.Scalar.addOne();
        assertEquals(onep.getLittleEndian()[0], kp.Scalar.getLittleEndian()[0] + 1);
    }

    @Test
    void scalarMultiply() {
        String priv_str = "66F1874A926079F5907A26B57079B5583E42C3D0FDBB2B7B8638A8DBC1AD4622";
        String priv_str_reduced = "8C499C905D9A5445E440376FB385F72E3E42C3D0FDBB2B7B8638A8DBC1AD4602";
        String pub_str = "6ECFEB30C65BA92D16521DB20BA21C64F86E4CE294A733C66B38B691311078E6";
        Scalar priv = new Scalar(priv_str);
        Point pub = priv.scalarMult(null);
        assertEquals(pub_str, pub.toString());

        Scalar priv_reduced = new Scalar(priv_str_reduced);
        assertEquals(pub_str, priv_reduced.reduce().scalarMult(null).toString());

        Scalar priv1 = priv.addOne();
        Point base = Ed25519.base;
        Point pub1 = pub.add(base);
        assertTrue(pub1.equals(priv1.scalarMult(null)));
    }

    @Test
    void endianness() {
        String priv_str = "66F1874A926079F5907A26B57079B5583E42C3D0FDBB2B7B8638A8DBC1AD4622";
        String priv_reduced_str = "8C499C905D9A5445E440376FB385F72E3E42C3D0FDBB2B7B8638A8DBC1AD4602";
        String priv1_reduced_str = "8D499C905D9A5445E440376FB385F72E3E42C3D0FDBB2B7B8638A8DBC1AD4602";
        String pub_str = "6ECFEB30C65BA92D16521DB20BA21C64F86E4CE294A733C66B38B691311078E6";
        String pub1_str = "FBDAFDA7941D5088990B8DAEAE35B2D7F3E3342B427ABFCF94664374A93C0719";

        Scalar priv = new Scalar(priv_str, false);
        assertEquals(priv_str, priv.toString());
        assertEquals(priv_reduced_str, priv.reduce().toString());

        Point pub = priv.scalarMult(null);
        assertEquals(pub_str, pub.toString());

        Scalar priv_next = priv.reduce().addOne();
        assertEquals(priv1_reduced_str, priv_next.toString());
        assertEquals(pub1_str, priv_next.scalarMult(null).toString());
    }

    @Test
    void reduce() {
        String priv_str = "66F1874A926079F5907A26B57079B5583E42C3D0FDBB2B7B8638A8DBC1AD4622";
        String priv_reduced_str = "8C499C905D9A5445E440376FB385F72E3E42C3D0FDBB2B7B8638A8DBC1AD4602";
        Scalar priv = new Scalar(priv_str, false);
        assertEquals(priv_str, priv.toString());
        assertEquals(priv_reduced_str, priv.reduce().toString());

        Scalar reduced = new Scalar(priv_reduced_str);
        assertEquals(priv_reduced_str, reduced.toString());

        Scalar reduced2 = reduced.reduce();
        assertTrue(reduced2.equals(reduced));
        assertTrue(reduced2.reduce().equals(reduced));
        assertTrue(reduced2.reduce().reduce().equals(reduced));
    }

    @Test
    void negate() {
        Scalar e = new Scalar("762755eb09f5a1b3927d89625a90ac93351eba404aa0d0a62315985cc94ba304");
        Scalar neg = e.negate();
        Scalar sum = e.add(neg);
        assertTrue(sum.isZero());

        Scalar f = new Scalar("77aca071106e70a4431f6e4084693281cae145bfb55f2f59dcea67a336b45c0b");
        assertArrayEquals(neg.reduce().getLittleEndian(), f.getLittleEndian());
    }

    @Test
    void storeLoad() {
        Scalar s = new Scalar("762755eb09f5a1b3927d89625a90ac93351eba404aa0d0a62315985cc94ba304").reduce();
        Point S = s.scalarMult(null);

        Scalar sprime = new Scalar(s.toBytes());
        Point Sprime = new Point(S.toBytes());
        assertTrue(s.equals(sprime));
        assertTrue(S.equals(Sprime));
    }

    @Test
    void schnorrVerify() {
        byte[] msg = "Hello Schnorr".getBytes();
        byte[] sigBuf = DatatypeConverter.parseHexBinary("b95fc52a5fd2e18aa7ace5b2250c2a25e368f75c148ea3403c8f32b5f100781b" +
                "362c668aab4cf50eafdc2fcf45214c0dfbe86fce72e4632158c02c571e977306");
        SchnorrSig sig = new SchnorrSig(sigBuf);
        Point pub = new Point("59d7fd947fc88e47d3f878e82e26629dea7a28e8d4233f11068a6b464e195bfd");
        Scalar s = new Scalar(new byte[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-1});
        assertTrue(sig.verify(msg, pub));
        assertFalse(sig.verify(msg, pub.add(pub)));
        assertFalse(sig.verify("Hi Schnorr".getBytes(), pub));
    }

    @Test
    void schnorrSig() {
        byte[] msg = "Hello Schnorr".getBytes();
        KeyPair kp1 = new KeyPair();
        kp1.Scalar = new Scalar("379ccd218573e8ac7c9184de1bdce3398cf37bd2d66460275d11d0517f0f6700");
        kp1.Point = kp1.Scalar.scalarMult(null);
        KeyPair kp2 = new KeyPair();
        SchnorrSig sig = new SchnorrSig(msg, kp1.Scalar);

        assertTrue(sig.verify(msg, kp1.Point));
        assertFalse(sig.verify(msg, kp2.Point));
    }

    @Test
    void scalarMult(){
        Scalar s1 = new Scalar("67e6be35d39af08420fedc3d7911fc4f59b011228df409bc90db25605c85d60d");
        Scalar s2 = new Scalar("1afde431894a4cd4a54c2bad4fa38b94d53c9749914f70743adb86dd7cb05c0d");
        Scalar res = new Scalar("57e990d0d54655e38be2278fc109902ad5a24fdfc7de72c9d8216e4179474205");

        assertTrue(res.equals(s1.mul(s2)));
    }

    @Test
    void testEncryption() throws Exception {
        byte[] orig = "My cool file".getBytes();
        byte[] symmetricKey = new byte[16];
        int ivSize = 16;
        byte[] iv = new byte[ivSize];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        random.nextBytes(symmetricKey);
        Cipher cipher = Cipher.getInstance(Encryption.algo);
        SecretKeySpec key = new SecretKeySpec(symmetricKey, Encryption.algoKey);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
        byte[] data_enc = cipher.doFinal(orig);

        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
        byte[] data = cipher.doFinal(data_enc);
        assertArrayEquals(orig, data);
    }

    @Test
    void testDocumentEncryption()throws Exception{
        byte[] orig = "foo beats bar".getBytes();
        byte[] keyMaterial = new byte[Encryption.ivLength + 16];
        new SecureRandom().nextBytes(keyMaterial);

        byte[] dataEnc = Encryption.encryptData(orig, keyMaterial);
        byte[] data = Encryption.decryptData(dataEnc, keyMaterial);
        assertArrayEquals(orig, data);
    }
}