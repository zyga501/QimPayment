package pf.chanpay.utils;

import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DigestCalculatorProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;
import org.bouncycastle.util.encoders.Base64;
import pf.ProjectLogger;
import pf.ProjectSettings;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.*;

public class Signature {
    public static String generateSign(String plain) {
        try {
            byte[] plainBs = plain.getBytes("UTF-8");
            CMSTypedData cmsdata = new CMSProcessableByteArray(plainBs);
            CMSSignedData signeddata = cmsSignedDataGenerator_.generate(cmsdata, true);
            byte[] signBs = signeddata.getEncoded();
            String sign = new String(Base64.encode(signBs));
            return sign;
        }
        catch (Exception exception) {

        }

        return "";
    }

    public static boolean verifySign(String plain, String signed) {
        try {
            byte[] signedBs = Base64.decode(signed);//pkcs7

            byte[] plainBs = plain.getBytes("UTF-8");
            byte[] sha1Hash = MessageDigest.getInstance("SHA1").digest(plainBs);
            Map<String, Object> hashes = new HashMap<String, Object>();
            hashes.put("1.3.14.3.2.26", sha1Hash);
            hashes.put("1.2.156.10197.1.410", sha1Hash);

            CMSSignedData sign = new CMSSignedData(hashes, signedBs);

            //假定只有一个证书签名者
            SignerInformation signer = (SignerInformation) sign.getSignerInfos().getSigners().iterator().next();
            SignerInformationVerifier signerInformationVerifier = new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(
                    cjServerPublicKey_);

            return signer.verify(signerInformationVerifier);
        }
        catch (Exception exception) {

        }

        return false;
    }

    static {
        try {
            initPrivateKey();
            initPublicKey();
        }
        catch (Exception exception) {
            ProjectLogger.debug("ChanPay SignUtils InitFailed!");
        }
    }

    private static void initPrivateKey() throws Exception {
        String pfxPath = ProjectSettings.getTopPackagePath().concat("/chanpay/PrivateCertificate.pfx");
        String pfxPasswd = ((Map<Object, Object>) ProjectSettings.getData("chanPay")).get("YS_PFX_PASSWD").toString();
        cmsSignedDataGenerator_ = buildCmsSignedDataGenerator(pfxPath, pfxPasswd);
    }

    private static void initPublicKey() throws Exception {
        String certPath = ProjectSettings.getTopPackagePath().concat("/chanpay/PublicCertificate.cer");
        CertificateFactory factory = CertificateFactory.getInstance("X509");
        FileInputStream cjCertInputStream = new FileInputStream(certPath);
        cjServerPublicKey_ = (X509Certificate) factory.generateCertificate(cjCertInputStream);
        cjCertInputStream.close();
    }

    private static CMSSignedDataGenerator buildCmsSignedDataGenerator(String pfxPath, String pfxPasswd) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream pfxInputStream = new FileInputStream(pfxPath);
        keyStore.load(pfxInputStream, pfxPasswd.toCharArray());

        Enumeration aliasesEnum = keyStore.aliases();
        aliasesEnum.hasMoreElements();
        String alias = (String) aliasesEnum.nextElement();

        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, pfxPasswd.toCharArray());

        X509Certificate publicKey = (X509Certificate) keyStore.getCertificate(alias);
        pfxInputStream.close();

        BouncyCastleProvider bouncyCastlePd = new BouncyCastleProvider();
        Security.addProvider(bouncyCastlePd);

        JcaContentSignerBuilder jcaContentSignerBuilder = new JcaContentSignerBuilder("SHA1withRSA").setProvider(bouncyCastlePd.getName());
        ContentSigner signer = jcaContentSignerBuilder.build(privateKey);

        DigestCalculatorProvider digestCalculatorProvider = new JcaDigestCalculatorProviderBuilder().setProvider(bouncyCastlePd.getName()).build();
        JcaSignerInfoGeneratorBuilder jcaBuilder = new JcaSignerInfoGeneratorBuilder(digestCalculatorProvider);
        SignerInfoGenerator signGen = jcaBuilder.build(signer, publicKey);

        CMSSignedDataGenerator generator = new CMSSignedDataGenerator();
        generator.addSignerInfoGenerator(signGen);

        List<Certificate> certChainList = new LinkedList<Certificate>();
        certChainList.add(publicKey);
        Store certstore = new JcaCertStore(certChainList);
        generator.addCertificates(certstore);

        return generator;
    }

    private static CMSSignedDataGenerator cmsSignedDataGenerator_;
    private static X509Certificate cjServerPublicKey_;
}
