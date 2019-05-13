package com.IMeeting.util;

import java.util.Date;

/**
 * Created by gjw on 2019/5/2.
 */
public class test {
    public static void main(String arugs[]) {
//        DecimalFormat df=new DecimalFormat("0.00");
//        System.out.println(df.format((float)60/114));
//        Date addDay= DateUtil.addDay(new Date(),-14);
//        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
//        System.out.println(simpleDateFormat.format(addDay));
//        List<Object> list = new ArrayList<>();
//        List<String> list1;
//        for (int i = 0; i < 3; i++) {
//            list1=new ArrayList<>();
//            list1.add("meetRoom" + i);
//            list1.add(Math.random() * 10);
//        }
//        for (Object o : list) {
//            System.out.println(o);
//        }
//        Comparator comparator= Collections.reverseOrder();
//        Collections.sort(list,comparator);
//        Iterator iterator_reverse=list.iterator();
//        while(iterator_reverse.hasNext()){
//            list1 s=(Student)iterator_reverse.next();
//            System.out.println(s.getName()+" "+s.getAge());
//        }

        String privStr = "-----BEGIN PRIVATE KEY-----\n" +
                "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgD5PeS6Qtywn8mo0Q\n" +
                "UHdvweAnZbqP8WbQVSnFJmGpm+yhRANCAAQdjpZQaB1JNU/GGIk0zLKulhNviqHC\n" +
                "/wMDdiPhUCyeP1PvXPdyCNwrIiFUMZYWBRHf0LJ/PRlMSH8Y2siE0iFy\n" +
                "-----END PRIVATE KEY-----\n";

        //change public pem string to public string
        String pubStr = "-----BEGIN PUBLIC KEY-----\n" +
                "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEHY6WUGgdSTVPxhiJNMyyrpYTb4qh\n" +
                "wv8DA3Yj4VAsnj9T71z3cgjcKyIhVDGWFgUR39Cyfz0ZTEh/GNrIhNIhcg==\n" +
                "-----END PUBLIC KEY-----\n";
        tls_sigature.GenTLSSignatureResult result = tls_sigature.GenTLSSignatureEx(1400208454, "12321", privStr);
//        Assert.assertNotEquals(null, result);
//        Assert.assertNotEquals(null, result.urlSig);
//        Assert.assertNotEquals(0, result.urlSig.length());
        System.out.println(result.urlSig);
        System.out.println(new Date().getTime());
    }
}
